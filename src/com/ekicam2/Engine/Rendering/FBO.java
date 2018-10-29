package com.ekicam2.Engine.Rendering;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL45;

public class FBO {
    private int Handle;
    private Vector2f FrameBufferSize;

    public FBO(){
        Handle = GL45.glGenFramebuffers();
    }

    public Vector2f GetFrameBufferSize() {
        return FrameBufferSize;
    }

    public void Bind() {
        GL45.glBindFramebuffer(GL45.GL_FRAMEBUFFER, Handle);
    }

    public void BindForDraw() {
        GL45.glBindFramebuffer(GL45.GL_DRAW_FRAMEBUFFER, Handle);
    }

    public void BindForRead() {
        GL45.glBindFramebuffer(GL45.GL_READ_FRAMEBUFFER, Handle);
    }

    public static void Unbind() {
        GL45.glBindFramebuffer(GL45.GL_FRAMEBUFFER, 0);
    }
}
