package model;

import java.net.InetAddress;
import java.util.Vector;

public class VectorModel extends Vector<chatModel> {

    @Override
    public boolean contains(Object o) {
        if (o instanceof chatModel) {
            chatModel model = (chatModel) o;
            for (chatModel m : this) {
                if (m.getIp().equals(model.getIp()) && m.getPort() == model.getPort()) {
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized Vector<String> toVectorStrings() {
        Vector<String> v = new Vector<>();
        VectorModel tg = this;
        for (chatModel m : tg) {
            v.add(m.toString());
        }
        return v;
    }

    public String extractedMethod(byte[] packet) {
        String[] partsData = new String(packet).trim().split("\\|");
        return partsData[2];
    }

    public synchronized boolean joined(byte[] packet) {
        VectorModel data = new VectorModel();
        data = extractedVector(packet);
        return this.addAll(data);
    }

    public synchronized boolean leave(byte[] packet) {
        return this.removeAll(extractedVector(packet));
    }

    public synchronized boolean update(byte[] packet) {
        this.clear();
        return this.addAll(extractedVector(packet));
    }

    public synchronized VectorModel extractedVector(byte[] packet) {
        VectorModel data = new VectorModel();
        String[] partsData = new String(packet).trim().split("\\|");
        String[] elements = partsData[1].substring(1, partsData[1].length() - 1).split(",");
        for (String element : elements) {
            String[] parts = element.split(":");
            if (parts.length == 2) {
                try {
                    String addressString = parts[0].trim();
                    String portString = parts[1].trim();

                    InetAddress address = InetAddress.getByName(addressString);
                    int port = Integer.parseInt(portString);

                    chatModel model = new chatModel(address, port);
                    data.add(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    public synchronized String extractedMsg(byte[] packet) {
        String[] partsData = new String(packet).trim().split("\\|");
        return partsData[1];
    }

}
