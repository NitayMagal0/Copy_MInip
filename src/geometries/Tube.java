package geometries;

import primitives.Ray;
import primitives.Point;
import primitives.Vector;
import primitives.Util;

import java.util.List;

/**
 * Tube class represents an infinite cylinder (tube) in 3D space.
 * A tube is defined by a central axis (a {@link Ray}) and a radius.
 * This class does not define a finite length — it models a tube extending infinitely in both directions along the axis.
 */
public class Tube extends RadialGeometry {

    /**
     * The central axis of the tube.
     */
    protected final Ray axis;

    /**
     * Constructs a tube with a given radius and axis.
     *
     * @param radius the radius of the tube (must be positive)
     * @param axis   the central axis of the tube represented by a ray
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        // Calculate the projection of the point on the axis
        double t = Util.alignZero(this.axis.getDirection().dotProduct(point.subtract(this.axis.getPoint(0d))));

        // Find center of the tube
        // Return the normalized vector from the center of the tube to the point
        return point.subtract(this.axis.getPoint(t)).normalize();
    }

    @Override
    public String toString() {
        return "Tube{" +
                "axis=" + axis +
                ", radius=" + radius +
                '}';
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        // Placeholder implementation for tube intersection logic
        return null; // Actual intersection logic to be implemented
    }
}