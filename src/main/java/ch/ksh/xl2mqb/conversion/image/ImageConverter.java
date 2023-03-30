package ch.ksh.xl2mqb.conversion.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class ImageConverter {

    public static String convertIfNecessary(String image) {
        return isRemoteImage(image) ? image : convertToBase64(image);
    }

    public static boolean isRemoteImage(String image) {
        return image.startsWith("http");
    }

    private static String convertToBase64(String image) {
        try {
            byte[] imageContent = Files.readAllBytes(Path.of(image));
            String base64Encoded = Base64.getEncoder().encodeToString(imageContent);
            String prefix = "data:image/"+ getFileType(image) + ";base64,";
            return prefix + base64Encoded;
        } catch (IOException e) {
            e.printStackTrace();
            // TODO log exception
            return "";
        }
    }

    private static String getFileType(String image) {
        return image.substring(image.lastIndexOf(".")).toLowerCase();
    }
}
