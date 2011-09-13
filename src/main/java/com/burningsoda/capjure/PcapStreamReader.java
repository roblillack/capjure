package com.burningsoda.capjure;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class PcapStreamReader implements PacketReader {
    private static final int MAGIC_NUMER = 0xa1b2c3d4;
    private static final int REVERSE_MAGIC_NUMER = 0xd4c3b2a1;

    protected DataInputStream inputStream;
    protected boolean reverse = false;

    public PcapStreamReader(InputStream in) {
        inputStream = new DataInputStream(in);
    }

    @Override
    public void open() throws IOException {
        int magic = inputStream.readInt();
        if (magic == REVERSE_MAGIC_NUMER) {
            reverse = true;
        } else if (magic != MAGIC_NUMER) {
            throw new IOException("Unable to read stream, wrong magic number.");
        }
        int major = readUnsignedShort();
        int minor = readUnsignedShort();
        if (major != 2 || minor != 4) {
            throw new IOException(String.format("Unknown PCAP protocol version %d.%d", major, minor));
        }

        inputStream.skipBytes(16);
        // thiszone (int)
        // sigfigs (uint)
        // snaplen (uint)
        // network (uint)
    }

    private long readUnsignedInt() throws IOException {
        if (reverse) {
            return inputStream.readUnsignedByte() +
                    (inputStream.readUnsignedByte() <<8) +
                    (inputStream.readUnsignedByte() <<16) +
                    (inputStream.readUnsignedByte() <<24);
        }

        return (inputStream.readUnsignedShort() <<16) +
                inputStream.readUnsignedShort();
    }

    private int readUnsignedShort() throws IOException {
        if (reverse) {
            return inputStream.readUnsignedByte() +
                    (inputStream.readUnsignedByte()<<8);
        }

        return inputStream.readUnsignedShort();
    }

    @Override
    public Packet readPacket() throws IOException {
        inputStream.skipBytes(8);
        //long seconds = readUnsignedInt();
        //long milliSecs = readUnsignedInt();
        long streamLen = readUnsignedInt();
        inputStream.skipBytes(4);
        //long origLen = readUnsignedInt();

        if (streamLen > Integer.MAX_VALUE) {
            throw new IOException("Packet too large to handle");
        }

        //System.out.format("Reading packet %d (orig: %d)\n", streamLen, origLen);
        //System.out.format("Captured: %s\n", new Date(seconds * 1000L + milliSecs));

        byte[] buf = new byte[(int) streamLen];
        inputStream.readFully(buf, 0, buf.length);
        return new Packet(buf);
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
