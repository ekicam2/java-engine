package com.ekicam2.Engine.Editor;

import com.ekicam2.Engine.Engine;
import com.ekicam2.Engine.Rendering.OpenGL.BindingPolicy;
import com.ekicam2.Engine.Rendering.OpenGL.FBO;
import com.ekicam2.Engine.Rendering.OpenGL.RBO;
import com.ekicam2.Engine.Rendering.PixelFormat;
import org.lwjgl.opengl.GL45;

public class ObjectPicker {
    private Engine Engine;

    //TODO: MOAR ABSTRACTION!!111!
    private FBO Framebuff = new FBO();
    private RBO Renderbuff = new RBO(RBO.AttachmentType.Color, 0);

    public ObjectPicker(Engine InEngine) {
        Engine = InEngine;
        Renderbuff.Allocate(Engine.GetWindowWidth(), Engine.GetWindowHeight(), PixelFormat.RGB_32);
        Renderbuff.BindToFrameBuffer(Framebuff, BindingPolicy.Write);
        System.out.println(Framebuff.CheckForCompleteness());
    }

    public void PrepareForRender() {
        Framebuff.Bind(BindingPolicy.Write);
        GL45.glClear(GL45.GL_COLOR_BUFFER_BIT);
    }

    public void CleanupAfterRender() {
        Framebuff.Unbind();
    }

    int GetObjectID(int X, int Y) {

        Framebuff.Bind(BindingPolicy.Read);
        GL45.glReadBuffer(Renderbuff.GetOGLAttachmentType());

        float Pixel[] = new float [3];
        GL45.glReadPixels(X,  -Y, 1, 1, GL45.GL_RGB, GL45.GL_FLOAT, Pixel);


        GL45.glReadBuffer(GL45.GL_NONE);
        Framebuff.Unbind();

        System.err.println(X + " " + Y);
        System.out.println(Pixel[0] + ", " + Pixel[1] + ", " + Pixel[2]);
        return (int)Pixel[2];
    }

    public void Free() {
        Renderbuff.Free();
        Framebuff.Free();
    }
}
