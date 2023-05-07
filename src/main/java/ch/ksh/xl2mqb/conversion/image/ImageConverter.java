package ch.ksh.xl2mqb.conversion.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

/**
 * This class is used to convert local images to a base64 encoded String
 *
 * @author Lorenzo Giuntini
 * @version 1.0
 */
public class ImageConverter {

    /**
     * Converts a given image to base64 if it is a local image
     *
     * @param image path to the image
     * @return if given image is a local image converted base64 encoding, else the given path
     */
    public static String convertIfNecessary(String image) {
        return isRemoteImage(image) ? image : convertToBase64(image);
    }

    /**
     * Checks weither or not the given image is a remote image or not.
     *
     * @param image path to the image
     * @return true if the given path is a http(s) url, otherwise false
     */
    public static boolean isRemoteImage(String image) {
        return image.startsWith("http");
    }

    /**
     * Converts a given image to base64 encoding.
     * Also adds the prefix for web base64 images e.g. (data:image/png;base64,)
     *
     * @param image path to the image
     * @return base64 encoded image
     */
    private static String convertToBase64(String image) {
        try {
            byte[] imageContent = Files.readAllBytes(Path.of(image));
            String base64Encoded = Base64.getEncoder().encodeToString(imageContent);
            String prefix = "data:image/" + getFileType(image) + ";base64,";
            return prefix + base64Encoded;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Gets the filetype of the given image e.g. (png or jpg)
     *
     * @param image path of the image
     * @return Filetype of the given image
     */
    private static String getFileType(String image) {
        return image.substring(image.lastIndexOf(".")).toLowerCase();
    }
}
