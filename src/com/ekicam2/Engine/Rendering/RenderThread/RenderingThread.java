package com.ekicam2.Engine.Rendering.RenderThread;

import com.ekicam2.Engine.Engine;
import com.ekicam2.Engine.GameFoundation.Components.Camera;
import com.ekicam2.Engine.GameFoundation.Objects.DrawableObject;
import com.ekicam2.Engine.Resources.StaticMeshResource;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RenderingThread implements Runnable {
    final Lock Lock = new ReentrantLock();
    final Condition CanRenderCond = Lock.newCondition();
    boolean bShouldWait = true;

    boolean bIsInitialized = false;

    boolean bShouldExit = false;
    public Thread Thread;
    Renderer MainRenderer = null;
    Engine Engine;

    Renderer.RenderScene RenderScene;

    ResourceManager ResourceManager = new ResourceManager();

    List<DrawableObject> GTRenderScene;
    Camera GTCamera;

    public RenderingThread(Engine InEngine) {
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
        bIsInitialized = true;

        while(!bShouldExit)
        {
            Lock.lock();

            try {
                while(bShouldWait)
                    CanRenderCond.await();

                if(PrepareRenderScene(GTRenderScene, GTCamera))
                    MainRenderer.RenderScene(RenderScene);

                bShouldWait = true;
            } catch (Exception e) {
                System.err.println("Rendering Thread.java:62");
                System.err.println();
                System.err.println("exception catched: " + e.getMessage());
                System.err.println("stack was as follows");
                for(int i = 0; i < e.getStackTrace().length; ++i) {
                    System.err.println(e.getStackTrace()[i]);
                }

                bShouldExit = true;
            }

            Lock.unlock();
        }
    }

    public void Render(List<DrawableObject> InRenderScene, Camera InCamera) {
        Lock.lock();

        GTRenderScene = InRenderScene;
        GTCamera = InCamera;
        CanRenderCond.signal();
        bShouldWait = false;

        Lock.unlock();
    }

    public void Terminate() {
        bShouldExit = true;
        ResourceManager.Free();
    }

    private boolean PrepareRenderScene(List<DrawableObject> InRenderScene, Camera InCamera) {
        if(InRenderScene == null)
            return false;

        Renderer.RenderScene RenderScene = new Renderer.RenderScene();
        RenderScene.ObjectsToRender.clear();
        RenderScene.Camera = InCamera;

        for(DrawableObject Drawable : InRenderScene) {
            StaticMeshResource MeshResource = Drawable.DrawableComponent.GetMesh();
            if(MeshResource == null) {
                System.err.println("trying to render null mesh");
                continue;
            }

            var VAOs = ResourceManager.GetOrCreateVAOs(MeshResource);
            for(int i = 0; i < VAOs.size(); ++i) {
                var MaterialResource = Drawable.DrawableComponent.GetMaterialAt(MeshResource.Meshes.get(i).MaterialIndex);
                if(MaterialResource == null) {
                    System.err.println("mesh " + MeshResource.Name + " was tied to render without material at index " + i + " dreawing with spicy magenta");
                    MaterialResource = Drawable.DrawableComponent.GetMaterialAt(0);// 0 = default
                    continue;
                }

                var Material = ResourceManager.GetOrCreateProgram(MaterialResource);
                RenderScene.ObjectsToRender.add(new Renderer.RenderObject( VAOs.get(i), Material, Drawable.Transform.GetModel()));
            }



        }

        this.RenderScene = RenderScene;
        return true;
    }
}
