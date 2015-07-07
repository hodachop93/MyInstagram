package example.com.hop.myinstagram.camera;

import android.graphics.Bitmap;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Created by Hop on 03/07/2015.
 */
public class ImageProcessing {
    static double[][] blur = new double[][]{
            {0, 0, 1, 0, 0},
            {0, 1, 1, 1, 0},
            {1, 1, 1, 1, 1},
            {0, 1, 1, 1, 0},
            {0, 0, 1, 0, 0}
    };

    static double[][] find_edges = new double[][]{
            {-1, -1, -1},
            {-1, 8, -1},
            {-1, -1, -1}
    };

    static double[][] sharpen = new double[][]{
            {-1, -1, -1},
            {-1, 9, -1},
            {-1, -1, -1}
    };

    static double[][] motion_blur = new double[][]{
            {1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1}
    };

    public static Bitmap getBlurImage(int[][] pixels) {
        int[][] processed_pixels = filterImage(pixels, blur);
        byte[] result = convertToRGBByteArray(processed_pixels);

        Bitmap bmp = convertByteArrayToBitmap(result, pixels[0].length, pixels.length);
        return bmp;
    }

    public static Bitmap getMotionBlurImage(int[][] pixels) {
        int[][] processed_pixels = filterImage(pixels, motion_blur);
        byte[] result = convertToRGBByteArray(processed_pixels);

        Bitmap bmp = convertByteArrayToBitmap(result, pixels[0].length, pixels.length);
        return bmp;
    }

    public static Bitmap getSharpenImage(int[][] pixels) {
        int[][] processed_pixels = filterImage(pixels, sharpen);
        byte[] result = convertToRGBByteArray(processed_pixels);

        Bitmap bmp = convertByteArrayToBitmap(result, pixels[0].length, pixels.length);
        return bmp;
    }

    public static Bitmap getFindEdgesImage(int[][] pixels) {
        int[][] processed_pixels = filterImage(pixels, find_edges);
        byte[] result = convertToRGBByteArray(processed_pixels);

        Bitmap bmp = convertByteArrayToBitmap(result, pixels[0].length, pixels.length);
        return bmp;
    }

    public static Bitmap getGrayscaleImage(int[][] pixels) {
        int[][] processed_pixels = convertRGBToGrayscale(pixels);
        byte[] result = convertToRGBByteArray(processed_pixels);

        Bitmap bmp = convertByteArrayToBitmap(result, pixels[0].length, pixels.length);
        return bmp;
    }

