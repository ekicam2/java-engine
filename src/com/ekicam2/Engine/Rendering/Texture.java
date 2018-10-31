package com.ekicam2.Engine.Rendering;

import jdk.jshell.spi.ExecutionControl;
import org.lwjgl.opengl.GL45;

public class Texture {
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

    public void Bind() {
        GL45.glBindTexture(Texture.GetOGLTextureType(Type), Handle);
    }

    public static void Unbind() {
        GL45.glBindTexture(GL45.GL_TEXTURE_2D, 0);
    }

    public void Free() {
        GL45.glDeleteTextures(Handle);
    }

    private static int GetOGLTextureType(TextureType InType) {
        switch (InType){
            case Texture2D:
                return GL45.GL_TEXTURE_2D;

            default:
                break;
        }

        return -1;
    }


}
