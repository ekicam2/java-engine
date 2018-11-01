package com.ekicam2.Engine.Rendering.OpenGL;

import org.lwjgl.opengl.GL45;

public class FBO implements OGLWrapper {
    private int Handle;
    private BindingPolicy LastBoundPolicy;

    public FBO(){
        Handle = GL45.glGenFramebuffers();
    }

    @Override
    public void Free() {
        GL45.glDeleteFramebuffers(Handle);
    }

    @Override
    public void Bind() {
        LastBoundPolicy = BindingPolicy.Write;
        GL45.glBindFramebuffer(GL45.GL_DRAW_FRAMEBUFFER, Handle);
    }

    @Override
    public int GetHandle() {
        return Handle;
    }

    public void Bind(BindingPolicy PolicyToBind) {
        switch (PolicyToBind) {
            case Write:
                LastBoundPolicy = PolicyToBind;
                GL45.glBindFramebuffer(GL45.GL_DRAW_FRAMEBUFFER, Handle);
                break;

            case Read:
                LastBoundPolicy = PolicyToBind;
                GL45.glBindFramebuffer(GL45.GL_READ_FRAMEBUFFER, Handle);
                break;
        }
    }

    public void Unbind() {
        switch (LastBoundPolicy) {
            case Write:
                GL45.glBindFramebuffer(GL45.GL_DRAW_FRAMEBUFFER, 0);
                break;

            case Read:
                GL45.glBindFramebuffer(GL45.GL_READ_FRAMEBUFFER, 0);
                break;
        }
    }

    public boolean CheckForCompleteness() {
        Bind(BindingPolicy.Write);
        int Result = GL45.glCheckFramebufferStatus(GL45.GL_FRAMEBUFFER);
        if (Result != GL45.GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("FBO completeness test failed with result: " + Result);
            return false;
        }

        Bind(BindingPolicy.Read);
        Result = GL45.glCheckFramebufferStatus(GL45.GL_FRAMEBUFFER);
        if (Result != GL45.GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("FBO completeness test failed with result: " + Result);
            return false;
        }

        return true;
    }


}
