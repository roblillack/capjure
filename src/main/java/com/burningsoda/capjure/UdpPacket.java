package com.burningsoda.capjure;

public class UdpPacket extends TransportLayer {
    public UdpPacket(InternetLayer parent) {
        super(parent);
    }

    public short getSourcePort() {
        return readShort(0);
    }

    public short getDestinationPort() {
        return readShort(2);
    }
}
