package com.ekicam2.Engine.Editor;

import com.ekicam2.Engine.Rendering.OpenGL.BindingPolicy;
import com.ekicam2.Engine.Rendering.OpenGL.FBO;
import com.ekicam2.Engine.Rendering.OpenGL.RBO;
import com.ekicam2.Engine.Rendering.PixelFormat;
import org.lwjgl.opengl.GL45;

public class ObjectPicker {
    private Editor Editor;

    private FBO Framebuff = null;
    private RBO Renderbuff = null;

    public ObjectPicker(Editor InEditor) {
        Framebuff = new FBO();
        Renderbuff = new RBO(RBO.AttachmentType.Color, 0);

        Editor = InEditor;
        Renderbuff.Allocate(Editor.GetEngine().GetWindow().GetWindowWidth(), Editor.GetEngine().GetWindow().GetWindowHeight(), PixelFormat.RGB_32);
        Renderbuff.BindToFrameBuffer(Framebuff, BindingPolicy.Write);
    }

    public void PrepareForRender() {
        Framebuff.Bind(BindingPolicy.Write);
        GL45.glClear(GL45.GL_COLOR_BUFFER_BIT | GL45.GL_DEPTH_BUFFER_BIT);
    }

    int GetObjectID(int X, int Y) {
        Framebuff.Bind(BindingPolicy.Read);
        GL45.glReadBuffer(Renderbuff.GetOGLAttachmentType());

        float Pixel[] = new float [3];
        GL45.glReadPixels(X,  Y, 1, 1, GL45.GL_RGB, GL45.GL_FLOAT, Pixel);


        Framebuff.Unbind();
        GL45.glReadBuffer(GL45.GL_NONE);

        // System.err.println(X + " " + Y);
        // System.out.println(Pixel[0] + ", " + Pixel[1] + ", " + Pixel[2]);
        return (int)Pixel[2];
    }

    public void Free() {
        Renderbuff.Free();
        Framebuff.Free();
    }
}
