package com.ekicam2.Engine.Resources;

import java.util.HashMap;

public class ResourceManager {
    private HashMap<String, MeshResource> Meshes = new HashMap<>();

    MeshResource GetOrCreate(String Path) {
        MeshResource mr = Meshes.get(Path);
        if(mr == null)
        {
            mr = MeshLoader.LoadOBJ(Path);
            mr.Path = Path;
        }

        return mr;
    }
}
