package com.ekicam2.Engine;

import com.ekicam2.Engine.Editor.Editor;
import com.ekicam2.Engine.GameFoundation.Components.Camera;
import com.ekicam2.Engine.GameFoundation.Objects.DrawableObject;
import com.ekicam2.Engine.GameFoundation.Scene;
import com.ekicam2.Engine.Inputs.IInputHandler;
import com.ekicam2.Engine.Inputs.InputHandler;
import com.ekicam2.Engine.Rendering.RenderThread.RenderingThread;
import com.ekicam2.Engine.Resources.ResourceManager;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

public final class Engine {
    public static boolean bWithEditor = true;
    com.ekicam2.Engine.Resources.ResourceManager ResourceManager = new ResourceManager();


    private RenderingThread RenderThread = null;

    private Window MainWindow = null;
    private Editor EditorInstance = null;
    private IInputHandler InputHandler = null;
    private long LastUpdateTimestamp;
    private float DeltaTime;
    private Scene MainScene = new Scene();


    public Window GetWindow() {
        return MainWindow;
    }
    public float GetDeltaTime() {
        return DeltaTime;
    }
    public boolean WithEditor() { return EditorInstance != null; }
    public ResourceManager GetResourceManager() { return ResourceManager; }

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

        MainScene.AddCamera(new Camera());
        var d1 = new DrawableObject();
        d1.DrawableComponent.SetMesh(ResourceManager.GetOrLoadMesh("palm_detailed_long"));
        for(int i = 0; i < d1.DrawableComponent.GetRequiredMaterialsNum(); ++i)
            d1.DrawableComponent.SetMaterial(i, ResourceManager.GetOrLoadMesh("palm_detailed_long").Materials.get(i));
        d1.Transform.SetPosition(new Vector3f(20.0f, -2.0f, 50.0f));

        var d2 = new DrawableObject();
        d2.DrawableComponent.SetMesh(ResourceManager.GetOrLoadMesh("ship_light"));
        for(int i = 0; i < d2.DrawableComponent.GetRequiredMaterialsNum(); ++i)
            d2.DrawableComponent.SetMaterial(i, ResourceManager.GetOrLoadMesh("ship_light").Materials.get(i));
        d2.Transform.SetPosition(new Vector3f(-20.0f, -16.90f, 80.0f));

        MainScene.AddDrawable(d1);
        MainScene.AddDrawable(d2);

        while ( !glfwWindowShouldClose(MainWindow.GetHandle()) ) {
            UpdateDeltaTime();
            glfwPollEvents();
            InputHandler.Update(GetDeltaTime());

            UpdateScene();
            RenderScene();

        }
    }

    public void Terminate() {
        try{
            RenderThread.Terminate();
            RenderThread.Thread.join();
        } catch (Exception e) {
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
        MainScene.Update(GetDeltaTime());
    }

    private void RenderScene() {
        RenderThread.Render(MainScene.GetDrawables(), MainScene.GetCurrentCamera());
    }

    private void InitInput() {
        glfwSetKeyCallback(GetWindow().GetHandle(), InputHandler::HandleGLFKeyboardInputs);
        glfwSetMouseButtonCallback(GetWindow().GetHandle(), InputHandler::HandleGLFMouseInputs);
    }
}
