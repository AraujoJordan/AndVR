package araujo.jordan.andvr.engine.resources;

import java.util.HashMap;

/**
 * Created by jordan on 02/01/18.
 */

public class BufferCache {
    public static BufferCache instance = null;

    public HashMap<String, int[]> modelBuffer = new HashMap<>();
    public HashMap<String, Integer> textureBuffer = new HashMap<>();

    public static BufferCache getInstance() {
        if(instance == null) {
            instance = new BufferCache();
        }
        return instance;
    }
}
