package primitives;

/**
 * Material class represents the material properties of geometric objects
 * for light reflection calculations using the Phong reflection model.
 * This is a PDS (Plain Data Structure) class
 */
public class Material {
    /** Ambient reflection coefficient (kA) - reflects ambient light */
    public Double3 kA = Double3.ONE;

    /** Diffuse reflection coefficient (kD) - how much diffuse light is reflected */
    public Double3 kD = Double3.ZERO;

    /** Specular reflection coefficient (kS) - how much specular light is reflected */
    public Double3 kS = Double3.ZERO;

    /**
     * Transparency coefficient - determines how much light passes through the material
     * Each component (R,G,B) should be between 0.0 (opaque) and 1.0 (fully transparent)
     */
    public Double3 kT = Double3.ZERO;

    /**
     * Reflection coefficient - determines how much light is reflected like a mirror
     * Each component (R,G,B) should be between 0.0 (no reflection) and 1.0 (perfect mirror)
     */
    public Double3 kR = Double3.ZERO;

    /** Shininess exponent (nShininess) - controls specular highlight size */
    public int nShininess = 0;

    // ===== Ambient Setters =====

    /**
     * Sets the ambient reflection coefficient (kA).
     * @param kA ambient reflection coefficient
     * @return the current Material object
     */
    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Sets the ambient reflection coefficient (kA) using a double value.
     * @param kA ambient reflection coefficient as a double
     * @return the current Material object
     */
    public Material setKA(double kA) {
        this.kA = new Double3(kA);
        return this;
    }

    // ===== Diffuse Setters =====

    /**
     * Sets the diffuse reflection coefficient (kD).
     * @param kD diffuse reflection coefficient
     * @return the current Material object
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient (kD) using a double value.
     * @param kD diffuse reflection coefficient as a double
     * @return the current Material object
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    // ===== Specular Setters =====

    /**
     * Sets the specular reflection coefficient (kS).
     * @param kS specular reflection coefficient
     * @return the current Material object
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the specular reflection coefficient (kS).
     * @param kS specular reflection coefficient
     * @return the current Material object
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    // ===== Transparency and Reflection Setters =====

    /**
     * Sets the transparency coefficient using a Double3 value
     *
     * @param kT transparency coefficient for R,G,B components
     * @return this Material object for method chaining
     */
    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Sets the transparency coefficient using a single double value for all RGB components
     *
     * @param kT transparency coefficient (applied to all R,G,B components)
     * @return this Material object for method chaining
     */
    public Material setKT(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Sets the reflection coefficient using a Double3 value
     *
     * @param kR reflection coefficient for R,G,B components
     * @return this Material object for method chaining
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the reflection coefficient using a single double value for all RGB components
     *
     * @param kR reflection coefficient (applied to all R,G,B components)
     * @return this Material object for method chaining
     */
    public Material setKR(double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    // ===== Shininess Setter =====

    /**
     * Sets the shininess exponent (nShininess).
     * @param nShininess shininess exponent
     * @return the current Material object
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
