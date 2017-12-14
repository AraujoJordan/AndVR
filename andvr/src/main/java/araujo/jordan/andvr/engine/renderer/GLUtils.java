package araujo.jordan.andvr.engine.renderer;

import android.opengl.GLES30;
import android.util.Log;

/**
 * Created by araujojordan on 11/05/17.
 */

public class GLUtils {
    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     * <p>
     * <pre>
     * mColorHandle = GLES30.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     * <p>
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        if ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
            Log.e(GLESRenderer.class.getCanonicalName(), glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}
