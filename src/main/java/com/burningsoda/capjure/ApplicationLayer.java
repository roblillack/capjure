package com.burningsoda.capjure;

public class ApplicationLayer extends PacketLayer {
    protected TransportLayer parent;

    public ApplicationLayer(TransportLayer p) {
        parent = p;
    }

    public TransportLayer getTransportLayerPacket() {
        return parent;
    }

    @Override
    public PacketLayer getParentLayer() {
        return parent;
    }

    @Override
    public int getHeaderSize() {
        return 0;
    }
}
