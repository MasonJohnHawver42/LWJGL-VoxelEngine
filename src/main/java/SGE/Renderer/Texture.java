package SGE.Renderer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

    private final int id;

    public Texture(String fileName) throws Exception {
        int width;
        int height;
        ByteBuffer buff;

        // Load SGE.User.Input.User.Renderer.Texture file
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            buff = stbi_load(fileName, w, h, channels, 4);
            if (buff == null) { throw new Exception("Image file [" + fileName  + "] not loaded: " + stbi_failure_reason()); }

            /* Get width and height of image */
            width = w.get();
            height = h.get();
        }

        // Create a new OpenGL texture
        id = glGenTextures();
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
        glBindTexture(GL_TEXTURE_2D, id);
    }
    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getId() {
        return id;
    }

    public void cleanup() {
        glDeleteTextures(id);
    }
}