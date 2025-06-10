package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Cylinder class getNormal method.
 * TDD Stage - Three equivalence partitions and two boundary cases.
 * Author: Nitay Magal
 */
class CylinderTest {

    /**
     * Test method for {@link geometries.Cylinder#getNormal(Point)}.
     * This test checks the getNormal method of the Cylinder class
     */
    @Test
    void testGetNormal() {
        // Arrange
        Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Cylinder cylinder = new Cylinder(1, axis, 5);

        // ============ Equivalence Partition Tests =============

        // TC01: Point on side
        Point pSide = new Point(1, 0, 2);
        Vector normalSide = cylinder.getNormal(pSide);
        assertEquals(1, normalSide.length(), 1e-10, "Side normal is not a unit vector");
        assertEquals(new Vector(1, 0, 0), normalSide, "Side normal direction is incorrect");

        // TC02: Point on bottom base
        Point pBottom = new Point(0.5, 0, 0);
        Vector normalBottom = cylinder.getNormal(pBottom);
        assertEquals(1, normalBottom.length(), 1e-10, "Bottom base normal is not a unit vector");
        assertEquals(new Vector(0, 0, -1), normalBottom, "Bottom base normal direction is incorrect");

        // TC03: Point on top base
        Point pTop = new Point(0.5, 0, 5);
        Vector normalTop = cylinder.getNormal(pTop);
        assertEquals(1, normalTop.length(), 1e-10, "Top base normal is not a unit vector");
        assertEquals(new Vector(0, 0, 1), normalTop, "Top base normal direction is incorrect");

        // =============== Boundary Value Tests ==================

        // TC04: Center of bottom base
        Point pBottomCenter = new Point(0, 0, 0);
        Vector normalBottomCenter = cylinder.getNormal(pBottomCenter);
        assertEquals(1, normalBottomCenter.length(), 1e-10, "Bottom center normal is not a unit vector");
        assertEquals(new Vector(0, 0, -1), normalBottomCenter, "Bottom center normal direction is incorrect");

        // TC05: Center of top base
        Point pTopCenter = new Point(0, 0, 5);
        Vector normalTopCenter = cylinder.getNormal(pTopCenter);
        assertEquals(1, normalTopCenter.length(), 1e-10, "Top center normal is not a unit vector");
        assertEquals(new Vector(0, 0, 1), normalTopCenter, "Top center normal direction is incorrect");

        // TC06: Point on the edge of the bottom base ***
        Point pBottomEdge = new Point(1, 0, 0);
        Vector normalBottomEdge = cylinder.getNormal(pBottomEdge);
        assertEquals(1, normalBottomEdge.length(), 1e-10, "Bottom edge normal is not a unit vector");
        assertEquals(new Vector(0, 0, -1), normalBottomEdge, "Bottom edge normal direction is incorrect");

        // TC07: Point on the edge of the top base ***
        Point pTopEdge = new Point(1, 0, 5);
        Vector normalTopEdge = cylinder.getNormal(pTopEdge);
        assertEquals(1, normalTopEdge.length(), 1e-10, "Top edge normal is not a unit vector");
        assertEquals(new Vector(0, 0, 1), normalTopEdge, "Top edge normal direction is incorrect");

        // TC08: Side point exactly at bottom base height ***
        Point pSideBottomEdge = new Point(1, 0, 0);
        Vector normalSideBottomEdge = cylinder.getNormal(pSideBottomEdge);
        assertEquals(1, normalSideBottomEdge.length(), 1e-10, "Side-bottom edge normal is not a unit vector");
        // Depending on your implementation, this could be either a side or a base point.
        // Adjust the expected value accordingly.
        assertEquals(new Vector(0, 0, -1), normalSideBottomEdge, "Side-bottom edge normal direction is incorrect");
    }
}