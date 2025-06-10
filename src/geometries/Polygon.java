package geometries;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 */
public class Polygon extends Geometry {
   /** List of polygon's vertices */
   protected final List<Point> vertices;
   /** Associated plane in which the polygon lays */
   protected final Plane plane;
   /** The size of the polygon - the amount of the vertices in the polygon */
   private final int size;

   /**
    * Polygon constructor based on vertices list. The list must be ordered by edge
    * path. The polygon must be convex.
    * @param vertices list of vertices according to their order by edge path
    * @throws IllegalArgumentException in any case of illegal combination of
    *                                  vertices:
    *                                  <ul>
    *                                  <li>Less than 3 vertices</li>
    *                                  <li>Consequent vertices are in the same
    *                                  point</li>
    *                                  <li>The vertices are not in the same
    *                                  plane</li>
    *                                  <li>The order of vertices is not according
    *                                  to edge path</li>
    *                                  <li>Three consequent vertices lay in the
    *                                  same line (180&#176; angle between two
    *                                  consequent edges)</li>
    *                                  <li>The polygon is concave (not convex)</li>
    *                                  </ul>
    */
   public Polygon(Point... vertices) {
      if (vertices.length < 3)
         throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
      this.vertices = List.of(vertices);
      size = vertices.length;

      // Generate the plane according to the first three vertices and associate the
      // polygon with this plane.
      // The plane holds the invariant normal (orthogonal unit) vector to the polygon
      plane = new Plane(vertices[0], vertices[1], vertices[2]);
      if (size == 3) return; // no need for more tests for a Triangle

      Vector n = plane.getNormal(vertices[0]);
      Vector edge1 = vertices[size - 1].subtract(vertices[size - 2]);
      Vector edge2 = vertices[0].subtract(vertices[size - 1]);

      boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
      for (var i = 1; i < size; ++i) {
         if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
            throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
         edge1 = edge2;
         edge2 = vertices[i].subtract(vertices[i - 1]);
         if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
            throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
      }
   }

   @Override
   public Vector getNormal(Point point) {
      return plane.getNormal(point);
   }

   @Override
   protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
      List<Point> intersections = plane.findIntersections(ray);
      if (intersections == null) {
         return null; // No intersection with the plane
      }

      Point p0 = ray.getHead();
      Vector v = ray.getDirection();
      Point p = intersections.get(0);

      if (alignZero(p.distance(p0) - maxDistance) > 0) {
         return null;
      }

      Vector v1 = vertices.get(0).subtract(p0);
      Vector v2 = vertices.get(1).subtract(p0);
      Vector n = v1.crossProduct(v2).normalize();

      double sign = v.dotProduct(n);
      if (isZero(sign)) return null;
      boolean positive = sign > 0;

      int size = vertices.size();
      for (int i = 1; i < size; ++i) {
         v1 = vertices.get(i).subtract(p0);
         v2 = vertices.get((i + 1) % size).subtract(p0);
         n = v1.crossProduct(v2).normalize();
         sign = v.dotProduct(n);
         if (isZero(sign)) return null;
         if ((sign > 0) != positive) return null;
      }

      return List.of(new Intersection(this, p));
   }
}