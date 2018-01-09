package araujo.jordan.andvr.engine.entity.components.model3d;

import android.opengl.GLES32;
import android.opengl.Matrix;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import araujo.jordan.andvr.R;
import araujo.jordan.andvr.engine.VREngine;
import araujo.jordan.andvr.engine.VrActivity;
import araujo.jordan.andvr.engine.entity.Entity;
import araujo.jordan.andvr.engine.resources.BufferCache;
import araujo.jordan.andvr.engine.resources.object3D.GenericObject3D;

/**
 * Created by arauj on 23/03/2017.
 */

public class ModelDrawVR implements Draw {

    private static final int COORDS_PER_VERTEX = 3; // number of coordinates per vertex in this array
    private static final int POSITION_DATA_SIZE = 3;
    private static final int NORMAL_DATA_SIZE = 3;
    private static final int TEXTURE_COORDINATE_DATA_SIZE = 3;
    private static final int BYTES_PER_FLOAT = 4;

    private final int[] buffers;

    private final int mProgram;
    private final int modelPositionParam;
    private final int modelNormalParam;
    private final int modelModelParam;
    private final int modelModelViewParam;
    private final int modelModelViewProjectionParam;
    private final int mLightPosHandle;
    private final int mTextureCoordinateHandle;
    private final int mTextureUniformHandle;
//    private int mLightPowerHandle;

    private final int mTextureDataHandle;

    private final int vertexCount;
    private final Entity parentEntity;
    private final VREngine engine;

    //Used for speed
    private float[] MVPmatrix = new float[16];

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public ModelDrawVR(String objectID, GenericObject3D obj3D, VREngine engine, Entity parentEntity) throws ExceptionInInitializerError {
        this.parentEntity = parentEntity;
        this.engine = engine;

        vertexCount = obj3D.vertSize / COORDS_PER_VERTEX;

        FloatBuffer vertexBuffer = obj3D.vertBuffer.getFloatBuffer();
        FloatBuffer normalBuffer = obj3D.normalBuffer.getFloatBuffer();
        FloatBuffer textureCoordsBuffer = obj3D.uvwBuffer.getFloatBuffer();

        // Prepare shaders and OpenGL program
        mProgram = GLES32.glCreateProgram();
        GLES32.glAttachShader(mProgram, loadGLShader(GLES32.GL_VERTEX_SHADER, R.raw.mvertex_shader));
        GLES32.glAttachShader(mProgram, loadGLShader(GLES32.GL_FRAGMENT_SHADER, R.raw.mfragment_shader));
        GLES32.glLinkProgram(mProgram);
        GLES32.glUseProgram(mProgram);

        //ALOCATE VMEMORY
        modelPositionParam = GLES32.glGetAttribLocation(mProgram, "a_Position");
        modelNormalParam = GLES32.glGetAttribLocation(mProgram, "a_Normal");
        mTextureCoordinateHandle = GLES32.glGetAttribLocation(mProgram, "a_TexCoordinate");
        mTextureUniformHandle = GLES32.glGetUniformLocation(mProgram, "u_Texture");
//        if (engine.light != null)
//            mLightPowerHandle = GLES32.glGetUniformLocation(mProgram, "a_lightPower");

        modelModelParam = GLES32.glGetUniformLocation(mProgram, "u_Model");
        modelModelViewParam = GLES32.glGetUniformLocation(mProgram, "u_MVMatrix");
        modelModelViewProjectionParam = GLES32.glGetUniformLocation(mProgram, "u_MVPMatrix");
        mLightPosHandle = GLES32.glGetUniformLocation(mProgram, "u_LightPos");

        //CACHE
        BufferCache bufferCache = BufferCache.getInstance();

        //TEXTURES
        if (parentEntity.getTexture() != null) {
            mTextureDataHandle = parentEntity.getTexture().initTexture();
        } else
            mTextureDataHandle = -1;

        //INIT VBO
        if (bufferCache.modelBuffer.containsKey(objectID)) {
            buffers = bufferCache.modelBuffer.get(objectID);
        } else {
            buffers = new int[3];
            GLES32.glGenBuffers(3, buffers, 0);

            GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, buffers[0]);
            GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, vertexBuffer.capacity() * BYTES_PER_FLOAT, vertexBuffer, GLES32.GL_STATIC_DRAW);

            GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, buffers[1]);
            GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, normalBuffer.capacity() * BYTES_PER_FLOAT, normalBuffer, GLES32.GL_STATIC_DRAW);

            GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, buffers[2]);
            GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, textureCoordsBuffer.capacity() * BYTES_PER_FLOAT, textureCoordsBuffer,
                    GLES32.GL_STATIC_DRAW);
        }

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0);

        //CLEAR MEMORY
//        vertexBuffer.clear();
//        normalBuffer.clear();
//        textureCoordsBuffer.clear();
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing the triangle.
     */
    public void draw() {

        GLES32.glUseProgram(mProgram);

        // Set the Model in the shader, used to calculate lighting
        GLES32.glUniformMatrix4fv(modelModelParam, 1, false, parentEntity.getTransformation().modelMatrix, 0);

        float[] modelView = new float[16];
        Matrix.multiplyMM(modelView, 0, VrActivity.mViewMatrix, 0, parentEntity.getTransformation().modelMatrix, 0);

        // Set the ModelView in the shader, used to calculate lighting
        GLES32.glUniformMatrix4fv(modelModelViewParam, 1, false, modelView, 0);

        //Calculate mvp for this object
        Matrix.multiplyMM(MVPmatrix, 0, VrActivity.mProjectionViewMatrix, 0, parentEntity.getTransformation().modelMatrix, 0);

        // Set the ModelViewProjection matrix in the shader
        GLES32.glUniformMatrix4fv(modelModelViewProjectionParam, 1, false, MVPmatrix, 0);

        // Pass in the light position in eye space.
        GLES32.glUniform3f(mLightPosHandle, VrActivity.mLightPosInEyeSpace[0], VrActivity.mLightPosInEyeSpace[1], VrActivity.mLightPosInEyeSpace[2]);

        // Vertex Buffer Object (VBO)

        // Blind Texture
        if (parentEntity.getTexture() != null) {
            GLES32.glActiveTexture(GLES32.GL_TEXTURE0);
            GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, mTextureDataHandle);
            GLES32.glUniform1i(mTextureUniformHandle, 0);
        }

//        if (engine.light != null) {
//            GLES32.glUniform1f(mLightPowerHandle, engine.light.power);
//        }

        // Pass in the position buffer id
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, buffers[0]);
        GLES32.glEnableVertexAttribArray(modelPositionParam);
        GLES32.glVertexAttribPointer(modelPositionParam, POSITION_DATA_SIZE, GLES32.GL_FLOAT, false, 0, 0);

        // Pass in the normal buffer id
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, buffers[1]);
        GLES32.glEnableVertexAttribArray(modelNormalParam);
        GLES32.glVertexAttribPointer(modelNormalParam, NORMAL_DATA_SIZE, GLES32.GL_FLOAT, false, 0, 0);

        // Pass in the texture buffer id
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, buffers[2]);
        GLES32.glEnableVertexAttribArray(mTextureCoordinateHandle);
        GLES32.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE, GLES32.GL_FLOAT, false,
                0, 0);


        // Bind buffers
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0);

        // Draw all
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, vertexCount);
//        GLUtils.checkGlError("Drawing model");
    }

    /**
     * Converts a raw text file, saved as a resource, into an OpenGL ES shader.
     *
     * @param type  The type of shader we will be creating.
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return The shader object handler.
     */
    private int loadGLShader(int type, int resId) {

        String code = "";

        InputStream inputStream = engine.vrAct.getResources().openRawResource(resId);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            code = sb.toString();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int shader = GLES32.glCreateShader(type);
        GLES32.glShaderSource(shader, code);
        GLES32.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES32.glGetShaderiv(shader, GLES32.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            Log.e(getClass().getSimpleName(), "Error compiling shader: " + GLES32.glGetShaderInfoLog(shader));
            GLES32.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }
}
