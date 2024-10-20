package com.hzw.test03;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;


/**
 * Create by fqf17 on 2024/10/19 16:29
 */
public class UDPBroadCastService extends Service {
    private static final String TAG = "UDPBroadCastService";
    private static final int PORT = 8000;
    private MulticastSocket multicastSocket;
    private String broadGroup = "224.0.0.100";
    private Thread myThread;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: 服务开启");
        myThread =  new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    multicastSocket = new MulticastSocket(PORT);
                    String value = intent.getStringExtra("value");
                    byte[] valueBytes = value.getBytes();
                    sendData(valueBytes,multicastSocket, InetAddress.getByName(broadGroup),PORT);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        myThread.start();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public static String getLocalIPAddress(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        if (wifiManager!=null){
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String str_ip = ipInt2Str(ipAddress);
            return str_ip;
        }
        Log.e(TAG, "LocalIPAddress is null ");
        return "";
    }
    public static String ipInt2Str(int ip) {
        int first = ip >> 24;
        if (first < 0) {
            first = 0xff + first + 1;
        }
        int second = ip >> 16 & 0xff;
        int third = ip >> 8 & 0xff;
        int four = ip & 0xff;

        StringBuffer buf = new StringBuffer();
        buf.append(four).append(".").append(third).append(".")
                .append(second).append(".").append(first);
        return buf.toString();
    }

    private static void sendData(byte[] message,MulticastSocket socket, InetAddress address, int port) throws IOException {
        // 创建 DatagramPacket 并发送数据
        DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
        socket.send(packet);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (multicastSocket!=null&&myThread!=null&&myThread.isAlive()){
            multicastSocket.close();
            myThread.interrupt();
        }
        Log.i(TAG, "UDP 服务已关闭");
    }
}
