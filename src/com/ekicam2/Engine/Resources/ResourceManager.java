package com.ekicam2.Engine.Resources;

import com.ekicam2.Engine.EngineUtils.MeshLoader;

import java.util.HashMap;

public class ResourceManager {
    private static final String OBJ_PATH = "resources\\Models\\OBJ format\\";


    public StaticMeshResource GetOrLoadMesh(String Path) {
        int Hash = Path.hashCode();

        StaticMeshResource Returner = Meshes.get(Hash);

        if(Returner == null) {
            Returner = MeshLoader.LoadOBJ(OBJ_PATH + Path + ".obj");
            Meshes.put(Hash, Returner);
        }

        return Returner;
    }

    //public MaterialResource GetOrLoadMaterial(String Name)

    private HashMap<Integer, StaticMeshResource> Meshes = new HashMap<>();
    private HashMap<Integer, MaterialResource> Materials = new HashMap<>();
}
