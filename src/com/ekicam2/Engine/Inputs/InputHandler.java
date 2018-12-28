package com.ekicam2.Engine.Inputs;

import org.joml.Vector2i;

import static org.lwjgl.glfw.GLFW.*;

public class InputHandler extends IInputHandler {
    public InputHandler(com.ekicam2.Engine.Engine InEngine) {
        super(InEngine);
    }

    protected com.ekicam2.Engine.Inputs.Mouse Mouse = new Mouse();

    public boolean HandleGLFKeyboardInputs(long Window, int Key, int Scancode, int Action, int Mods) {
        return false;
    }
    public boolean HandleGLFMouseInputs(long Window, int Button, int Action, int Mods) {
        UpdateMouseState(Window, Button, Action);
        return false;
    }

    @Override
    public void Update(float DeltaTime) {
        UpdateMousePosition();
        Mouse.Update(DeltaTime);
    }

    private void UpdateMousePosition() {
        double[] X = {0};
        double[] Y = {0};

        glfwGetCursorPos(Engine.GetWindow().GetHandle(), X, Y);
        Mouse.SetCursorPos(new Vector2i((int)X[0], Engine.GetWindow().GetWindowHeight() - (int)Y[0]));
    }

    private void UpdateMouseState(long Window, int Button, int Action) {
        if(Button == GLFW_MOUSE_BUTTON_LEFT) {
            Mouse.SetLeftButtonState(Action == GLFW_PRESS ? true : false);
        }

        if(Button == GLFW_MOUSE_BUTTON_RIGHT) {
            Mouse.SetRightButtonState(Action == GLFW_PRESS ? true : false);
        }
    }
}
