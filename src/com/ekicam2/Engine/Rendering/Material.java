package com.ekicam2.Engine.Rendering;

import com.ekicam2.Engine.Rendering.OpenGL.OGLWrapper;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL45;

import java.nio.FloatBuffer;


public class Material implements OGLWrapper {
    private int Handle;
    private boolean bIsLinked = false;

    public Material(Shader VertexShader, Shader FragmentShader) {
        Handle = GL45.glCreateProgram();
        AddShader(VertexShader);
        AddShader(FragmentShader);
        Link();
        Unbind();
    }

    @Override
    public void Free() {
        GL45.glDeleteProgram(Handle);
    }

    public void AddShader(Shader ShaderToAdd) {
        if(!ShaderToAdd.WasCompiled())
            return;
        
        GL45.glAttachShader(Handle, ShaderToAdd.GetBinding());
    }

    public void BindAttrib(int Index, String AttribName) {
        Bind();
        GL45.glBindAttribLocation(Handle, Index, AttribName);
        Unbind();
    }

    public void BindUniform(String Name, Matrix4f InMatrix) {
        Bind();
        int Location = GL45.glGetUniformLocation(Handle, Name);
        if(Location != -1)
        {
            FloatBuffer Buffer = BufferUtils.createFloatBuffer(16);;
            InMatrix.get(Buffer);
            GL45.glUniformMatrix4fv(Location, false, Buffer);
        }
    }

    public void BindUniform(String Name, float x, float y, float z) {
        Bind();
        int Location = GL45.glGetUniformLocation(Handle, Name);
        if(Location != -1)
        {
            GL45.glUniform3f(Location, x, y, z);
        }
    }

    public boolean Link() {
        GL45.glLinkProgram(Handle);
        GL45.glGetProgrami(Handle, GL45.GL_LINK_STATUS);
        String programLog = GL45.glGetProgramInfoLog(Handle);
        if (programLog.trim().length() > 0) {
            System.err.println(programLog);

            GL45.glDeleteProgram(Handle);
            return false;
        }

        bIsLinked = true;
        return true;
    }

    public boolean IsValid() {
        return bIsLinked;
    }

    public void Bind() {
        GL45.glUseProgram(Handle);
    }

    @Override
    public void Unbind() {
        GL45.glUseProgram(0);
    }

    public int GetHandle() { return Handle; }
}
