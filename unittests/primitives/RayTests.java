package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Unit tests for {@link Ray#getPoint(double)} method.
 */
class RayTests {

    /**
     * Test method for {@link primitives.Ray#getPoint(double)}.
     * This test checks the getPoint method of the Ray class
     */
    @Test
    void testGetPoint() {
        Ray ray = new Ray(new Point(1, 2, 3), new Vector(1, 0, 0));

        // EP1: Positive t (in direction)
        Point result1 = ray.getPoint(2);
        assertEquals(new Point(3, 2, 3), result1, "EP1: Incorrect point for positive t");

        // BVA: t = 0 (should return head)
        Point result2 = ray.getPoint(0);
        assertEquals(ray.getHead(), result2, "BVA: t = 0 should return head");

        // EP2: Negative t (opposite direction)
        Point result3 = ray.getPoint(-1.5);
        assertEquals(new Point(-0.5, 2, 3), result3, "EP2: Incorrect point for negative t");
    }

    /**
     * Test method for {@link primitives.Ray#findClosestPoint(java.util.List)}.
     */
    @Test
    public void testFindClosestPoint() {
        Point p0 = new Point(0, 0, 0);
        Vector dir = new Vector(1, 0, 0);
        Ray ray = new Ray(p0, dir);

        // ============ Equivalence Partitions Tests ==============
        // TC01: The middle point in the list is closest to the ray's head
        List<Point> points = List.of(
                new Point(3, 0, 0),
                new Point(1, 0, 0),
                new Point(2, 0, 0)
        );
        assertEquals(points.get(1), ray.findClosestPoint(points), "Wrong closest point - should be middle point");

        // =============== Boundary Values Tests ==================
        // TC11: Empty list - should return null
        assertNull(ray.findClosestPoint(null), "Empty list - findClosestPoint should return null");

        // TC12: First point is closest to ray's head
        points = List.of(
                new Point(1, 0, 0),
                new Point(2, 0, 0),
                new Point(3, 0, 0)
        );
        assertEquals(points.get(0), ray.findClosestPoint(points), "Wrong closest point - should be first point");

        // TC13: Last point is closest to ray's head
        points = List.of(
                new Point(3, 0, 0),
                new Point(2, 0, 0),
                new Point(1, 0, 0)
        );
        assertEquals(points.get(2), ray.findClosestPoint(points), "Wrong closest point - should be last point");
    }
}
