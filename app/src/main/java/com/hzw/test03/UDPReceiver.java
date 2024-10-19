package com.hzw.test03;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UDPReceiver extends Service implements Receiver {
    private static final int PORT = 8000;
    private MulticastSocket multicastSocket;
    private static final String TAG = "UDPReceiver";
    private DatagramPacket datagramPacket;
    private byte[] buffer;
    private String resultCode;
    private String receiveGroup = "224.0.0.100";




    @Override
    public String onReceive(MulticastSocket socket, InetAddress address, int port) {
        //缓冲区大小
        buffer = new byte[1024];
        datagramPacket = new DatagramPacket(buffer,buffer.length);
        //接收数据
        try {
            socket.receive(datagramPacket);
            resultCode = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
            return resultCode;
        } catch (IOException e) {
            Log.i(TAG, "onReceive:IO异常产生",e);
        }
        return resultCode;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    multicastSocket = new MulticastSocket(PORT);
                    //224.0.0.100为组播地址 不用进行更改
                    InetAddress address = InetAddress.getByName(receiveGroup);
                    //加入组播组
                    multicastSocket.joinGroup(address);
                    //接收数据
                    while (!Thread.interrupted()){
                        String resultCode = onReceive(multicastSocket,address,PORT);
                        Log.i(TAG, "onReceive: "+resultCode);
                        //根据返回值s 进行逻辑判断
                        if (resultCode.equals("hello")){
                            //返回成功
                            Log.i(TAG, "接收成功");
                        }else {
                            //返回失败
                            Log.i(TAG, "接收失败1，请检查网络连接或重新发送");
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "接收失败2，请检查网络连接或重新发送",e);
                }
            }
        }).start();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (multicastSocket!=null){
            multicastSocket.close();
        }
    }
}