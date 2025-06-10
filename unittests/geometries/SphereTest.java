package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Sphere} class.
 * Includes tests for both getNormal() and findIntersections().
 * Organized into Equivalence Partitions and Boundary Value Analyses.
 *
 * Author: Nitay Magal
 */
class SphereTest {
//
    /**
     * Unit tests for the Sphere class.
     */
    @Test
    void testSphere() {
        Sphere sphere = new Sphere(new Point(0, 0, 0),1 );
        Vector dir = new Vector(1, 0, 0);

        // ============ Test getNormal ==============
        Point p = new Point(0, 0, 1);
        Vector normal = sphere.getNormal(p);
        assertEquals(1, normal.length(), 1e-10, "Normal is not a unit vector");
        assertEquals(new Vector(0, 0, 1), normal, "Normal direction is incorrect")
        ;

        // ============ Equivalence Partition Tests ==============

        // EP1 TC01: Ray outside sphere (0 intersections)
        Ray ray1 = new Ray(new Point(2, 0, 0), new Vector(1, 1, 0));
        assertNull(sphere.findIntersections(ray1), "EP1: Ray outside sphere should return null");

        // EP2 TC02: Ray intersects sphere in 2 points
        Ray ray2 = new Ray(new Point(-2, 0, 0), dir);
        List<Point> result2 = sphere.findIntersections(ray2);
        assertNotNull(result2);
        assertEquals(2, result2.size());

        // EP3 TC03: Ray starts inside sphere (1 intersection)
        Ray ray3 = new Ray(new Point(0.5, 0, 0), dir);
        List<Point> result3 = sphere.findIntersections(ray3);
        assertNotNull(result3);
        assertEquals(1, result3.size());

        // EP4 TC04: Ray starts after sphere (no intersection)
        Ray ray4 = new Ray(new Point(2, 0, 0), dir);
        assertNull(sphere.findIntersections(ray4), "EP4: Ray after sphere should return null");

        // ============ Boundary Value Tests ==============

        // Group 1: Ray grazes the sphere without going through the center
        // TC05: Ray grazes the sphere — 1 point
        Ray ray5 = new Ray(new Point(-0.8, 0.6, 0), new Vector(1, 0.1, 0));
        List<Point> result5 = sphere.findIntersections(ray5);
        assertNotNull(result5);
        assertEquals(1, result5.size());

        // TC06: Ray directed away — no intersection
        Ray ray6 = new Ray(new Point(-0.8, 0.6, 0), new Vector(-1, 0.1, 0));
        assertNull(sphere.findIntersections(ray6));

        // Group 2: Ray goes through the center
        // TC07: From outside through center — 2 points
        Ray ray7 = new Ray(new Point(-2, 0, 0), dir);
        assertEquals(2, sphere.findIntersections(ray7).size());

        // TC08: From surface through center — 1 point
        Ray ray8 = new Ray(new Point(-1, 0, 0), dir);
        assertEquals(1, sphere.findIntersections(ray8).size());

        // TC09: From center outward — 1 point
        Ray ray9 = new Ray(new Point(0, 0, 0), dir);
        assertEquals(1, sphere.findIntersections(ray9).size());

        // TC10: From surface outward — 0 points
        Ray ray10 = new Ray(new Point(1, 0, 0), dir);
        assertNull(sphere.findIntersections(ray10));

        // TC11: Beyond sphere — 0 points
        Ray ray11 = new Ray(new Point(2, 0, 0), dir);
        assertNull(sphere.findIntersections(ray11));

        // TC12: Reverse direction through center — 2 points
        Ray ray12 = new Ray(new Point(2, 0, 0), new Vector(-1, 0, 0));
        assertEquals(2, sphere.findIntersections(ray12).size());

        // Group 3: Tangent cases
        // TC13: Before tangent — no intersection
        Ray ray13 = new Ray(new Point(-2, 1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray13));

        // TC14: At tangent point — no intersection
        Ray ray14 = new Ray(new Point(0, 1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray14));

        // TC15: After tangent — no intersection
        Ray ray15 = new Ray(new Point(2, 1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray15));

        // Group 4: Orthogonal cases
        // TC16: Inside, orthogonal to radius — 1 point
        Ray ray16 = new Ray(new Point(0.5, 0, 0), new Vector(0, 1, 0));
        List<Point> result16 = sphere.findIntersections(ray16);
        assertNotNull(result16);
        assertEquals(1, result16.size());

        // TC17: Outside, orthogonal — 0 points
        Ray ray17 = new Ray(new Point(2, 0, 0), new Vector(0, -1, 0));
        assertNull(sphere.findIntersections(ray17));
    }
}
