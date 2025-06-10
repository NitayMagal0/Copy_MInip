package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 * @author Dan Zilberstein
 */
class ReflectionRefractionTests {
   /** Default constructor to satisfy JavaDoc generator */
   ReflectionRefractionTests() { /* to satisfy JavaDoc generator */ }

   /** Scene for the tests */
   private final Scene          scene         = new Scene("Test scene");
   /** Camera builder for the tests with triangles */
   private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
      .setRayTracer(scene, RayTracerType.SIMPLE);

   /** Produce a picture of a sphere lighted by a spot light */
   @Test
   void twoSpheres() {
      scene.geometries.add( //
                           new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(BLUE)) //
                              .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setKT(0.3)), //
                           new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(RED)) //
                              .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100))); //
      scene.lights.add( //
                       new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
                          .setKl(0.0004).setKq(0.0000006));

      cameraBuilder
         .setLocation(new Point(0, 0, 1000)) //
         .setDirection(Point.ZERO, Vector.AXIS_Y) //
         .setVpDistance(1000).setVpSize(150, 150) //
         .setResolution(500, 500) //
         .build() //
         .renderImage() //
         .writeToImage("refractionTwoSpheres");
   }

   /** Produce a picture of a sphere lighted by a spot light */
   @Test
   void twoSpheresOnMirrors() {
      scene.geometries.add( //
                           new Sphere(new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 50, 100)) //
                              .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20) //
                                 .setKT(new Double3(0.5, 0, 0))), //
                           new Sphere(new Point(-950, -900, -1000), 200d).setEmission(new Color(100, 50, 20)) //
                              .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20)), //
                           new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                                        new Point(670, 670, 3000)) //
                              .setEmission(new Color(20, 20, 20)) //
                              .setMaterial(new Material().setKR(1)), //
                           new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                                        new Point(-1500, -1500, -2000)) //
                              .setEmission(new Color(20, 20, 20)) //
                              .setMaterial(new Material().setKR(new Double3(0.5, 0, 0.4))));
      scene.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));
      scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
         .setKl(0.00001).setKq(0.000005));

      cameraBuilder
         .setLocation(new Point(0, 0, 10000)) //
         .setDirection(Point.ZERO, Vector.AXIS_Y) //
         .setVpDistance(10000).setVpSize(2500, 2500) //
         .setResolution(500, 500) //
         .build() //
         .renderImage() //
         .writeToImage("reflectionTwoSpheresMirrored");
   }

   /**
    * Produce a picture of a two triangles lighted by a spot light with a
    * partially
    * transparent Sphere producing partial shadow
    */
   @Test
   void trianglesTransparentSphere() {
      scene.geometries.add(
                           new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                                        new Point(75, 75, -150))
                              .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
                           new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                              .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
                           new Sphere(new Point(60, 50, -50), 30d).setEmission(new Color(BLUE))
                              .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.6)));
      scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
      scene.lights.add(
                       new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                          .setKl(4E-5).setKq(2E-7));

      cameraBuilder
         .setLocation(new Point(0, 0, 1000)) //
         .setDirection(Point.ZERO, Vector.AXIS_Y) //
         .setVpDistance(1000).setVpSize(200, 200) //
         .setResolution(600, 600) //
         .build() //
         .renderImage() //
         .writeToImage("refractionShadow");
   }

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
              .setResolution(400, 400) // Image resolution in pixels
              .build()
              .renderImage() // Render the scene
              .writeToImage("myCustomScene"); // Save to file
   }
}
