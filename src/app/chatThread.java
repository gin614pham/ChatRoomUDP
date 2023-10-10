package app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class chatThread extends Thread {
    DatagramSocket ds;
    connectThread ct;

    public chatThread(connectThread ct, DatagramSocket ds) {
        this.ds = ds;
        this.ct = ct;
    }

    public void run() {
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                ds.receive(packet);
                // System.out.println("Have: " + new String(packet.getData()).trim());
                ct.extracted(packet);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
