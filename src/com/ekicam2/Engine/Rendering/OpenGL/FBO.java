package com.ekicam2.Engine.Rendering.OpenGL;

import org.lwjgl.opengl.GL45;

public class FBO {
    private int Handle;
    private int LastBoundPolicy;

    public FBO(){
        Handle = GL45.glGenFramebuffers();
    }

    public void Bind() {
        GL45.glBindFramebuffer(GL45.GL_FRAMEBUFFER, Handle);
    }

    public void BindForDraw() {
        LastBoundPolicy = GL45.GL_DRAW_FRAMEBUFFER;
        GL45.glBindFramebuffer(LastBoundPolicy, Handle);
    }

    public void BindForRead() {
        LastBoundPolicy = GL45.GL_READ_FRAMEBUFFER;
        GL45.glBindFramebuffer(LastBoundPolicy, Handle);
    }

    public void Unbind() {
        GL45.glBindFramebuffer(LastBoundPolicy, 0);
    }
}
