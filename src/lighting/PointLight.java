package lighting;

import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import java.util.ArrayList;
import java.util.List;

/**
 * Point light source — has a position and intensity affected by distance.
 */
public class PointLight extends Light implements LightSource {
    /**
     * Position of the point light source in 3D space.
     */
    protected final Point position;

    /**
     * Attenuation factors for the light intensity.
     * kC: constant attenuation
     */
    private Double3 kC = Double3.ONE;       // constant attenuation

    /**
     * kL: linear attenuation
     */
    private Double3 kL = Double3.ZERO;      // linear attenuation

    /**
     * kQ: quadratic attenuation
     */
    private Double3 kQ = Double3.ZERO;      // quadratic attenuation

    /**
     * Constructor for PointLight.
     * @param intensity the light's color intensity
     * @param position the position of the light source
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Set the constant attenuation factor.
     * @param kC the constant term
     * @return the same PointLight (for chaining)
     */
    public PointLight setKc(double kC) {
        this.kC = new Double3(kC);
        return this;
    }

    /**
     * Set the linear attenuation factor.
     * @param kL the linear term
     * @return the same PointLight (for chaining)
     */
    public PointLight setKl(double kL) {
        this.kL = new Double3(kL);
        return this;
    }

    /**
     * Set the quadratic attenuation factor.
     * @param kQ the quadratic term
     * @return the same PointLight (for chaining)
     */
    public PointLight setKq(double kQ) {
        this.kQ = new Double3(kQ);
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double dSquared = p.distanceSquared(position);
        double d = Math.sqrt(dSquared);
        double attenuationFactor = 1.0 / (kC.d1() + kL.d1() * d + kQ.d1() * dSquared);
        return intensity.scale(attenuationFactor);
    }


    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }


    @Override
    public double getDistance(Point p) {
        return position.distance(p);
    }


    private static final double DELTA = 0.1;


    @Override
    public List<Ray> generateShadowRays(Point point, Vector normal) {
        List<Ray> rays = new ArrayList<>();
        Vector toLight = getL(point).scale(-1);
        Vector up = normal;
        Vector right = toLight.crossProduct(up).normalize();
        up = right.crossProduct(toLight).normalize();

        double radius = 5; // area light radius
        int samples = 16;  // number of rays

        for (int i = 0; i < samples; i++) {
            double dx = (Math.random() - 0.5) * 2 * radius;
            double dy = (Math.random() - 0.5) * 2 * radius;
            Point jittered = position.add(right.scale(dx)).add(up.scale(dy));
            rays.add(new Ray(point.add(normal.scale(DELTA)), jittered.subtract(point), normal));
        }
        return rays;
    }

}
