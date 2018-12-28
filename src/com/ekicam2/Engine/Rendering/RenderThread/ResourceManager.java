package com.ekicam2.Engine.Rendering.RenderThread;

import com.ekicam2.Engine.EngineUtils.EngineUtils;
import com.ekicam2.Engine.Rendering.OpenGL.Program;
import com.ekicam2.Engine.Rendering.OpenGL.Shader;
import com.ekicam2.Engine.Rendering.OpenGL.VAO;
import com.ekicam2.Engine.Resources.StaticMeshResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResourceManager {
    private static final String SHADER_PATH = "resources\\Shaders\\";


    public void Free() {
        Programs.forEach((integer, Program) -> Program.Free());
        Vaos.forEach((integer, VaoList) -> VaoList.forEach(vao -> vao.Free()));
    }

    Program GetOrCreateProgram(String Path) {
        int Hash = Path.hashCode();

        if (!Programs.containsKey(Hash)) {
            Program newmat = null;
            Shader vsShader = new Shader(Shader.Type.Vertex, EngineUtils.ReadFromFile(SHADER_PATH + Path + ".vs"));
            Shader fsShader = new Shader(Shader.Type.Fragment, EngineUtils.ReadFromFile(SHADER_PATH + Path + ".fs"));
            newmat = new Program(vsShader, fsShader);
            newmat.BindAttrib(0, "position");
            fsShader.Free();
            vsShader.Free();

            Programs.put(Hash, newmat);
        }

        return Programs.get(Hash);
    }

    List<VAO> GetOrCreateVAOs(StaticMeshResource Mesh) {
        if(Mesh == null)
            return null;

        List<VAO> Returner = Vaos.get(Mesh.ID);

        if(Returner == null) {
            Returner = new ArrayList<>();

            for(var InnerMesh : Mesh.Meshes) {
                Returner.add(new VAO(InnerMesh.Vertices, InnerMesh.Indices));
            }
        }

        return Returner;
    }

    /*public VAO GetOrCreateVAO(String Path) {
        int Hash = Path.hashCode();

        if(!Vaos.containsKey(Hash)) {
            List<MeshLoader.Obj> objs = LoadOBJ(OBJ_PATH + Path + ".obj");

            Vaos.put(Hash, new VAO(objs.get(0).Vertices, objs.get(0).Indices));
        }

        return Vaos.get(Hash);
    }*/

    private HashMap<Integer, Program> Programs = new HashMap<>();
    private HashMap<Integer, List<VAO>> Vaos = new HashMap<>();
}
