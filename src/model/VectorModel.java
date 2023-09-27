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

    @Override
    public int indexOf(Object o) {
        if (o instanceof chatModel) {
            chatModel model = (chatModel) o;
            for (chatModel m : this) {
                if (m.getIp().equals(model.getIp()) && m.getPort() == model.getPort()) {
                    return this.indexOf(m);
                }
            }
        }
        return -1;
    }

    public Vector<String> toVectorStrings() {
        Vector<String> v = new Vector<>();
        for (chatModel m : this) {
            v.add(m.toString());
        }
        return v;
    }

    public VectorModel ExtractedData(byte[] packet) {
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

        switch (partsData[2]) {
            case "JOINED":
                this.addAll(data);
                return data;
            case "LEAVE":
                this.removeAll(data);
                break;
            case "UPDATE":
                this.clear();
                this.addAll(data);
                break;
        }
        return null;
    }

}
