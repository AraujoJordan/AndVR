package araujo.jordan.andvr.engine.resources;

import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * Created by jordan on 02/01/18.
 */

public class BufferCache {
    public static BufferCache instance = null;

    public HashMap<String,int[]> modelBuffer;
    public HashMap<String,Integer> textureBuffer;

    public static BufferCache getInstance() {
        if(instance == null) {
            instance = new BufferCache();
        }
        return instance;
    }
}
