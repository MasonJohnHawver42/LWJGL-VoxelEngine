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
