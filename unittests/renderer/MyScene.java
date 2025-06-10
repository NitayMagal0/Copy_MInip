package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Test Class to make out Scenes
 */
class MyScene {
    /** Scene for the tests */
    private final Scene          scene         = new Scene("Test scene");
    /** Camera builder for the tests with triangles */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(scene, RayTracerType.SIMPLE);

    /**
     * Custom test scene with different shapes showing all effects
     */
    @Test
    void myCustomScene() {
        scene.geometries.add(
                // Reflective sphere (mirror ball) - shows reflections of other objects
                // Blue-tinted sphere with high reflection coefficient (kR=0.6)
                new Sphere(new Point(0, 0, -100), 30)
                        .setEmission(new Color(10, 10, 30)) // Dark blue emission
                        .setMaterial(new Material().setKD(0.3).setKS(0.7).setKR(0.6)), // High specular and reflection

                // Transparent sphere - you can see through it (demonstrates transparency)
                // Very dark sphere with slight red tint, but appears mostly dark due to low emission values
                new Sphere(new Point(-60, 20, -80), 20)
                        .setEmission(new Color(20, 10, 10)) // Very dark with slight red tint (barely visible)
                        .setMaterial(new Material().setKD(0.2).setKS(0.2).setKT(0.7)), // High transparency

                // Background triangle - provides surface for reflections and shadows
                // Yellow-tinted triangle positioned behind the spheres
                new Triangle(new Point(-80, -40, -120), new Point(80, -40, -120), new Point(0, 60, -120))
                        .setEmission(new Color(30, 30, 10)) // Dark yellow emission
                        .setMaterial(new Material().setKD(0.8).setKS(0.2)), // Mostly diffuse, low specular

                // Ground plane - shows shadows cast by spheres
                // Gray plane positioned below the spheres to catch shadows
                new Plane(new Point(0, -40, -100), new Vector(0, 1, 0)) // Horizontal plane at y=-40
                        .setEmission(new Color(20, 20, 20)) // Dark gray emission
                        .setMaterial(new Material().setKD(0.6).setKS(0.4)) // Medium diffuse and specular
        );

        // Point light source positioned above and to the right of the scene
        // Creates shadows from spheres on the plane and triangle
        scene.lights.add(
                new PointLight(new Color(400, 400, 400), new Point(50, 50, 0)) // White light above scene
                        .setKl(0.001).setKq(0.0001) // Linear and quadratic attenuation factors
        );

        // Ambient light provides basic illumination to all surfaces
        // Low intensity to keep shadows visible
        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20))); // Dark gray ambient

        // Camera positioned in front of the scene looking towards negative Z
        // Close enough to see details but far enough to capture whole scene
        cameraBuilder.setLocation(new Point(0, 0, 100)) // Camera in front of scene
                .setDirection(new Point(0, 0, -100), Vector.AXIS_Y) // Looking towards scene center
                .setVpDistance(100) // Distance from camera to view plane
                .setVpSize(150, 150) // View plane size (width x height)
                .setResolution(1000, 1000) // Image resolution in pixels
                .build()
                .renderImage() // Render the scene
                .writeToImage("myCustomScene"); // Save to file
    }
    /**
     * Create a beautiful and simple 3D scene with multiple spheres and triangles
     */
    /**
     * Create a ice cream scene
     */
    @Test
    void createIceCreamScene() {
        scene.geometries.add(
                // Ice cream scoops - using darker, more intense colors

                // Top scoop - strawberry (pink/red)
                new Sphere(new Point(0, 40, -200), 35d)
                        .setEmission(new Color(180, 20, 60))
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.4).setShininess(30)
                                .setKR(0.2)),  // ← Added reflection

                // Middle scoop - vanilla (cream/yellow) - TRANSPARENT, SOFTER LOOK
                new Sphere(new Point(0, -10, -200), 40d)
                        .setEmission(new Color(120, 90, 50))  // softened yellow
                        .setMaterial(new Material()
                                .setKD(0.3).setKS(0.1)
                                .setKT(0.5)),


                // Bottom scoop - chocolate (dark brown)
                new Sphere(new Point(0, -65, -200), 45d)
                        .setEmission(new Color(80, 40, 20))
                        .setMaterial(new Material()
                                .setKD(0.7).setKS(0.3).setShininess(20)
                                .setKR(0.2)),


                // Ice cream cone - triangular cone shape
                new Triangle(new Point(-30, -90, -180), new Point(30, -90, -180), new Point(0, -190, -200))
                        .setEmission(new Color(160, 100, 40))
                        .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10)),

                new Triangle(new Point(-30, -90, -180), new Point(0, -190, -200), new Point(-30, -90, -220))
                        .setEmission(new Color(140, 80, 30))
                        .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10)),

                new Triangle(new Point(30, -90, -180), new Point(30, -90, -220), new Point(0, -190, -200))
                        .setEmission(new Color(120, 70, 25))
                        .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10)),

                // Cherry on top - small red sphere
                new Sphere(new Point(-8, 65, -185), 8d)
                        .setEmission(new Color(200, 10, 10))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80)),

                // Cherry stem - small green triangle
                new Triangle(new Point(-8, 73, -185), new Point(-6, 85, -185), new Point(-10, 85, -185))
                        .setEmission(new Color(20, 120, 20))
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(30)),

                // Table surface - dark wood (aligned and extended)
                new Triangle(new Point(-200, -250, -350), new Point(200, -250, -350), new Point(-200, -250, 100))
                        .setEmission(new Color(45, 22, 12))
                        .setMaterial(new Material().setKD(0.9).setKS(0.1).setShininess(10)),

                new Triangle(new Point(200, -250, -350), new Point(200, -250, 100), new Point(-200, -250, 100))
                        .setEmission(new Color(50, 25, 12))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(25)),


                // Background wall - soft pastel (touches floor at Z = -350)
                new Triangle(new Point(-200, -250, -350), new Point(200, -250, -350), new Point(-200, 200, -350))
                        .setEmission(new Color(40, 60, 80))
                        .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10)),

                new Triangle(new Point(200, -250, -350), new Point(200, 200, -350), new Point(-200, 200, -350))
                        .setEmission(new Color(60, 40, 70))
                        .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10))

                );

        // Soft ambient lighting - much darker
        scene.setAmbientLight(new AmbientLight(new Color(8, 8, 10)));

        // Main lighting - softer and warmer
        scene.lights.add(
                // Key light from upper left - warm and soft
                new SpotLight(new Color(300, 250, 200), new Point(-100, 120, 150), new Vector(1, -1, -1.5))
                        .setKl(2E-5).setKq(3E-7)
        );



        // After (all required camera parameters set)
        cameraBuilder
                .setLocation(new Point(0, 0, 100))
                .setDirection(new Point(0, 0, -100), Vector.AXIS_Y)
                .setVpDistance(100)
                .setVpSize(150, 150)
                .setResolution(1000, 1000)
                .build()
                .renderImage()
                .writeToImage("Ice-Cream");
    }

}
