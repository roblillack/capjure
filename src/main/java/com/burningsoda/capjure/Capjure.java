package com.burningsoda.capjure;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

public class Capjure {
    public static final long FLOW_TIMEOUT_MS = 30000L;

    public static void main(String[] args) {
        String dev = "en1";

        System.out.format("Device: %s\n", dev);

        PacketReader reader = new InterfacePacketReader(dev);
        try {
            reader.open();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(2);
        }

        final Map<FlowPeers, FlowTraffic> flowTrafficMap = new HashMap<FlowPeers, FlowTraffic>();

        for (;;) {
            Packet packet;
            try {
                packet = reader.readPacket();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                continue;
            }

            EthernetFrame frame = EthernetFrame.fromPacket(packet);
            if (frame == null) {
                continue;
            }

            InternetLayer ip = frame.getInternetLayer();
            if (ip == null || !(ip instanceof IPv4Packet)) {
                continue;
            }

            TransportLayer transport = ip.getTransportLayer();
            FlowPeers peers;

            try {
                peers = new FlowPeers(transport);
            } catch (UnknownHostException uhe) {
                uhe.printStackTrace();
                continue;
            }

            System.out.format("%s: Jacked a packet with length of [%d], IP proto: %s\n", new Date(), frame.getSize(), transport.getClass().getSimpleName());
            System.out.println(peers);

            FlowTraffic traffic = flowTrafficMap.get(peers);
            if (traffic == null) {
                traffic = new FlowTraffic();
                traffic.setFirstPacket(frame.getReadTimestamp());
                flowTrafficMap.put(peers, traffic);
            }
            traffic.setBytesDown(traffic.getBytesDown() + frame.getSize());
            traffic.setPacketsDown(traffic.getPacketsDown() + 1);
            traffic.setLastPacket(frame.getReadTimestamp());

            Set<FlowPeers> removePeers = new HashSet<FlowPeers>();
            for (Map.Entry<FlowPeers, FlowTraffic> e: flowTrafficMap.entrySet()) {
                if (e.getValue().getLastPacket() < System.currentTimeMillis() - FLOW_TIMEOUT_MS) {
                    removePeers.add(e.getKey());
                }
            }
            for (FlowPeers p: removePeers) {
                flowTrafficMap.remove(p);
            }

            new Timer().scheduleAtFixedRate(
                    new TimerTask() {
                        @Override
                        public void run() {
                            System.out.format("%d flows.\n", flowTrafficMap.size());
                        }
                    }, 0L, 1000L);
        }
    }
}