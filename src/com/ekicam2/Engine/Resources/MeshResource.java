package com.ekicam2.Engine.Resources;

public class MeshResource extends IResource<MeshResource> {
    private float Vertices[] = null;
    private int Indices[] = null;

    private int MaterialIndex;

    MeshResource(float[] Vertices, int[] Indices) {
        this.Vertices = Vertices;
        this.Indices = Indices;
        this.MaterialIndex = 0;
    }

    MeshResource(float Vertices[], int[] Indices, int MaterialIndex) {
        this.Vertices = Vertices;
        this.Indices = Indices;
        this.MaterialIndex = MaterialIndex;
    }

    @Override
    public void Free() {

    }
}
