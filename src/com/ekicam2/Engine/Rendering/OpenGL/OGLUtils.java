package com.ekicam2.Engine.Rendering.OpenGL;

import com.ekicam2.Engine.Rendering.PixelFormat;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL45;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class OGLUtils {
    static int PixelFormatToOGL(PixelFormat InPixelFormat) {
        switch (InPixelFormat) {
            case RGB_8:
                return GL45.GL_RGB8I;

            case RGB_16:
                return GL45.GL_RGB16F;

            case RGB_32:
                return GL45.GL_RGB32F;

            default:
                return -1;
        }
    }

    static int GetOGLTextureType(Texture.TextureType InType) {
        switch (InType){
            case Texture2D:
                return GL45.GL_TEXTURE_2D;

            default:
                System.err.println(InType.toString() + " not implemented yet");
                break;
        }

        return -1;
    }

    static String[] GetAllUniformsForMaterial(Material MaterialToLookIn) {
        {
            String Returner[] = null;
            int[] count = {0};
            MaterialToLookIn.Bind();
            GL45.glGetProgramiv(MaterialToLookIn.GetHandle(), GL45.GL_ACTIVE_UNIFORMS, count);

            Returner = new String[count[0]];

            for (int i = 0; i < count[0]; i++) {
                var size = BufferUtils.createIntBuffer(20);
                var length = BufferUtils.createIntBuffer(20);
                var type = BufferUtils.createIntBuffer(20);;
                ByteBuffer name = BufferUtils.createByteBuffer(20);

                GL45.glGetActiveUniform(MaterialToLookIn.GetHandle(), i, length, size, type, name);

                Returner[i] = StandardCharsets.UTF_8.decode(name).toString();
            }

            return Returner;
        }
    }
}
