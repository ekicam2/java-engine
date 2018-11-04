package com.ekicam2.Engine.Rendering;

import com.ekicam2.Engine.Rendering.OpenGL.Material;
import com.ekicam2.Engine.Transform;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.aiReleaseImport;

public class Model {
    public com.ekicam2.Engine.Transform Transform = new Transform();
    public AIScene Scene;
    private List<Mesh> Meshes;
    private List<Material> Materials;

    public Model(AIScene InScene) {
        Scene = InScene;
        Meshes = new ArrayList<>();

        int MeshCount = Scene.mNumMeshes();

        Materials = new ArrayList<>(MeshCount);
        for(int i = 0; i < MeshCount; ++i) {
            Materials.add(null);
        }

        PointerBuffer MeshesBuffer = Scene.mMeshes();

        for (int i = 0; i < MeshCount; ++i) {
            AIMesh Mesh = AIMesh.create(MeshesBuffer.get(i));
            Meshes.add(new Mesh(Mesh));
        }

    }

    public boolean SetMaterial(int MaterialIndex, Material MaterialToSet)
    {
        if(Materials.size() > 0 && MaterialIndex < Materials.size())
        {
            Materials.set(MaterialIndex, MaterialToSet);
            return true;
        }

        return false;
    }

    public Material GetMaterial(int MaterialIndex) {
        if(Materials.size() > 0 && MaterialIndex < Materials.size()) {
            return Materials.get(MaterialIndex);
        }
        return null;
    }

    public List<Mesh> GetMeshes() {
        return Meshes;
    }

    public void Free() {
        aiReleaseImport(Scene);
        Scene = null;
        Meshes.forEach(mesh -> { mesh.Free(); });
    }
}