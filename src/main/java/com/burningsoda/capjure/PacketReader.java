package com.burningsoda.capjure;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import java.io.IOException;

public class PacketReader {
    protected String device;
    protected Pointer handle;
    //protected PointerByReference errbuf;
    protected PcapLibrary.pcap_pkthdr header = new PcapLibrary.pcap_pkthdr();


    public PacketReader(String device) {
        this.device = device;
    }

    public void open() throws IOException {
        PointerByReference errbuf = new PointerByReference();
        handle = PcapLibrary.pcap_open_live(device, 2048, 1, 1000, errbuf);

        if (handle == Pointer.NULL) {
            throw new IOException(String.format("Couldn't open device: %s\n", device));
        }
    }

    public Packet readPacket() throws IOException {
        if (handle == Pointer.NULL) {
            throw new IOException(String.format("Device not open: %s\n", device));
        }

        Pointer p = PcapLibrary.pcap_next(handle, header);

        if (p == null) {
            return readPacket();
        }

        if (header.len > 2000) {
            return readPacket();
        }

        if (header.caplen < header.len) {
            return readPacket();
        }

        return new Packet(p.getByteArray(0, header.caplen));
    }

    public void close() throws IOException {
        // haha.
    }
}
