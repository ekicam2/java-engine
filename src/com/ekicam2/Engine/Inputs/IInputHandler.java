package com.ekicam2.Engine.Inputs;


import com.ekicam2.Engine.Engine;

public abstract class IInputHandler {

    protected com.ekicam2.Engine.Engine Engine = null;
    public IInputHandler(Engine InEngine) {
        Engine = InEngine;
    }

    public abstract void Update(float DeltaTime);

    public abstract boolean HandleGLFKeyboardInputs(long Window, int Key, int Scancode, int Action, int Mods);
    public abstract boolean HandleGLFMouseInputs(long Window, int Button, int Action, int Mods);
}
