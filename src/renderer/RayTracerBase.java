package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract base class for all ray tracers
 * This class helps follow rays in a scene and figure out what color they should be
 */
public abstract class RayTracerBase {


    /**
     * Represents the 3D scene to be traced. It contains the geometries, lighting,
     * and background information that are used for ray tracing calculations.
     */
    protected final Scene scene;

    /**
     * Constructor construct a ray tracer for a given scene
     * @param scene the scene to trace
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Find the color of a given ray
     * @param ray the ray to trace
     * @return the color from tracing the ray
     */
    public abstract Color traceRay(Ray ray);
}