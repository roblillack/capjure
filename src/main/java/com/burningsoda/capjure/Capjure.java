package com.burningsoda.capjure;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import java.net.UnknownHostException;
import java.util.Date;

public class Capjure {

    public static void main(String[] args) {
        String dev = "en1";

        System.out.format("Device: %s\n", dev);

        PointerByReference errbuf = new PointerByReference();
        Pointer handle = PcapLibrary.pcap_open_live(dev, 2048, 1, 1000, errbuf);

        if (handle == Pointer.NULL) {
            System.err.format("Couldn't open device: %s\n", dev);
            System.exit(2);
        }

        System.out.format("HANDLE: %s\n", handle);

        PcapLibrary.pcap_pkthdr header = new PcapLibrary.pcap_pkthdr();

        for (;;) {
            Pointer p = PcapLibrary.pcap_next(handle, header);
            if (p == null) {
                continue;
            }

            if (header.len > 2000) {
                continue;
            }

            if (header.caplen < header.len) {
                continue;
            }

            Packet packet = new Packet(p.getByteArray(0, header.caplen));

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