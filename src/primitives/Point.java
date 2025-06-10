package primitives;

/**
 * Represents a point in 3D space using a {@link Double3} to store its (x, y, z) coordinates.
 * Supports basic operations such as vector subtraction, vector addition, and distance calculations.
 */
public class Point {

    /**
     * 3D coordinates of the point.
     */
    protected final Double3 xyz;

    /**
     * Constant for the origin point (0, 0, 0).
     */
    public static final Point ZERO = new Point(0, 0, 0);

    /**
     * Constructs a point from three coordinate values.
     *
     * @param x X-coordinate
     * @param y Y-coordinate
     * @param z Z-coordinate
     */
    public Point(double x, double y, double z) {
        this.xyz = new Double3(x, y, z);
    }

    /**
     * Constructs a point from a {@link Double3} object.
     *
     * @param xyz A Double3 representing the coordinates
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Subtracts another point from this point, returning a vector from the other point to this point.
     *
     * @param rhs The point to subtract
     * @return A vector from {@code rhs} to this point
     */
    public Vector subtract(Point rhs) {
        return new Vector(this.xyz.subtract(rhs.xyz));
    }

    /**
     * Adds a vector to this point, returning a new point.
     *
     * @param vct The vector to add
     * @return A new point after adding the vector
     */
    public Point add(Vector vct) {
        return new Point(xyz.add(vct.xyz));
    }

    /**
     * Calculates the squared distance between this point and another.
     *
     * @param rhs The other point
     * @return The squared distance between the two points
     */
    public double distanceSquared(Point rhs) {
        double x = xyz.d1() - rhs.xyz.d1();
        double y = xyz.d2() - rhs.xyz.d2();
        double z = xyz.d3() - rhs.xyz.d3();

        return (x * x + y * y + z * z);
    }

    /**
     * Calculates the Euclidean distance between this point and another.
     *
     * @param rhs The other point
     * @return The distance between the two points
     */
    public double distance(Point rhs) {
        return Math.sqrt(distanceSquared(rhs));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && this.xyz.equals(other.xyz);
    }

    /**
     * Gets the X coordinate of the point.
     * @return The X coordinate
     */
    public double getX() { return xyz.d1(); }
    /**
     * Gets the Y coordinate of the point.
     * @return The Y coordinate
     */
    public double getY() { return xyz.d2(); }
    /**
     * Gets the Z coordinate of the point.
     * @return The Z coordinate
     */
    public double getZ() { return xyz.d3(); }

    @Override
    public String toString() {
        return "Point{" +
                "xyz=" + xyz +
                '}';
    }

}
