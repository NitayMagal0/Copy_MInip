package geometries;

import java.util.LinkedList;
import java.util.List;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

import static primitives.Util.isZero;
import static primitives.Util.alignZero;

/**
 * Sphere class represents a 3D sphere in space.
 * A sphere is defined by a center point and a radius.
 */
public class Sphere extends RadialGeometry {

    /**
     * The center point of the sphere.
     */
    final Point center;

    /**
     * Constructs a sphere with a given center and radius.
     *
     * @param radius the radius of the sphere (must be positive)
     * @param center the center point of the sphere
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Point pnt) {
        return pnt.subtract(center).normalize();
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "center=" + center +
                ", radius=" + radius +
                '}';
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        if (ray.getHead().equals(center)) {
            Point intersection = ray.getPoint(radius);
            if (alignZero(intersection.distance(ray.getHead()) - maxDistance) <= 0) {
                return List.of(new Intersection(this, intersection));
            }
            return null;
        }

        Vector u = center.subtract(ray.getHead());
        double tm = u.dotProduct(ray.getDirection());
        double dSquared = u.lengthSquared() - tm * tm;
        double radiusSquared = radius * radius;

        if (dSquared >= radiusSquared || isZero(dSquared - radiusSquared)) {
            return null;
        }

        double th = Math.sqrt(radiusSquared - dSquared);
        double t1 = tm - th;
        double t2 = tm + th;
        List<Intersection> intersections = new LinkedList<>();

        if (t1 > 0 && !isZero(t1)) {
            Point intersection = ray.getPoint(t1);
            if (alignZero(intersection.distance(ray.getHead()) - maxDistance) <= 0) {
                intersections.add(new Intersection(this, intersection));
            }
        }
        if (t2 > 0 && !isZero(t2)) {
            Point intersection = ray.getPoint(t2);
            if (alignZero(intersection.distance(ray.getHead()) - maxDistance) <= 0) {
                intersections.add(new Intersection(this, intersection));
            }
        }

        return intersections.isEmpty() ? null : intersections;
    }
}