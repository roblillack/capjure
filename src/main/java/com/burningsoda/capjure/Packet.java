package com.burningsoda.capjure;

public class Packet {
    protected byte[] raw;

    public Packet(byte[] data) {
        raw = data;
    }

    public int getRawPacketSize() {
        return raw.length;
    }

    public byte[] getRawData() {
        return raw;
    }

    public byte[] getPayload() {
        return raw;
    }

    public int getPayloadSize() {
        return raw.length;
    }

    public Packet getPayloadPacket() {
        return this;
    }
}
