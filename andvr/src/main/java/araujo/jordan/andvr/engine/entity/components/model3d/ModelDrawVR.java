package araujo.jordan.andvr.engine.entity.components.model3d;

import android.opengl.GLES32;
import android.opengl.Matrix;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.Arrays;

import araujo.jordan.andvr.R;
import araujo.jordan.andvr.engine.VREngine;
import araujo.jordan.andvr.engine.VrActivity;
import araujo.jordan.andvr.engine.draw.Color;
import araujo.jordan.andvr.engine.entity.Entity;
import araujo.jordan.andvr.engine.renderer.GLUtils;
import araujo.jordan.andvr.engine.resources.object3D.GenericObject3D;
import araujo.jordan.andvr.engine.texture.TextureHelper;
import araujo.jordan.andvr.engine.utils.BufferFactory;

/**
 * Created by arauj on 23/03/2017.
 */

public class ModelDrawVR implements Draw {

    private static final int COORDS_PER_VERTEX = 3; // number of coordinates per vertex in this array
    private static final int POSITION_DATA_SIZE = 3;
    private static final int NORMAL_DATA_SIZE = 3;
//    private static final int COLOR_DATA_SIZE = 4;
    private static final int TEXTURE_COORDINATE_DATA_SIZE = 2;
    private static final int BYTES_PER_FLOAT = 4;


//    private final int[] buffers;

    FloatBuffer vertexBuffer;
    FloatBuffer normalBuffer;
    FloatBuffer colorBuffer;
    FloatBuffer textureCoordsBuffer;

    private final int mProgram;
    private final int modelPositionParam;
    private final int modelNormalParam;
//    private final int modelColorParam;
    private final int modelModelParam;
    private final int modelModelViewParam;
    private final int modelModelViewProjectionParam;
    private final int modelLightPosParam;
    private final int modelTexCoordinateParam;
    private final int modelTexUniformParam;

    private final int mTextureDataHandle;

    private float color[] = {1f, 0f, 0f, 1f}; //default color
    private int vertexCount;

