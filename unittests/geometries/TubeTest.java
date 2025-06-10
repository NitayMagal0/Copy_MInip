/**
 * Unit tests for Tube class getNormal method.
 * TDD Stage - One equivalence partition and one boundary case.
 * Author: Nitay Magal
 */
package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Tube} class.
 * Includes tests for the getNormal method.
 */
class TubeTest {

    /**
     * Test method for {@link geometries.Tube#getNormal(Point)}.
     * This test checks the getNormal method of the Tube class.
     */
    @Test
    void testGetNormal() {
        // Arrange
        Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Tube tube = new Tube(1, axis);

        // ============ Equivalence Partition Test =============
        // TC01: General point on side surface
        Point pSide = new Point(1, 0, 5);
        Vector normalSide = tube.getNormal(pSide);
        assertEquals(1, normalSide.length(), 1e-10, "Normal is not a unit vector (EP)");
        assertEquals(new Vector(1, 0, 0), normalSide, "Normal direction is incorrect (EP)");

        // =============== Boundary Value Test ==================
        // TC02: (p-p0) orthogonal to axis direction
        Point pBoundary = new Point(1, 0, 0);
        Vector normalBoundary = tube.getNormal(pBoundary);
        assertEquals(1, normalBoundary.length(), 1e-10, "Normal is not a unit vector (BVA)");
        assertEquals(new Vector(1, 0, 0), normalBoundary, "Normal direction is incorrect (BVA)");
    }

}
