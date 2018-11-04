package com.ekicam2.Engine;

public class InputHandler extends IInputHandler {
    public InputHandler(Engine InEngine) {
        super(InEngine);
    }

    public boolean HandleGLFKeyboardWInputs(long Window, int Key, int Scancode, int Action, int Mods) {
        return false;
    }

    public boolean HandleGLFMouseWInputs(long Window, int Button, int Action, int Mods) {
        return false;
    }
}
