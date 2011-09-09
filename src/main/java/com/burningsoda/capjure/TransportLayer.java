package com.burningsoda.capjure;

public class TransportLayer extends PacketLayer {
    protected InternetLayer parent;

    public TransportLayer(InternetLayer parent) {
        this.parent = parent;
    }

    public ApplicationLayer getApplicationLayer() {
        return null;
    }

    public InternetLayer getInternetLayer() {
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
