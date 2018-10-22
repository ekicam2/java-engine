package com.ekicam2;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.aiReleaseImport;

public class Model {
    public Transform Transform = new Transform();
    public AIScene Scene;
    private List<VAO> VAOs;

    public Model(AIScene InScene) {
        Scene = InScene;
        VAOs = new ArrayList<>();

        int meshCount = Scene.mNumMeshes();
        PointerBuffer MeshesBuffer = Scene.mMeshes();

        for (int i = 0; i < meshCount; ++i) {
            AIMesh Mesh = AIMesh.create(MeshesBuffer.get(i));
            AIVector3D.Buffer VerticesBuffer = Mesh.mVertices();

            float Vertices[] = new float[VerticesBuffer.capacity() * 3];
            for(int j = 0; j < VerticesBuffer.capacity(); ++j) {
                AIVector3D VectorToAdd = VerticesBuffer.get(j);
                Vertices[j] = VectorToAdd.x();
                Vertices[j + 1] = VectorToAdd.y();
                Vertices[j + 2] = VectorToAdd.z();
            }

            AIFace.Buffer FaceBuffer = Mesh.mFaces();
            int FaceCount = Mesh.mNumFaces();
            int Indices[] = new int[FaceCount * 3]; // i assume here all faces have only 3 vertices

            for(int j = 0; j < FaceCount; ++j) {
                AIFace Face = FaceBuffer.get(j);
                IntBuffer IndicesBuffer = Face.mIndices();

                Indices[j * 3] = IndicesBuffer.get(0);
                Indices[j * 3 + 1] = IndicesBuffer.get(1);
                Indices[j * 3 + 2] = IndicesBuffer.get(2);

                //System.out.println("f " + IndicesBuffer.get(0) + "/" + IndicesBuffer.get(1) + "/" + IndicesBuffer.get(2));
            }

            VAOs.add(new VAO(Vertices));//, Indices));
        }
    }

    public void Draw() {
        for(VAO vao : VAOs) {
            vao.Draw();
        }
    }

    public void Free() {
        aiReleaseImport(Scene);
        Scene = null;
    }
}