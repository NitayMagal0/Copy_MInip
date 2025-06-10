package geometries;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import primitives.Material;

/**
 * Represents an abstract base class for geometric shapes in 3D space.
 * This class provides a contract for geometric objects to calculate
 * their normal vectors at a given point on their surfaces.
 */
public abstract class Geometry extends Intersectable {

    /**
     * Emission color
     */
    protected Color emission = Color.BLACK;

    /**
     * Material properties for light calculations
     */
    private Material material = new Material();

    /**
     * Returns the emission color of the geometry.
     * @return emission color
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color.
     * @param emission color to set
     * @return the current Geometry object
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }


    /**
     * Gets the material properties of this geometry
     * @return the material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material properties using Builder pattern
     * @param material the material to set to
     * @return the geometry
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * Calculates the normal vector to the surface of the geometric object at a given point.
     *
     * @param pnt the point on the surface of the geometric object where the normal vector is to be calculated
     * @return the normal vector at the given point
     */
    public abstract Vector getNormal(Point pnt);
}
