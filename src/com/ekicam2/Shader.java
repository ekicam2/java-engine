package com.ekicam2;

import org.lwjgl.opengl.GL45;

public class Shader {
    enum Type {
        Vertex,
        Fragment
    }

    private int Handle;
    private Type ShaderType;
    private boolean bIsCompiled = false;

    public Shader(Shader.Type Type) {
        ShaderType = Type;

        switch(ShaderType) {
            case Vertex:
                Handle = GL45.glCreateShader(GL45.GL_VERTEX_SHADER);
                String VertexSource = "#version 330 core \n" +
                        "layout (location = 0) in vec3 position; \n" +
                        "void main() { \n" +
                        "gl_Position = vec4(position, 1.0f); \n" +
                        "}";

                GL45.glShaderSource(Handle, VertexSource);
                break;
            case Fragment:
                Handle = GL45.glCreateShader(GL45.GL_FRAGMENT_SHADER);

                String FragmentSource = "#version 330 core \n" +
                        "out vec3 color; \n" +
                        "void main() { \n" +
                        "color = vec3(0.2f, 1.0f, 0.7f); \n" +
                        "} \n";
                GL45.glShaderSource(Handle, FragmentSource);
                break;
        }

        Compile();
    }

    public Shader(Shader.Type Type, String ShaderSource) {
        ShaderType = Type;


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
}
