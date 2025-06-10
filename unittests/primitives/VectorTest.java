package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Vector} class
 * This class contains test cases for various methods of the Vector class
 */
class VectorTest {
    /**
     * Test method for {@link primitives.Vector#Vector(double, double, double)}
     * Test method for {@link primitives.Vector#Vector(Double3)}.
     * test checks the constructor of the Vector class
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertDoesNotThrow(() -> new Vector(1,1,1), "Failed constructing a vector with 3 coordinates");

        // TC02: current vector with the other ctor
        assertDoesNotThrow(() -> new Vector(new Double3(1, 2, 3)), "Failed constructing a vector with Double3 param");

        // =============== Boundary Values Tests ==================
        // TC03: Zero vector
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0), "Constructed a zero vector");

        // TC04: Zero vector with the other ctor
        assertThrows(IllegalArgumentException.class, () -> new Vector(new Double3(0, 0, 0)), "Constructed a zero vector");
    }

    /**
     * Test method for {@link primitives.Vector subtract(primitives.Vector)}.
     * This test checks the subtraction of two vectors
     */
    @Test
    void testSubtract() {
        Vector v1 = new Vector(1,1,1);
        Vector v2 = new Vector(-1,-1,-1);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(new Vector(2,2,2), v1.subtract(v2), "Wrong result of subtracting two vectors");

        // =============== Boundary Values Tests ==================
        // TC02: result is zero vector
        assertThrows(IllegalArgumentException.class, () -> v1.subtract(v1), "Constructed a zero vector");
    }

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     * This test checks the addition of two vectors
     */
    @Test
    void testAdd() {
        Vector v1 = new Vector(1,1,1);
        Vector v2 = new Vector(-1,-1,-1);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(new Vector(2,2,2), v1.add(v1), "Wrong result of adding two vectors");

        // =============== Boundary Values Tests ==================
        // TC02: result is zero vector
        assertThrows(IllegalArgumentException.class, () -> v1.add(v2), "Constructed a zero vector");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     * This test checks the scaling of a vector
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector scaling by positive scalar
        assertEquals(new Vector(2,2,2), new Vector(1,1,1).scale(2), "Wrong result of scaling a vector");
        // TC02: Correct vector scaling by negative scalar
        assertEquals(new Vector(-2,-2,-2), new Vector(1,1,1).scale(-2), "Wrong result of scaling a vector");

        // =============== Boundary Values Tests ==================
        // TC03: scale by zero
        assertThrows(IllegalArgumentException.class, () -> new Vector(1,1,1).scale(0), "Constructed a zero vector");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     * This test checks the dot product of two vectors
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(3, new Vector(1,1,1).dotProduct(new Vector(1,1,1)), "Wrong result of dot product");
        // TC02: Correct vector with negative result
        assertEquals(-3, new Vector(1,1,1).dotProduct(new Vector(-5,1,1)), "Wrong result of dot product");

        // =============== Boundary Values Tests ==================
        // TC03: dot product resaults zero
        assertEquals(0, new Vector(0,1,1).dotProduct(new Vector(1,0,0)), "Constructed a zero vector");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     * This test checks the cross product of two vectors
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: two vectors with an obtuse angle
        assertEquals(new Vector(-5, -5, 5), new Vector(1, 2, 3).crossProduct(new Vector(-2, 1, -1)));

        // TC02: two vectors with an acute angle
        assertEquals(new Vector(3, -6, 3), new Vector(2, 1, 0).crossProduct(new Vector(1, 2, 3)));

        // =============== Boundary Values Tests ==================
        // TC03: cross product results in zero because vectors are parallel
        assertThrows(IllegalArgumentException.class, () -> new Vector(2, 2,2).crossProduct(new Vector(4,4,4)), "Constructed a zero vector");

        // TC04: the vectors are equal
        assertThrows(IllegalArgumentException.class, () -> new Vector(1, 2, 3).crossProduct(new Vector(1, 2, 3)),"Constructed a zero vector");

        // TC05: the vectors are the same length but not parallel
        assertEquals(new Vector(1, 7, -5), new Vector(1, 2, 3).crossProduct(new Vector(3, 1, 2)),"Wrong result of cross product");

        // TC06: two vectors in opposite directions
        assertThrows(IllegalArgumentException.class, () -> new Vector(1, 2, 3).crossProduct(new Vector(-2, -4, -6)), "crossProduct() with opposite direction does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     * This test checks the length squared of a vector
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(9, new Vector(2,2,-1).lengthSquared(), "Wrong result of length squared");
    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     * This test checks the length of a vector
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(3, new Vector(2,2,-1).length(), "Wrong result of length");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     * This test checks the normalization of a vector
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(1, new Vector(1,2,3).normalize().length(), "Wrong result of normalization");

        // =============== Boundary Values Tests ==================
        // TC02: normalize a zero vector
        assertThrows(IllegalArgumentException.class, () -> new Vector(1,2,3).normalize().crossProduct(new Vector(1,2,3)), "Normalized a zero vector");
    }
}