package com.ekicam2.Engine;

import com.ekicam2.Engine.Editor.Editor;
import com.ekicam2.Engine.Inputs.IInputHandler;
import com.ekicam2.Engine.Inputs.InputHandler;
import com.ekicam2.Engine.Rendering.RenderThread.RenderingThread;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

//TODO: render thread
//TODO: make game playable without any rendering
//TODO: split Resources from graphics, audio, whatever


public final class Engine {
    public static boolean bWithEditor = true;

    private RenderingThread RenderThread = null;

    private Window MainWindow = null;
    private Editor EditorInstance = null;
    private IInputHandler InputHandler = null;
    //TODO: GameScenes

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
    public boolean Init() {
        if(!InitGLFW()) {
            return false;
        }

        MainWindow = new Window();
        if(!MainWindow.Init()) {
            return false;
        }
        RenderThread = new RenderingThread(this);

        if(bWithEditor) {
            EditorInstance = new Editor(this);
        }
        InputHandler = WithEditor() ? EditorInstance.GetInputHandler() : new InputHandler(this);

        InitInput();

        return true;
    }

    public void Run() {
    /* debug playground */

        // At the moment there are no scenes
        //
        // CurrentScene.AddModel(MeshLoader.LoadOBJ("resources\\Models\\OBJ format\\chest.obj"));
        // CurrentScene.GetModels().get(0).Transform.SetPosition(new Vector3f(0.0f, 0.0f, 20.0f));

    /* debug playground end */

        while ( !glfwWindowShouldClose(MainWindow.GetHandle()) ) {
            UpdateDeltaTime();
            glfwPollEvents();
            InputHandler.Update(GetDeltaTime());

            UpdateScene();
            PrepareAndRender();

        }
    }

    public void Terminate() {
        try{
            RenderThread.bShouldExit = true;
            RenderThread.Thread.join();
        } catch (Exception e)
        {
            e.getMessage();
        }
        MainWindow.Free();

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
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

    private void UpdateDeltaTime() {
        long CurrentTimestamp = System.nanoTime();
        final int NanoToSec = 1_000_000_000;
        DeltaTime = (CurrentTimestamp - LastUpdateTimestamp) / 1_000_000_000.f;
        LastUpdateTimestamp = CurrentTimestamp;
    }

    private void UpdateScene() {

    }

    private void PrepareAndRender() {
        RenderThread.Render(null);
    }

    private void InitInput() {
        glfwSetKeyCallback(GetWindow().GetHandle(), InputHandler::HandleGLFKeyboardInputs);
        glfwSetMouseButtonCallback(GetWindow().GetHandle(), InputHandler::HandleGLFMouseInputs);
    }
}
