package app;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

import model.chatModel;

public class chatRoom {
    public static void main(String[] args) throws IOException {
        DatagramSocket ds = new DatagramSocket();
        InetAddress ip = InetAddress.getLocalHost();
        chatModel model = new chatModel(ip, ds.getLocalPort());
        chatGUI gui = new chatGUI();
        connectThread ct = new connectThread(model, gui, ds);
        ct.start();
    }
}
