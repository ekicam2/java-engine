package com.ekicam2.Engine.Editor;

import com.ekicam2.Engine.Engine;
import com.ekicam2.Engine.InputHandler;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class EditorIInputHandler extends InputHandler {
    public EditorIInputHandler(Engine InEngine) {
        super(InEngine);
    }

    @Override
    public boolean HandleGLFKeyboardInputs(long Window, int Key, int Scancode, int Action, int Mods) {
        if ( Key == GLFW_KEY_ESCAPE && Action == GLFW_RELEASE ) {
            glfwSetWindowShouldClose(Window, true);
            return true;
        }

        return super.HandleGLFKeyboardInputs(Window, Key, Scancode, Action, Mods);
    }

    @Override
    public boolean HandleGLFMouseInputs(long Window, int Button, int Action, int Mods) {
        return super.HandleGLFMouseInputs(Window, Button, Action, Mods);
    }
}
