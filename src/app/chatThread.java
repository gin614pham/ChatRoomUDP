package app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class chatThread extends Thread {
    int multicastPort = 8888;
    InetAddress multicastGroup;
    DatagramSocket socket;
    chatGUI gui;

    public chatThread(InetAddress multicastGroup, int multicastPort) throws UnknownHostException, IOException {
        this.multicastGroup = multicastGroup;
        this.multicastPort = multicastPort;
        this.socket = new DatagramSocket();

        socket.joinGroup(new InetSocketAddress(multicastGroup, multicastPort),
                NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
        gui.frame.setVisible(true);
        gui.btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String message = gui.textInput.getText();
                    byte[] messageBytes = message.getBytes();
                    DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, multicastGroup,
                            multicastPort);
                    socket.send(packet);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
    }
}
