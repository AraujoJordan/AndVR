package araujo.jordan.andvr.engine.resources.object3D;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import araujo.jordan.andvr.engine.math.Vector3D;
import araujo.jordan.andvr.engine.utils.BufferFactory;

/**
 * Created by jordan on 02/01/18.
 */

public abstract class GenericObject3D {

    public String id;

    public Vector3D center;
    public int textureVTSize;

    public BufferFactory vertBuffer, normalBuffer, textureBuffer, uvwBuffer;
    public int vertSize = 0;

    public float width, height, depth;

    public GenericObject3D(String id, InputStream inputStream) throws IOException {
        this.id = id;

//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
//        String line;
//        int vertexNumber = 1;
//
//        float minWidth = 0, maxWidth = 0;
//        float minHeight = 0, maxHeight = 0;
//        float minDepth = 0, maxDepth = 0;
//
//        Log.v(getClass().getSimpleName(), "Reading lines");
//        while ((line = reader.readLine()) != null) {
//            line = line.trim();
//            // handle line continuation marker \
//            while (line.endsWith("\\")) {
//                line = line.substring(0, line.length() - 1);
//                final String s = reader.readLine();
//                if (s != null) {
//                    line += s;
//                    line = line.trim();
//                }
//            }
//
//            // ignore comments. goto next line
//            if (line.length() > 0 && line.charAt(0) == '#') {
//                continue;
//            }
//
//            // tokenize line
//            final String[] tokens = line.split("\\s+");
//
//            // no tokens? must be an empty line. goto next line
//            if (tokens.length == 0) {
//                continue;
//            }
//
//            final String keyword = tokens[0];
//
//            // if vertex
//            if ("v".equals(keyword)) {
//                float x = Float.valueOf(tokens[1]);
//                float y = Float.valueOf(tokens[2]);
//                float z = Float.valueOf(tokens[3]);
//
//                if (vertexNumber == 1) {
//                    minWidth = maxWidth = x;
//                    minHeight = maxHeight = y;
//                    minDepth = maxDepth = z;
//                } else {
//                    if (x < minWidth)
//                        minWidth = x;
//                    if (x > maxWidth)
//                        maxWidth = x;
//                    if (y < minHeight)
//                        minHeight = y;
//                    if (y > maxHeight)
//                        maxHeight = y;
//                    if (z < minDepth)
//                        minDepth = z;
//                    if (z > maxDepth)
//                        maxDepth = z;
//                }
//                vertexNumber++;
//            }
//        }
//
//        width = maxWidth - minWidth;
//        height = maxHeight - minHeight;
//        depth = maxDepth - minDepth;
//
//        Log.v(getClass().getSimpleName(), "width: "+width);
//        Log.v(getClass().getSimpleName(), "height: "+height);
//        Log.v(getClass().getSimpleName(), "depth: "+depth);
//
//
//        center = new Vector3D((maxWidth - minWidth) / 2, (maxHeight - minHeight) / 2, (maxDepth - minDepth) / 2);
//        Log.v(getClass().getSimpleName(), "Center: " + center.xyz[0]+", "+center.xyz[1] + ", " + center.xyz[2]);
////        center= new Vector3D(0,0,0);

        inputStream.reset();

    }
}
