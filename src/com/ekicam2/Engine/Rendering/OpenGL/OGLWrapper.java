package com.ekicam2.Engine.Rendering.OpenGL;

public interface OGLWrapper {
    void Bind();
    void Unbind();
    void Free();
    int GetHandle();
}
