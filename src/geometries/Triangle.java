package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Triangle class represents a triangle in 3D space.
 * It is a specific case of {@link Polygon} with exactly 3 vertices.
 */
public class Triangle extends Polygon {

    /**
     * Constructs a triangle from 3 points.
     *
     * @param p1 First vertex
     * @param p2 Second vertex
     * @param p3 Third vertex
     * @throws IllegalArgumentException if the points do not form a valid triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
        // Optional: you could assert this in case Polygon logic changes in the future
        if (super.vertices.size() != 3) {
            throw new IllegalArgumentException("A triangle must have exactly 3 vertices");
        }
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "vertices=" + vertices +
                '}';
    }

    /**
     * Calculates the intersection points between the triangle and a given ray,
     * considering a maximum distance from the ray's origin.
     *
     * @param ray the ray to test for intersection
     * @param maxDistance the maximum allowed distance from the ray's origin
     * @return a list containing the intersection, or null if there is no valid intersection
     */
    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        // Step 1: intersect with the triangle's plane
        List<Point> planeIntersections = plane.findIntersections(ray);
        if (planeIntersections == null) {
            return null;
        }

        Point p = planeIntersections.get(0); // the only intersection point
        Point p0 = ray.getHead();

        // Check if intersection is within maxDistance
        if (Util.alignZero(p.distance(p0) - maxDistance) > 0) {
            return null;
        }

        // Vectors from ray head to each vertex
        Vector v1 = vertices.get(0).subtract(p0);
        Vector v2 = vertices.get(1).subtract(p0);
        Vector v3 = vertices.get(2).subtract(p0);

        // Normals to triangle's pyramid faces
        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        // Dot products with ray direction
        double dot1 = ray.getDirection().dotProduct(n1);
        double dot2 = ray.getDirection().dotProduct(n2);
        double dot3 = ray.getDirection().dotProduct(n3);

        // Reject if point lies on triangle edge
        if (isZero(dot1) || isZero(dot2) || isZero(dot3)) {
            return null;
        }

        // Accept only if all signs match
        boolean allPositive = dot1 > 0 && dot2 > 0 && dot3 > 0;
        boolean allNegative = dot1 < 0 && dot2 < 0 && dot3 < 0;

        return (allPositive || allNegative)
                ? List.of(new Intersection(this, p))
                : null;
    }

}