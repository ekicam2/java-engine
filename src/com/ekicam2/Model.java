package com.ekicam2;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.aiReleaseImport;

public class Model {
    public Transform Transform = new Transform();
    public AIScene Scene;
    private List<Mesh> Meshes;

    public Model(AIScene InScene) {
        Scene = InScene;
        Meshes = new ArrayList<>();

        int meshCount = Scene.mNumMeshes();
        PointerBuffer MeshesBuffer = Scene.mMeshes();

        for (int i = 0; i < meshCount; ++i) {
            AIMesh Mesh = AIMesh.create(MeshesBuffer.get(i));
            Meshes.add(new Mesh(Mesh));
        }
    }

    public void Draw() {
        for(Mesh Mesh : Meshes) {
            Mesh.Draw();
        }
    }

    public void Free() {
        aiReleaseImport(Scene);
        Scene = null;
    }
}