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
import araujo.jordan.andvr.engine.entity.Entity;
import araujo.jordan.andvr.engine.renderer.GLUtils;

/**
 * Created by jordan on 02/05/17.
 */

public class VrActivity extends GvrActivity implements GvrView.StereoRenderer {

    private static final float Z_NEAR = 0.5f;
    private static final float Z_FAR = 1000f;
    public float[] LIGHT_POS_IN_WORLD_SPACE = new float[]{200f, 200f, 200f, 1.0f};
    public static float[] mViewMatrix = new float[16];
    public static float[] mProjectionViewMatrix = new float[16];
    public static float[] mLightEyeMatrix = new float[16];
    private GvrView gvrView;
    public VREngine engine;
    private float[] camera = new float[16];
    private double theta = 0;


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

        GLES32.glEnable(GLES32.GL_DEPTH_TEST);
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT);

        GLUtils.checkGlError("colorParam");

        //-----------------------------------------------------------------------------------------
        //VIEW MATRIX CREATION
        Matrix.multiplyMM(mViewMatrix, 0, eye.getEyeView(), 0, camera, 0);

        //UPDATE LIGHT
        //-----------------------------------------------------------------------------------------
        Matrix.multiplyMV(mLightEyeMatrix, 0, mViewMatrix, 0, LIGHT_POS_IN_WORLD_SPACE, 0);

        //PROJECTION MATRIX CREATION
        float[] mProjectionMatrix = eye.getPerspective(Z_NEAR, Z_FAR);

        //-----------------------------------------------------------------------------------------
        //PROJECTIONVIEW MATRIX CREATION
        Matrix.multiplyMM(mProjectionViewMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        //UPDATE THE MODELS WITH THE PROJECTIONVIEW MATRIX
        GLES32.glFrontFace(GLES32.GL_CCW);
        GLES32.glEnable(GLES32.GL_CULL_FACE);
        GLES32.glCullFace(GLES32.GL_BACK);
        GLES32.glEnable(GLES32.GL_DEPTH_TEST);

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
        engine.initGeometry();
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
