package com.ekicam2.Engine.GameFoundation.Objects;

public abstract class Base {
    private int ID;

    public Base(int inID) {
        ID = inID;
    }

    public int GetID() {
        return ID;
    }
}
