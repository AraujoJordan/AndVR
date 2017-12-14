package araujo.jordan.andvr.engine.math;

import java.util.Arrays;

/**
 * Created by arauj on 24/02/2017.
 */

public class Vector3D {
    public float[] xyz = new float[3];

    public Vector3D(float x, float y, float z) {
        xyz[0] = x;
        xyz[1] = y;
        xyz[2] = z;
    }

    public Vector3D() {
        xyz[0] = 0;
        xyz[1] = 0;
        xyz[2] = 0;
    } //init xyz with 0 value

    /**
     * Clone the vector
     *
     * @param vector3D the vector to be clone
     */
    public Vector3D(Vector3D vector3D) {
        System.arraycopy(vector3D.xyz, 0, xyz, 0, vector3D.xyz.length);
    }

    public static double distance(Vector3D vector3D, Vector3D vector3D2) {
        return Math.sqrt(Math.pow(vector3D.xyz[0] - vector3D2.xyz[0], 2) +
                Math.pow(vector3D.xyz[1] - vector3D2.xyz[1], 2) +
                Math.pow(vector3D.xyz[2] - vector3D2.xyz[2], 2));
    }

    /**
     * Get the size of the vector (for colision or other calculations)
     *
     * @return the float length of the 3d vector
     */
    public float length() {
        return (float) Math.sqrt(xyz[0] * xyz[0] + xyz[1] * xyz[1] + xyz[2] * xyz[2]);
    }

    /**
     * Normalize the 3d vector in place
     */
    public Vector3D normalize() {
        float lenthNorm = length();
        lenthNorm = lenthNorm < 0 ? -lenthNorm : lenthNorm; //norm

        xyz[0] = xyz[0] / lenthNorm;
        xyz[1] = xyz[1] / lenthNorm;
        xyz[2] = xyz[2] / lenthNorm;

        return this;
    }

    /**
     * Cross one vector to this vector (it can be a subtraction if is negative)
     *
     * @param v the vector to be add (or subtract)
     */
    public Vector3D cross(Vector3D v) {
        Vector3D u = new Vector3D(this);
        Vector3D uv = new Vector3D();
        uv.xyz[0] = u.xyz[1] * v.xyz[2] - v.xyz[1] * u.xyz[2];
        uv.xyz[1] = v.xyz[0] * u.xyz[2] - u.xyz[0] * v.xyz[2];
        uv.xyz[2] = u.xyz[0] * v.xyz[1] - v.xyz[0] * u.xyz[1];
        return uv;
    }


    /**
     * Add one vector to this vector (it can be a subtraction if is negative)
     *
     * @param vectorToAdd the vector to be add (or subtract)
     */
    public Vector3D add(Vector3D vectorToAdd) {
        Vector3D newVector = new Vector3D(this);
        newVector.xyz[0] += vectorToAdd.xyz[0];
        newVector.xyz[1] += vectorToAdd.xyz[1];
        newVector.xyz[2] += vectorToAdd.xyz[2];
        return newVector;
    }

    /**
     * Sub one vector to this vector (it can be a subtraction if is negative)
     *
     * @param vectorToSub the vector to be add (or subtract)
     */
    public Vector3D sub(Vector3D vectorToSub) {
        Vector3D newVector = new Vector3D(this);
        newVector.xyz[0] -= vectorToSub.xyz[0];
        newVector.xyz[1] -= vectorToSub.xyz[1];
        newVector.xyz[2] -= vectorToSub.xyz[2];
        return newVector;
    }

    /**
     * Multiply vector to the scalar
     *
     * @param scalar the scalar to multiply the vector
     */
    public Vector3D scalarMultiply(float scalar) {
        Vector3D newVector = new Vector3D(this);
        newVector.xyz[0] *= scalar;
        newVector.xyz[1] *= scalar;
        newVector.xyz[2] *= scalar;
        return newVector;
    }

    public float getX() {
        return xyz[0];
    }

    public void setX(float x) {
        xyz[0] = x;
    }

    public float getY() {
        return xyz[1];
    }

    public void setY(float y) {
        xyz[1] = y;
    }

    public float getZ() {
        return xyz[2];
    }

    public void setZ(float z) {
        xyz[2] = z;
    }

    @Override
    public String toString() {
        return Arrays.toString(xyz);
    }
}
