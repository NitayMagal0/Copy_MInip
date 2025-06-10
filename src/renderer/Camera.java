package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util; // Import Util class
import java.util.MissingResourceException;

import primitives.Color;
import scene.Scene;
import static primitives.Util.isZero;
/**
 * Represents a camera in 3D space.
 * Built using the Builder design pattern (first approach).
 */
public class Camera implements Cloneable {
    /**
     * The location of the camera in 3D space.
     */
    private Point location;
    /**
     * The direction vectors of the camera:
     *
     * vTo: forward direction (looking towards)
     */
    private Vector vTo;
    /**
     * 90 degrees Up from location
     */
    private Vector vUp;
    /**
     * 90 degrees Right from location
     * This vector is calculated as the cross product of vTo and vUp.
     */
    private Vector vRight;
    /**
     * The dimensions of the view plane:
     * width: horizontal size of the view plane
     */
    private double width = 0;
    /**
     * height: vertical size of the view plane
     */
    private double height = 0;
    /**
     * distance: distance from the camera to the view plane
     * This is the distance along the vTo vector.
     */
    private double distance = 0; // Distance from camera to view plane

    // Added in stage 5
    /**
     * ImageWriter for creating the image.
     * This is used to write pixels to the image file.
     */
    private ImageWriter imageWriter; // For creating image
    /**
     * RayTracerBase for coloring the pixels.
     * This is used to trace rays and determine the color of each pixel.
     */
    private RayTracerBase rayTracer; // for coloring
    /**
     * The horizontal resolution of the view plane (number of pixels in the X direction).
     */
    private int nX = 1; // For image resolution the number of pixels in the x direction
    /**
     * The vertical resolution of the view plane (number of pixels in the Y direction).
     */
    private int nY = 1; // For image resolution the number of pixels in the y direction

    /**
     * Private constructor to enforce use of the Builder.
     */
    private Camera() {
    }

    /**
     * Creates a new Builder instance for constructing a Camera object.
     * @return a new Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }


    /**
     * Constructs a ray that passes through the center of a specified pixel in the view plane.
     *
     * @param nX the number of pixels in the x direction
     * @param nY the number of pixels in the y direction
     * @param j  the x index of the pixel
     * @param i  the y index of the pixel
     * @return the ray that passes through the pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pIJ = location;
        // Calculate the y-coordinate (vertical offset) in the view plane
        // The formula centers the coordinate system at the middle of the view plane
        // where i ranges from 0 to nY-1 (top to bottom)
        // Negative sign is used because pixel indices increase downward, while the y-coordinate increases upward
        double yI = -(i - (nY - 1) / 2d) * height / nY;
        // Calculate the x-coordinate (horizontal offset) in the view plane
        // where j ranges from 0 to nX-1 (left to right)
        // This transforms pixel coordinates to physical coordinates in the view plane
        double xJ = (j - (nX - 1) / 2d) * width / nX;

        //check if xJ or yI are not zero, so we will not add zero vector
        if (!Util.isZero(xJ)) pIJ = pIJ.add(vRight.scale(xJ));
        if (!Util.isZero(yI)) pIJ = pIJ.add(vUp.scale(yI));

        // we need to move the point in the direction of vTo by distance
        pIJ = pIJ.add(vTo.scale(distance));

        // Create a ray from the camera location to the calculated pixel center point (pIJ)
        return new Ray(location, pIJ.subtract(location).normalize());
    }

    // Added in stage 5 - START
    /**
     * Cast a ray through a pixel and color it
     * @param j column (X coordinate)
     * @param i row (Y coordinate)
     */
    private void castRay(int j, int i) {
        Ray ray = constructRay(nX, nY, j, i); // Construct a ray through the pixel
        Color color = rayTracer.traceRay(ray); // Get the color for the ray
        imageWriter.writePixel(j, i, color); // Color the pixel in the image
    }

