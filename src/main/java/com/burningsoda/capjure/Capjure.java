package com.burningsoda.capjure;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

public class Capjure {
    public static final long FLOW_TIMEOUT_MS = 30000L;

    public static void main(String[] args) {
        boolean silent = false;
        PacketReader reader;

        if (args.length < 1) {
            String dev = "en1";
            System.out.format("Device: %s\n", dev);

            reader = new InterfacePacketReader(dev);
            try {
                reader.open();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(2);
                return;
            }
        } else {
            silent = true;
            try {
                reader = new PcapStreamReader(new FileInputStream(args[0]));
                reader.open();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(2);
                return;
            }
        }

        final Map<FlowPeers, FlowTraffic> flowTrafficMap = new HashMap<FlowPeers, FlowTraffic>();

        long start = System.currentTimeMillis();
        long totalBytes = 0L;
        long totalPackets = 0L;
        long lastCleanup = start;

        for (;;) {
            Packet packet;
            try {
                packet = reader.readPacket();
            } catch (EOFException eof) {
                break;
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

            if (!silent) {
                System.out.format("%s: Jacked a packet with length of [%d], IP proto: %s\n", new Date(), frame.getSize(), transport.getClass().getSimpleName());
                System.out.println(peers);
            }

            FlowTraffic traffic = flowTrafficMap.get(peers);
            if (traffic == null) {
                traffic = new FlowTraffic();
                traffic.setFirstPacket(frame.getReadTimestamp());
                flowTrafficMap.put(peers, traffic);
            }
            traffic.setBytesDown(traffic.getBytesDown() + frame.getSize());
            traffic.setPacketsDown(traffic.getPacketsDown() + 1);
            traffic.setLastPacket(frame.getReadTimestamp());

            totalBytes += frame.getSize();
            totalPackets++;

            long now = System.currentTimeMillis();

            if (now >= lastCleanup + 1000L) {
                Set<FlowPeers> removePeers = new HashSet<FlowPeers>();
                for (Map.Entry<FlowPeers, FlowTraffic> e : flowTrafficMap.entrySet()) {
                    if (e.getValue().getLastPacket() < now - FLOW_TIMEOUT_MS) {
                        removePeers.add(e.getKey());
                    }
                }
                for (FlowPeers p : removePeers) {
                    flowTrafficMap.remove(p);
                }
                lastCleanup = now;
            }
            /*new Timer().scheduleAtFixedRate(
                    new TimerTask() {
                        @Override
                        public void run() {
                            System.out.format("%d flows.\n", flowTrafficMap.size());
                        }
                    }, 0L, 1000L);*/
        }

        long end = System.currentTimeMillis();
        System.out.format("%d Packets (%.4f MB) processed in %.2f seconds (~ %.4f mbit/s, %.4f pps)\n",
                totalPackets, totalBytes/1000000.0, (end-start)/1000.0,
                totalBytes*8/1000000.0/((end-start)/1000.0), totalPackets/((end-start)/1000.0));
    }
}