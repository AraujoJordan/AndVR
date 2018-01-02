package araujo.jordan.andvr.engine.resources.object3D;

import android.util.Log;

import com.mokiat.data.front.parser.IOBJParser;
import com.mokiat.data.front.parser.OBJDataReference;
import com.mokiat.data.front.parser.OBJFace;
import com.mokiat.data.front.parser.OBJMesh;
import com.mokiat.data.front.parser.OBJModel;
import com.mokiat.data.front.parser.OBJNormal;
import com.mokiat.data.front.parser.OBJObject;
import com.mokiat.data.front.parser.OBJParser;
import com.mokiat.data.front.parser.OBJVertex;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import araujo.jordan.andvr.engine.utils.BufferFactory;

/**
 * Created by jordan on 02/01/18.
 */

public class MokiatWavefront extends GenericObject3D {


    public MokiatWavefront(String id, InputStream inputStream) throws IOException {
        super(id, inputStream);

        // Open a stream to your OBJ resource
        // Create an OBJParser and parse the resource
        final IOBJParser parser = new OBJParser();
        final OBJModel model = parser.parse(inputStream);

        ArrayList<Float> vertexs = new ArrayList<>();
        ArrayList<Float> normals = new ArrayList<>();
        ArrayList<Float> txtcoords = new ArrayList<>();

        for (OBJObject object : model.getObjects()) {
            for (OBJMesh mesh : object.getMeshes()) {
                for (OBJFace face : mesh.getFaces()) {
                    for (OBJDataReference reference : face.getReferences()) {
                        if (reference.hasVertexIndex()) {
                            final OBJVertex vertex = model.getVertex(reference);
                            vertexs.add(vertex.x);
                            vertexs.add(vertex.y);
                            vertexs.add(vertex.z);

                            if (reference.hasNormalIndex()) {
                                final OBJNormal normal = model.getNormal(reference);
                                normals.add(normal.x);
                                normals.add(normal.y);
                                normals.add(normal.z);
                            }
                        }
                    }
                }
            }
        }

        vertBuffer = new BufferFactory(vertexs);
        normalBuffer = new BufferFactory(normals);

        vertSize = vertexs.size();
    }

}

