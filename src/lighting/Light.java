package lighting;

import primitives.Color;

/**
 * Abstract base class for all light sources in the scene
 * Provides common functionality for light intensity management
 */
abstract class Light {
    /**
     * The intensity (color) of the light source
     */
    protected final Color intensity;

    /**
     * Constructor for Light class
     *
     * @param intensity the color intensity of the light source
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Getter for light intensity
     *
     * @return the intensity (color) of the light source
     */
    public Color getIntensity() {
        return intensity;
    }
}