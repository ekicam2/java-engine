package com.ekicam2.Engine.Rendering.OpenGL;

import org.lwjgl.opengl.GL45;

public class VAO implements OGLWrapper {
    private int Handle;

    private int PosVBO;
    private int VerticesCount = 0;

    private int IndVBO;
    private int IndicesCount = 0;

    public VAO(float Vertices[], int Indices[]) {
        Handle = GL45.glGenVertexArrays();

        SetVertices(Vertices);
        SetIndices(Indices);
    }

    public VAO(float Vertices[]) {
        Handle = GL45.glGenVertexArrays();

        SetVertices(Vertices);
    }

    public void Free() {
        GL45.glDeleteBuffers(PosVBO);
        GL45.glDeleteBuffers(IndVBO);
        GL45.glDeleteVertexArrays(Handle);
    }

    public void Bind() {
        GL45.glBindVertexArray(Handle);
    }

    public void Unbind() {
        GL45.glBindVertexArray(0);
    }

    public int GetHandle() { return Handle; }

    public int GetIndicesCount() {
        return IndicesCount;
    }

    public int GetVerticesCount() {
        return VerticesCount;
    }

    public void Draw(){
        GL45.glBindVertexArray(Handle);
        if(IndicesCount != 0) {
            GL45.glPolygonMode(GL45.GL_FRONT_AND_BACK, GL45.GL_FILL);
            GL45.glDrawElementsBaseVertex(GL45.GL_TRIANGLES, IndicesCount, GL45.GL_UNSIGNED_INT, 0L, 0);
        } else {
            GL45.glPointSize(4.5f);
            GL45.glPolygonMode(GL45.GL_FRONT_AND_BACK, GL45.GL_POINT);
            GL45.glDrawArrays(GL45.GL_TRIANGLES, 0, VerticesCount);

            GL45.glPolygonMode(GL45.GL_FRONT_AND_BACK, GL45.GL_LINE);
            GL45.glDrawArrays(GL45.GL_TRIANGLES, 0, VerticesCount);
        }
    }

    private void SetVertices(float[] Vertices) {
        GL45.glBindVertexArray(Handle);

        VerticesCount = Vertices.length;
        PosVBO = GL45.glGenBuffers();

        GL45.glBindBuffer(GL45.GL_ARRAY_BUFFER, PosVBO);
        GL45.glBufferData(GL45.GL_ARRAY_BUFFER, Vertices, GL45.GL_STATIC_DRAW);
        GL45.glEnableVertexAttribArray(0);
        GL45.glVertexAttribPointer(0, 3, GL45.GL_FLOAT, false, 0, 0L);

        GL45.glBindVertexArray(0);
    }

    private void SetIndices(int[] Indices) {
        GL45.glBindVertexArray(Handle);

        IndicesCount = Indices.length;
        IndVBO = GL45.glGenBuffers();

        GL45.glBindBuffer(GL45.GL_ELEMENT_ARRAY_BUFFER, IndVBO);
        GL45.glBufferData(GL45.GL_ELEMENT_ARRAY_BUFFER, Indices, GL45.GL_STATIC_DRAW);
        GL45.glEnableVertexAttribArray(1);
        GL45.glVertexAttribPointer(1, 3, GL45.GL_UNSIGNED_INT, false, 0,0L);

        GL45.glBindVertexArray(0);
    }
}
