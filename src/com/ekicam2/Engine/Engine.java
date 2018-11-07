package com.ekicam2.Engine;

import com.ekicam2.Engine.EngineUtils.MeshLoader;
import com.ekicam2.Engine.Rendering.Camera;
import com.ekicam2.Engine.Editor.Editor;
import com.ekicam2.Engine.Rendering.Renderer;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

public final class Engine {
    private Window MainWindow = null;
    private Renderer MainRenderer = null;
    private Editor EditorInstance = null;
    private IInputHandler InputHandler = null;
    private Mouse Mouse = new Mouse();
    //TODO: Scenes manager
    private Scene CurrentScene = new Scene();

    private long LastUpdateTimestamp;
    private float DeltaTime;

    public Window GetWindow() {
        return MainWindow;
    }
    public float GetDeltaTime() {
        return DeltaTime;
    }
    public boolean WithEditor() { return EditorInstance != null; }
    public Editor GetEditorInstance() {
        return EditorInstance;
    }
    public Scene GetCurrentScene() { return CurrentScene; }
    public Mouse GetMouse() { return Mouse; }

    public boolean Init() {
        if(!InitGLFW()) {
            return false;
        }

        MainWindow = new Window();
        if(!MainWindow.Init()) {
            return false;
        }

        MainRenderer = new Renderer(this);
        boolean bWithEditor = true;
        if(bWithEditor) {
            EditorInstance = new Editor(this);
        }
        InputHandler = WithEditor() ? EditorInstance.GetInputHandler() : new InputHandler(this);
        CurrentScene.SetActiveCamera(CurrentScene.AddCamera(new Camera()));

        InitInput();

        return true;
    }
    public void Run() {
    /* debug playground */
        CurrentScene.AddModel(MeshLoader.LoadOBJ("resources\\Models\\OBJ format\\chest.obj"));
        CurrentScene.GetModels().get(0).Transform.SetPosition(new Vector3f(0.0f, -0.0f, 520.0f));
        /* debug playground end */

        while ( !glfwWindowShouldClose(MainWindow.GetHandle()) ) {
            UpdateDeltaTime();
            glfwPollEvents();
            EditorInstance.Update(GetDeltaTime());
            CurrentScene.Update(GetDeltaTime());
            MainRenderer.RenderScene(CurrentScene);
        }
    }
    public void Terminate() {
        MainWindow.Free();

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        CurrentScene.Free();
        MainRenderer.Free();
    }

    private void UpdateDeltaTime() {
        long CurrentTimestamp = System.nanoTime();
        final int NanoToSec = 1_000_000_000;
        DeltaTime = (CurrentTimestamp - LastUpdateTimestamp) / 1_000_000_000.f;
        LastUpdateTimestamp = CurrentTimestamp;
    }

    private boolean InitGLFW() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() ) {
            System.err.println("Unable to initialize GLFW");
            return false;
        }

        return true;
    }

    private void InitInput() {
        glfwSetKeyCallback(GetWindow().GetHandle(), InputHandler::HandleGLFKeyboardInputs);
        //(window, key, scancode, action, mods) -> {
        //    HandleGLFKeyboardInputs(window, key, scancode, action, mods);
        //});

        glfwSetMouseButtonCallback(GetWindow().GetHandle(), InputHandler::HandleGLFMouseInputs);
        //(Window, Button, Action, Mods) -> {
        //    HandleGLFMouseInputs(Window, Button, Action, Mods);
        //});
    }
}
