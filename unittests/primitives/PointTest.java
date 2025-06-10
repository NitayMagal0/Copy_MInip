package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Point} class.
 * This class contains test cases for various methods of the Point class,
 * including subtract, add, distanceSquared, distance, equals, and toString.
 */
class PointTest {

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     * Checks the subtraction of two points, and checks if exceptions are thrown
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Subtracting two points
        assertEquals(new Vector(1,1,1), (new Point(2,2,2).subtract(new Point(1,1,1))), "subtract() does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC02: Subtracting a point from itself, should throw an exception (from constructor)
        assertThrows(IllegalArgumentException.class, () -> new Point(1,1,1).subtract(new Point(1,1,1)), "subtract() does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     * Checks the addition of a vector to a point
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Adding a point to a point
        assertEquals(new Point(3,3,3), (new Point(2,2,2).add(new Vector(1,1,1))), "add() does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC02: adding a point to itself to check if it throws an exception
        assertEquals(Point.ZERO, new Point(1,1,1).add(new Vector(-1,-1,-1)), "add() does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     * Checks the squared distance between two points
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Squared distance between two points
        assertEquals(3, (new Point(1,1,1).distanceSquared(new Point(2,2,2))), "distanceSquared() does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC02: Squared distance between a point and itself
        assertEquals(0, (new Point(1,1,1).distanceSquared(new Point(1,1,1))), "distanceSquared() does not work correctly");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     * Checks the distance between two points
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Distance between two points
        assertEquals(Math.sqrt(3), (new Point(1,1,1).distance(new Point(2,2,2))), "distance() does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC02: Distance between a point and itself
        assertEquals(0, (new Point(1,1,1).distance(new Point(1,1,1))), "distance() does not work correctly");
    }
}