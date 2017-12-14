package araujo.jordan.andvr.engine.resources;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import araujo.jordan.andvr.engine.math.Vector3D;
import araujo.jordan.andvr.engine.utils.BufferFactory;

/**
 * Created by jordan on 14/12/17.
 */

public class Object3D {
    public final Vector3D center;
    public  final int textureVTSize;

    public BufferFactory vertBuffer, normalBuffer, textureBuffer;
    public int vertSize = 0;

    public float width, height, depth;

    public Object3D(InputStream inputStream) throws IOException {

        Log.v(getClass().getSimpleName(),"Loading object 3D");

        ArrayList<Float> vertFloats = new ArrayList<>();
        ArrayList<Float> normFloats = new ArrayList<>();
        ArrayList<Float> textureFloats = new ArrayList<>();

        ArrayList<Vector3D> tempVerts = new ArrayList<>();
        ArrayList<Vector3D> tempNormals = new ArrayList<>();
        ArrayList<float[]> tempText = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String line;
        int vertexNumber = 1;

        float minWidth = 0, maxWidth = 0;
        float minHeight = 0, maxHeight = 0;
        float minDepth = 0, maxDepth = 0;

        Log.v(getClass().getSimpleName(),"Reading lines");
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            // handle line continuation marker \
            while (line.endsWith("\\")) {
                line = line.substring(0, line.length() - 1);
                final String s = reader.readLine();
                if (s != null) {
                    line += s;
                    line = line.trim();
                }
            }

            // ignore comments. goto next line
            if (line.length() > 0 && line.charAt(0) == '#') {
                continue;
            }

            // tokenize line
            final String[] tokens = line.split("\\s+");

            // no tokens? must be an empty line. goto next line
            if (tokens.length == 0) {
                continue;
            }

            final String keyword = tokens[0];

            // if vertex
            if ("v".equals(keyword)) {
                float x = Float.valueOf(tokens[1]);
                float y = Float.valueOf(tokens[2]);
                float z = Float.valueOf(tokens[3]);
                Vector3D vertex = new Vector3D(x, y, z);

                if (vertexNumber == 1) {
                    minWidth = maxWidth = x;
                    minHeight = maxHeight = y;
                    minDepth = maxDepth = z;
                } else {
                    if (x < minWidth)
                        minWidth = x;
                    if (x > maxWidth)
                        maxWidth = x;
                    if (y < minHeight)
                        minHeight = y;
                    if (y > maxHeight)
                        maxHeight = y;
                    if (z < minDepth)
                        minDepth = z;
                    if (z > maxDepth)
                        maxDepth = z;
                }
                vertexNumber++;
                tempVerts.add(vertex);
            }

            // if texture coords
            else if ("vt".equals(keyword)) {
                final float v = tokens.length > 2 ? Float.valueOf(tokens[2]) : 0;
                final float w = tokens.length > 3 ? Float.valueOf(tokens[3]) : 0;
                float[] coord = {v, w};
                tempText.add(coord);
            }

            // if normal vector
            else if ("vn".equals(keyword)) {
//                    Log.d("VerticeNormal",line);
                Vector3D normal = new Vector3D(
                        Float.valueOf(tokens[1]),
                        Float.valueOf(tokens[2]),
                        Float.valueOf(tokens[3]));
                tempNormals.add(normal);
//                    Log.d("VerticeNormal",""+tokens[1]+" "+ tokens[2]+" "+tokens[3]);
            }

            // if face f 51/12/51 62/12/62 60/12/60
            if (("f".equals(keyword) || "fo".equals(keyword)) && tokens.length > 1) {
                for (int i = 0; i < tokens.length-1; i++) {
                    if(tokens[i+1].contains("//")) {
                        String[] faceValues = tokens[i + 1].split("//");
                        int index = Integer.parseInt(faceValues[0]);
                        Vector3D v = tempVerts.get(index - 1);
                        vertFloats.add(v.xyz[0]);
                        vertFloats.add(v.xyz[1]);
                        vertFloats.add(v.xyz[2]);

                        index = Integer.parseInt(faceValues[1]);
                        v = tempNormals.get(index - 1);
                        normFloats.add(v.xyz[0]);
                        normFloats.add(v.xyz[1]);
                        normFloats.add(v.xyz[2]);
                    } else {
                        String[] faceValues = tokens[i + 1].split("/");
                        int index = Integer.parseInt(faceValues[0]);
                        Vector3D v = tempVerts.get(index - 1);
                        vertFloats.add(v.xyz[0]);
                        vertFloats.add(v.xyz[1]);
                        vertFloats.add(v.xyz[2]);

                        index = Integer.parseInt(faceValues[1]);
                        float[] vts = tempText.get(index - 1);
                        textureFloats.add(vts[0]);
                        textureFloats.add(vts[1]);

                        index = Integer.parseInt(faceValues[2]);
                        v = tempNormals.get(index - 1);
                        normFloats.add(v.xyz[0]);
                        normFloats.add(v.xyz[1]);
                        normFloats.add(v.xyz[2]);
                    }
                }
            }
        }

        tempNormals.clear();
        tempText.clear();
        tempVerts.clear();

        vertSize = vertFloats.size();
        textureVTSize = textureFloats.size();

        vertBuffer = new BufferFactory(vertFloats);
        textureBuffer = new BufferFactory(textureFloats);
        normalBuffer = new BufferFactory(normFloats);

        width = maxWidth - minWidth;
        height = maxHeight - minHeight;
        depth = maxDepth - minDepth;

        center = new Vector3D((maxWidth - minWidth) / 2, (maxHeight - minHeight) / 2, (maxDepth - minDepth) / 2);
        Log.v(getClass().getSimpleName(),"Loading complete");
    }



    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getDepth() {
        return depth;
    }

}