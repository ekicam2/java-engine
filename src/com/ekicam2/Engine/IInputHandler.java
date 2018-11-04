package com.ekicam2.Engine;


public abstract class IInputHandler {

    protected Engine Engine = null;
    public IInputHandler(Engine InEngine) {
        Engine = InEngine;
    }

    public abstract boolean HandleGLFKeyboardWInputs(long Window, int Key, int Scancode, int Action, int Mods);
    public abstract boolean HandleGLFMouseWInputs(long Window, int Button, int Action, int Mods);
}
