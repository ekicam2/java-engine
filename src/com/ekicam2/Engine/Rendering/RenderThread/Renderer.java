package com.ekicam2.Engine.Rendering.RenderThread;

import com.ekicam2.Engine.GameFoundation.Components.Camera;
import com.ekicam2.Engine.Rendering.OpenGL.Program;
import com.ekicam2.Engine.Rendering.OpenGL.VAO;
import com.ekicam2.Engine.Window;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL45;

import java.util.ArrayList;
import java.util.List;

//TODO: lights
//TODO: shadows

class Renderer {
    public static class RenderObject {
        RenderObject(VAO InVAO, Program InProgram, Matrix4f InModel) {
            VAO = InVAO;
            Program = InProgram;
            Model = InModel;
        }

        RenderObject(VAO InVAO, Program InProgram) {
            VAO = InVAO;
            Program = InProgram;
            Model = new Matrix4f().identity();
        }

        VAO VAO;
        Program Program;
        Matrix4f Model;
    }

    public static class RenderScene {
        RenderScene() {
            ObjectsToRender = new ArrayList<>();
        }

        boolean IsValid() {
            if(Camera == null || ObjectsToRender == null)
                return false;

            return true;
        }

        Camera Camera;
        List<RenderObject> ObjectsToRender;
    }

    public enum RenderMode {
        Wireframe,
        Shaded
    }

    public RenderMode Mode = RenderMode.Shaded;



    Window Window = null;

    public Renderer(Window InWindow){
        Window = InWindow;

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Enable v-sync
        GLFW.glfwSwapInterval(1);

        GL45.glViewport(0, 0, Window.GetWindowWidth(), Window.GetWindowHeight());
    }

    public void Free() {

    }

    public void RenderScene(RenderScene SceneToRender) {
        PrepareFrame();
        SetupRendererDependingOnMode();

        if(!SceneToRender.IsValid())
            return;

        // fist phase render to the default Framebuffer
        for(RenderObject ObjectToRender : SceneToRender.ObjectsToRender) {
            Matrix4f MVP = new Matrix4f();

            var tempoffset = new Matrix4f();
            tempoffset.setTranslation(0.0f, 0.0f, 160.0f);

            SceneToRender.Camera.GetViewProjection().mul(ObjectToRender.Model, MVP);
            //for(var Mesh : ObjectToRender.GetMeshes())
            {
                Program MaterialToUse = ObjectToRender.Program;//ModelEntity.GetMaterial(Mesh.GetMaterialIndex());
                MaterialToUse.Bind();

                MaterialToUse.BindUniform("mvp", MVP);
                MaterialToUse.BindUniform("in_color", 0.85f,0.30f, 0.2f);
                RenderVAO(ObjectToRender.VAO);
            }
        }


        /*if(false)
        {
         //   Engine.GetEditorInstance().PrepareForRendering();
            Program MaterialToUse = objid;
            MaterialToUse.Bind();

            for(Model ModelEntity: SceneToRender.GetModels()) {
                Matrix4f MVP = new Matrix4f();
                SceneToRender.GetCurrentCamera().GetViewProjection().mul(ModelEntity.Transform.GetModel(), MVP);

                MaterialToUse.BindUniform("mvp", MVP);
                MaterialToUse.BindUniform("object_id", 1.0f,1.0f, 45.0f);

                for(var Mesh : ModelEntity.GetMeshes())
                {
                    //RenderVAO(Mesh.GetVAO());
                }
            }

            //  GL45.glBindFramebuffer(GL45.GL_DRAW_FRAMEBUFFER, 0);

        }*/

        Present();
    }

    private void PrepareFrame() {
       GL45.glBindFramebuffer(GL45.GL_DRAW_FRAMEBUFFER, 0);

        GL45.glClearColor(0.0f, 1.0f, 0.00f, 0.0f);
        GL45.glEnable(GL45.GL_DEPTH_TEST);

        GL45.glClear(GL45.GL_COLOR_BUFFER_BIT | GL45.GL_DEPTH_BUFFER_BIT);
    }

    private void Present() {
        GL45.glBindFramebuffer(GL45.GL_READ_FRAMEBUFFER, 0);

        GLFW.glfwSwapBuffers(Window.GetHandle());
    }

    private void SetupRendererDependingOnMode() {
        switch (Mode) {
            case Wireframe:
                GL45.glPolygonMode(GL45.GL_FRONT_AND_BACK, GL45.GL_LINE);
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

