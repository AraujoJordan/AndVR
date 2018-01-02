package araujo.jordan.andvr.engine.resources;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;

import araujo.jordan.andvr.engine.resources.object3D.GenericObject3D;
import araujo.jordan.andvr.engine.resources.object3D.ManualWavefront;
import araujo.jordan.andvr.engine.resources.object3D.MokiatWavefront;
import araujo.jordan.andvr.engine.texture.TextureHelper;

/**
 * Created by arauj on 24/02/2017.
 * Here it will be load image and sound resources of the game, this will draw on the begin,
 * so it will not slow down the performance on the runtime
 */
public class GameResources {

    private Hashtable<String, GenericObject3D> object3dList;
    private boolean isLoaded;
    private Hashtable<String, Integer> textureList;

    public GameResources() {
        isLoaded = false;
        object3dList = new Hashtable<>();
        textureList = new Hashtable<>();
    }

    public void addOBJ(Activity act, String idLabel, String fileName) {
        if (isLoaded)
            throw new RuntimeException("Can't create a 3d object now, create before");
        try {

            if (!Arrays.asList(act.getResources().getAssets().list("")).contains(fileName))
                throw new IOException("File " + fileName + " in assets folder doesn't exist. Check if the assets folder and the file is there");

//            GenericObject3D obj = new ManualWavefront(idLabel, act.getAssets().open(fileName));
            GenericObject3D obj = new MokiatWavefront(act, idLabel, act.getAssets().open(fileName));
            object3dList.put(idLabel, obj);
            Log.v(getClass().getSimpleName(), "GenericObject3D added on hash");
        } catch (Exception error) {
            Log.e(getClass().getSimpleName(), "Can't create a 3d object" + error.getMessage());
        }
    }

    public void loadTexture(Activity act, String label, int res) {
        if (isLoaded)
            throw new RuntimeException("Can't create a texture object now, create before");
        try {
            textureList.put(label, TextureHelper.loadTexture(act, res));
        } catch (Exception error) {
            Log.e(getClass().getSimpleName(), "Can't create a texture object" + error.getMessage());
        }
    }

    public GenericObject3D get3DModel(String idLabel) {
        return object3dList.get(idLabel);
    }

    public int getTextureID(String textureLabel) {
        return textureList.get(textureLabel);
    }

    public void isLoaded() {
        isLoaded = true;
    }


}
