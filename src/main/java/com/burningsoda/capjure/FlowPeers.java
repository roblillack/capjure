package com.burningsoda.capjure;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class FlowPeers {
    private InetAddress source;
    private int sourcePort;
    private InetAddress destination;
    private int destinationPort;
    private Class<? extends TransportLayer> transportProtocol;

    public FlowPeers(TransportLayer packet) throws UnknownHostException {
        this(packet.getInternetLayer().getSourceAddress(),
                packet.getInternetLayer().getDestinationAddress(), packet.getClass());

        if (packet instanceof TcpPacket) {
            sourcePort = ((TcpPacket) packet).getSourcePort();
            destinationPort = ((TcpPacket) packet).getDestinationPort();
        } else if (packet instanceof UdpPacket) {
            sourcePort = ((UdpPacket) packet).getSourcePort();
            destinationPort = ((UdpPacket) packet).getDestinationPort();
        }
    }

    public FlowPeers(InetAddress src, int srcPort, InetAddress dst, int dstPort, Class<? extends TransportLayer> proto) {
        source = src;
        sourcePort = srcPort;
        destination = dst;
        destinationPort = dstPort;
        transportProtocol = proto;
    }

    public FlowPeers(InetAddress src, InetAddress dst, Class<? extends TransportLayer> proto) {
        this(src, 0, dst, 0, proto);
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(int destinationPort) {
        this.destinationPort = destinationPort;
    }

    public Class<? extends TransportLayer> getTransportProtocolProtocol() {
        return transportProtocol;
    }

    public void setTransportProtocol(Class<? extends TransportLayer> proto) {
        transportProtocol = proto;
    }

    public InetAddress getSource() {
        return source;
    }

    public void setSource(InetAddress source) {
        this.source = source;
    }

    public InetAddress getDestination() {
        return destination;
    }

    public void setDestination(InetAddress destination) {
        this.destination = destination;
    }

    public String toString() {
        return source.getHostAddress() + (sourcePort > 0 ? (":" + sourcePort) : "") + " --> " +
                destination.getHostAddress() + (destinationPort > 0 ? (":" + destinationPort) : "") + " [" +
                transportProtocol.getSimpleName() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlowPeers p = (FlowPeers) o;

        if (!transportProtocol.equals(p.transportProtocol)) return false;

        if (destinationPort == p.destinationPort &&
                sourcePort == p.sourcePort &&
                destination.equals(p.destination) &&
                source.equals(p.source)) {
            return true;
        }

        if (destinationPort == p.sourcePort &&
                sourcePort == p.destinationPort &&
                destination.equals(p.source) &&
                source.equals(p.destination)) {
            return true;
        }

        return false;
    }

    public boolean equalsIncludingDirection(FlowPeers p) {
        return p != null &&
                destinationPort == p.destinationPort &&
                sourcePort == p.sourcePort &&
                destination.equals(p.destination) &&
                source.equals(p.source);
    }

    @Override
    public int hashCode() {
        int result = source.hashCode() + destination.hashCode();
        result = 31 * result + sourcePort + destinationPort;
        result = 31 * result + transportProtocol.hashCode();
        return result;
    }
}
