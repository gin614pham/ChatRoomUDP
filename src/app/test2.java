package app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Vector;

import model.chatModel;

public class test2 {

    public static void main(String[] args) {
        try {
            // Specify the multicast group address and port
            InetAddress groupAddress = InetAddress.getByName("239.0.0.1");
            int groupPort = 8888;
            DatagramSocket ds = new DatagramSocket();

            // Create a MulticastSocket
            MulticastSocket multicastSocket = new MulticastSocket();

            // Join the multicast group (not necessary for sending, but for loopback)
            multicastSocket.joinGroup(new InetSocketAddress(groupAddress, groupPort),
                    NetworkInterface.getByInetAddress(groupAddress));

            String message = "Hello, Multicast World!";
            byte[] messageBytes = message.getBytes();
            Vector<String> v = new Vector<>();
            v.add(new chatModel(groupAddress, groupPort).toString());
            v.add(new chatModel(InetAddress.getLocalHost(), 1212).toString());
            messageBytes = v.toString().getBytes();

            // Create a DatagramPacket with the message and group information
            DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, groupAddress, groupPort);

            // Send the multicast packet
            multicastSocket.send(packet);

            System.out.println("Multicast message sent: " + message);

            // Close the socket
            multicastSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
