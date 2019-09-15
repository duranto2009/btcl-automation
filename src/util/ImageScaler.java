package util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;


public class ImageScaler {
    /**
     * Scale an image while preserving aspect ratio
     *
     * @param args Name of image file to be scaled (testing only)
     *
     */
    public static void main(String[] args) throws Exception {
        
    }

    /**
     *
     * @param image The image to be scaled
     * @param imageType Target image type, e.g. TYPE_INT_RGB
     * @param newWidth The required width
     * @param newHeight The required width
     *
     * @return The scaled image
     */
    public static BufferedImage scaleImage(BufferedImage image, int imageType,
            int newWidth, int newHeight) {
        // Make sure the aspect ratio is maintained, so the image is not distorted
        //double thumbRatio = (double) newWidth / (double) newHeight;
        //int imageWidth = image.getWidth(null);
       // int imageHeight = image.getHeight(null);
       // double aspectRatio = (double) imageWidth / (double) imageHeight;

        /*if (thumbRatio < aspectRatio) {
            newHeight = (int) (newWidth / aspectRatio);
        } else {
            newWidth = (int) (newHeight * aspectRatio);
        }
*/
        // Draw the scaled image
        BufferedImage newImage = new BufferedImage(newWidth, newHeight,
                imageType);
        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, newWidth, newHeight, null);

        return newImage;
    }
}