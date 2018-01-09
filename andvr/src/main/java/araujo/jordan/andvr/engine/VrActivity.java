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

    private static final float Z_NEAR = 0.5f;
    private static final float Z_FAR = 1000f;
    public static float[] mLightPosInEyeSpace = new float[4];
    public static float[] mViewMatrix = new float[16];
    public static float[] mProjectionViewMatrix = new float[16];
    public VREngine engine;
    private GvrView gvrView;
    private float[] lookAtMatrix = new float[16];
    private float[] mLightModelMatrix = new float[16];
    private float[] mLightPosInWorldSpace = new float[4];
    private float[] mProjectionMatrix;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vr);

        getIntent().getStringExtra("produtoid");

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

        //Get the lookAtMatrix
        lookAtMatrix = engine.getCamera().updateCamera(headTransform);

        //Init Light
        if (engine.light != null) {
            Matrix.setIdentityM(mLightModelMatrix, 0);
            Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -1.0f);
            Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0,
                    new float[]{engine.light.position.xyz[0], engine.light.position.xyz[1], engine.light.position.xyz[2], 1.0f},
                    0);
        }

        //Update engine logic
        engine.engineUpdates();
    }


    @Override
    public void onDrawEye(Eye eye) {

        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT);

        // Update of view Matrix
        Matrix.multiplyMM(mViewMatrix, 0, eye.getEyeView(), 0, lookAtMatrix, 0);

        // Put light in the correct position of the eye side
        if (engine.light != null) {
            Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);
        }
        // Update the projection Matrix
        mProjectionMatrix = eye.getPerspective(Z_NEAR, Z_FAR);

        // Update projectionView Matrix
        Matrix.multiplyMM(mProjectionViewMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        GLES32.glEnable(GLES32.GL_DEPTH_TEST); // Enable depth testing
        GLES32.glEnable(GLES32.GL_CULL_FACE); // Use culling to remove back faces.
        GLES32.glCullFace(GLES32.GL_BACK); // specify which faces to not draw

        //DRAW 3D MODELS COMPONENTS
        engine.draw();

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
