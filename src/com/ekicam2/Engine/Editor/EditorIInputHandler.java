package com.ekicam2.Engine.Editor;

import com.ekicam2.Engine.Engine;
import com.ekicam2.Engine.InputHandler;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class EditorIInputHandler extends InputHandler {
    public EditorIInputHandler(Engine InEngine) {
        super(InEngine);
    }

    @Override
    public boolean HandleGLFKeyboardWInputs(long Window, int Key, int Scancode, int Action, int Mods) {
        if ( Key == GLFW_KEY_ESCAPE && Action == GLFW_RELEASE ) {
            glfwSetWindowShouldClose(Window, true);
            return true;
        }

        if(Key == GLFW_KEY_RIGHT) {
            if(Action == GLFW_PRESS) {
                Engine.test = true;
            } else if(Action == GLFW_RELEASE)
            {
                Engine.test = false;
            }

            return true;
        }

        return super.HandleGLFKeyboardWInputs(Window, Key, Scancode, Action, Mods);
    }

    @Override
    public boolean HandleGLFMouseWInputs(long Window, int Button, int Action, int Mods) {
        if(Button == GLFW_MOUSE_BUTTON_LEFT && Action == GLFW_PRESS) {
            double[] X = {0};
            double[] Y = {0};

            glfwGetCursorPos(Window, X, Y);
            Engine.GetEditorInstance().SelectObject(Engine.GetEditorInstance().GetScenePicker().GetObjectID((int)X[0], Engine.GetWindow().GetWindowHeight() - (int)Y[0]));
            return true;
        } else if(Button == GLFW_MOUSE_BUTTON_LEFT && Action == GLFW_RELEASE) {

            return true;
        }

        return super.HandleGLFMouseWInputs(Window, Button, Action, Mods);
    }
}
