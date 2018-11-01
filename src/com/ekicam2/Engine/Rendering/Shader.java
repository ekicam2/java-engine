package com.ekicam2.Engine.Rendering;

import com.ekicam2.Engine.Rendering.OpenGL.OGLWrapper;
import org.lwjgl.opengl.GL45;

public class Shader implements OGLWrapper {
    enum Type {
        Vertex,
        Fragment
    }

    private int Handle;
    private Type ShaderType;
    private boolean bIsCompiled = false;

    public Shader(Shader.Type Type, String ShaderSource) {
        ShaderType = Type;

        switch(ShaderType) {
            case Vertex:
                Handle = GL45.glCreateShader(GL45.GL_VERTEX_SHADER);
                break;
            case Fragment:
                Handle = GL45.glCreateShader(GL45.GL_FRAGMENT_SHADER);
                break;
        }

        GL45.glShaderSource(Handle, ShaderSource);
        Compile();
    }

    public boolean Compile() {
        GL45.glCompileShader(Handle);
        GL45.glGetShaderi(Handle, GL45.GL_COMPILE_STATUS);
        String shaderLog = GL45.glGetShaderInfoLog(Handle);
        if (shaderLog.trim().length() > 0) {
            System.err.println("shader compile error: ");
            System.err.println(shaderLog);

            GL45.glDeleteShader(Handle);
            return false;
        }

        bIsCompiled = true;
        return true;
    }

    public boolean WasCompiled() {
        return bIsCompiled;
    }

    public int GetBinding() {
        return Handle;
    }

    @Override
    public void Bind() {

    }

    @Override
    public void Unbind() {

    }

    public void Free() {
        GL45.glDeleteShader(Handle);
    }

    @Override
    public int GetHandle() {
        return Handle;
    }
}
