package com.ekicam2.Engine.Rendering;

import com.ekicam2.Engine.Rendering.OpenGL.OGLWrapper;
import jdk.jshell.spi.ExecutionControl;
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
        GL45.glBindTexture(Texture.GetOGLTextureType(Type), Handle);
    }

    @Override
    public void Unbind() {
        GL45.glBindTexture(Texture.GetOGLTextureType(Type), 0);
    }

    @Override
    public void Free() {
        GL45.glDeleteTextures(Handle);
    }

    @Override
    public int GetHandle() {
        return Handle;
    }

    private static int GetOGLTextureType(TextureType InType) {
        switch (InType){
            case Texture2D:
                return GL45.GL_TEXTURE_2D;

            default:
                System.err.println(InType.toString() + " not implemented yet");
                break;
        }

        return -1;
    }


}
