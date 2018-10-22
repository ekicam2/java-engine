package com.ekicam2;

import org.lwjgl.assimp.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.system.MemoryUtil.*;

public class FBXLoader {
    static Model LoadFBX(String PathToFile) {
        AIFileIO FileHandle = AIFileIO.create();

        AIFileOpenProcI FileOpenProc = new AIFileOpenProc() {
            @Override
            public long invoke(long pFileIO, long FileName, long OpenMode) throws RuntimeException {
                AIFile File = AIFile.create();
                final ByteBuffer Data;
                String FileNameUTF8 = memUTF8(FileName);
                try {
                    java.io.File FileInner = new java.io.File(PathToFile); //Files.get(Paths.get(PathToFile));//new File(Thread.currentThread().getContextClassLoader().getResource(PathToFile).getFile());
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
        AIScene Scene = aiImportFileEx(PathToFile, aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_MakeLeftHanded, FileHandle);
        if(Scene == null) {
            System.err.println("Could not load file: " + PathToFile);
        }

        return new Model(Scene);
    }
}
