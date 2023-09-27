package app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import model.chatModel;

public class chatRoom {
    public static void main(String[] args) throws IOException {
        DatagramSocket ds = new DatagramSocket();
        InetAddress ip = InetAddress.getLocalHost();
        chatModel model = new chatModel(ip, ds.getLocalPort());
        connectThread ct = new connectThread(model);
        ct.start();
        while (true) {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            ds.receive(packet);
            System.out.println(new String(packet.getData()).trim());
            ct.setVetor(packet);
        }

    }
}
