package com.burningsoda.capjure;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EthernetFrame extends PacketLayer {
    public enum Protocol {
        UNKNOWN(-1),
        PUP(0x200),
        IP(0x800),
        ARP(0x806),
        REVARP(0x8035),
        VLAN(0x8100),
        IPV6(0x86dd),
        PAE(0x888e),
        RSN_PREAUTH(0x88c7);

        private int id;

        Protocol(int t) {
            id = t;
        }

        private static Map<Short, Protocol> map;
        static {
            map = new HashMap<Short, Protocol>();
            for (Protocol p : Protocol.values()) {
                map.put((short) p.id, p);
            }
        }

        public static Protocol byValue(short val) {
            Protocol proto = map.get(val);
            return proto == null ? UNKNOWN : proto;
        }

        public static Protocol fromBytes(byte[] buf, int offset) {
            return Protocol.byValue((short) (buf[offset] << 8 + buf[offset + 1]));
        }
    }

    public static int HEADER_SIZE = 6 + 6 + 2;

    private byte[] raw;

    @Override
    public PacketLayer getParentLayer() {
        return null;
    }

    @Override
    public int getHeaderSize() {
        return HEADER_SIZE;
    }

    @Override
    public int getHeaderOffset() {
        return 0;
    }

    @Override
    public byte[] getRawData() {
        return raw;
    }

    @Override
    public int getSize() {
        return raw.length;
    }

    public EthernetFrame(byte[] rawData) {
        raw = rawData;
    }

    public static EthernetFrame fromPacket(Packet p) {
        if (p.getRawPacketSize() < HEADER_SIZE) {
            return null;
        }

        return new EthernetFrame(p.getRawData());
    }

    public Protocol getProtocol() {
        return Protocol.fromBytes(raw, 12);
    }

    public byte[] getSourceMacAddress() {
        return Arrays.copyOfRange(raw, 0, 6);
    }

    public byte[] getDestinationMacAddress() {
        return Arrays.copyOfRange(raw, 6, 12);
    }

    public InternetLayer getInternetLayer() {
        if (getProtocol() == Protocol.IP) {
            return new IPv4Packet(this);
        }

        return new InternetLayer(this);
    }

}
