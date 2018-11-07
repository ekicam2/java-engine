package com.ekicam2.Engine;


public abstract class IInputHandler {

    protected Engine Engine = null;
    public IInputHandler(Engine InEngine) {
        Engine = InEngine;
    }

    public abstract boolean HandleGLFKeyboardInputs(long Window, int Key, int Scancode, int Action, int Mods);
    public abstract boolean HandleGLFMouseInputs(long Window, int Button, int Action, int Mods);
}
