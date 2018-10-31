package com.ekicam2.Engine.Rendering;

import com.ekicam2.Engine.Rendering.OpenGL.VAO;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector3D;

import java.nio.IntBuffer;

public class Mesh {
    private com.ekicam2.Engine.Rendering.OpenGL.VAO VAO;
    private int MaterialIndex;

    public Mesh(AIMesh Mesh) {
        AIVector3D.Buffer VerticesBuffer = Mesh.mVertices();
        float Vertices[] = new float[VerticesBuffer.capacity() * 3];
        for(int j = 0; j < VerticesBuffer.capacity(); ++j) {
            AIVector3D VectorToAdd = VerticesBuffer.get(j);
            Vertices[j * 3] = VectorToAdd.x();
            Vertices[j * 3 + 1] = VectorToAdd.y();
            Vertices[j * 3 + 2] = VectorToAdd.z();
        }

        AIFace.Buffer FaceBuffer = Mesh.mFaces();
        int FaceCount = Mesh.mNumFaces();
        int Indices[] = new int[FaceCount * 3]; // i assume here all faces have only 3 vertices if not we will see mesh is corrupted

        for(int j = 0; j < FaceCount; ++j) {
            AIFace Face = FaceBuffer.get(j);
            IntBuffer IndicesBuffer = Face.mIndices();

            Indices[j * 3] = IndicesBuffer.get(0);
            Indices[j * 3 + 1] = IndicesBuffer.get(1);
            Indices[j * 3 + 2] = IndicesBuffer.get(2);

            // System.out.println("f " + IndicesBuffer.get(0) + "/" + IndicesBuffer.get(1) + "/" + IndicesBuffer.get(2));
        }

        MaterialIndex = Mesh.mMaterialIndex();

        VAO = new VAO(Vertices, Indices);
    }

    public VAO GetVAO() {
        return VAO;
    }

    public int GetMaterialIndex()
    {
        return MaterialIndex;
    }

    public void Free() {
        VAO.Free();
    }
}