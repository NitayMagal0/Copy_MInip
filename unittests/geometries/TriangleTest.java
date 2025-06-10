/**
 * Unit tests for Triangle class findIntersections method.
 * TDD Stage - Includes Equivalence Partition and Boundary Value Analysis.
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
 * Unit tests for the Triangle class.
 */
class TriangleTest {

    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}.
     * This test checks the findIntersections method of the Triangle class
     */
    @Test
    void testFindIntersections() {
        // Arrange
        Triangle triangle = new Triangle(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(0, 1, 0)
        );

        // ============ Equivalence Partition Tests =============

        // TC01: Inside the triangle
        Ray ray1 = new Ray(new Point(0.25, 0.25, 1), new Vector(0, 0, -1));
        List<Point> result1 = triangle.findIntersections(ray1);
        assertNotNull(result1, "TC01: Expected intersection inside the triangle (EP)");
        assertEquals(1, result1.size(), "TC01: Expected one intersection (EP)");
        assertEquals(new Point(0.25, 0.25, 0), result1.get(0), "TC01: Wrong intersection point (EP)");

        // TC02: Outside against edge
        Ray ray2 = new Ray(new Point(-0.5, 0.5, 1), new Vector(0, 0, -1));
        assertNull(triangle.findIntersections(ray2), "TC02: Expected no intersection (outside edge) (EP)");

        // TC03: Outside against vertex
        Ray ray3 = new Ray(new Point(1, 1, 1), new Vector(0, 0, -1));
        assertNull(triangle.findIntersections(ray3), "TC03: Expected no intersection (outside vertex) (EP)");

        // =============== Boundary Value Tests ==================

        // TC04: On edge
        Ray ray4 = new Ray(new Point(0.5, 0.5, 1), new Vector(0, 0, -1));
        assertNull(triangle.findIntersections(ray4), "TC04: Expected no intersection (on edge) (BVA)");

        // TC05: On vertex
        Ray ray5 = new Ray(new Point(0, 0, 1), new Vector(0, 0, -1));
        assertNull(triangle.findIntersections(ray5), "TC05: Expected no intersection (on vertex) (BVA)");

        // TC06: On edge's continuation
        Ray ray6 = new Ray(new Point(-0.5, -0.5, 1), new Vector(0, 0, -1));
        assertNull(triangle.findIntersections(ray6), "TC06: Expected no intersection (on edge extension) (BVA)");
    }
}
