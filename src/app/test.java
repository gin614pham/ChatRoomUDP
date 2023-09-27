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

public class test {

    public static void main(String[] args) {
        try {
            // Specify the multicast group address and port
            InetAddress groupAddress = InetAddress.getByName("239.0.0.1");
            int groupPort = 8888;

            // Create a MulticastSocket and bind it to the group port
            MulticastSocket multicastSocket = new MulticastSocket(groupPort);

            DatagramSocket ds = new DatagramSocket();

            // Join the multicast group
            multicastSocket.joinGroup(new InetSocketAddress(groupAddress, groupPort),
                    NetworkInterface.getByInetAddress(groupAddress));

            System.out.println("Multicast Receiver started. Waiting for messages...");

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                // Receive a multicast packet
                multicastSocket.receive(packet);
                Vector<chatModel> v = new Vector<>();
                v = convertBack(packet.getData());

                // Display the received message
                String message = new String(packet.getData());
                // System.out.println("Received message: " + message);
                // System.out.println(convertBack(packet.getData()).size() + " messages
                // received.");
                for (chatModel model : v) {
                    System.out.println("Received message from " + model.getIp() + ":" + model.getPort());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Vector<chatModel> convertBack(byte[] bytes) {
        // Assuming messageBytes contains the byte array
        String vectorString = new String(bytes).trim();
        vectorString = vectorString.substring(1, vectorString.length() - 1);

        // Remove the square brackets from the string
        // vectorString = vectorString.substring(1, vectorString.length() - 1);

        // Split the string into individual elements
        String[] elements = vectorString.split(", ");

        // Create a new Vector to store chatModel objects
        Vector<chatModel> recreatedVector = new Vector<>();

        // Loop through the elements and parse them to chatModel
        for (String element : elements) {
            String[] parts = element.split(":");
            if (parts.length == 2) {
                try {
                    String addressString = parts[0].trim();
                    String portString = parts[1].trim();

                    InetAddress address = InetAddress.getByName(addressString);
                    int port = Integer.parseInt(portString);

                    chatModel model = new chatModel(address, port);
                    recreatedVector.add(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return recreatedVector;
    }
}
