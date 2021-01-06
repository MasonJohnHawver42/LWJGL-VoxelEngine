import SGE.Renderer.Mesh;
import SGE.Renderer.Texture;
import org.lwjgl.system.MemoryUtil;

import javax.swing.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class QVMesh extends Mesh {

    //pos x, y, z (0 - 31) 15 bits
    //color cs * 4 (0 - 3) 8 bits
    //id (0 - 5) 3 bits
    //texture id (0 - 63) 6 bits
    //32 bits overall same as an int

    public QVMesh(int[] positions, int[] lightings, int[] face_ids, int[] tex_ids, Texture tex) {

        System.out.println("QV MESH INIT");

        quad_count = face_ids.length;

        int[] stuffed = new int[quad_count];

        for (int i  = 0; i < quad_count; i++) {
            int stuff = 0;

            stuff += positions[i * 3]; //x
            stuff += positions[(i * 3) + 1] << 5; //y
            stuff += positions[(i * 3) + 2] << 10; //z

            stuff += lightings[(i * 4) + 0] << 15; //corner 1 lighting
            stuff += lightings[(i * 4) + 1] << 17; //corner 2 lighting
            stuff += lightings[(i * 4) + 2] << 19; //corner 3 lighting
            stuff += lightings[(i * 4) + 3] << 21; //corner 4 lighting

            stuff += face_ids[i] << 23; // face id
            stuff += tex_ids[i] << 26; // texture id

            stuffed[i] = stuff;
            System.out.println(stuff);
        }


        IntBuffer stuffedBuffer = null;

        texture = tex;

        try {

            vao = glGenVertexArrays();
            glBindVertexArray(vao);

            int vbo = glGenBuffers();
            vbos.add(vbo);

            stuffedBuffer = MemoryUtil.memAllocInt(quad_count);
            stuffedBuffer.put(stuffed).flip();

            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, stuffedBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 1, GL_INT, false, 0, 0);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);


        } finally {
            if (stuffedBuffer != null) { MemoryUtil.memFree(stuffedBuffer); }
        }
    }

    public void render() {
        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        glBindVertexArray(vao);
        System.out.println(quad_count);
        glDrawElements(GL_POINTS, quad_count, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
        texture.unbind();
    }

    private Texture texture;
    private int quad_count;
}
