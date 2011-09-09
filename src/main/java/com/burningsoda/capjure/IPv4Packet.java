package com.burningsoda.capjure;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class IPv4Packet extends InternetLayer {

    public IPv4Packet(EthernetFrame parent) {
        super(parent);
    }

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

    public byte getProtocolVersion() {
        return (byte) (readByte(0) >> 4);
    }

    public Protocol getProtocol() {
        return Protocol.byValue(readByte(9));
    }

    public Inet4Address getSourceAddress() throws UnknownHostException {
        return (Inet4Address) InetAddress.getByAddress(readBytes(12, 4));
    }

    public Inet4Address getDestinationAddress() throws UnknownHostException {
        return (Inet4Address) InetAddress.getByAddress(readBytes(16, 4));
    }

    public TransportLayer getTransportLayer() {
        if (getProtocol() == Protocol.UDP) {
            return new UdpPacket(this);
        } else if (getProtocol() == Protocol.TCP) {
            return new TcpPacket(this);
        }

        return new TransportLayer(this);
    }
}
