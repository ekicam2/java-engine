package com.ekicam2.Engine;

public abstract class  InputHandler {

    private Engine Engine = null;
    public InputHandler(Engine InEngine) {
        Engine = InEngine;
    }

    public abstract void HandleGLFWInputs(long Window, int Key, int Scancode, int Action, int Mods);
}
