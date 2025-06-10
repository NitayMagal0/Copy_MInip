package renderer;

import static org.junit.jupiter.api.Assertions.*;

import geometries.*;
import primitives.*;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for the Camera class
 * Uses default constructor for test initialization.
 */
public class CameraIntersectionsIntegrationTests {

    /**
     * Number of columns and rows in the view plane.
     * This is used to construct rays through each pixel in the view plane.
     * For 3x3 matrix
     */
    private static final int NX = 3; // Number of columns
    /**
     * For 3x3 matrix
     */
    private static final int NY = 3; // Number of rows

    /**
     * Error message for intersection count mismatch.
     */
    private static final String ERROR_INTERSECTION_COUNT = "ERROR: Wrong number of intersections";

    /**
     * Camera builder for the tests.
     * The camera is positioned at the origin, looking towards the negative Z-axis,
     * with a view plane distance of 1 and a size of 3x3.
     */
    private static final Camera.Builder CAMERA_BUILDER = Camera.getBuilder()
            .setLocation(Point.ZERO)
            .setVpDistance(1)
            .setVpSize(3, 3)
            .setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0));

    /**
     * Helper method that calculates the total number of intersections between camera rays and a geometry
     *
     * @param geometry the geometry to check intersections with
     * @param camera the camera to construct rays from
     * @return total number of intersections
     */
    private int countIntersections(Geometry geometry, Camera camera) {
        int count = 0;
        // Go through all pixels of the view plane
        for (int i = 0; i < NY; i++) {
            for (int j = 0; j < NX; j++) {
                // Construct ray through pixel
                Ray ray = camera.constructRay(NX, NY, j, i);
                // Find intersections
                var intersections = geometry.findIntersections(ray);
                // Count intersections (if any)
                if (intersections != null)
                    count += intersections.size();
            }
        }
        return count;
    }

    /**
     * Tests ray-sphere intersections
     */
    @Test
    void testRaySphereIntersections() {
        Camera camera = CAMERA_BUILDER.build();

        // TC01: Small sphere in front of view plane (2 intersection points)
        Sphere sphere1 = new Sphere(new Point(0, 0, -3),1 );
        assertEquals(2, countIntersections(sphere1, camera), ERROR_INTERSECTION_COUNT);

        // TC02: Large sphere containing all the view plane (9 intersection points)
        Sphere sphere2 = new Sphere( new Point(0, 0, -2.5),2.5);
        assertEquals(18, countIntersections(sphere2,
                        CAMERA_BUILDER.setLocation(new Point(0, 0, 0.5)).build()),
                ERROR_INTERSECTION_COUNT);

        // TC03: Medium sphere intersecting the view plane (10 intersection points)
        Sphere sphere3 = new Sphere(new Point(0, 0, -2),2 );
        assertEquals(10, countIntersections(sphere3,
                        CAMERA_BUILDER.setLocation(new Point(0, 0, 0.5)).build()),
                ERROR_INTERSECTION_COUNT);

        // TC04: Sphere behind the camera (0 intersection points)
        Sphere sphere4 = new Sphere( new Point(0, 0, 1),0.5);
        assertEquals(0, countIntersections(sphere4, camera), ERROR_INTERSECTION_COUNT);

        // TC05: Sphere containing the camera (9 intersection points)
        Sphere sphere5 = new Sphere( new Point(0, 0, 0),4);
        assertEquals(9, countIntersections(sphere5, camera), ERROR_INTERSECTION_COUNT);
    }

    /**
     * Tests ray-plane intersections
     */
    @Test
    void testRayPlaneIntersections() {
        Camera camera = CAMERA_BUILDER.build();

        // TC01: Plane against camera (9 intersection points)
        Plane plane1 = new Plane(new Point(0, 0, -5), new Vector(0, 0, 1));
        assertEquals(9, countIntersections(plane1, camera), ERROR_INTERSECTION_COUNT);

        // TC02: Plane with small angle to view plane (9 intersection points)
        Plane plane2 = new Plane(new Point(0, 0, -5), new Vector(0, 1, 2));
        assertEquals(9, countIntersections(plane2, camera), ERROR_INTERSECTION_COUNT);

        // TC03: Plane parallel to lower rays (6 intersection points)
        Plane plane3 = new Plane(new Point(0, 0, -5), new Vector(0, 1, 1));
        assertEquals(6, countIntersections(plane3, camera), ERROR_INTERSECTION_COUNT);
    }

    /**
     * Tests ray-triangle intersections
     */
    @Test
    void testRayTriangleIntersections() {
        Camera camera = CAMERA_BUILDER.build();

        // TC01: Small triangle in center of view (1 intersection point)
        Triangle triangle1 = new Triangle(
                new Point(0, 1, -2),
                new Point(1, -1, -2),
                new Point(-1, -1, -2)
        );
        assertEquals(1, countIntersections(triangle1, camera), ERROR_INTERSECTION_COUNT);

        // TC02: Triangle covering most of view (2 intersection points)
        Triangle triangle2 = new Triangle(
                new Point(0, 20, -2),
                new Point(1, -1, -2),
                new Point(-1, -1, -2)
        );
        assertEquals(2, countIntersections(triangle2, camera), ERROR_INTERSECTION_COUNT);
    }
}