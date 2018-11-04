package com.ekicam2.Engine;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private long Window;

    private int WindowWidth = 1080;
    private int WindowHeight = 1080;

    private boolean bIsFullscreen = false;

    public int GetWindowWidth() { return WindowWidth; }
    public int GetWindowHeight() {return WindowHeight;}
    public long GetHandle() { return Window; }

    public boolean Init() {
        // Configure GLFW
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if(!bIsFullscreen) {
            glfwDefaultWindowHints(); // optional, the current Window hints are already the default
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the Window will stay hidden after creation
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the Window will be resizable
        }
        glfwWindowHint(GLFW_RED_BITS, vidmode.redBits());
        glfwWindowHint(GLFW_GREEN_BITS, vidmode.greenBits());
        glfwWindowHint(GLFW_BLUE_BITS, vidmode.blueBits());
        glfwWindowHint(GLFW_REFRESH_RATE, vidmode.refreshRate());

        WindowWidth = bIsFullscreen ? vidmode.width() : WindowWidth;
        WindowHeight = bIsFullscreen ? vidmode.height() : WindowHeight;
        long MonitorID = bIsFullscreen ? glfwGetPrimaryMonitor() : NULL;

        // Create the Window
        Window = glfwCreateWindow(WindowWidth, WindowHeight, "Hello World!", MonitorID, NULL);
        if ( Window == NULL ) {
            System.err.println("Failed to create the GLFW Window");
            return false;
        }

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

        return true;
    }

    public void Free() {
        // Free the Window callbacks and destroy the Window
        glfwFreeCallbacks(Window);
        glfwDestroyWindow(Window);
    }
}
