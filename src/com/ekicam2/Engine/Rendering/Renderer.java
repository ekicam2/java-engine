package com.ekicam2.Engine.Rendering;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL45;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Renderer {
    public enum RenderMode {
        Wireframe,
        WireframeWithVertices,
        Shaded
    }

    public RenderMode Mode = RenderMode.Shaded;


    Material newmat = null;

    //TODO: create materials manager
    public Renderer(){

        Shader vsShader = null;
        Shader fsShader = null;
        try {
            vsShader = new Shader(Shader.Type.Fragment, new String(Files.readAllBytes(Paths.get("resources\\Shaders\\SimpleFragment.fs")), StandardCharsets.UTF_8));
            fsShader = new Shader(Shader.Type.Vertex, new String(Files.readAllBytes(Paths.get("resources\\Shaders\\SimpleVertex.vs")), StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        newmat = new Material(vsShader, fsShader);
    }
    public void DrawModels(Camera CurrentCamera, List<Model> Models) {
        SetupRendererDependingOnMode();

        for(Model ModelEntity: Models) {
            Matrix4f MVP = new Matrix4f();
            CurrentCamera.GetViewProjection().mul(ModelEntity.Transform.GetModel(), MVP);


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

        VAO.Unbind();
    }
}
