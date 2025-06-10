package renderer;

import java.util.List;

//import geometries.Intersectable;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.Intersection;
import lighting.LightSource;
//package renderer;

import static primitives.Util.alignZero;

/**
 * A simple implementation of RayTracerBase that only supports ambient lighting
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * A small delta value used to avoid shadow acne (self-shadowing artifacts)
     * This value is used to offset the intersection point slightly along the normal
     * to ensure that the shadow ray starts just outside the surface.
     */
    private static final double DELTA = 0.1;

    /**
     * Maximum recursion depth for reflection and refraction calculations(stops infinite loops)
     * Prevents infinite recursion in scenes with multiple reflective/transparent objects
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;

    /**
     * Minimum threshold for collected Weakening coefficient
     * When the effect becomes too small to matter, we stop calculating - recursion stops early
     */
    private static final double MIN_CALC_COLOR_K = 0.001;

    /**
     * Initial Weakening coefficient for starting recursion
     * Starting value for how much light effect we have (100%)
     * Represents 100% light contribution at the beginning
     */
    private static final Double3 INITIAL_K = Double3.ONE;

    /**
     * Determines whether the intersection point is unshaded with respect to a light source.
     * @param intersection the intersection point
     * @param l the light direction vector (from point to light)
     * @param n the surface normal at the point
     * @param nl the dot product of the light direction and the normal
     * @return true if the point is unshaded (i.e., not blocked), false otherwise
     */
    private boolean unshaded(Intersection intersection, Vector l, Vector n, double nl) {
        Vector pointToLight = l.scale(-1);

        // Offset ray origin to avoid self-intersection
        Ray shadowRay = new Ray(intersection.point, pointToLight, n);

        // Calculate max distance from point to light
        double lightDistance = intersection.lightSource.getDistance(intersection.point);

        // Only find intersections closer than the light source
        List<Intersection> intersections = scene.geometries.calculateIntersections(shadowRay, lightDistance);
        if (intersections == null) return true;

        // Return false if any opaque object blocks the light
        for (Intersection occ : intersections) {
            if (occ.geometry.getMaterial().kT.lowerThan(MIN_CALC_COLOR_K)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Calculates how much light passes through transparent objects between point and light source
     * @param intersection the intersection point
     * @return transparency factor (how much light gets through)
     */
    private Double3 transparency(Intersection intersection) {
        Vector pointToLight = intersection.lightDir.scale(-1);

        // Construct shadow ray with offset to prevent self-shadowing
        Ray shadowRay = new Ray(intersection.point, pointToLight, intersection.normal);

        // Max distance from point to light
        double lightDistance = intersection.lightSource.getDistance(intersection.point);

        // Use optimized method with maxDistance
        List<Intersection> intersections = scene.geometries.calculateIntersections(shadowRay, lightDistance);
        if (intersections == null) {
            return Double3.ONE; // No objects block the light — fully transparent
        }

        Double3 ktr = Double3.ONE; // Start with full transparency

        for (Intersection occ : intersections) {
            // Multiply transparency by blocking object's transparency factor
            ktr = ktr.product(occ.geometry.getMaterial().kT);

            // If transparency is too low, exit early
            if (ktr.lowerThan(MIN_CALC_COLOR_K)) {
                return Double3.ZERO;
            }
        }

        return ktr;
    }


    /**
     * Constructor to create a simple ray tracer for a given scene
     * @param scene the scene to render
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
        Intersection closest = ray.findClosestIntersection(intersections);
        return closest == null ? scene.background : calcColor(closest, ray);
    }

    private Color calcColor(Intersection intersection, Ray ray) {
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK;
        }

        return scene.ambientLight.getIntensity().scale(intersection.material.kA)
                .add(calcColor(intersection, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K));
    }

//    I_P = Final color of the pixel (what we see)
//    k_A = How much ambient light the material absorbs/reflects
//    I_A = Ambient light intensity (background lighting)
//    I_E = Emission light (if object glows by itself)

    /**
     * Calculates color with recursion for reflections and transparency
     * @param intersection the point where ray hits geometry
     * @param ray the ray that hit this point
     * @param level how many more times we can do recursion
     * @param k how much light effect is left
     * @return the color at this point
     */
    private Color calcColor(Intersection intersection, Ray ray, int level, Double3 k) {
        // Calculate local effects (direct lighting)
        Color color = calcColorLocalEffects(intersection);

        // If we reached maximum depth, stop here
        if (level == 1) {
            return color;
        }

        // Add global effects (reflections and transparency)
        return color.add(calcGlobalEffects(intersection, ray, level, k));
    }

    /**
     * Creates a reflection ray from an intersection point
     * @param intersection the point where light hits
     * @param n the normal vector at the point
     * @param v the incoming ray direction
     * @param nv dot product of normal and ray direction
     * @return new ray for reflection
     */
    private Ray constructReflectedRay(Intersection intersection, Vector n, Vector v, double nv) {
        // Calculate reflection direction: r = v - 2(v·n)n
        Vector r = v.subtract(n.scale(2 * nv));

        // Use new constructor that handles moving the start point
        return new Ray(intersection.point, r, n);
    }

    /**
     * Creates a refraction (transparency) ray from an intersection point
     * @param intersection the point where light hits
     * @param n the normal vector at the point
     * @param v the incoming ray direction
     * @return new ray for transparency
     */
    private Ray constructRefractedRay(Intersection intersection, Vector n, Vector v) {
        // For simple transparency, ray continues in same direction
        // Use new constructor that handles moving the start point
        return new Ray(intersection.point, v, n);
    }

    /**
     * Calculates color from one secondary ray (reflection or transparency)
     * @param ray the secondary ray to trace
     * @param level how many more recursion levels we can do
     * @param k how much light effect is left
     * @param kx the material coefficient (kR or kT)
     * @return color from this secondary ray
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        // Calculate how much effect this ray will have
// k = Current accumulated light effect (starts at INITIAL_K = Double3.ONE)
// kx = Material coefficient (either kR for reflection or kT for transparency)
// kkx = Combined effect after this reflection/transparency step
        Double3 kkx = k.product(kx); // Combine k (light effect left) with kx (material coefficient)

        // If effect is too small, don't bother calculating
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) {
            return Color.BLACK;
        }

        // Find where this ray hits something
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) {
            return scene.background.scale(kx);
        }

        // Check if intersection data is valid
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK;
        }

        // Calculate color at that point recursively
        return calcColor(intersection, ray, level - 1, kkx).scale(kx);
    }

    /**
     * Calculates all global effects (reflection and transparency) at a point
     * @param intersection the intersection point
     * @param ray the original ray
     * @param level how many more recursion levels we can do
     * @param k how much light effect is left
     * @return color from global effects
     */
    private Color calcGlobalEffects(Intersection intersection, Ray ray, int level, Double3 k) {
        Vector v = ray.getDirection();
        Vector n = intersection.normal;
        double nv = intersection.rayDotNormal;
        Material mat = intersection.material;

        // Calculate reflection effect
        Ray reflectedRay = constructReflectedRay(intersection, n, v, nv);
        Color reflectionColor = calcGlobalEffect(reflectedRay, level, k, mat.kR);

        // Calculate transparency effect
        Ray refractedRay = constructRefractedRay(intersection, n, v);
        Color transparencyColor = calcGlobalEffect(refractedRay, level, k, mat.kT);

        // Return sum of both effects
        return reflectionColor.add(transparencyColor);
    }

    /**
     * Finds the closest intersection point along a ray
     * @param ray the ray to check for intersections
     * @return the closest intersection, or null if no intersections
     */
    private Intersection findClosestIntersection(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
        if (intersections == null) {
            return null;
        }
        return ray.findClosestIntersection(intersections);
    }

    /**
     * Preprocesses intersection data by calculating ray-related vectors (intersection direction, normal and their dotProduct)
     * Once we "preprocess", we can use it for multiple light sources without recalculating the same vectors over and over
     * @param intersection the intersection to preprocess
     * @param rayDirection the direction of the ray that hit the intersection
     * @return false if the ray is parallel to the surface (dot product = 0), true otherwise
     */
    private boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        intersection.rayDir = rayDirection;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.rayDotNormal = alignZero(intersection.rayDir.dotProduct(intersection.normal));
        return intersection.rayDotNormal != 0;
    }

    /**
     * Sets light source data in the intersection cache
     *
     * @param intersection the intersection to update
     * @param lightSource the light source to set
     * @return false if light and view directions are on opposite sides of surface, true otherwise
     */
    private boolean setLightSource(Intersection intersection, LightSource lightSource) {
        intersection.lightSource = lightSource;
        intersection.lightDir = lightSource.getL(intersection.point);
        intersection.lightDotNormal = alignZero(intersection.lightDir.dotProduct(intersection.normal));
        return intersection.lightDotNormal * intersection.rayDotNormal > 0; // if its they are both positive or both negative, then they are on the same side because the normal is always pointing outwards
    }

    /**
     * Calculates local lighting effects at an intersection (diffuse and specular components)
     * @param intersection the intersection point with data
     * @return the color result-contribution from local lighting effects
     */
    private Color calcColorLocalEffects(Intersection intersection) {
        Color color = intersection.geometry.getEmission();

        for (LightSource lightSource : scene.lights) {
            // Update light source data in intersection object
            if (!setLightSource(intersection, lightSource)) {
                continue; // Skip this light if setup failed
            }

            // Get transparency factor instead of just checking if unshaded
            Double3 ktr = transparency(intersection);
            if (!ktr.lowerThan(MIN_CALC_COLOR_K)) {
                // Compute light intensity at the point, scaled by transparency
                Color iL = lightSource.getIntensity(intersection.point).scale(ktr);

                // Add combined diffuse and specular contribution
                Double3 combinedEffects = calcDiffusive(intersection).add(calcSpecular(intersection));
                color = color.add(iL.scale(combinedEffects));
            }
        }

        return color;
    }


    /**
     * Calculates the specular reflection component using intersection data
     *
     * @param intersection the intersection with light data
     * @return the specular reflection coefficient effect
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector r = intersection.lightDir.subtract(intersection.normal //r = reflection vector
                .scale(2 * intersection.lightDotNormal)); // Calculate reflection vector twice the projection of light onto normal
        // Measures angle between "where camera looks" and "where light reflects"
        // Flips the sign because rayDir points toward surface, not away from it
        double minusVR = alignZero(-intersection.rayDir.dotProduct(r)); // Calculate viewer-reflection angle, Creates mirror reflection (minusVR- minus ViewerDirection dot ReflectionDirection)
        //Math.max(0, minusVR) = Only positive angles (no negative highlights)
        //Math.pow(..., nShininess) = Make highlight sharper/more focused
        //kS.scale(...) = Apply material's specular properties
        return intersection.material.kS.scale(Math.pow(Math.max(0, minusVR), intersection.material.nShininess)); // Apply specular formula (kS = specular material coefficient nShininess = shininess factor
    }

    /**
     * Calculates the diffusive reflection component using intersection data
     *intersection.lightDotNormal = How directly light hits the surface
     * Math.abs(...) = Only positive values (no negative lighting)
     * intersection.material.kD = How much diffuse light the material reflects
     * Result-Creates the matte (non shiny) reflection that makes objects visible under light.
     *
     * @param intersection the intersection with light data
     * @return the diffusive reflection coefficient
     */
    private Double3 calcDiffusive(Intersection intersection) {
        return intersection.material.kD.scale(Math.abs(intersection.lightDotNormal));
    }
}