package com.memorama;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageLoader {
    public static BufferedImage loadImage(String path) {
        try {
            // Use the class loader to get the input stream for the resource
            InputStream inputStream = ImageLoader.class.getClassLoader().getResourceAsStream(path);

            // Check if the resource was found
            if (inputStream == null) {
                throw new IOException(path);
            }

            // Read the image from the input stream
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            return null;
        }
    }
}
