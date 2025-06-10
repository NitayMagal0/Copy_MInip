/**
 * Unit tests for {@link geometries.Plane} class.
 * Covers both getNormal() and findIntersections() methods.
 * TDD Stage: Includes Equivalence Partition and Boundary Value Analysis.
 *
 * Author: Nitay Magal
 */
package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Plane class.
 * This class contains test cases for the getNormal and findIntersections methods.
 */
class PlaneTest {

    /**
     * Test method for {@link geometries.Plane#getNormal(Point)}.
     * This test checks the getNormal method of the Plane class
     */
    @Test
    void testGetNormal() {
        // Arrange
        Plane plane = new Plane(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(0, 1, 0)
        );

        // Act
        Vector normal = plane.getNormal(new Point(0.5, 0.5, 0));

        // Assert
        assertEquals(1, normal.length(), 1e-10, "Normal is not a unit vector");
        Vector expected = new Vector(0, 0, 1);
        assertTrue(normal.equals(expected) || normal.equals(expected.scale(-1)), "Normal direction is incorrect");
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     * This test checks the findIntersections method of the Plane class
     */
    @Test
    void testFindIntersections() {
        Plane plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects the plane
        Ray ray1 = new Ray(new Point(0, 0, 0), new Vector(0, 1, 1));
        List<Point> result1 = plane.findIntersections(ray1);
        assertNotNull(result1, "TC01: Expected intersection (EP)");
        assertEquals(1, result1.size(), "TC01: Expected one intersection point (EP)");

        // TC02: Ray misses the plane
        Ray ray2 = new Ray(new Point(0, 0, 2), new Vector(0, 1, 1));
        assertNull(plane.findIntersections(ray2), "TC02: Expected no intersection (EP)");

        // =============== Boundary Value Tests ==================

        // TC03: Ray parallel to plane, not included
        Ray ray3 = new Ray(new Point(0, 0, 2), new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray3), "TC03: Expected no intersection (BVA)");

        // TC04: Ray parallel and lies in the plane
        Ray ray4 = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray4), "TC04: Expected no intersection (BVA - ray in plane)");

        // TC05: Ray orthogonal and starts before plane
        Ray ray5 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        List<Point> result5 = plane.findIntersections(ray5);
        assertNotNull(result5, "TC05: Expected intersection (BVA)");
        assertEquals(1, result5.size(), "TC05: Expected one intersection point (BVA)");

        // TC06: Ray orthogonal and starts in the plane
        Ray ray6 = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray6), "TC06: Expected no intersection (BVA)");

        // TC07: Ray orthogonal and starts after the plane
        Ray ray7 = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray7), "TC07: Expected no intersection (BVA)");

        // TC08: Ray starts in the plane, not parallel or orthogonal
        Ray ray8 = new Ray(new Point(0, 0, 1), new Vector(1, 1, 1));
        assertNull(plane.findIntersections(ray8), "TC08: Expected no intersection (BVA)");

        // TC09: Ray starts at plane’s reference point Q
        Ray ray9 = new Ray(new Point(0, 0, 1), new Vector(1, 1, 1));
        assertNull(plane.findIntersections(ray9), "TC09: Expected no intersection (BVA - at Q)");
    }
}
