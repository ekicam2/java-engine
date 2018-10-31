package com.ekicam2.Engine;

import com.ekicam2.Engine.Editor.EditorInputHandler;
import com.ekicam2.Engine.EngineUtils.MeshLoader;
import com.ekicam2.Engine.Rendering.Camera;
import com.ekicam2.Engine.Rendering.Renderer;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class Engine {
    // The Widnow handle
    private long Widnow;
    private InputHandler InputHandler = new EditorInputHandler(this);
    private Renderer MainRenderer;
    //TODO: Scenes manager
    private Scene CurrentScene = new Scene();

    private long LastUpdateTimestamp;
    private float DeltaTime;

    public long GetWindow() {
        return Widnow;
    }

    public float GetDeltaTime() {
        return DeltaTime;
    }

    public boolean Init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current Widnow hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the Widnow will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the Widnow will be resizable

        // Create the Widnow
        Widnow = glfwCreateWindow(1080, 1080, "Hello World!", NULL, NULL);
        if ( Widnow == NULL )
            throw new RuntimeException("Failed to create the GLFW Widnow");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(Widnow, (window, key, scancode, action, mods) -> {
            InputHandler.HandleGLFWInputs(window, key, scancode, action, mods);
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the Widnow size passed to glfwCreateWindow
            glfwGetWindowSize(Widnow, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the Widnow
            glfwSetWindowPos(
                    Widnow,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(Widnow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the Widnow visible
        glfwShowWindow(Widnow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        MainRenderer = new Renderer(this);
        CurrentScene.SetActiveCamera(CurrentScene.AddCamera(new Camera()));

        return true;
    }

    public void Run() {
        /* debug playground */
        CurrentScene.AddModel(MeshLoader.LoadOBJ("resources\\Models\\OBJ format\\chest.obj"));
        CurrentScene.GetModels().get(0).Transform.SetPosition(new Vector3f(0.0f, -0.0f, 520.0f));
        /* debug playground end */


        while ( !glfwWindowShouldClose(Widnow) ) {
            UpdateDeltaTime();
            glfwPollEvents();
            CurrentScene.Update(DeltaTime);
            MainRenderer.RenderScene(CurrentScene);

            System.out.println("---------------------------");
            System.out.printf("%.5f \n", GetDeltaTime());
            System.out.println(DeltaTime);
        }
    }

    private void UpdateDeltaTime() {
        long CurrentTimestamp = System.nanoTime();
        final int NanoToMili = 1000000;
        DeltaTime = (float) (CurrentTimestamp - LastUpdateTimestamp) * NanoToMili;
        LastUpdateTimestamp = CurrentTimestamp;
    }

    public void Terminate() {
        // Free the Widnow callbacks and destroy the Widnow
        glfwFreeCallbacks(Widnow);
        glfwDestroyWindow(Widnow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        CurrentScene.Free();
    }
}
