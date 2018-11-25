package com.ekicam2.Engine.Rendering.RenderThread;

import com.ekicam2.Engine.Components.Transform;
import com.ekicam2.Engine.Engine;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;

public class RenderingThread implements Runnable {
    public class Entity {
        int ModelId;
        Transform Transform;

    }

    public boolean bShouldExit = false;

    public Thread Thread;
    Renderer MainRenderer = null;
    List<Entity> RenderScene = null;
    //Long Window = 0L;
    Engine Engine;

    public RenderingThread(Engine InEngine)
    {
        Engine = InEngine;
        Thread = new Thread(this, "RenderingThread");
        Thread.start();
    }

    @Override
    public void run() {
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 5);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
        GLFW.glfwMakeContextCurrent(Engine.GetWindow().GetHandle());

        MainRenderer = new Renderer(Engine.GetWindow());

        while(!bShouldExit)
        {
            try {
                if(RenderScene != null)
                {
                    GLFW.glfwMakeContextCurrent(Engine.GetWindow().GetHandle());

                    MainRenderer.RenderScene(RenderScene);
                }
                else
                {
                    sleep(200);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.err.println(e.getCause());
                System.err.println(e.getStackTrace());
                bShouldExit = true;
            }
        }
    }

    public void Render(List<Entity> InRenderScene) {
        RenderScene = new ArrayList<>();
    }
}
