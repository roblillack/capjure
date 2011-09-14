package com.burningsoda.capjure;

public class FlowTraffic {
    FlowPeers peers;
    // UP:    src --> dest
    // DOWN: dest --> src
    long bytesUp;
    long bytesDown;
    long packetsUp;
    long packetsDown;
    Protocol protocol;
    long firstPacket;
    long lastPacket;

    public FlowTraffic(FlowPeers p) {
        peers = p;
    }

    public FlowPeers getPeers() {
        return peers;
    }

    public void addTraffic(FlowTraffic o) {
        if (peers.equalsIncludingDirection(o.getPeers())) {
            bytesUp += o.bytesUp;
            bytesDown += o.bytesDown;
            packetsUp += o.packetsUp;
            packetsDown += o.packetsDown;
        } else {
            bytesUp += o.bytesDown;
            bytesDown += o.bytesUp;
            packetsUp += o.packetsDown;
            packetsDown += o.packetsUp;
        }
        firstPacket = Math.min(firstPacket, o.firstPacket);
        lastPacket = Math.max(lastPacket, o.lastPacket);

        // TODO: What happenz to the protocol? Shouldn't we take this into the peers?
    }

    public long getBytesUp() {
        return bytesUp;
    }

    public void setBytesUp(long bytesUp) {
        this.bytesUp = bytesUp;
    }

    public void addBytesUp(long count) {
        bytesUp += count;
    }

    public void addBytesDown(long count) {
        bytesDown += count;
    }

    public void addPacketsUp(long count) {
        packetsUp += count;
    }

    public void addPacketsDown(long count) {
        packetsDown += count;
    }

    public long getBytesDown() {
        return bytesDown;
    }

    public void setBytesDown(long bytesDown) {
        this.bytesDown = bytesDown;
    }

    public long getPacketsUp() {
        return packetsUp;
    }

    public void setPacketsUp(long packetsUp) {
        this.packetsUp = packetsUp;
    }

    public long getPacketsDown() {
        return packetsDown;
    }

    public void setPacketsDown(long packetsDown) {
        this.packetsDown = packetsDown;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public long getFirstPacket() {
        return firstPacket;
    }

    public void setFirstPacket(long firstPacket) {
        this.firstPacket = firstPacket;
    }

    public long getLastPacket() {
        return lastPacket;
    }

    public void setLastPacket(long lastPacket) {
        this.lastPacket = lastPacket;
    }}