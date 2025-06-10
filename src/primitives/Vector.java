package primitives;

/**
 * Represents a 3D vector in space.
 * Inherits from {@link Point} since a vector can be treated as a point from the origin.
 * Provides operations like addition, scaling, dot product, cross product, normalization, and length calculations.
 */
public class Vector extends Point {

    /**
     * Constructs a vector from three coordinate values.
     *
     * @param x X-coordinate
     * @param y Y-coordinate
     * @param z Z-coordinate
     * @throws IllegalArgumentException if the resulting vector is a zero vector
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (this.xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Zero vector is not allowed");
        }
    }

    /**
     * Constant for the unit vector along the X-axis.
     */
    public static final Vector AXIS_X = new Vector(1, 0, 0);
    /**
     * Constant for the unit vector along the Y-axis.
     */
    public static final Vector AXIS_Y = new Vector(0, 1, 0);
    /**
     * Constant for the unit vector along the Z-axis.
     */
    public static final Vector AXIS_Z = new Vector(0, 0, 1);

    /**
     * Constructs a vector from a {@link Double3} object.
     *
     * @param xyz coordinate triple
     * @throws IllegalArgumentException if the resulting vector is a zero vector
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (this.xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Zero vector is not allowed");
        }
    }

    /**
     * Adds this vector to another vector.
     *
     * @param other the vector to add
     * @return a new vector representing the sum
     */
    public Vector add(Vector other) {
        return new Vector(this.xyz.add(other.xyz));
    }

    /**
     * Scales this vector by a scalar.
     *
     * @param scalar the scalar to multiply by
     * @return a new scaled vector
     */
    public Vector scale(double scalar) {
        return new Vector(this.xyz.scale(scalar));
    }

    /**
     * Calculates the dot product of this vector and another.
     *
     * @param other the other vector
     * @return the scalar dot product result
     */
    public double dotProduct(Vector other) {
        return this.xyz.d1() * other.xyz.d1()
                + this.xyz.d2() * other.xyz.d2()
                + this.xyz.d3() * other.xyz.d3();
    }

    /**
     * Calculates the cross product of this vector and another.
     *
     * @param other the other vector
     * @return a new vector that is perpendicular to both
     */
    public Vector crossProduct(Vector other) {
        return new Vector(
                this.xyz.d2() * other.xyz.d3() - this.xyz.d3() * other.xyz.d2(),
                this.xyz.d3() * other.xyz.d1() - this.xyz.d1() * other.xyz.d3(),
                this.xyz.d1() * other.xyz.d2() - this.xyz.d2() * other.xyz.d1());
    }

    /**
     * Calculates the squared length (magnitude) of the vector.
     *
     * @return the squared length
     */
    public double lengthSquared() {
        return dotProduct(this);
    }

    /**
     * Calculates the length (magnitude) of the vector.
     *
     * @return the vector length
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Normalizes the vector to a unit vector in the same direction.
     *
     * @return a new normalized vector
     */
    public Vector normalize() {
        return scale(1 / length());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other && super.equals(other));
    }

    @Override
    public String toString() {
        return ("->" + super.toString());
    }
}
