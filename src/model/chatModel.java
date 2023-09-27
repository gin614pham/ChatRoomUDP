package model;

import java.net.InetAddress;

public class chatModel {
    InetAddress ip;
    int port;

    public chatModel(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        // split ip with / and get the last part
        String[] split = ip.toString().split("/");
        return split[split.length - 1] + ":" + port;
    }
}
