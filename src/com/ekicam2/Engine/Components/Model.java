package com.ekicam2.Engine.Components;

import com.ekicam2.Engine.Rendering.OpenGL.Program;

import java.util.List;

public class Model {
    public com.ekicam2.Engine.Components.Transform Transform = new Transform();
    private List<Mesh> Meshes;
    private List<Program> Materials;

    public boolean SetMaterial(int MaterialIndex, Program MaterialToSet)
    {
        if(Materials.size() > 0 && MaterialIndex < Materials.size())
        {
            Materials.set(MaterialIndex, MaterialToSet);
            return true;
        }

        return false;
    }

    public Program GetMaterial(int MaterialIndex) {
        if(Materials.size() > 0 && MaterialIndex < Materials.size()) {
            return Materials.get(MaterialIndex);
        }
        return null;
    }

    public List<Mesh> GetMeshes() {
        return Meshes;
    }

    public void Free() {
    }
}