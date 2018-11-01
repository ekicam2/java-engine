package com.ekicam2.Engine.Rendering.OpenGL;

import com.ekicam2.Engine.Rendering.PixelFormat;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL45;

public class RBO implements OGLWrapper {
    private int Handle;
    private AttachmentType AttachmentType;
    private int ColorAttachmentID = -1;
    public enum AttachmentType {
        Color,
        Stencil,
        Depth,
        DepthStencil
    }

    public int GetOGLAttachmentType() {
        if (ColorAttachmentID >= 0 && ColorAttachmentID <= 31) {
            return GL45.GL_COLOR_ATTACHMENT0 + ColorAttachmentID;
        }

        switch (AttachmentType) {
            case Stencil:
                return GL45.GL_STENCIL_ATTACHMENT;

            case Depth:
                return GL45.GL_DEPTH_ATTACHMENT;

            case DepthStencil:
                return GL45.GL_DEPTH_STENCIL_ATTACHMENT;

            default:
                return -1;
        }
    }

    public RBO(AttachmentType InAttachmentType, int InColorAttachmentID) {
        Handle = GL45.glGenRenderbuffers();
        if(InAttachmentType == AttachmentType.Color) {
            ColorAttachmentID = InColorAttachmentID;
        }

        AttachmentType = InAttachmentType;
    }

    @Override
    public void Bind() {
        GL45.glBindRenderbuffer(GL45.GL_RENDERBUFFER, Handle);
    }

    @Override
    public void Unbind() {
        GL45.glBindRenderbuffer(GL45.GL_RENDERBUFFER, 0);
    }

    @Override
    public void Free() {
        GL45.glDeleteRenderbuffers(Handle);
    }

    @Override
    public int GetHandle() {
        return Handle;
    }

    public void Allocate(int Width, int Height, PixelFormat InPixelFormat) {
        Bind();
        GL45.glRenderbufferStorage(GL30.GL_RENDERBUFFER, OGLUtils.PixelFormatToOGL(InPixelFormat), Width, Height);
    }

    // shouldn't it be in the FBO implementation?
    public void BindToFrameBuffer(FBO FrameBufferToBindTo, BindingPolicy Policy) {
        FrameBufferToBindTo.Bind();

        switch (Policy) {
            case Read:
                GL45.glFramebufferRenderbuffer(GL45.GL_READ_FRAMEBUFFER, GetOGLAttachmentType(), GL45.GL_RENDERBUFFER, Handle);
                break;

            case Write:
                GL45.glFramebufferRenderbuffer(GL45.GL_DRAW_FRAMEBUFFER, GetOGLAttachmentType(), GL45.GL_RENDERBUFFER, Handle);
                break;
        }

        FrameBufferToBindTo.Unbind();
    }

}