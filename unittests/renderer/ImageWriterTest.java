package renderer;

import org.junit.jupiter.api.Test;

import primitives.Color;
import renderer.ImageWriter;

/**
 * Test for {@link renderer.ImageWriter}
 */
public class ImageWriterTest {

    /**
     * Test method for {@link renderer.ImageWriter} creating a simple image with a grid
     */
    @Test
    public void writeImageTest() {
        // Image dimensions and grid dimensions
        int nX = 800;
        int nY = 500;
        int gridRows = 10;
        int gridCols = 16;

        // Create an ImageWriter with the specified dimensions
        ImageWriter imageWriter = new ImageWriter(nX, nY);

        // Choose colors for background and grid
        Color backgroundColor = new Color(255, 255, 0); // Yellow background
        Color gridColor = new Color(0, 0, 0);          // Black grid

        // Calculate the width and height of each grid cell
        int cellWidth = nX / gridCols;
        int cellHeight = nY / gridRows;

        // Fill the entire image with the background color
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                // Draw grid lines
                if (i % cellHeight == 0 || j % cellWidth == 0) { //If we're on either a horizontal or vertical grid line
                    imageWriter.writePixel(j, i, gridColor); //Color this pixel with the grid color
                } else {
                    imageWriter.writePixel(j, i, backgroundColor); // Color this pixel with the background color
                }
            }
        }

        // Write the image to a file
        imageWriter.writeToImage("test_image");
    }
}