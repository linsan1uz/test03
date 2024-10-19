package com.hzw.test03;

import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Create by fqf17 on 2024/10/19 18:03
 */
public interface Receiver {
    String onReceive(MulticastSocket socket, InetAddress address, int port);
}
