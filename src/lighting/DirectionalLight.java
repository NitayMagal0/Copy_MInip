package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Directional light that has constant intensity and direction everywhere.
 * Implements the LightSource interface.
 */
public class DirectionalLight extends Light implements LightSource {

    /**
     * Direction of the light source.
     * It is normalized to ensure it has a length of 1.
     */
    private final Vector direction;

    /**
     * Constructs a directional light with given intensity and direction.
     * @param intensity color intensity of the light
     * @param direction direction of the light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

    @Override
    public double getDistance(Point p) {
        return Double.POSITIVE_INFINITY;
    }
}
