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
    public boolean HandleGLFKeyboardWInputs(long Window, int Key, int Scancode, int Action, int Mods) {
        if ( Key == GLFW_KEY_ESCAPE && Action == GLFW_RELEASE ) {
            glfwSetWindowShouldClose(Window, true);
            return true;
        }

        return false;
    }

    @Override
    public boolean HandleGLFMouseWInputs(long Window, int Button, int Action, int Mods) {
        if(Button == GLFW_MOUSE_BUTTON_LEFT && Action == GLFW_PRESS) {
            double[] X = {0};
            double[] Y = {0};

            glfwGetCursorPos(Window, X, Y);
            Engine.GetEditorInstance().GetScenePicker().GetObjectID((int)X[0], Engine.GetWindowHeight() - (int)Y[0]);
            return true;
        }

        return false;
    }
}
