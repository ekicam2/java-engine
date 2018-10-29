package com.ekicam2.Engine;

import com.ekicam2.Engine.Editor.EditorInputHandler;
import com.ekicam2.Engine.EngineUtils.MeshLoader;
import com.ekicam2.Engine.Rendering.Camera;
import com.ekicam2.Engine.Rendering.Material;
import com.ekicam2.Engine.Rendering.Model;
import com.ekicam2.Engine.Rendering.Renderer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class Engine {
    // The window handle
    private long window;
    private InputHandler InputHandler = new EditorInputHandler(this);
    private Renderer MainRenderer;

    public boolean Init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(1080, 1080, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            InputHandler.HandleGLFWInputs(window, key, scancode, action, mods);
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        MainRenderer = new Renderer();

        return true;
    }

    public void Run() {
        // Set the clear color
        glClearColor(0.60f, 0.20f, 0.70f, 0.0f);
        glEnable(GL_DEPTH_TEST);

        /* debug playground */
        Material mat = new Material();

        List<Model> ModelsToRender = new ArrayList<>();

        ModelsToRender.add(MeshLoader.LoadFBX("resources\\Models\\FBX format\\bottle.fbx"));
        ModelsToRender.add(MeshLoader.LoadFBX("resources\\Models\\FBX format\\cannonBall.fbx"));
        //ModelsToRender.get(1).Transform.SetRotation(new Vector3f(180.0f, -45.0f, 0.0f));
        //ModelsToRender.get(1).Transform.SetPosition(new Vector3f(0.0f, 0.0f, 100.0f));
        ModelsToRender.get(0).SetMaterial(0, mat);

        Camera CurrCamera = new Camera();
        // MainRenderer.Mode = Renderer.RenderMode.Wireframe;
        /* debug playground end */

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            /* debug playground */
            MainRenderer.DrawModels(CurrCamera, ModelsToRender);
            /* debug playground end */

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }

        ModelsToRender.forEach((model -> model.Free()));
    }

    public void Terminate() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
