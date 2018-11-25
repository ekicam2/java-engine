package com.ekicam2.Engine.Rendering.RenderThread;

import com.ekicam2.Engine.EngineUtils.EngineUtils;
import com.ekicam2.Engine.Rendering.OpenGL.Program;
import com.ekicam2.Engine.Rendering.OpenGL.Shader;
import com.ekicam2.Engine.Rendering.OpenGL.VAO;
import com.ekicam2.Engine.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL45;

import java.util.List;

//TODO: lights
//TODO: shadows
//TODO: make Renderer TOTALY INDEPENDENT FROM ENGINE

class Renderer {
    public enum RenderMode {
        Wireframe,
        WireframeWithVertices,
        Shaded
    }

    public RenderMode Mode = RenderMode.Shaded;

    private Program newmat = null;
    private Program objid  = null;

    Window Window = null;

    //TODO: create materials manager
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

        Shader vsShader = new Shader(Shader.Type.Vertex, EngineUtils.ReadFromFile("resources\\Shaders\\SimpleVertex.vs"));
        Shader fsShader = new Shader(Shader.Type.Fragment, EngineUtils.ReadFromFile("resources\\Shaders\\SimpleFragment.fs"));
        newmat = new Program(vsShader, fsShader);
        newmat.BindAttrib(0, "position");
        fsShader.Free();

        Shader fsIdShader = new Shader(Shader.Type.Fragment, EngineUtils.ReadFromFile("resources\\Shaders\\ObjID.fs"));
        objid = new Program(vsShader, fsIdShader);
        objid.BindAttrib(0, "position");
        vsShader.Free();
        fsIdShader.Free();
    }

    public void Free() {
         newmat.Free();
         objid.Free();
    }

    public void RenderScene(List<RenderingThread.Entity> SceneToRender) {
        PrepareFrame();
        SetupRendererDependingOnMode();

        /*
        // fist phase render to the default Framebuffer
        for(Model ModelEntity: SceneToRender.GetModels()) {
            Matrix4f MVP = new Matrix4f();
            SceneToRender.GetCurrentCamera().GetViewProjection().mul(ModelEntity.Transform.GetModel(), MVP);

            for(var Mesh : ModelEntity.GetMeshes())
            {
                Program MaterialToUse = null;//ModelEntity.GetMaterial(Mesh.GetMaterialIndex());
                if(MaterialToUse == null) {
                    MaterialToUse = newmat;
                }
                MaterialToUse.Bind();

                MaterialToUse.BindUniform("mvp", MVP);
                MaterialToUse.BindUniform("in_color", 0.85f,0.30f, 0.2f);
                //RenderVAO(Mesh.GetVAO());
            }
        }


        if(Engine.bWithEditor)
        if(false)
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

        }
        */
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
            case WireframeWithVertices:
                //TODO: WireframeWithVertices second pass
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

