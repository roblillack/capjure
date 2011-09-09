package com.burningsoda.capjure;

import java.lang.reflect.Array;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class IPv4Packet extends Packet {
    public enum Protocol {
        UNKNOWN(-1),
        ICMP(0x01),
        IGMP(0x02),
        TCP(0x06),
        UDP(0x11),
        L2TP(0x73),
        SCTP(0x84);

        private int id;

        Protocol(int t) {
            id = t;
        }

        private static Map<Byte, Protocol> map;
        static {
            map = new HashMap<Byte, Protocol>();
            for (Protocol p : Protocol.values()) {
                map.put((byte) p.id, p);
            }
        }

        public static Protocol byValue(byte val) {
            Protocol proto = map.get(val);
            return proto == null ? UNKNOWN : proto;
        }
    }

    public IPv4Packet(byte[] rawData) {
        super(rawData);
    }

    public byte getProtocolVersion() {
        return (byte) (raw[0] >> 4);
    }

    public Protocol getProtocol() {
        return Protocol.byValue(raw[9]);
    }

    public Inet4Address getSourceAddress() throws UnknownHostException {
        return (Inet4Address) InetAddress.getByAddress(Arrays.copyOfRange(raw, 12, 16));
    }

    public Inet4Address getDestinationAddress() throws UnknownHostException {
        return (Inet4Address) InetAddress.getByAddress(Arrays.copyOfRange(raw, 16, 20));
    }
}
