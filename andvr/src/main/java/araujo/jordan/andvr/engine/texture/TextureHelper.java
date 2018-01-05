package araujo.jordan.andvr.engine.texture;

import android.graphics.Bitmap;
import android.opengl.GLES32;
import android.opengl.GLUtils;

/**
 * Created by araujojordan on 28/05/17.
 */

public class TextureHelper {
    private static final String TAG = "TextureHelper";

    /**
     * Loads a texture from a resource ID, returning the OpenGL ID for that
     * texture. Returns 0 if the load failed.
     *
     * @return
     */
    public static int loadTexture(Bitmap bitmap) {
        final int[] textureHandle = new int[1];

        GLES32.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] == 0) {
            throw new RuntimeException("Error generating texture name.");
        }

        // Bind to the texture in OpenGL
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, textureHandle[0]);

        // Set filtering
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MIN_FILTER, GLES32.GL_NEAREST);
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MAG_FILTER, GLES32.GL_NEAREST);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES32.GL_TEXTURE_2D, 0, bitmap, 0);

        // Recycle the bitmap, since its data has been loaded into OpenGL.
        bitmap.recycle();

        return textureHandle[0];
    }
}