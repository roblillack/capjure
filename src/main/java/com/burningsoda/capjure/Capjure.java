package com.burningsoda.capjure;

import java.io.FileInputStream;
import java.io.IOException;

public class Capjure {
    public static void main(String[] args) {
        PacketReader reader;

        if (args.length < 1) {
            String dev = "en1";
            System.out.format("Device: %s\n", dev);

            reader = new InterfacePacketReader(dev);
            try {
                reader.open();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(2);
                return;
            }
        } else {
            try {
                reader = new PcapStreamReader(new FileInputStream(args[0]));
                reader.open();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(2);
                return;
            }
        }

        PacketProcessor processor = new PacketProcessor(reader);
        processor.setFlowTimeoutMillis(30000L);
        processor.run();
    }
}