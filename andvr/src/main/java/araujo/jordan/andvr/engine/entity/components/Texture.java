package araujo.jordan.andvr.engine.entity.components;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import araujo.jordan.andvr.engine.VREngine;
import araujo.jordan.andvr.engine.resources.BufferCache;
import araujo.jordan.andvr.engine.texture.TextureHelper;

/**
 * Created by jordan on 01/01/18.
 */

public class Texture extends Component {

    public final String resourceLabel;
    private final VREngine engine;
    private final Bitmap bitmap;
    public int textureID = -1;

    public Texture(VREngine engine, String label) {
        this.engine = engine;
        this.resourceLabel = label;

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;    // No pre-scaling

        // Read in the resource
        bitmap = BitmapFactory.decodeResource(engine.vrAct.getResources(), engine.resouces.getTexture(label), options);
        Log.v("Texture", bitmap == null ? "Bitmap " + label + " is NULL" : "Bitmap " + label + " is OK");


    }

    public int initTexture() {

        textureID = TextureHelper.loadTexture(bitmap);
        BufferCache.getInstance().textureBuffer.put(resourceLabel, textureID);
        Log.v("Texture", "Texture " + resourceLabel + " inited with ID " + textureID);

        return textureID;
    }

    @Override
    public void run(VREngine engine) {

    }
}
