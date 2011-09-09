package com.burningsoda.capjure;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InternetLayer extends PacketLayer {
    protected EthernetFrame parent;

    public InternetLayer(EthernetFrame parent) {
        this.parent = parent;
    }

    public TransportLayer getTransportLayer() {
        return null;
    }

    @Override
    public PacketLayer getParentLayer() {
        return parent;
    }

    @Override
    public int getHeaderSize() {
        return 0;
    }

    public InetAddress getSourceAddress() throws UnknownHostException {
        return null;
    }

    public InetAddress getDestinationAddress() throws UnknownHostException {
        return null;
    }

}
