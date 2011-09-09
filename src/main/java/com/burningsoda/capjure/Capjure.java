package com.burningsoda.capjure;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;

public class Capjure {

    public static void main(String[] args) {
        String dev = "en1";

        System.out.format("Device: %s\n", dev);

        PacketReader reader = new PacketReader(dev);
        try {
            reader.open();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(2);
        }

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

            if (frame.getProtocol() != EthernetFrame.Protocol.IP) {
                continue;
            }
            IPv4Packet ipv4 = new IPv4Packet(frame.getPayload());

            System.out.format("%s: Jacked a packet with length of [%d], IP proto: %s\n", new Date(), frame.getRawPacketSize(), ipv4.getProtocol().name());
            try {
                System.out.format("%s --> %s\n", ipv4.getSourceAddress(), ipv4.getDestinationAddress());
            } catch (UnknownHostException e) {
                continue;
            }
        }
    }
}