    private static Bitmap convertByteArrayToBitmap(byte[] data, int imgWidth, int imgHeight) {
        Buffer buffer = ByteBuffer.wrap(data);
        Bitmap newbmp = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888);
        newbmp.copyPixelsFromBuffer(buffer);
        return newbmp;
    }

    private static int[][] convertRGBToGrayscale(int[][] pixels) {
        int imageWidth = pixels[0].length;
        int imageHeight = pixels.length;
        int[][] result;
        result = new int[imageHeight][imageWidth];
        for (int row = 0; row < imageHeight; row++)
            for (int col = 0; col < imageWidth; col++) {
                int blue = pixels[row][col] & 0xFF;
                int green = (pixels[row][col] >> 8) & 0xFF;
                int red = (pixels[row][col] >> 16) & 0xFF;
                int alpha = (pixels[row][col] >> 24);
                int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);

                int new_pixel = 0;
                new_pixel |= (alpha << 24) | (gray << 16) | (gray << 8) | gray;
                result[row][col] = new_pixel;

            }
        return result;
    }

    private static int[][] filterImage(int[][] pixels, double[][] filter) {
        int imageWidth = pixels[0].length;
        int imageHeight = pixels.length;

        int filter_width = filter[0].length;
        int filter_height = filter.length;

        int sum = 0;
        for (int row = 0; row < filter_height; row++)
            for (int col = 0; col < filter_width; col++) {
                sum += filter[row][col];
            }

        double factor = 1.0 / sum;
        double bias = 0.0;
        int[][] result = new int[imageHeight][imageWidth];
        for (int x = 0; x < imageHeight; x++)
            for (int y = 0; y < imageWidth; y++) {

                int red = 0, green = 0, blue = 0,alpha=0;

                for (int filterX = 0; filterX < filter_height; filterX++)
                    for (int filterY = 0; filterY < filter_width; filterY++) {

                        int imageX = (x - filter_height / 2 + filterX + imageHeight)
                                % imageHeight;
                        int imageY = (y - filter_width / 2 + filterY + imageWidth)
                                % imageWidth;

                        blue += (pixels[imageX][imageY] & 0xFF)
                                * filter[filterX][filterY];
                        green += ((pixels[imageX][imageY] >> 8) & 0xFF)
                                * filter[filterX][filterY];
                        red += ((pixels[imageX][imageY] >> 16) & 0xFF)
                                * filter[filterX][filterY];

                    }
                alpha = pixels[x][y] >> 24;
                red = Math.min(Math.abs((int) (factor * red + bias)), 255);
                green = Math.min(Math.abs((int) (factor * green + bias)), 255);
                blue = Math.min(Math.abs((int) (factor * blue + bias)), 255);

                int new_argb = 0;
                new_argb |= (alpha << 24) | (red << 16) | (green << 8) | (blue);
                result[x][y] = new_argb;
            }
        return result;
    }

    /**
     * Convert a 2D pixel array to 1D byte array
     *
     * @param pixels
     * @return
     */
    public static byte[] convertToRGBByteArray(int[][] pixels) {
        int imageWidth = pixels[0].length;
        int imageHeight = pixels.length;

        byte[] byteArrayImage = null;

        final int pixelLength = 4;
        int byteArrayImageLength = (imageWidth * imageHeight) * pixelLength;
        byteArrayImage = new byte[byteArrayImageLength];

        for (int bytecount = 0, row = 0, col = 0; bytecount < byteArrayImageLength; ) {
            int argb = pixels[row][col];
            // blue
            int blue = argb & 0xFF;
            byteArrayImage[bytecount++] = (byte) blue;
            // green
            int green = (argb >> 8) & 0xFF;
            byteArrayImage[bytecount++] = (byte) green;
            // red
            int red = (argb >> 16) & 0xFF;
            byteArrayImage[bytecount++] = (byte) red;
            //alpha = -1
            int alpha = -1;
            byteArrayImage[bytecount++] = (byte) alpha;

            col++;
            if (col == imageWidth) {
                col = 0;
                row++;
            }
        }
        return byteArrayImage;
    }


    /**
     * Chuyen 1 bitmap thanh 1 mang 2 chieu cac pixels
     *
     * @param bmp bitmap truyen veo
     * @return mang 2 chieu cac pixels
     */
    public static int[][] convertBitmapToPixels(Bitmap bmp) {
        int[][] result;
        int imageWidth = bmp.getWidth();
        int imageHeight = bmp.getHeight();
        result = new int[imageHeight][imageWidth];

        int size = bmp.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(size);
        bmp.copyPixelsToBuffer(buffer);
        byte[] byteArray = buffer.array();

        final int pixelLength = 4;
        for (int bytecount = 0, row = 0, col = 0; bytecount < byteArray.length; bytecount += pixelLength) {
            int argb = 0;
            argb |= (((int) byteArray[bytecount] & 0xff) << 16); // red
            argb |= (((int) byteArray[bytecount + 1] & 0xff) << 8); // green
            argb |= ((int) byteArray[bytecount + 2] & 0xff); // blue
            argb |= (((int) byteArray[bytecount + 3]) << 24); //alpha
            result[row][col] = argb;
            col++;
            if (col == imageWidth) {
                col = 0;
                row++;
            }

        }
        /*int lastpixel = result[0][0];
        int pixel = bmp.getPixel(0, 0);*/
        return result;
    }


}
