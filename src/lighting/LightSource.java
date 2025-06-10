// Package: lighting

package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Interface representing a light source in the scene.
 */
public interface LightSource {

    /**
     * Returns the intensity of the light at a given point in the scene.
     * @param p the point at which the intensity is calculated
     * @return the light color intensity at point p
     */
    Color getIntensity(Point p);

    /**
     * Returns the direction vector from the light source to a given point.
     * @param p the point for which the light direction is needed
     * @return the normalized direction vector to point p
     */
    Vector getL(Point p);

    double getDistance(Point p);


    default List<Ray> generateShadowRays(Point point, Vector normal) {
        // By default, return a single shadow ray (for point/spot lights)
        return List.of(new Ray(point.add(normal.scale(0.1)), getL(point).scale(-1), normal));
    }

}
