package lighting;

import primitives.Color;

/**
 * Class for ambient light in the scene
 */
public class AmbientLight extends Light {
    /**
    * Constant representing for when there's no ambient light
    */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructor - sets the intensity of the ambient light
     * @param myIntensity the intensity/color of the ambient light (I_A)
     */
    public AmbientLight(Color myIntensity) {
        super(myIntensity);
    }
}