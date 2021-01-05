package VoxelGameEngine;

import SGE.Renderer.Mesh;
import SGE.Renderer.Texture;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class VVMesh extends Mesh<VVMesh.Quad> {

    public VVMesh(Texture texture_sheet) {
        this.texture = texture_sheet;

        positions = new LinkedList<Integer>();
        textures = new LinkedList<Integer>();
        lighting = new LinkedList<Integer>();
        indices = new LinkedList<Integer>();
    }

    private int quad_count = 0;
    private LinkedList<Integer> positions;
    private LinkedList<Integer> textures;
    private LinkedList<Integer> lighting;
    private LinkedList<Integer> indices;

    public void add(Quad quad) {

        if ( 0 <= quad.x && quad.x <= 30 && 0 <= quad.y && quad.y <= 30 && 0 <= quad.z && quad.z <= 30 && 0 <= quad.tex_id && quad.tex_id < 64 ) {
            int x, y, z, tv, tid, l;

            x = quad.x + dzs[quad.id][0]; //x (0 - 31)
            y = quad.y + dzs[quad.id][1]; //y (0 - 31)
            z = quad.z + dzs[quad.id][2]; //z (0 - 31)

            tid = quad.tex_id ; //tid
            tv = 0; //tvid

            l = ls[quad.id];

            positions.add(x); positions.add(y); positions.add(z);
            textures.add(tid); textures.add(tv);
            lighting.add(l);

            x = quad.x + dzs[quad.id][0] + dxs[quad.id][0];
            y = quad.y + dzs[quad.id][1] + dxs[quad.id][1];
            z = quad.z + dzs[quad.id][2] + dxs[quad.id][2];

            tid = quad.tex_id;
            tv = 1;

            l = ls[quad.id];

            positions.add(x); positions.add(y); positions.add(z);
            textures.add(tid); textures.add(tv);
            lighting.add(l);

            x = quad.x + dzs[quad.id][0] + dys[quad.id][0];
            y = quad.y + dzs[quad.id][1] + dys[quad.id][1];
            z = quad.z + dzs[quad.id][2] + dys[quad.id][2];

            tid = quad.tex_id;
            tv = 2;

            l = ls[quad.id];

            positions.add(x); positions.add(y); positions.add(z);
            textures.add(tid); textures.add(tv);
            lighting.add(l);

            x = quad.x + dzs[quad.id][0] + dxs[quad.id][0] + dys[quad.id][0];
            y = quad.y + dzs[quad.id][1] + dxs[quad.id][1] + dys[quad.id][1];
            z = quad.z + dzs[quad.id][2] + dxs[quad.id][2] + dys[quad.id][2];

            tid = quad.tex_id;
            tv = 3;

            l = ls[quad.id];

            positions.add(x); positions.add(y); positions.add(z);
            textures.add(tid); textures.add(tv);
            lighting.add(l);

            indices.add((quad_count * 4) + 0);
            indices.add((quad_count * 4) + 1);
            indices.add((quad_count * 4) + 2);
            indices.add((quad_count * 4) + 1);
            indices.add((quad_count * 4) + 2);
            indices.add((quad_count * 4) + 3);

            quad_count++;
        }

    }

    public void build() {

        FloatBuffer posBuffer = null;
        IntBuffer texBuffer = null;
        IntBuffer lightsBuffer = null;
        IntBuffer indicesBuffer = null;

        try {
            vao = glGenVertexArrays();
            glBindVertexArray(vao);

            // Position VBO
            int pos_vbo = glGenBuffers();
            float[] pos = new float[positions.size()];
            for (int i = 0; i < positions.size(); i++) { pos[i] = positions.get(i); }
            posBuffer = MemoryUtil.memAllocFloat(pos.length);
            posBuffer.put(pos).flip();
            glBindBuffer(GL_ARRAY_BUFFER, pos_vbo);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Tex VBO
            int tex_vbo = glGenBuffers();
            int[] tex = textures.stream().mapToInt(i->i).toArray();
            texBuffer = MemoryUtil.memAllocInt(tex.length);
            texBuffer.put(tex).flip();
            glBindBuffer(GL_ARRAY_BUFFER, tex_vbo);
            glBufferData(GL_ARRAY_BUFFER, texBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_INT, false, 0, 0);

            //Lighting VBO
            int lights_vbo = glGenBuffers();
            int[] lights = lighting.stream().mapToInt(i->i).toArray();
            lightsBuffer = MemoryUtil.memAllocInt(lights.length);
            lightsBuffer.put(lights).flip();
            glBindBuffer(GL_ARRAY_BUFFER, lights_vbo);
            glBufferData(GL_ARRAY_BUFFER, lightsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 1, GL_INT, false, 0, 0);

            // Index VBO
            int idx_vbo = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.size());
            indicesBuffer.put(indices.stream().mapToInt(i->i).toArray()).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idx_vbo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);

            vbos.add(pos_vbo);
            vbos.add(tex_vbo);
            vbos.add(idx_vbo);

        } finally {
            if (posBuffer != null) { MemoryUtil.memFree(posBuffer); }
            if (texBuffer != null) { MemoryUtil.memFree(texBuffer); }
            if (lightsBuffer != null) { MemoryUtil.memFree(lightsBuffer); }
            if (indicesBuffer != null) { MemoryUtil.memFree(indicesBuffer); }
        }

    }

    public Texture getTexture() { return texture; }

    public void render() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getId());

        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, quad_count * 6, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private Texture texture;

    static public class Quad {
         public Quad(int x1, int y1, int z1, int id1, int tid1) {
            x = x1;
            y = y1;
            z = z1;

            id = id1;
            tex_id = tid1;
        }
        int x, y, z;
        int id;
        int tex_id;
    }


    static final int[][] dxs = {
            {0, 0, 1},
            {0, 0, 1},
            {-1, 0, 0},
            {0, 0, 1},
            {1, 0, 0},
            {0, 0, -1}
    };

    static final int[][] dys = {
            {-1, 0, 0},
            {0, -1, 0},
            {0, -1, 0},
            {-1, 0, 0},
            {0, -1, 0},
            {0, -1, 0}
    };

    static final int[][] dzs = {
            {1, 1, 0},
            {0, 1, 0},
            {1, 1, 0},
            {1, 0, 0},
            {0, 1, 1},
            {1, 1, 1}
    };

    static final int[] ls = {
            0,
            1,
            2,
            3,
            2,
            3,
    };


}
