package com.ekicam2.Engine.Rendering.OpenGL;

import org.lwjgl.opengl.GL45;

public class Texture implements OGLWrapper {
    public enum TextureType {
        Texture1D,
        Texture2D,
        Texture3D,
        Texture6D
    }

    private int Handle;
    private TextureType Type;

    public Texture() {
        Handle = GL45.glGenTextures();
    }

    @Override
    public void Bind() {
        GL45.glBindTexture(OGLUtils.GetOGLTextureType(Type), Handle);
    }

    @Override
    public void Unbind() {
        GL45.glBindTexture(OGLUtils.GetOGLTextureType(Type), 0);
    }

    @Override
    public void Free() {
        GL45.glDeleteTextures(Handle);
    }

    @Override
    public int GetHandle() {
        return Handle;
    }




}
