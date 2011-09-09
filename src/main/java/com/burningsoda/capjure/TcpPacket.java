package com.burningsoda.capjure;

public class TcpPacket extends TransportLayer {
    public TcpPacket(InternetLayer parent) {
        super(parent);
    }

    public short getSourcePort() {
        return readShort(0);
    }

    public short getDestinationPort() {
        return readShort(2);
    }
}
