package geometries;

import primitives.Ray;
import primitives.Point;

import java.util.LinkedList;
import java.util.List;

/**
 * Composite class that aggregates multiple geometric objects.
 */
public class Geometries extends Intersectable {

    /** Collection of geometric shapes */
    private final List<Intersectable> geometries = new LinkedList<>();

    /** Default constructor - initializes empty collection */
    public Geometries() {}

    /**
     * Constructor to initialize with given geometries.
     * @param geometries one or more shapes to add
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds given geometries to the collection.
     * @param geometries one or more shapes
     */
    public void add(Intersectable... geometries) {
        for (Intersectable geo : geometries) {
            this.geometries.add(geo);
        }
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersection> intersections = null;

        for (Intersectable geometry : geometries) {
            List<Intersection> geometryIntersections = geometry.calculateIntersections(ray, maxDistance);
            if (geometryIntersections != null) {
                if (intersections == null)
                    intersections = new LinkedList<>();
                intersections.addAll(geometryIntersections);
            }
        }

        return intersections;
    }
}