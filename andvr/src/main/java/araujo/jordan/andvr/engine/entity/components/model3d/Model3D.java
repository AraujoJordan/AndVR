package araujo.jordan.andvr.engine.entity.components.model3d;


import android.util.Log;

import araujo.jordan.andvr.engine.VREngine;
import araujo.jordan.andvr.engine.draw.Color;
import araujo.jordan.andvr.engine.entity.components.Component;
import araujo.jordan.andvr.engine.entity.components.Transformation;
import araujo.jordan.andvr.engine.math.Vector3D;
import araujo.jordan.andvr.engine.resources.object3D.GenericObject3D;

/**
 * Created by arauj on 05/03/2017.
 */

public class Model3D extends Component {

    private final float width, height, depth;
    private final Vector3D centerOfModel;
    private final GenericObject3D obj3D;
    public String resourceLabel;
    private ModelDrawVR shape;
    private VREngine engine;

    public Model3D(String resourceLabel, VREngine engine) {
        this.resourceLabel = resourceLabel;

        obj3D = engine.resouces.get3DModel(resourceLabel);

        this.width = obj3D.width;
        this.height = obj3D.height;
        this.depth = obj3D.depth;
        this.centerOfModel = obj3D.center;
        this.engine = engine;
    }

    public Model3D(String resourceLabel, VREngine engine, Color color) {

        this.resourceLabel = resourceLabel;

        obj3D = engine.resouces.get3DModel(resourceLabel);

        if (obj3D == null) {
            Log.e(getClass().getSimpleName(), "Object not found for given label");
            throw new ExceptionInInitializerError("Object not found for given label");
        }

        this.width = obj3D.width;
        this.height = obj3D.height;
        this.depth = obj3D.depth;
        this.centerOfModel = obj3D.center;
        this.engine = engine;
//        this.color = color;
    }

    public Model3D(String resourceLabel, String textureLabel, VREngine engine) {

        this.resourceLabel = resourceLabel;

        obj3D = engine.resouces.get3DModel(resourceLabel);

        this.width = obj3D.width;
        this.height = obj3D.height;
        this.depth = obj3D.depth;
        this.centerOfModel = obj3D.center;
        this.engine = engine;
    }

    @Override
    public void run(VREngine engine) {
        if (shape == null) {
            initTriangles();
        }

        //RUN WITH TRANSFORMATION
        Transformation transformation;
        try {
            transformation = parentEntity.getTransformation();
            shape.draw();
            return;
        } catch (NullPointerException noTrans) {
            Log.e("NullPointerException", noTrans.getMessage());
        }

        //RUN WITHOUT TRANSFORMATION
        shape.draw();
    }


    /**
     * Can't init the model draw in the model3d constructor,
     * otherwise the OpenGL will doesn't see more than 1 triangle.
     */
    public void initTriangles() {
        if (shape != null)
            return;

        shape = new ModelDrawVR(resourceLabel, obj3D, engine, parentEntity);
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

    public Vector3D getCenterOfModel() {
        return centerOfModel;
    }
}
