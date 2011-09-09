package com.burningsoda.capjure;

public class FlowTraffic {
    long bytesUp;
    long bytesDown;
    long packetsUp;
    long packetsDown;
    Protocol protocol;
    long firstPacket;
    long lastPacket;

    public FlowTraffic() {
    }

    public long getBytesUp() {
        return bytesUp;
    }

    public void setBytesUp(long bytesUp) {
        this.bytesUp = bytesUp;
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