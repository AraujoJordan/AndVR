package araujo.jordan.andvr.engine;

import android.opengl.GLES32;
import android.opengl.Matrix;
import android.os.Bundle;

import com.google.vr.sdk.base.AndroidCompat;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

import araujo.jordan.andvr.R;

/**
 * Created by jordan on 02/05/17.
 */

public class VrActivity extends GvrActivity implements GvrView.StereoRenderer {

    private static final float Z_NEAR = 1.0f;
    private static final float Z_FAR = 1000f;
    public static float[] mLightPosInEyeSpace = new float[4];
    public static float[] mViewMatrix = new float[16];
    public static float[] mProjectionViewMatrix = new float[16];
    public float[] mLightPosInModelSpace = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
    public VREngine engine;
    private GvrView gvrView;
    private float[] camera = new float[16];
    private float[] mLightModelMatrix = new float[16];
    private float[] mLightPosInWorldSpace = new float[4];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vr);

        gvrView = findViewById(R.id.gvr_view);

        gvrView.setRenderer(this);
        gvrView.setTransitionViewEnabled(true);

        // Enable Cardboard-trigger feedback with Daydream headsets. This is a simple way of supporting
        // Daydream controller input for basic interactions using the existing Cardboard trigger API.
        gvrView.enableCardboardTriggerEmulation();

        if (gvrView.setAsyncReprojectionEnabled(true)) {
            // Async reprojection decouples the app framerate from the display framerate,
            // allowing immersive interaction even at the throttled clockrates set by
            // sustained performance mode.
            AndroidCompat.setSustainedPerformanceMode(this, true);
        }

        setGvrView(gvrView);
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {

        //Creation of a beautiful blue sky
        GLES32.glClearColor(0.529411765f, 0.807843137f, 0.980392157f, 1.0f);

        //Get the Camera Matrix
        camera = engine.getCamera().updateCamera(headTransform);

        //Update engine logic
        engine.engineUpdates();
    }


    @Override
    public void onDrawEye(Eye eye) {

        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT);

        GLES32.glEnable(GLES32.GL_DEPTH_TEST);
        GLES32.glEnable(GLES32.GL_CULL_FACE);

        //-----------------------------------------------------------------------------------------
        //VIEW MATRIX CREATION
        Matrix.multiplyMM(mViewMatrix, 0, eye.getEyeView(), 0, camera, 0);

        //-----------------------------------------------------------------------------------------
        //UPDATE LIGHT
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -1.0f);
        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);

        //PROJECTION MATRIX CREATION
        float[] mProjectionMatrix = eye.getPerspective(Z_NEAR, Z_FAR);

        //-----------------------------------------------------------------------------------------
        //PROJECTIONVIEW MATRIX CREATION
        Matrix.multiplyMM(mProjectionViewMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        //DRAW MODEL3D COMPONENTS
        engine.draw();
        //-----------------------------------------------------------------------------------------

    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int i, int i1) {

    }

    public void setup(VREngine engine) {
        this.engine = engine;
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        engine.loadIntoOpenGL();
    }

    @Override
    public void onRendererShutdown() {

    }

    @Override
    protected void onPause() {
        engine.pause();
        super.onPause();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        engine.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        engine.finish();
        finish();
    }
}
