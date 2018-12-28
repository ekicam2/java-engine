package com.ekicam2.Engine.GameFoundation.Components;

import com.ekicam2.Engine.Resources.StaticMeshResource;

import java.util.ArrayList;
import java.util.List;

public class Drawable {

    public void SetMesh(StaticMeshResource ResourceToUse) {
        Mesh = ResourceToUse;

        // fuuu depending on GC is just not cool
        if(Mesh != null) {
            MaterialNames = new ArrayList<>(Mesh.Materials.size());
            for(int i = 0; i < Mesh.Materials.size(); ++i) {
                MaterialNames.add(null);
            }
        }
        else
            MaterialNames = null;
    }

    public int GetRequiredMaterialsNum() { return MaterialNames != null ? MaterialNames.size() : 0; }
    public void SetMaterial(int MaterialIndex, String MaterialName) {
        if(MaterialNames != null && MaterialIndex >= 0 && MaterialIndex < MaterialNames.size()) {
            MaterialNames.set(MaterialIndex, MaterialName);
        }
    }

    public StaticMeshResource GetMesh() { return Mesh; }
    public String GetMaterialAt(int MaterialIndex) {
        if(MaterialNames != null && MaterialIndex >= 0 && MaterialIndex < MaterialNames.size()) {
            return MaterialNames.get(MaterialIndex);
        }

        return null;
    }

    private StaticMeshResource Mesh = null;
    private List<String> MaterialNames;
}
