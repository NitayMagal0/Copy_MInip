package primitives;
import geometries.Intersectable.Intersection;
import java.util.List;

/**
 * Represents a ray in 3D space, defined by a starting point (head)
 * and a normalized direction vector.
 */
public class Ray {

    /**
     * Starting point of the ray (origin).
     */
    final Point head;

    /**
     * Direction of the ray (always normalized).
     */
    final Vector direction;

    /**
     * Small delta value used to move ray starting point along the normal vector
     * to avoid self-intersection when creating secondary rays (shadow, reflection, transparency)
     */
    private static final double DELTA = 0.1;

    /**
     * Constructs a ray with a given origin point and direction vector.
     * The direction vector is automatically normalized.
     *
     * @param head the origin point of the ray
     * @param direction the direction of the ray (does not have to be normalized)
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * Constructs a ray with moved starting point to avoid self-intersection
     * @param head the original point
     * @param direction the direction of the ray
     * @param normal the normal vector at the point
     */
    public Ray(Point head, Vector direction, Vector normal) {
        this.direction = direction.normalize();

        // Check if direction and normal are parallel (avoid division by zero)
        double dn = direction.dotProduct(normal);
        if (Util.isZero(dn)) {
            this.head = head;
            return;
        }

        // Move the starting point along the normal to avoid self-intersection
        double deltaSign = dn > 0 ? DELTA : -DELTA;
        Vector deltaVector = normal.scale(deltaSign);
        this.head = head.add(deltaVector);
    }

    /**
     *  Getter for the head point of the ray
      * @return the head point of the ray
     */
    public Point getHead() {
        return head;
    }

    /**
     * Getter for the point on the ray at a certain distance from the head
     *
     * @param t the distance from the head
     * @return the point on the ray at the distance t from the head
     */
    public Point getPoint(double t) {
        // if t is zero, return the head point
        if (Util.isZero(t))
            return head;
        return head.add(direction.scale(t));
    }

    /**
     * Getter for the direction vector of the ray
     *
     * @return the direction vector of the ray
     */
    public Vector getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "Ray{" +
                "head=" + head +
                ", direction=" + direction +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ray other)) return false;
        return head.equals(other.head) && direction.equals(other.direction);
    }

    /**
     * Finds the closest intersection point to the ray's head from a list of intersections.
     *
     * @param intersections the list of intersections to search through
     * @return the closest intersection to the ray's head, or null if the list is null or empty
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        if (intersections == null || intersections.isEmpty()) return null;

        Intersection closest = intersections.get(0); // Initialize with the first intersection
        double minDistance = head.distanceSquared(closest.point); // Calculate the squared distance to the first point

        // Iterate through the remaining intersections to find the closest one
        for (int i = 1; i < intersections.size(); i++) {
            double d = head.distanceSquared(intersections.get(i).point); // Calculate squared distance
            if (d < minDistance) { // Update if a closer intersection is found
                minDistance = d;
                closest = intersections.get(i);
            }
        }

        return closest; // Return the closest intersection
    }

    /**
     * Finds the closest point to the ray's head from a list of points.
     *
     * @param points the list of points to search through
     * @return the closest point to the ray's head, or null if the list is null or empty
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null ? null
                // Map each point to an Intersection object and find the closest intersection
                : findClosestIntersection(points.stream()
                .map(p -> new Intersection(null, p)).toList()).point;
    }

}