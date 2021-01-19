package VGE;

import SGE.Renderer.Mesh;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class Chunk {

    public Chunk(int x1, int y1, int z1) {
        voxels = new int[31][31][31];

        for(int x = 0; x < 31; x++) {
            for(int y = 0; y < 31; y++) {
                for(int z = 0; z < 31; z++) {
                    //voxels[x][y][z] = ( Math.random() > .9 ? 2 : 0);
                    voxels[x][y][z] = ((x - 15.5)*(x - 15.5) + (y - 15.5)*(y - 15.5) + (z - 15.5)*(z - 15.5) < 240 ? ((x - 15.5)*(x - 15.5) + (y + 1 - 15.5)*(y + 1 - 15.5) + (z - 15.5)*(z - 15.5) < 240 ? 2 : 1) : 0);
                    //voxels[x][y][z] = (Math.sin(x / 4.0)*Math.cos(z/ 4.0) * 5 <= y - 10 ? 0 : 2);
                    //voxels[x][y][z] = (y == 30 ? 1 : 2);
                }
            }
        }

        this.x = x1;
        this.y = y1;
        this.z = z1;

        mesh = new VVMesh();
    }

    public Voxel getVoxel(int x, int y, int z) {
        if (0 <= x && x < 31 && 0 <= y && y < 31 && 0 <= z && z < 31)
            return getVoxel(voxels[x][y][z]);
        else
            return Voxel.AIR;
    }

    public void createMesh() {
        for(int x = 0; x < 31; x++) {
            for(int y = 0; y < 31; y++) {
                for(int z = 0; z < 31; z++) {
                    Voxel voxel = getVoxel(x, y, z);
                    voxel.createMesh(this, x, y, z);
                }
            }
        }

        System.out.println("here butt");

        mesh.build();
    }

    public void terminate() {
        mesh.terminate();
    }

    public VVMesh mesh;

    private int[][][] voxels;
    public final int x, y, z;

    static private Voxel getVoxel(int id) {
        return Voxel.values()[id];
    }

    static final int width = 31, height = 31, length = 31;

    //Chunck Meshes

    public static class VVMesh extends Mesh<VVMesh.Quad> {

        public VVMesh() {

            data = new LinkedList<Integer>();
            indices = new LinkedList<Integer>();
        }

        private int quad_count = 0;
        private LinkedList<Integer> data;
        private LinkedList<Integer> indices;

        public void add(Quad quad) {

            if ( 0 <= quad.x && quad.x <= 30 && 0 <= quad.y && quad.y <= 30 && 0 <= quad.z && quad.z <= 30 && 0 <= quad.tex_id && quad.tex_id < 64 ) {
                int stuff, x, y, z, tv, tid, l;

                x = quad.x + dzs[quad.id][0]; //x (0 - 31)
                y = quad.y + dzs[quad.id][1]; //y (0 - 31)
                z = quad.z + dzs[quad.id][2]; //z (0 - 31) 15 bits

                tid = quad.tex_id ; //tid (0 - 63)
                tv = 0; //tvid (0 - 2) 8 bits

                l = ls[quad.id]; //lighting (0 - 15) //4 bits

                // 15 + 8 + 4 = 27 - 32 = 5 bits left ( room for texture id and 3 bits extra for whatever )

                stuff = x + (y << 5) + (z << 10) + (tid << 15) + (tv << 21) + (l << 23);

                data.add(stuff);

                x = quad.x + dzs[quad.id][0] + dxs[quad.id][0];
                y = quad.y + dzs[quad.id][1] + dxs[quad.id][1];
                z = quad.z + dzs[quad.id][2] + dxs[quad.id][2];

                tid = quad.tex_id;
                tv = 1;

                l = ls[quad.id];

                stuff = x + (y << 5) + (z << 10) + (tid << 15) + (tv << 21) + (l << 23);

                data.add(stuff);

                x = quad.x + dzs[quad.id][0] + dys[quad.id][0];
                y = quad.y + dzs[quad.id][1] + dys[quad.id][1];
                z = quad.z + dzs[quad.id][2] + dys[quad.id][2];

                tid = quad.tex_id;
                tv = 2;

                l = ls[quad.id];

                stuff = x + (y << 5) + (z << 10) + (tid << 15) + (tv << 21) + (l << 23);

                data.add(stuff);

                x = quad.x + dzs[quad.id][0] + dxs[quad.id][0] + dys[quad.id][0];
                y = quad.y + dzs[quad.id][1] + dxs[quad.id][1] + dys[quad.id][1];
                z = quad.z + dzs[quad.id][2] + dxs[quad.id][2] + dys[quad.id][2];

                tid = quad.tex_id;
                tv = 3;

                l = ls[quad.id];

                stuff = x + (y << 5) + (z << 10) + (tid << 15) + (tv << 21) + (l << 23);

                data.add(stuff);

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

            IntBuffer stuffBuffer = null;
            IntBuffer indicesBuffer = null;

            try {
                vao = glGenVertexArrays();
                glBindVertexArray(vao);

                // Position VBO
                int vbo = glGenBuffers();
                int[] stuff = data.stream().mapToInt(i->i).toArray();
                stuffBuffer = MemoryUtil.memAllocInt(stuff.length);
                stuffBuffer.put(stuff).flip();
                glBindBuffer(GL_ARRAY_BUFFER, vbo);
                glBufferData(GL_ARRAY_BUFFER, stuffBuffer, GL_STATIC_DRAW);
                glEnableVertexAttribArray(0);
                glVertexAttribIPointer(0, 1, GL_INT, 0, 0);

                // Index VBO
                int idx_vbo = glGenBuffers();
                indicesBuffer = MemoryUtil.memAllocInt(indices.size());
                indicesBuffer.put(indices.stream().mapToInt(i->i).toArray()).flip();
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idx_vbo);
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

                glBindBuffer(GL_ARRAY_BUFFER, 0);
                glBindVertexArray(0);

                vbos.add(vbo);
                vbos.add(idx_vbo);

            } finally {
                if (stuffBuffer != null) { MemoryUtil.memFree(stuffBuffer); }
                if (indicesBuffer != null) { MemoryUtil.memFree(indicesBuffer); }
            }

        }

        public void render() {
            glBindVertexArray(vao);
            glDrawElements(GL_TRIANGLES, quad_count * 6, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);
        }

        static public class Quad {
             public Quad(int x1, int y1, int z1, int id1, int tid1, int c1l, int c2l, int c3l, int c4l) {
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
                1,
        };


    }
}
