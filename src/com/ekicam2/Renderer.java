package com.ekicam2;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL45;

import java.util.List;

public class Renderer {
    public enum RenderMode {
        Wireframe,
        WireframeWithVertices,
        Shaded
    }

    public Material CurrentMaterial;

    public RenderMode Mode = RenderMode.Shaded;

    public void DrawModels(Camera CurrentCamera, List<Model> Models) {
        SetupRendererDependingOnMode();

        for(Model ModelEntity: Models) {
            Matrix4f MVP = new Matrix4f();

            CurrentCamera.GetViewProjection().mul(ModelEntity.Transform.GetModel(), MVP);
            CurrentMaterial.BindUniform("mvp", MVP);

            for(var Mesh : ModelEntity.GetMeshes())
            {
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
