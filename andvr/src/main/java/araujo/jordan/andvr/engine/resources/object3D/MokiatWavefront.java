package araujo.jordan.andvr.engine.resources.object3D;

import android.app.Activity;
import android.util.Log;

import com.mokiat.data.front.parser.IMTLParser;
import com.mokiat.data.front.parser.IOBJParser;
import com.mokiat.data.front.parser.MTLColor;
import com.mokiat.data.front.parser.MTLLibrary;
import com.mokiat.data.front.parser.MTLMaterial;
import com.mokiat.data.front.parser.MTLParser;
import com.mokiat.data.front.parser.OBJDataReference;
import com.mokiat.data.front.parser.OBJFace;
import com.mokiat.data.front.parser.OBJMesh;
import com.mokiat.data.front.parser.OBJModel;
import com.mokiat.data.front.parser.OBJNormal;
import com.mokiat.data.front.parser.OBJObject;
import com.mokiat.data.front.parser.OBJParser;
import com.mokiat.data.front.parser.OBJTexCoord;
import com.mokiat.data.front.parser.OBJVertex;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import araujo.jordan.andvr.engine.utils.BufferFactory;

/**
 * Created by jordan on 02/01/18.
 */

public class MokiatWavefront extends GenericObject3D {


    public MokiatWavefront(Activity act, String id, InputStream inputStream) throws IOException {
        super(id, inputStream);

        final IOBJParser parser = new OBJParser();
        final IMTLParser mtlParser = new MTLParser();

        ArrayList<Float> vertexs = new ArrayList<>();
        ArrayList<Float> normals = new ArrayList<>();
        ArrayList<Float> txtcoords = new ArrayList<>();

        final OBJModel model = parser.parse(inputStream);
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
                            if(reference.hasTexCoordIndex()) {
                                final OBJTexCoord objTexCoord = model.getTexCoord(reference);
                                txtcoords.add(objTexCoord.u);
                                txtcoords.add(1.0f-objTexCoord.v);
                                txtcoords.add(objTexCoord.w);
                            }
                        }
                    }
                }
            }


            for (String libraryReference : model.getMaterialLibraries()) {
                Log.v("libraryReference",libraryReference);
                final InputStream mtlStream = act.getAssets().open(libraryReference);
                final MTLLibrary library = mtlParser.parse(mtlStream);
                for (MTLMaterial material : library.getMaterials()) {
                    final MTLColor diffuseColor = material.getDiffuseColor();


                    final MTLColor ambientColor = material.getAmbientColor();
                    final MTLColor specularColor = material.getSpecularColor();

                }
            }
        }



        vertBuffer = new BufferFactory(vertexs);
        normalBuffer = new BufferFactory(normals);
        uvwBuffer = new BufferFactory(txtcoords);
        vertSize = vertexs.size();
    }

}

