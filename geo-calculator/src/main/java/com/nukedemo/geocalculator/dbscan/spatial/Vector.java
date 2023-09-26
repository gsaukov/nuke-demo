/**
 * Implementation of the net.sf.geographiclib.Vector class
 */
package com.nukedemo.geocalculator.dbscan.spatial;

/**
 * Vector in three-dimensional space.
 */
public class Vector {
    /**
     * Component in x dimension.
     */
    public double x = Double.NaN;
    /**
     * Component in y dimension.
     */
    public double y = Double.NaN;
    /**
     * Component in z dimension.
     */
    public double z = Double.NaN;

    /**
     * Vector constructor.
     * <p>
     *
     * @param x Component in x dimension.
     * @param y Component in y dimension.
     * @param z Component in z dimension.
     */
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Addition with a vector.
     * <p>
     *
     * @param other Vector for addition.
     * @return Added {@link Vector} object.
     */
    public Vector add(Vector other) {
        return new Vector(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /**
     * Multiplication with a scalar.
     * <p>
     *
     * @param a Scalar multiplicator.
     * @return Scaled {@link Vector} object.
     */
    public Vector multiply(double a) {
        return new Vector(this.x * a, this.y * a, this.z * a);
    }

    /**
     * Cross product with a vector.
     * <p>
     *
     * @param other Vector for cross product.
     * @return {@link Vector} object as result of cross product.
     */
    public Vector cross(Vector other) {
        return new Vector((y * other.z) - (z * other.y), (z * other.x) - (x * other.z),
                (x * other.y) - (y * other.x));
    }

    /**
     * Dot product with a vector.
     * <p>
     *
     * @param other Vector for dot product.
     * @return Scalar as result of dot product.
     */
    public double dot(Vector other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }
}
