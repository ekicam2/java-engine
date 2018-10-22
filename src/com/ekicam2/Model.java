package com.ekicam2;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;

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
            AIVector3D.Buffer aiVector3DS = Mesh.mVertices();

            float Vertices[] = new float[aiVector3DS.capacity() * 3];
            for(int j = 0; j < aiVector3DS.capacity(); ++j) {
                AIVector3D VectorToAdd = aiVector3DS.get(j);
                Vertices[j] = VectorToAdd.x();
                Vertices[j + 1] = VectorToAdd.y();
                Vertices[j + 2] = VectorToAdd.z();
            }

            VAOs.add(new VAO(Vertices));
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