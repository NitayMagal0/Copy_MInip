package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * SpotLight emits light in a specific direction with intensity that diminishes with angle and distance.
 */
public class SpotLight extends PointLight {

    /**
     * Direction of the spotlight.
     * It is normalized to ensure it has a length of 1.
     */
    private final Vector direction;
    /**
     * Narrow beam effect factor.
     * This controls how focused the light beam is.
     * A value of 1 means normal spotlight behavior, higher values make it more focused.
     */
    private double narrowBeam = 1; // Add this field for bonus its for narrow beam effect

    /**
     * Constructs a spotlight with the given intensity, position, and direction.
     * @param intensity the color intensity of the light
     * @param position the position of the light source
     * @param direction the direction of the spotlight
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        Vector l = super.getL(p); // direction from light to point
        double dirDotL = direction.dotProduct(l);

        if (dirDotL <= 0) {
            return Color.BLACK; // light is facing away
        }

        // include narrow beam effect
        double beamEffect = Math.pow(dirDotL, narrowBeam);
        // Apply angular intensity dropoff (dirDotL) * attenuation
        return super.getIntensity(p).scale(beamEffect); // scale intensity by beam effect
    }

    // Builder-style setters that return SpotLight (not PointLight)
    @Override
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }

    @Override
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }

    @Override
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }

    /**
     * Sets the narrow beam factor for focused lighting.
     * @param narrowBeam the beam focus factor (1 = normal, higher = more focused)
     * @return this SpotLight object for method chaining
     */
    public SpotLight setNarrowBeam(double narrowBeam) {  //bonus
        if (narrowBeam <= 0)
            throw new IllegalArgumentException("narrowBeam must be positive");
        this.narrowBeam = narrowBeam;
        return this;
    }
    @Override
    public double getDistance(Point p) {
        return position.distance(p);
    }

}
