package com.burningsoda.capjure;

import java.io.IOException;

public interface PacketReader {
    void open() throws IOException;
    Packet readPacket() throws IOException;
    void close() throws IOException;
}
