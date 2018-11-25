package com.ekicam2.Engine.Editor;

import com.ekicam2.Engine.Engine;
import com.ekicam2.Engine.Inputs.InputHandler;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class EditorIInputHandler extends InputHandler {
    Editor Editor;
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
        boolean Returner = super.HandleGLFMouseInputs(Window, Button, Action, Mods);
        if(!Returner) {
            //fill it up
        }

        return Returner;
    }

    private void SelectObjectRequest() {
/*        var Offset = new Vector3f();
        Offset.x = (Mouse.GetCursorPos().x);// - Mouse.GetCursorPressedPos().x);
        Offset.y = (Mouse.GetCursorPos().y);// - Mouse.GetCursorPressedPos().y);

        Offset.x = ((Offset.x / (float)Engine.GetWindow().GetWindowWidth()) * 2.0f) - 1.0f;
        Offset.y = ((Offset.y / (float)Engine.GetWindow().GetWindowHeight()) * 2.0f) - 1.0f;
        Offset.z = 0.882f;

        var Cam = Engine.GetCurrentScene().GetCurrentCamera();
        var InvVP = Cam.GetViewProjection().invert();
        Vector4f Offset4d = (new Vector4f(Offset.x, Offset.y, Offset.z, 1.0f)).mul(InvVP);

        Offset4d.x /= Offset4d.w;
        Offset4d.y /= Offset4d.w;
        Offset4d.z /= Offset4d.w;

        if(Editor.GetSelectedObject() != null)
        {
            var NewPos = new Vector3f();
            NewPos.x = Offset4d.x;
            NewPos.y = Offset4d.y;
            NewPos.z = Editor.GetSelectedObject().Transform.GetPosition().z;

            Editor.GetSelectedObject().Transform.SetPosition(NewPos);
        } else {
            // Create Model and set its' position
            // M.Transform.SetPosition(new Vector3f(Offset4d.x, Offset4d.y, Offset4d.z));
        }
*/
    }
}
