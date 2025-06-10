package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util;

import java.util.List;

/**
 * Represents a 3D cylinder that extends the {@link Tube} class.
 * A cylinder is defined by a radial geometry (radius), a central axis (ray),
 * and a length (height)
 */
public class Cylinder extends Tube {
    /**
     * The height of the cylinder, which is the length along the central axis.
     */
    final double height;

    /**
     * Constructs a 3D cylinder, defined by a radial geometry (radius), a central axis (ray),
     * and a height (length along of central axis).
     *
     * @param radius the radius of the cylinder
     * @param axis the central axis of the cylinder, represented as a {@link Ray}
     * @param height the height of the cylinder along the axis
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        Point p0 = axis.getPoint(0d);
        Vector direction = this.axis.getDirection();

        //  If p0 is the head of the axis
        if (point.equals(p0))
            return direction.scale(-1);

        // If p1 is the end of the axis
        if (point.equals(axis.getPoint(height)))
            return direction;

        // If the point is on the top or bottom surface of the cylinder
        if (Util.isZero(p0.subtract(point).dotProduct(direction)))
            return direction.scale(-1d);

        if (Util.isZero(axis.getPoint(height).subtract(point).dotProduct(direction)))
            return direction;

        // Otherwise, call the superclass method
        return super.getNormal(point);
    }

    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null; // TDD placeholder
    }
}