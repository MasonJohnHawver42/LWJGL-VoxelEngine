package SGE.User.Assets;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {

    public AssetManager(String b) {
        assets = new HashMap<String, Asset>();
        base = b;
    }

    public void load(String filename, Asset asset) {
        try {
            asset.load(base + filename);
            assets.put(filename, asset);
        } catch (Exception e) { System.out.println(e); }
    }

    public void terminate() {
        for (Asset ass : assets.values() ) {
            ass.terminate();
        }
    }

    public Asset get(String filename) {
        return assets.get(filename);
    }

    public Map<String, Asset> assets;
    private String base;
}
