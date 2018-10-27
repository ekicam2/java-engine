package com.ekicam2;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL45;

import java.nio.FloatBuffer;


public class Material {
    private int Handle;
    private boolean bIsLinked = false;

    public Material() {
        Handle = GL45.glCreateProgram();

        AddShader(new Shader(Shader.Type.Fragment));
        AddShader(new Shader(Shader.Type.Vertex));

        BindAttrib(0, "position");

        Link();
    }

    public Material(Shader VertexShader, Shader FragmentShader) {
        Handle = GL45.glCreateProgram();
        AddShader(VertexShader);
        AddShader(FragmentShader);
        Link();
    }


    public void AddShader(Shader ShaderToAdd) {
        if(!ShaderToAdd.WasCompiled())
            return;
        
        GL45.glAttachShader(Handle, ShaderToAdd.GetBinding());
    }

    public void BindAttrib(int Index, String AttribName) {
        GL45.glBindAttribLocation(Handle, Index, AttribName);
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
}
