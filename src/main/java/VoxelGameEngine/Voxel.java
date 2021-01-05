package VoxelGameEngine;

public enum Voxel {
    AIR(new Empty()),
    GRASS(new Block(2, 0, 0, 1, 0, 0)),
    DIRT(new Block(1, 1, 1, 1, 1, 1));

    public void createMesh(Chunk chunk, int x, int y, int z) { meshable.mesh(chunk, x, y, z); }

    private Meshable meshable;

    private Voxel(Meshable m) { meshable = m; }

    public interface Meshable {
        public void mesh(Chunk chunk, int x, int y, int z);
    }

    static public class Empty implements Meshable {
        public Empty() {}
        public void mesh(Chunk chunk, int x, int y, int z) {}
    }

    static public class Block implements Meshable {
        public Block(int t, int tr, int tl, int b, int br, int bl) {
            tex_ids = new int[] { t, tr, tl, b, br, bl };
        }

        public void mesh(Chunk chunk, int x, int y, int z) {
            if (chunk.getVoxel(x, y + 1, z) == AIR) {  chunk.mesh.add(new VVMesh.Quad(x, y, z, 0, tex_ids[0])); }
            if (chunk.getVoxel(x - 1, y, z) == AIR) {  chunk.mesh.add(new VVMesh.Quad(x, y, z, 1, tex_ids[1])); }
            if (chunk.getVoxel(x, y, z - 1) == AIR) {  chunk.mesh.add(new VVMesh.Quad(x, y, z, 2, tex_ids[2])); }
            if (chunk.getVoxel(x, y - 1, z) == AIR) {  chunk.mesh.add(new VVMesh.Quad(x, y, z, 3, tex_ids[3])); }
            if (chunk.getVoxel(x, y, z + 1) == AIR) {  chunk.mesh.add(new VVMesh.Quad(x, y, z, 4, tex_ids[4])); }
            if (chunk.getVoxel(x + 1, y, z) == AIR) {  chunk.mesh.add(new VVMesh.Quad(x, y, z, 5, tex_ids[5])); }
        }

        private int[] tex_ids;
    }
}
