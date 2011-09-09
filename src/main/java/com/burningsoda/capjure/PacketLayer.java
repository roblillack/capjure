package com.burningsoda.capjure;

import java.util.Arrays;

public abstract class PacketLayer {
    public abstract PacketLayer getParentLayer();
    public abstract int getHeaderSize();

    private long read = System.currentTimeMillis();

    public long getReadTimestamp() {
        return read;
    }

    public byte readByte(int offset) {
        return getRawData()[getHeaderOffset() + offset];
    }

    public short readShort(int offset) {
        return (short) (readByte(offset) << 8 + readByte(offset + 1));
    }

    public byte[] readBytes(int offset, int length) {
        return Arrays.copyOfRange(getRawData(), getHeaderOffset() + getHeaderSize() + offset, getHeaderOffset() + getHeaderSize() + offset + length);
    }

    public byte[] getRawData() {
        return getParentLayer().getRawData();
    }

    public int getHeaderOffset() {
        return getParentLayer().getHeaderOffset() + getParentLayer().getHeaderSize();
    }

    public int getSize() {
        return getParentLayer().getSize() - getHeaderSize();
    }
}
