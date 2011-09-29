package com.burningsoda.capjure.processor;

import com.burningsoda.capjure.*;

import java.io.EOFException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

public class PacketProcessor {
    PacketReader reader;
    long flowTimeoutMillis = 60000L;
    final Map<FlowPeers, FlowTraffic> flowTrafficMap = new HashMap<FlowPeers, FlowTraffic>();
    long totalBytes = 0L;
    long totalPackets = 0L;
    long totalFlows = 0L;

    public PacketProcessor(PacketReader r) {
        reader = r;
    }

    public long getFlowTimeoutMillis() {
        return flowTimeoutMillis;
    }

    public void setFlowTimeoutMillis(long flowTimeoutMillis) {
        this.flowTimeoutMillis = flowTimeoutMillis;
    }

    public Map<FlowPeers, FlowTraffic> getFlowTrafficMap() {
        return flowTrafficMap;
    }

    public void run() {
        final long start = System.currentTimeMillis();
        long lastCleanup = start;

        new Timer().scheduleAtFixedRate(
                new TimerTask() {
                    long lastBytes = 0L;
                    long lastPackets = 0L;

                    @Override
                    public void run() {
                        long end = System.currentTimeMillis();
                        double secs = (end - start)/1000.0;
                        System.out.format("%d Packets (%.4f MB) processed in %.2f seconds (~ %.4f mbit/s, %.4f pps)\n",
                                totalPackets, totalBytes/1000000.0, (end-start)/1000.0,
                                totalBytes*8/1000000.0/secs, totalPackets/secs);
                        System.out.format("Total Flows: %d (~ %.4f new flows/sec)\n", totalFlows, totalFlows/secs);
                        System.out.format("CURRENT: %.4f mbit/s, %d pps\n",
                                (totalBytes - lastBytes) * 8 / 1000000.0, totalPackets - lastPackets);
                        lastBytes = totalBytes;
                        lastPackets = totalPackets;
                        System.out.format("%d active flows.\n", flowTrafficMap.size());
                    }
                }, 1000L, 1000L);

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

            FlowTraffic traffic = flowTrafficMap.get(peers);
            if (traffic == null) {
                traffic = new FlowTraffic(peers);
                traffic.setFirstPacket(frame.getReadTimestamp());
                flowTrafficMap.put(peers, traffic);
                totalFlows++;
            }
            if (traffic.getPeers().equalsIncludingDirection(peers)) {
                traffic.addBytesUp(frame.getSize());
                traffic.addPacketsUp(1);
            } else {
                traffic.addBytesDown(frame.getSize());
                traffic.addPacketsDown(1);
            }
            traffic.setLastPacket(frame.getReadTimestamp());

            totalBytes += frame.getSize();
            totalPackets++;

            long now = System.currentTimeMillis();

            if (now >= lastCleanup + 500L) {
                Set<FlowPeers> removePeers = new HashSet<FlowPeers>();
                for (Map.Entry<FlowPeers, FlowTraffic> e : flowTrafficMap.entrySet()) {
                    if (e.getValue().getLastPacket() < now - flowTimeoutMillis) {
                        removePeers.add(e.getKey());
                    }
                }
                for (FlowPeers p : removePeers) {
                    flowTrafficMap.remove(p);
                }
                lastCleanup = now;
            }
        }

        long end = System.currentTimeMillis();
        double secs = (end - start)/1000.0;
        System.out.format("%d Packets (%.4f MB) processed in %.2f seconds (~ %.4f mbit/s, %.4f pps)\n",
                totalPackets, totalBytes/1000000.0, (end-start)/1000.0,
                totalBytes*8/1000000.0/secs, totalPackets/secs);
        System.out.format("Total Flows: %d (~ %.4f new flows/sec)\n", totalFlows, totalFlows/secs);
    }
}
