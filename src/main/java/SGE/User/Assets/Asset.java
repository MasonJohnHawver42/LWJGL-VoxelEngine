package SGE.User.Assets;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.stb.STBImage.*;

public abstract class Asset {
    abstract public void load(String file_name) throws Exception;
    abstract public void terminate();

    public static class Shader extends Asset {

        public Shader(int type) throws Exception {
            this.id = glCreateShader(type);
            this.type = type;
        }

        public void load(String fn) throws Exception {
            String source = Shader.getSource(fn);
            if (id == 0) { throw new Exception("Error creating shader. Type: " + type); }

            glShaderSource(id, source);
            glCompileShader(id);

            if (glGetShaderi(id, GL_COMPILE_STATUS) == 0) { throw new Exception("Error compiling SGE.Shader code: " + glGetShaderInfoLog(id, 1024)); }
        }

        public void terminate() {}

        public final int id;
        public final int type;

        private static String getSource(String filename) throws Exception {
            Path path = Paths.get(filename);
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    public static class Texture extends Asset {

        private final int id;

        public Texture() {
            id = glGenTextures();
        }

        public void load(String filename) throws Exception {
            int width;
            int height;
            ByteBuffer buff;

            // Load SGE.User.Input.User.Renderer.Texture file
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer w = stack.mallocInt(1);
                IntBuffer h = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);

                buff = stbi_load(filename, w, h, channels, 4);
                if (buff == null) { throw new Exception("Image file [" + filename  + "] not loaded: " + stbi_failure_reason()); }

                /* Get width and height of image */
                width = w.get();
                height = h.get();
            }

            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, id);

            // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            // Upload the texture data
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buff);
            //glGenerateMipmap(GL_TEXTURE_2D);

            stbi_image_free(buff);
        }

        public void bind() {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, id);
        }
        public void unbind() {
            glBindTexture(GL_TEXTURE_2D, 0);
        }

        public int getId() {
            return id;
        }

        public void terminate() {
            glDeleteTextures(id);
        }
    }
}
