package geometries;

import java.util.List;
import primitives.Point;
import primitives.Util;
import primitives.Vector;
import primitives.Ray;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Plane class represents a plane in 3D space.
 * A plane is defined either by a point and a normal vector,
 * or by three non-collinear points that define the plane.
 */
public class Plane extends Geometry {

    /**
     * A point on the plane.
     */
    final Point q;

    /**
     * The normal vector perpendicular to the plane.
     */
    final Vector normal;

    @Override
    public Vector getNormal(Point pnt) {
        return normal;
    }

    /**
     * Constructs a plane from three non-collinear points.
     * The normal is computed by the cross product of the two edge vectors.
     *
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     */
    public Plane(Point p1, Point p2, Point p3) {
        this.q = p1;
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        this.normal = v1.crossProduct(v2).normalize();
    }

    /**
     * Constructs a plane from a point and a normal vector.
     * The vector is normalized before being stored.
     *
     * @param q      a point on the plane
     * @param normal the normal vector to the plane
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    @Override
    public String toString() {
        return "Plane{" +
                "point=" + q +
                ", normal=" + normal +
                '}';
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        Point p0 = ray.getHead();
        Vector direction = ray.getDirection();

        double denominator = normal.dotProduct(direction);

        // If the ray is parallel to the plane or starts on the plane
        if (isZero(denominator) || q.equals(p0)) {
            return null;
        }

        double t = normal.dotProduct(q.subtract(p0)) / denominator;

        // No intersection if behind the ray's origin, at the origin, or beyond maxDistance
        if (alignZero(t) <= 0 || alignZero(t - maxDistance) > 0) {
            return null;
        }

        Point intersectionPoint = ray.getPoint(t);
        return List.of(new Intersection(this, intersectionPoint));
    }
}