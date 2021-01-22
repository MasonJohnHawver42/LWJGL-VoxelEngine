package VGE;
import SGE.Renderer.Camera;
import SGE.Renderer.ShaderProgram;
import SGE.User.Assets.Asset;
import org.joml.Vector3i;

import java.util.LinkedList;

public class Vorld {

    public Vorld(ShaderProgram sp, Asset.Texture tex) {
        sprog = sp;
        texture = tex;

        chunks = new LinkedList<Chunk>();
    }


    public Chunk generateChunk(int x, int y, int z) {
        Chunk c = new Chunk(x, y, z);
        c.createMesh();
        return c;
    }

    public void loadChunk(int x, int y, int z) {

        boolean gen = true;

        for (Chunk chunk : chunks) {
            if (chunk.x == x && chunk.y == y && chunk.z == z) {
                gen = false; break;
            }
        }

        if (gen) { chunks.add(generateChunk(x, y, z)); }
    }

    public void removeChunk(int x, int y, int z) {
        Chunk degen = null;

        for (Chunk chunk : chunks) {
            if (chunk.x == x && chunk.y == y && chunk.z == z) {
                degen = chunk; break;
            }
        }

        if (degen != null) {
            degen.terminate();
            chunks.remove(degen);
        }
    }
    
    public void setChunks(int[][] positions) {
        LinkedList<Boolean> loaded = new LinkedList<Boolean>();
        for (int i = 0; i < positions.length; i++) { loaded.add(false); }

        LinkedList<Chunk> copy = new LinkedList<Chunk>(chunks);
        for (Chunk c : copy) {
            boolean degen = true;

            int i = 0;
            for (int[] pos : positions) {
                int x = pos[0]; int y = pos[1]; int z = pos[2];
                if (c.x == x && c.y == y && c.z == z) {
                    degen = false;
                    loaded.set(i, true);
                    break;
                }
                i++;
            }

            if (degen) {
                c.terminate();
                chunks.remove(c);
            }
        }

        int i = 0;
        for (boolean l : loaded) {
            if (!l) {
                int[] pos = positions[i];
                int x = pos[0]; int y = pos[1]; int z = pos[2];
                chunks.add(generateChunk(x, y, z));
            }
            i++;
        }
    }
    
    public void render(Camera cam) {

        sprog.bind();

        sprog.setUniform("cam_mat", cam.getCameraMatrix());

        sprog.setUniform("texture_sampler", 0);

        texture.bind();

        for (Chunk c : chunks) {
            sprog.setUniform("chunk_pos", c.x, c.y, c.z);
            c.mesh.render();
        }

        texture.unbind();

        sprog.unbind();

    }

    LinkedList<Chunk> chunks;

    ShaderProgram sprog;
    Asset.Texture texture;
}
