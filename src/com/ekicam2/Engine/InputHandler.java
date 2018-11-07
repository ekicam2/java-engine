package com.ekicam2.Engine;

import org.joml.Vector2i;

import static org.lwjgl.glfw.GLFW.*;

public class InputHandler extends IInputHandler {
    public InputHandler(Engine InEngine) {
        super(InEngine);
    }

    public boolean HandleGLFKeyboardInputs(long Window, int Key, int Scancode, int Action, int Mods) {
        return false;
    }
    public boolean HandleGLFMouseInputs(long Window, int Button, int Action, int Mods) {
        GatherMouseData(Window, Button, Action);
        return false;
    }

    private void GatherMouseData(long Window, int Button, int Action) {
        double[] X = {0};
        double[] Y = {0};
        glfwGetCursorPos(Window, X, Y);
        var Mouse = Engine.GetMouse();

        if(Button == GLFW_MOUSE_BUTTON_LEFT) {
            if(Action == GLFW_RELEASE) {
                Mouse.SetLeftButtonState(true);
                Mouse.SetCursorPressedPos(new Vector2i((int)X[0], (int)Y[0]));
            } else if(Action == GLFW_PRESS) {
                Mouse.SetLeftButtonState(false);
                Mouse.SetCursorReleasedPos(new Vector2i((int)X[0], (int)Y[0]));
            }
        }

        if(Button == GLFW_MOUSE_BUTTON_RIGHT) {
            Mouse.SetRightButtonState(Action == GLFW_RELEASE ? true : false);
        }
    }
}
