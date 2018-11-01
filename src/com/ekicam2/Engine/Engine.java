package com.ekicam2.Engine;

import com.ekicam2.Engine.Editor.EditorInputHandler;
import com.ekicam2.Engine.EngineUtils.MeshLoader;
import com.ekicam2.Engine.Rendering.Camera;
import com.ekicam2.Engine.Editor.Editor;
import com.ekicam2.Engine.Rendering.Renderer;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL45;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class Engine {
    // The Window handle
    private long Window;

    private int WindowWidth = 1080;
    private int WindowHeight = 1080;

    private Renderer MainRenderer = null;
    private Editor EditorInstance = null;
    //TODO: Scenes manager
    private Scene CurrentScene = new Scene();

    private long LastUpdateTimestamp;
    private float DeltaTime;

    public long GetWindow() {
        return Window;
    }

    public float GetDeltaTime() {
        return DeltaTime;
    }

    public Editor GetEditorInstance() {
        return EditorInstance;
    }

    public int GetWindowWidth() { return WindowWidth; }
    public int GetWindowHeight() {return WindowHeight;}

    public boolean Init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        //glfwDefaultWindowHints(); // optional, the current Window hints are already the default
        //glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the Window will stay hidden after creation
        //glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the Window will be resizable
        glfwWindowHint(GLFW_RED_BITS, vidmode.redBits());
        glfwWindowHint(GLFW_GREEN_BITS, vidmode.greenBits());
        glfwWindowHint(GLFW_BLUE_BITS, vidmode.blueBits());
        glfwWindowHint(GLFW_REFRESH_RATE, vidmode.refreshRate());

        WindowWidth = vidmode.width();
        WindowHeight = vidmode.height();

        // Create the Window
        Window = glfwCreateWindow(WindowWidth, WindowHeight, "Hello World!", glfwGetPrimaryMonitor(), NULL);
        if ( Window == NULL )
            throw new RuntimeException("Failed to create the GLFW Window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(Window, (window, key, scancode, action, mods) -> {
            boolean bWasHandled = EditorInstance.GetInputHandler().HandleGLFKeyboardWInputs(window, key, scancode, action, mods);
            //TODO: move to an engine method
        });

        glfwSetMouseButtonCallback(Window, (Window, Button, Action, Mods) -> {
            boolean bWasHandled = EditorInstance.GetInputHandler().HandleGLFMouseWInputs(Window, Button, Action, Mods);
            //TODO: move to an engine method
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the Window size passed to glfwCreateWindow
            glfwGetWindowSize(Window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            //GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the Window
            glfwSetWindowPos(
                    Window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(Window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the Window visible
        glfwShowWindow(Window);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        GL45.glViewport(0, 0, 1080, 1080);

        MainRenderer = new Renderer(this);
        EditorInstance = new Editor(this);
        CurrentScene.SetActiveCamera(CurrentScene.AddCamera(new Camera()));

        return true;
    }

    public void Run() {
        /* debug playground */
        CurrentScene.AddModel(MeshLoader.LoadOBJ("resources\\Models\\OBJ format\\chest.obj"));
        CurrentScene.GetModels().get(0).Transform.SetPosition(new Vector3f(0.0f, -0.0f, 520.0f));
        /* debug playground end */

        while ( !glfwWindowShouldClose(Window) ) {
            UpdateDeltaTime();
            glfwPollEvents();
            CurrentScene.Update(GetDeltaTime());
            MainRenderer.RenderScene(CurrentScene);
        }
    }

    private void UpdateDeltaTime() {
        long CurrentTimestamp = System.nanoTime();
        final int NanoToMili = 1000000;
        DeltaTime = (float) (CurrentTimestamp - LastUpdateTimestamp) * NanoToMili;
        LastUpdateTimestamp = CurrentTimestamp;
    }

    public void Terminate() {
        // Free the Window callbacks and destroy the Window
        glfwFreeCallbacks(Window);
        glfwDestroyWindow(Window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        CurrentScene.Free();
        MainRenderer.Free();
    }
}
