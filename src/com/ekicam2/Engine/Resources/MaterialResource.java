package com.ekicam2.Engine.Resources;

public class MaterialResource {
    MaterialResource(String InName, String InFragmentShader) {
        Name = InName;
        ID = Name.hashCode();
        FragmentShader = InFragmentShader;
        VertexShader = "Simple";
    }

    MaterialResource(String InName, String InVertexShader, String InFragmentShader) {
        Name = InName;
        ID = Name.hashCode();
        VertexShader = InVertexShader;
        FragmentShader = InFragmentShader;
    }

    public final String Name;
    public final int ID;

    public final String FragmentShader;
    public final String VertexShader;
}
