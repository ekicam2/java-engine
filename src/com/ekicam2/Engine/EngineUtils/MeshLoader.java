package com.ekicam2.Engine.EngineUtils;

import com.ekicam2.Engine.Resources.StaticMeshResource;
import org.lwjgl.BufferUtils;
import org.lwjgl.assimp.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

public class MeshLoader {
    /*
    @Deprecated
    static Model LoadOBJM(String PathToFile) {
        AIFileIO FileHandle = AIFileIO.create();

        AIFileOpenProcI FileOpenProc = new AIFileOpenProc() {
            @Override
            public long invoke(long pFileIO, long FileName, long OpenMode) throws RuntimeException {
                AIFile File = AIFile.create();
                final ByteBuffer Data;
                String FileNameUTF8 = memUTF8(FileName);
                try {
                    java.io.File FileInner = new java.io.File(PathToFile);
                    FileInputStream Fis = new FileInputStream(FileInner);
                    FileChannel Fc = Fis.getChannel();
                    Data = Fc.map(FileChannel.MapMode.READ_ONLY, 0, Fc.size());
                    Fc.close();
                    Fis.close();
                } catch(IOException e) {
                    throw new RuntimeException("Couldn't open file: " + FileNameUTF8);
                }

                AIFileReadProcI FileReadProc = new AIFileReadProc() {
                    @Override
                    public long invoke(long pFile, long pBuffer, long Size, long Count) {
                        long Max = Math.min(Data.remaining(), Size * Count);
                        memCopy(memAddress(Data) + Data.position(), pBuffer, Max);
                        return Max;
                    }
                };

                AIFileSeekI FileSeekProc = new AIFileSeek() {
                    @Override
                    public int invoke(long pFile, long Offset, int Origin) {
                        if(Origin == Assimp.aiOrigin_CUR) {
                            Data.position(Data.position() + (int) Offset);
                        } else if(Origin == Assimp.aiOrigin_SET) {
                            Data.position((int) Offset);
                        } else if(Origin == Assimp.aiOrigin_END) {
                            Data.position(Data.limit() + (int) Offset);
                        }

                        return 0;
                    }
                };

                AIFileTellProcI FileTellProc = new AIFileTellProc() {
                    @Override
                    public long invoke(long pFile) {
                        return Data.limit();
                    }
                };

                File.ReadProc(FileReadProc);
                File.SeekProc(FileSeekProc);
                File.FileSizeProc(FileTellProc);

                return File.address();
            }
        };

        AIFileCloseProcI FileCloseProc = new AIFileCloseProc() {
            @Override
            public void invoke(long pFileIO, long pFile) {
                ;
            }
        };

        FileHandle.set(FileOpenProc, FileCloseProc, NULL);
        int Flags = aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_OptimizeMeshes;
        //AIScene Scene = aiImportFile(PathToFile, Flags);
        AIScene Scene = aiImportFileEx(PathToFile, Flags, FileHandle);
        if(Scene == null) {
            System.err.println("Could not load file: " + PathToFile);
        }

        return null;
    }*/

    public static StaticMeshResource LoadOBJ(String PathToFile) {
        AIFileIO FileHandle = AIFileIO.create();
        int Flags = aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_OptimizeMeshes;

        AIScene Scene = aiImportFile(PathToFile, Flags);

        if(Scene == null) {
            System.err.println("Could not load file: " + PathToFile);
            return null;
        }

        List<StaticMeshResource.MeshData> Objs = new ArrayList<>();
        List<String> Materials = new ArrayList<>();



        for(int i = 0 ; i < Scene.mNumMaterials(); ++i) {
            AIMaterial Material = AIMaterial.create(Scene.mMaterials().get(i));

            // hacky af, just like ANSI C ;__________;
            ByteBuffer buff = BufferUtils.createByteBuffer(1032);
            AIString materialName = new AIString(buff);
            aiGetMaterialString(Material, AI_MATKEY_NAME, aiTextureType_NONE, 0, materialName);

            Materials.add(materialName.dataString());
        }

        for(int i = 0 ; i < Scene.mNumMeshes(); ++i)
        {
            AIMesh Mesh = AIMesh.create(Scene.mMeshes().get(i));

            float Vertices[] = null;
            int Indices[] = null;
            int MeshMaterialID = 0;

            AIVector3D.Buffer VerticesBuffer = Mesh.mVertices();
            Vertices = new float[VerticesBuffer.capacity() * 3];
            for(int j = 0; j < VerticesBuffer.capacity(); ++j) {
                AIVector3D VectorToAdd = VerticesBuffer.get(j);
                Vertices[j * 3] = VectorToAdd.x();
                Vertices[j * 3 + 1] = VectorToAdd.y();
                Vertices[j * 3 + 2] = VectorToAdd.z();
            }

            AIFace.Buffer FaceBuffer = Mesh.mFaces();
            int FaceCount = Mesh.mNumFaces();
            Indices = new int[FaceCount * 3]; // i assume here all faces have only 3 vertices if not we will see mesh is corrupted

            for(int j = 0; j < FaceCount; ++j) {
                AIFace Face = FaceBuffer.get(j);
                IntBuffer IndicesBuffer = Face.mIndices();

                Indices[j * 3] = IndicesBuffer.get(0);
                Indices[j * 3 + 1] = IndicesBuffer.get(1);
                Indices[j * 3 + 2] = IndicesBuffer.get(2);

                // System.out.println("f " + IndicesBuffer.get(0) + "/" + IndicesBuffer.get(1) + "/" + IndicesBuffer.get(2));
            }

            MeshMaterialID = Mesh.mMaterialIndex();

             Objs.add(new StaticMeshResource.MeshData(Vertices, Indices, MeshMaterialID));
        }


        aiReleaseImport(Scene);

        StaticMeshResource MeshResource = new StaticMeshResource(PathToFile, Objs, Materials);

        return MeshResource;
    }
}
