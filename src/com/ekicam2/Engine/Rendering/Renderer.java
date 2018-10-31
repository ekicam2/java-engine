package com.ekicam2.Engine.Rendering;

import com.ekicam2.Engine.Engine;
import com.ekicam2.Engine.EngineUtils.EngineUtils;
import com.ekicam2.Engine.Rendering.OpenGL.VAO;
import com.ekicam2.Engine.Scene;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL45;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Renderer {
    public enum RenderMode {
        Wireframe,
        WireframeWithVertices,
        Shaded
    }

    public RenderMode Mode = RenderMode.Shaded;

    private Material newmat = null;
    private Engine Engine = null;

    //TODO: create materials manager
    public Renderer(Engine InEngine){
        Engine = InEngine;

        Shader vsShader = new Shader(Shader.Type.Fragment, EngineUtils.ReadFromFile("resources\\Shaders\\SimpleFragment.fs"));
        Shader fsShader = new Shader(Shader.Type.Vertex, EngineUtils.ReadFromFile("resources\\Shaders\\SimpleVertex.vs"));
        newmat = new Material(vsShader, fsShader);
        newmat.BindAttrib(0, "position");
        vsShader.Free();
        fsShader.Free();
    }

    public void Free() {
         newmat.Free();
    }

    public void RenderScene(Scene SceneToRender) {
        PrepareFrame();
        SetupRendererDependingOnMode();

        for(Model ModelEntity: SceneToRender.GetModels()) {
            Matrix4f MVP = new Matrix4f();
            SceneToRender.GetCurrentCamera().GetViewProjection().mul(ModelEntity.Transform.GetModel(), MVP);

            for(var Mesh : ModelEntity.GetMeshes())
            {
                Material MaterialToUse = ModelEntity.GetMaterial(Mesh.GetMaterialIndex());
                if(MaterialToUse != null) {
                    MaterialToUse.Bind();
                } else {
                    MaterialToUse = newmat;
                    MaterialToUse.Bind();
                }

                MaterialToUse.BindUniform("mvp", MVP);
                RenderVAO(Mesh.GetVAO());
            }
        }

        Present();
    }

    private void PrepareFrame() {
        GL45.glClearColor(0.60f, 0.20f, 0.70f, 0.0f);
        GL45.glEnable(GL45.GL_DEPTH_TEST);

        GL45.glClear(GL45.GL_COLOR_BUFFER_BIT | GL45.GL_DEPTH_BUFFER_BIT);
    }

    private void Present() {
        GLFW.glfwSwapBuffers(Engine.GetWindow());
    }

    private void SetupRendererDependingOnMode() {
        switch (Mode) {
            case Wireframe:
                GL45.glPolygonMode(GL45.GL_FRONT_AND_BACK, GL45.GL_LINE);
                break;
            case WireframeWithVertices:
                //TODO double pass
                //
                //
                //
                //
                //
                //
                break;
            case Shaded:
                GL45.glPolygonMode(GL45.GL_FRONT_AND_BACK, GL45.GL_FILL);
                break;
        }
    }

    private void RenderVAO(VAO VAOToRender) {
        VAOToRender.Bind();

        if(VAOToRender.GetIndicesCount() != 0) {
            GL45.glDrawElementsBaseVertex(GL45.GL_TRIANGLES, VAOToRender.GetIndicesCount(), GL45.GL_UNSIGNED_INT, 0L, 0);
        } else {
            GL45.glDrawArrays(GL45.GL_TRIANGLES, 0, VAOToRender.GetVerticesCount());
        }

        VAOToRender.Unbind();
    }
}
