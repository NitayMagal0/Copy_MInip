package geometries;

/**
 * Represents an abstract base class for all radial geometries in 3D space.
 * Radial geometries are characterized by having a defined radius.
 *
 * This class extends the {@link Geometry} class, inheriting its behavior for
 * geometric operations, while adding the specific representation and functionality
 * related to radial shapes.
 * Subclasses may include specific types of radial geometries such as spheres, tubes,
 * and cylinders, which further define their unique characteristics.
 */
public abstract class RadialGeometry extends Geometry {

    /**
     * The radius of the radial geometry object.
     * This value is used to define the size of the geometry in 3D space.
     */
    final protected double radius; // Represents the radius of a radial geometry object in 3D space.

    /**
     * Constructs a radial geometry object with a specified radius.
     *
     * @param radius the radius of the radial geometry. This value represents the distance from the center
     *               of the geometry to the edge of the shape. The radius must be a positive double value.
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }
}
