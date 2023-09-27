package app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.UUID;
import java.util.Vector;

import model.VectorModel;
import model.chatModel;

public class connectThread extends Thread {
    VectorModel v = new VectorModel();
    InetAddress multicastGroup = InetAddress.getByName("239.0.0.1");
    int multicastPort = 8888;
    MulticastSocket socket = new MulticastSocket();
    String uniqueIdentifier = UUID.randomUUID().toString();
    chatModel c;
    final String J = "JOINED";
    final String L = "LEAVE";
    final String U = "UPDATE";

    public connectThread(chatModel c) throws IOException {
        this.c = c;
        this.v.add(c);
        System.out.println("Multicast Receiver started. Waiting for messages...");
        socket = new MulticastSocket(multicastPort);
    }

    public void setVetor(DatagramPacket packet) {
        v.ExtractedData(packet.getData());
    }

    public void run() {
        try {
            socket.joinGroup(new InetSocketAddress(multicastGroup, multicastPort),
                    NetworkInterface.getByInetAddress(multicastGroup));
            socket.send(joinPacket());
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                if (isMyMessage(packet))
                    continue;
                VectorModel vm = v.ExtractedData(packet.getData());
                if (vm != null) {
                    for (chatModel model : vm) {
                        System.out.println(model.getIp() + ":" + model.getPort() + " Has joined");
                        socket.send(updatePacket(model.getIp(), model.getPort()));
                    }
                }
                System.out.println("Received message: " + new String(packet.getData()).trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DatagramPacket joinPacket() {
        Vector<String> msg = new Vector<>();
        msg.add(c.toString());
        String msgString = uniqueIdentifier + "|" + msg.toString() + "|" + J;
        byte[] messageBytes = msgString.getBytes();
        return new DatagramPacket(messageBytes, messageBytes.length, multicastGroup, multicastPort);
    }

    public DatagramPacket updatePacket(InetAddress ip, int port) {
        Vector<String> msg = this.v.toVectorStrings();
        String msgString = uniqueIdentifier + "|" + msg.toString() + "|" + U;
        byte[] messageBytes = msgString.getBytes();
        return new DatagramPacket(messageBytes, messageBytes.length, ip, port);
    }

    public boolean isMyMessage(DatagramPacket packet) {
        String[] split = new String(packet.getData()).trim().split("\\|");
        return split[0].equals(uniqueIdentifier);
    }

}
