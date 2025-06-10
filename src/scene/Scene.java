package scene;

import primitives.Color;
import lighting.AmbientLight;
import geometries.Geometries;
import lighting.LightSource;
import java.util.List;
import java.util.LinkedList;

/**
 * Class representing a scene with 3D models and lighting.
 */
public class Scene {

    /** Scene name */
    public String name;

    /** Background color of the scene */
    public Color background = Color.BLACK;

    /** Ambient light for the entire scene */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /** The collection of geometries in the scene */
    public Geometries geometries = new Geometries();

    /** List of light sources affecting the scene */
    public List<LightSource> lights = new LinkedList<>();

    /**
     * Constructor that initializes the scene with a name.
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Sets the background color of the scene.
     * @param background the color to set
     * @return this scene object for chaining
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light for the scene.
     * @param ambientLight the ambient light to set
     * @return this scene object for chaining
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries in the scene.
     * @param geometries the geometries to set
     * @return this scene object for chaining
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * Sets the list of light sources for the scene.
     * @param lights the list of light sources
     * @return this scene object for chaining
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }
}
