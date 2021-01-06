package VoxelGameEngine;

import SGE.Renderer.Texture;

public class Chunk {

    public Chunk(Texture sheet) {
        voxels = new int[31][31][31];

        for(int x = 0; x < 31; x++) {
            for(int y = 0; y < 31; y++) {
                for(int z = 0; z < 31; z++) {
                    voxels[x][y][z] = ( Math.random() > .9 ? 2 : 0);
                    //voxels[x][y][z] = ((x - 15.5)*(x - 15.5) + (y - 15.5)*(y - 15.5) + (z - 15.5)*(z - 15.5) < 200 ? ((x - 15.5)*(x - 15.5) + (y + 1 - 15.5)*(y + 1 - 15.5) + (z - 15.5)*(z - 15.5) < 200 ? 2 : 1) : 0);
                }
            }
        }

        mesh = new VVMesh(sheet);
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

    public VVMesh mesh;

    private int[][][] voxels;


    static private Voxel getVoxel(int id) {
        return Voxel.values()[id];
    }
}
