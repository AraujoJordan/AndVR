package araujo.jordan.andvr.engine.entity;

import araujo.jordan.andvr.engine.math.Vector3D;

/**
 * Created by jordan on 09/01/18.
 */

public class Light extends Entity {

    public Vector3D position;
//    public float power;

    public Light(Vector3D position) {
        this.position = position;
//        this.power = power;
    }

}
