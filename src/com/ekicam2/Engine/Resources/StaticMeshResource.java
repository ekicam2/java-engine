package com.ekicam2.Engine.Resources;

import java.util.List;

public class StaticMeshResource {
    public static class MeshData {
        public MeshData(float[] InVertices, int[] InIndices, int InMaterialIndex) {
            Vertices = InVertices;
            Indices = InIndices;
            MaterialIndex = InMaterialIndex;
        }

        public final float[] Vertices;
        public final int[] Indices;
        public final int MaterialIndex;
    }

    public StaticMeshResource(String InName, List<MeshData> InMeshes, List<String> InMaterials) {
        Name = InName;
        ID = Name.hashCode();
        Meshes = InMeshes;
        Materials = InMaterials;
    }

    public final String Name;
    public final int ID;
    public final List<MeshData> Meshes;
    public final List<String> Materials;
}