    private Entity entity;
    private VREngine engine;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public ModelDrawVR(String objectID, GenericObject3D obj3D, int textureID, VREngine engine, Entity entity, Color colorObj) {
        this.entity = entity;
        this.engine = engine;

        //COLOR
//        if (colorObj != null) //If color is defined, use it. If not, get the default color
//            color = colorObj.getFloatRGBA();
//        Log.d("Color", Arrays.toString(color));
        float[] colorCoords = new float[(obj3D.vertSize / COORDS_PER_VERTEX) * BYTES_PER_FLOAT];
        int index = 0;
        for (int i = 0; i < obj3D.vertSize / COORDS_PER_VERTEX; i++) {
            colorCoords[index++] = color[0];
            colorCoords[index++] = color[1];
            colorCoords[index++] = color[2];
            colorCoords[index++] = color[3];
        }

        mTextureDataHandle = TextureHelper.loadTexture(engine.vrAct, R.drawable.cube);

        vertexCount = obj3D.vertSize / COORDS_PER_VERTEX;

//        FloatBuffer vertexBuffer;
//        FloatBuffer normalBuffer;
////        FloatBuffer colorBuffer;
//        FloatBuffer textureCoordsBuffer;

        vertexBuffer = obj3D.vertBuffer.getFloatBuffer();
//        normalBuffer = new BufferFactory(createNormals()).getFloatBuffer();
        normalBuffer = obj3D.normalBuffer.getFloatBuffer();
//        colorBuffer = new BufferFactory(colorCoords).getFloatBuffer();
        textureCoordsBuffer = obj3D.uvwBuffer.getFloatBuffer();

        int vertexShader;
        int passthroughShader;

        // prepare shaders and OpenGL program
        vertexShader = loadGLShader(GLES32.GL_VERTEX_SHADER, R.raw.texture_vertex_shader);
        passthroughShader = loadGLShader(GLES32.GL_FRAGMENT_SHADER, R.raw.texture_fragment_shader);

        mProgram = GLES32.glCreateProgram();
        GLES32.glAttachShader(mProgram, vertexShader);
        GLES32.glAttachShader(mProgram, passthroughShader);
        GLES32.glLinkProgram(mProgram);
        GLES32.glUseProgram(mProgram);

        //ALOCATE VMEMORY
        modelPositionParam = GLES32.glGetAttribLocation(mProgram, "a_Position");
        modelNormalParam = GLES32.glGetAttribLocation(mProgram, "a_Normal");
//        modelColorParam = GLES32.glGetAttribLocation(mProgram, "a_Color");
        modelTexCoordinateParam = GLES32.glGetAttribLocation(mProgram, "a_TexCoordinate");
        modelTexUniformParam = GLES32.glGetUniformLocation(mProgram, "u_Texture");

        modelModelParam = GLES32.glGetUniformLocation(mProgram, "u_Model");
        modelModelViewParam = GLES32.glGetUniformLocation(mProgram, "u_MVMatrix");
        modelModelViewProjectionParam = GLES32.glGetUniformLocation(mProgram, "u_MVP");
        modelLightPosParam = GLES32.glGetUniformLocation(mProgram, "u_LightPos");

        //TEXTURES
//        GLES32.glActiveTexture(GLES32.GL_TEXTURE0);
//        GLES32.glGenerateMipmap(GLES32.GL_TEXTURE_2D);
//        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, mTextureDataHandle);
//        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MAG_FILTER, GLES32.GL_LINEAR);
//
//        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, mTextureDataHandle);
//        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MIN_FILTER, GLES32.GL_LINEAR_MIPMAP_LINEAR);
//
//        GLES32.glUniform1i(modelTexUniformParam, 0);

//        //INIT VBO
//        buffers = new int[3];
//        GLES32.glGenBuffers(3, buffers, 0);
//
//        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, buffers[0]);
//        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, vertexBuffer.capacity() * BYTES_PER_FLOAT, vertexBuffer, GLES32.GL_STATIC_DRAW);
//
//        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, buffers[1]);
//        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, normalBuffer.capacity() * BYTES_PER_FLOAT, normalBuffer, GLES32.GL_STATIC_DRAW);
//
//        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, buffers[2]);
//        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, textureCoordsBuffer.capacity() * BYTES_PER_FLOAT, textureCoordsBuffer,
//                GLES32.GL_STATIC_DRAW);

//        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, buffers[4]);
//        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, colorBuffer.capacity() * BYTES_PER_FLOAT, colorBuffer,
//                GLES32.GL_STATIC_DRAW);

//        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0);

        //CLEAR MEMORY
//        vertexBuffer.clear();
//        normalBuffer.clear();
//        textureCoordsBuffer.clear();
//        colorBuffer.limit(0);
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing the triangle.
     */
    public void draw() {

        //Calculate mvp for this object
        float[] modelViewProjMatrix = new float[16];
        Matrix.multiplyMM(modelViewProjMatrix, 0, VrActivity.mProjectionViewMatrix, 0, entity.getTransformation().modelMatrix, 0);

        GLES32.glUseProgram(mProgram);

        GLES32.glUniform3fv(modelLightPosParam, 1, VrActivity.mLightEyeMatrix, 0);

        // Set the Model in the shader, used to calculate lighting
        GLES32.glUniformMatrix4fv(modelModelParam, 1, false, entity.getTransformation().modelMatrix, 0);

        float[] modelView = new float[16];
        Matrix.multiplyMM(modelView, 0, VrActivity.mViewMatrix, 0, entity.getTransformation().modelMatrix, 0);

        // Set the ModelView in the shader, used to calculate lighting
        GLES32.glUniformMatrix4fv(modelModelViewParam, 1, false, modelView, 0);


        //Vertex Array Object (VAO)

        // Set the position of theVertex Buffer Object model
        GLES32.glVertexAttribPointer(
                modelPositionParam, COORDS_PER_VERTEX, GLES32.GL_FLOAT, false, 0, vertexBuffer);

        // Set the ModelViewProjection matrix in the shader.
        GLES32.glUniformMatrix4fv(modelModelViewProjectionParam, 1, false, modelViewProjMatrix, 0);

        // Set the normal positions of the model, again for shading
        GLES32.glVertexAttribPointer(modelNormalParam, 3, GLES32.GL_FLOAT, false, 0, normalBuffer);

        // Set the colors of the model
//        GLES32.glVertexAttribPointer(modelColorParam, 4, GLES32.GL_FLOAT, false, 0, colorBuffer);

        // Set UVW of the model
        GLES32.glVertexAttribPointer(modelTexCoordinateParam, 2, GLES32.GL_FLOAT, false, 0, textureCoordsBuffer);

        // Vertex Buffer Object (VBO)
        // Pass in the position information
//        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, buffers[0] );
//        GLES32.glEnableVertexAttribArray(modelPositionParam);
//        GLES32.glVertexAttribPointer(modelPositionParam, POSITION_DATA_SIZE, GLES32.GL_FLOAT, false, 0, 0);
//
//        // Pass in the normal information
//        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, buffers[1]);
//        GLES32.glEnableVertexAttribArray(modelNormalParam);
//        GLES32.glVertexAttribPointer(modelNormalParam, NORMAL_DATA_SIZE, GLES32.GL_FLOAT, false, 0, 0);
//
//        // Pass in the texture information
//        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, buffers[2]);
//        GLES32.glEnableVertexAttribArray(modelTexCoordinateParam);
//        GLES32.glVertexAttribPointer(modelTexCoordinateParam, TEXTURE_COORDINATE_DATA_SIZE, GLES32.GL_FLOAT, false,
//                0, 0);

        // Pass in the color information
//        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, buffers[3]);
//        GLES32.glEnableVertexAttribArray(modelColorParam);
//        GLES32.glVertexAttribPointer(modelColorParam, COLOR_DATA_SIZE, GLES32.GL_FLOAT, false,
//                0, 0);

        // Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0);

        //Activate Texture
//        GLES32.glActiveTexture(GLES32.GL_TEXTURE0);

        // Enable vertex arrays
//        GLES32.glEnableVertexAttribArray(modelPositionParam);
//        GLES32.glEnableVertexAttribArray(modelNormalParam);
//        GLES32.glEnableVertexAttribArray(modelColorParam);
//        GLES32.glEnableVertexAttribArray(modelTexCoordinateParam);

        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex arrays
//        GLES32.glDisableVertexAttribArray(modelPositionParam);
//        GLES32.glDisableVertexAttribArray(modelNormalParam);
//        GLES32.glDisableVertexAttribArray(modelColorParam);
//        GLES32.glDisableVertexAttribArray(modelTexCoordinateParam);

        GLUtils.checkGlError("Drawing model");
    }

    public void setColor(Color color) {
        this.color = color.getFloatRGBA();
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
