package app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import model.VectorModel;
import model.chatModel;

public class connectThread extends Thread {
    VectorModel v = new VectorModel();
    final InetAddress multicastGroup = InetAddress.getByName("239.0.0.1");
    final int multicastPort = 8888;
    MulticastSocket socket = new MulticastSocket();
    String uniqueIdentifier = UUID.randomUUID().toString();
    InetSocketAddress ISA;
    NetworkInterface ni;
    DatagramSocket ds;
    chatModel c;
    chatGUI gui;
    chatThread chatT;
    final String J = "JOINED";
    final String L = "LEAVE";
    final String U = "UPDATE";
    final String M = "MESSAGE";

    public connectThread(chatModel c, chatGUI gui, DatagramSocket ds) throws IOException {
        this.gui = gui;
        this.c = c;
        this.v.add(c);
        this.ds = ds;
        this.chatT = new chatThread(this, ds);
        System.out.println("Multicast Receiver started. Waiting for messages...");
        socket = new MulticastSocket(multicastPort);
        ISA = new InetSocketAddress(multicastGroup, multicastPort);
        ni = NetworkInterface.getByInetAddress(multicastGroup);
        setUp();
    }

    public void extracted(DatagramPacket packet) throws IOException {
        VectorModel vm;
        // v.extractedVector(packet.getData());
        String method = v.extractedMethod(packet.getData());
        switch (method) {
            case J:
                vm = v.extractedVector(packet.getData());
                if (v.joined(packet.getData())) {
                    for (chatModel model : vm) {
                        gui.textTB.setText(gui.textTB.getText() + model.toString() + " Has joined\n");
                        ds.send(updatePacket(model.getIp(), model.getPort()));
                    }
                }
                updateList();
                break;
            case L:
                vm = v.extractedVector(packet.getData());
                v.leave(packet.getData());
                for (chatModel model : vm) {
                    gui.textTB.setText(gui.textTB.getText() + model.toString() + " Has left\n");
                }
                updateList();
                break;
            case U:
                v.update(packet.getData());
                updateList();
                break;
            case M:
                // System.out.println("Message: " + v.extractedMsg(packet.getData()));
                gui.textChat.setText(gui.textChat.getText() + "\n" + "Message from " + packet.getAddress() + " at port "
                        + packet.getPort() + ": " + v.extractedMsg(packet.getData()));
                break;
            default:
                break;
        }
    }

    public void setUp() {
        gui.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent w) {
                try {
                    int choice = JOptionPane.showConfirmDialog(gui.frame, "Are you sure to exit?", "Exit",
                            JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        socket.send(leavePacket());
                        socket.leaveGroup(ISA, ni);
                        gui.frame.dispose();
                        System.exit(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        gui.btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String msg = gui.textInput.getText();
                gui.textInput.setText("");
                // get a value from the list
                int index = gui.list.getSelectedIndex();
                // convert it to port number and inetAddress
                int port;
                InetAddress ip;
                if (index != -1) {
                    try {
                        port = v.elementAt(index).getPort();
                        ip = v.elementAt(index).getIp();
                        gui.textChat
                                .setText(gui.textChat.getText() + "\n" + "Send Message to "
                                        + ip
                                        + " at port " + port
                                        + ": " + msg);
                        socket.send(sendPacket(msg, ip, port));
                        // set color to green to show that the message has been sent

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });

    }

    public void updateList() {
        Object select = gui.list.getSelectedItem();
        if (select instanceof String)
            System.out.println("String");
        gui.list.setModel(new DefaultComboBoxModel(v.toVectorStrings().toArray()));
        gui.list.setSelectedItem(select);
    }

    public void run() {
        try {
            chatT.start();
            gui.frame.setVisible(true);
            socket.joinGroup(ISA, ni);
            socket.send(joinPacket());
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                if (isMyMessage(packet))
                    continue;
                extracted(packet);
                System.out.println("There is a message");
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
        gui.textTB.setText("You have joined\n");
        return new DatagramPacket(messageBytes, messageBytes.length, multicastGroup, multicastPort);
    }

    public DatagramPacket updatePacket(InetAddress ip, int port) {
        Vector<String> msg = this.v.toVectorStrings();
        String msgString = uniqueIdentifier + "|" + msg.toString() + "|" + U;
        byte[] messageBytes = msgString.getBytes();
        return new DatagramPacket(messageBytes, messageBytes.length, ip, port);
    }

    public DatagramPacket leavePacket() {
        Vector<String> msg = new Vector<>();
        msg.add(c.toString());
        String msgString = uniqueIdentifier + "|" + msg.toString() + "|" + L;
        byte[] messageBytes = msgString.getBytes();
        return new DatagramPacket(messageBytes, messageBytes.length, multicastGroup, multicastPort);
    }

    public DatagramPacket sendPacket(String msg, InetAddress ip, int port) {
        String msgString = uniqueIdentifier + "|" + msg + "|" + M;
        byte[] messageBytes = msgString.getBytes();
        return new DatagramPacket(messageBytes, messageBytes.length, ip, port);
    }

    public boolean isMyMessage(DatagramPacket packet) {
        String[] split = new String(packet.getData()).trim().split("\\|");
        return split[0].equals(uniqueIdentifier);
    }

}
