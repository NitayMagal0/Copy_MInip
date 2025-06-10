
package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;
import java.util.Objects;
import primitives.Material;
import primitives.Vector;
import lighting.LightSource;


/**
 * Abstract class for all geometries that can be intersected by a ray.
 * Supports the NVI pattern for intersection handling and includes
 * support for emission color and material-based effects.
 */
public abstract class Intersectable {

    /**
     * Passive data structure representing a single intersection between a Ray and a Geometry.
     */
    public static class Intersection {
        /**
         * The geometry that was intersected.
         * Can be null if no intersection occurred.
         */
        public final Geometry geometry;
        /**
         * The point of intersection.
         * This is the point where the ray intersects the geometry.
         */
        public final Point point;
        /**
         * The material of the intersected geometry.
         * This is used for shading and lighting calculations.
         */
        public final Material material;

        /**
         * The direction of the incoming ray.
         * This is used for calculating the angle of incidence and reflection.
         */
        public Vector rayDir;                // Direction of the incoming ray

        /**
         * Normal vector at the intersection point.
         * This is used for lighting calculations and shading.
         */
        public Vector normal;                // Normal at the intersection point

        /**
         * Dot product between the ray direction and the normal at the intersection point.
         * This is used to determine how much light is reflected or absorbed.
         */
        public double rayDotNormal;          // Dot product between ray direction and normal

        /**
         *  The light source that caused this intersection.
         * This is used for lighting calculations.
         */
        public LightSource lightSource;      // The light source being evaluated

        /**
         *  Direction from the light source to the intersection point.
         * This is used for lighting calculations.
         */
        public Vector lightDir;              // Direction from light to point

        /**
         * Dot product between the light direction and the normal at the intersection point.
         * This is used for lighting calculations to determine how much light reaches the surface.
         */
        public double lightDotNormal;        // Dot product between light direction and normal


        /**
         * Constructs an intersection record.
         * @param geometry the intersected geometry
         * @param point the point of intersection
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = geometry != null ? geometry.getMaterial() : null;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Intersection other = (Intersection) obj;
            return geometry == other.geometry && point.equals(other.point);
        }

        @Override
        public String toString() {
            return "Intersection{geometry=" + geometry + ", point=" + point + "}";
        }
    }


    /**
     * Finds all intersection points between the given ray and the geometry.
     * @param ray the ray for which to find intersections
     * @return list of intersection points or null if none
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(i -> i.point).toList();
    }

    /**
     * Default method using infinite distance.
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * Calculates intersections within a maximum distance from ray origin.
     */
    public final List<Intersection> calculateIntersections(Ray ray, double maxDistance) {
        return calculateIntersectionsHelper(ray, maxDistance);
    }

    /**
     * Abstract method that subclasses must implement, to return intersections within a max distance.
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance);
}
