package com.ekicam2.Engine.Editor;

import com.ekicam2.Engine.Engine;
import com.ekicam2.Engine.InputHandler;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class EditorInputHandler extends InputHandler {
    public EditorInputHandler(Engine InEngine) {
        super(InEngine);
    }

    @Override
    public void HandleGLFWInputs(long Window, int Key, int Scancode, int Action, int Mods) {
        if ( Key == GLFW_KEY_ESCAPE && Action == GLFW_RELEASE )
            glfwSetWindowShouldClose(Window, true);
    }
}