    /**
     * Render the image by casting rays through all pixels
     * @return the camera for chaining
     */
    public Camera renderImage() {
        if (imageWriter == null)
            throw new UnsupportedOperationException("Missing image writer");

        if (rayTracer == null)
            throw new UnsupportedOperationException("Missing ray tracer");

        // Get resolution from imageWriter
        nX = imageWriter.nX();
        nY = imageWriter.nY();

        // Cast ray through each pixel
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                castRay(j, i);
            }
        }

        return this;
    }

    /**
     * Print grid on the rendered image
     * @param interval the interval (barrier) between grid lines
     * @param color the color of grid lines
     * @return the camera for chaining
     */
    public Camera printGrid(int interval, Color color) {
        if (imageWriter == null)
            throw new UnsupportedOperationException("Missing image writer");

        int nX = imageWriter.nX();
        int nY = imageWriter.nY();

        // Draw grid lines on the existing image
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                // Draw grid lines at intervals
                if (i % interval == 0 || j % interval == 0) { // If the pixel is on a grid line color it
                    imageWriter.writePixel(j, i, color);
                }
            }
        }

        return this;
    }

    /**
     * Write the image to a file
     * @param imageName the name of the file
     * @return the camera for chaining
     */
    public Camera writeToImage(String imageName) {
        if (imageWriter == null)
            throw new UnsupportedOperationException("Missing image writer");

        imageWriter.writeToImage(imageName);
        return this;
    }
    // Added in stage 5 - END

    /**
     * Builder class for constructing Camera objects using the Builder design pattern.
     * Holds a Camera instance, updates its internal fields, and returns a cloned instance on build.
     */
    public static class Builder {
        /**
         * Exception message for missing camera parameters.
         */
        private static final String MISSING = "Missing camera parameter";
        /**
         * The fully qualified class name for error reporting.
         */
        private static final String CLASS_NAME = Camera.class.getName();

        /**
         * The camera object being constructed.
         */
        private final Camera camera = new Camera();

        /**
         * Default constructor that initializes a new camera.
         */
        public Builder() {}

        /**
         * Sets the location of the camera.
         *
         * @param location the camera location
         * @return this builder instance
         * @throws IllegalArgumentException if location is null
         */
        public Builder setLocation(Point location) {
            if (location == null)
                throw new IllegalArgumentException("Camera location cannot be null");
            camera.location = location;
            return this;
        }

        /**
         * Sets the direction of the camera using vTo and vUp vectors.
         *
         * @param vTo forward direction vector
         * @param vUp upward direction vector
         * @return this builder instance
         * @throws IllegalArgumentException if vectors are null or not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (vTo == null || vUp == null)
                throw new IllegalArgumentException("Direction vectors cannot be null");
            if (!Util.isZero(vTo.dotProduct(vUp)))
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");

            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }

        /**
         * Sets the direction of the camera using a target point and approximate up vector.
         *
         * @param target the point the camera looks at
         * @param vUp the approximate up direction
         * @return this builder instance
         * @throws IllegalArgumentException if target or vUp is null or target equals location
         */
        public Builder setDirection(Point target, Vector vUp) {
            if (target == null || vUp == null)
                throw new IllegalArgumentException("Target and vUp cannot be null");
            if (camera.location == null)
                throw new IllegalArgumentException("Location must be set before setting direction with target");
            if (target.equals(camera.location))
                throw new IllegalArgumentException("Target cannot be the same as location");

            camera.vTo = target.subtract(camera.location).normalize();
            camera.vRight = camera.vTo.crossProduct(vUp).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();

            return this;
        }

        /**
         * Sets the direction of the camera using only a target point.
         * Assumes the "up" direction is approximately the Y-axis.
         * Calculates the forward (vTo), right (vRight), and adjusted up (vUp) vectors.
         *
         * @param target the point the camera should look at
         * @return this builder instance
         * @throws IllegalArgumentException if the target is null, equals the camera's location,
         *                                  or results in a degenerate up/right configuration
         */
        public Builder setDirection(Point target) {
            if (target == null)
                throw new IllegalArgumentException("Target cannot be null");
            if (camera.location == null)
                throw new IllegalArgumentException("Location must be set before setting direction with target");

            if (target.equals(camera.location))
                throw new IllegalArgumentException("Target cannot be the same as location");

            Vector vTo = target.subtract(camera.location).normalize(); // Its direction vector
            Vector defaultUp = Vector.AXIS_Y; // Default up vector (Y-axis)

            // Check if vTo is parallel to default up vector
            if (Util.isZero(vTo.crossProduct(defaultUp).length())) {
                throw new IllegalArgumentException("vTo is parallel to up vector (Y-axis), cannot define right vector");
            }

            return setDirection(target, defaultUp);
        }

        /**
         * Sets the size of the view plane.
         *
         * @param width view plane width
         * @param height view plane height
         * @return this builder instance
         * @throws IllegalArgumentException if width or height are non-positive
         */
        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0)
                throw new IllegalArgumentException("View plane dimensions must be positive");
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the distance from camera to view plane.
         *
         * @param distance distance to view plane
         * @return this builder instance
         * @throws IllegalArgumentException if distance is non-positive
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0)
                throw new IllegalArgumentException("View plane distance must be positive");
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the resolution for the image
         * @param nX number of pixels in X dimension
         * @param nY number of pixels in Y dimension
         * @return this builder
         */
        public Builder setResolution(int nX, int nY) {
            if (nX <= 0 || nY <= 0)
                throw new IllegalArgumentException("Resolution cant be negative or zero");
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }

        // Added in stage 5 - START
        /**
         * Sets the ray tracer for the camera
         * @param scene the scene to render
         * @param type the type of ray tracer to use
         * @return this builder
         */
        public Builder setRayTracer(Scene scene, RayTracerType type) {
            if (type == RayTracerType.SIMPLE) {
                camera.rayTracer = new SimpleRayTracer(scene);
            }  else {
                camera.rayTracer = null;
            }
            return this;
        }


        // Added in stage 5 - END

        /**
         * Finalizes the construction of the Camera object.
         * Validates required fields, computes vRight, and returns a copy.
         *
         * @return a fully constructed Camera object
         * @throws MissingResourceException if any required value is missing
         */
        public Camera build() {
            if (camera.location == null)
                throw new MissingResourceException(MISSING, CLASS_NAME, "location");
            if (camera.vTo == null)
                throw new MissingResourceException(MISSING, CLASS_NAME, "vTo");
            if (camera.vUp == null)
                throw new MissingResourceException(MISSING, CLASS_NAME, "vUp");
            if (Util.isZero(camera.width))
                throw new MissingResourceException(MISSING, CLASS_NAME, "width");
            if (Util.isZero(camera.height))
                throw new MissingResourceException(MISSING, CLASS_NAME, "height");
            if (Util.isZero(camera.distance))
                throw new MissingResourceException(MISSING, CLASS_NAME, "distance");

            if (camera.vRight == null) {
                camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
                //camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
            }

            // Added in stage 5 - START
            // Check if resolution values are positive and initialize imageWriter
            if (camera.nX <= 0 || camera.nY <= 0)
                throw new IllegalArgumentException("Resolution values must be positive");

            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);

            // If rayTracer is null, create a SimpleRayTracer with an empty scene
            if (camera.rayTracer == null) {
                camera.rayTracer = new SimpleRayTracer(null);
            }
            // Added in stage 5 - END

            return camera.clone();
        }
    }

    @Override
    public Camera clone() {
        try {
            return (Camera) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }
}