package SGE.Renderer;

import java.util.LinkedList;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public abstract class Mesh<E> {

    public Mesh() {
        vbos = new LinkedList<Integer>();
    }

    public void add(E data) {}

    public void build() {}

    public void render() {}

    public void terminate() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vbo : vbos) { glDeleteBuffers(vbo); }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
    }

    protected int vao;
    protected LinkedList<Integer> vbos;
}
