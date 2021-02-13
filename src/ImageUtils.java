import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * This class contains utilities for image to matrix conversion in both directions
 *  and tools to apply basic RGB image convolution given a filter or separate
 * filters for each channel.
 * convolutions, strided convolutions, padded convolutions
 * max pooling of any matrix, max pooling with strides
 * flattening of a matrix.
 *
 * This class contains the following basic filters, however any given filter can be used.
 * horizontal ("h"), vertical ("h"), scharr v/h ("scharr v/h"), sobel v/h ("sobel v/h")
 *
 * @author Jacob Gordon
 * @version 1.0
 * @date 5/27/19
 **/
public class ImageUtils {

    /**
     * Returns true if the file with the given path (relative to src)
     * has a image file (ending in .png or .jpg).
     * @param path of given image.
     * @return true if file is an image.
     */
    public static boolean hasValidImageFile(String path) {
        boolean good = false;
        if (path.toLowerCase().endsWith(".png") || path.toLowerCase().endsWith(".jpg")) good = true;
        if (!good) return false;
        try {
            File f = new File(path);
            BufferedImage bi = ImageIO.read(f);
            if (!f.exists() || bi == null) return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Gets a BufferedImage object from a path name.
     * @param path title to check, relative to 'src' folder.
     * @return image at path, or null if doesn't exist
     */
    public static BufferedImage getImage(String path) {
        File f = null;
        BufferedImage img = null;
        try{
            f = new File(path);
            if (!f.exists()) return null;
            boolean good = false;
            if (f.getName().toLowerCase().endsWith(".png") || f.getName().toLowerCase().endsWith(".jpg")) good = true;
            if (!good) return null;
            img = ImageIO.read(f);
        }catch(Exception e){
            return null;
        }
        return img;
    }

    /**
     * Reads the image as three arrays of input colors (RGB).
     * @param img image to read.
     * @return triple integer array of RGB values.
     */
    public static Integer[][][] getRGBMatrixFromImage(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        Integer[][][] data = new Integer[3][h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color c = new Color(img.getRGB(x, y));
                    data[0][y][x] = c.getRed();
                    data[1][y][x] = c.getGreen();
                    data[2][y][x] = c.getBlue();
            }
        }
        return data;
    }

    /**
     * Preforms RGB convolution using the given matrix and filter
     * for all three channels.
     * Matrix is padded to fit the original size.
     * @param img to pad.
     * @param filter
     * @return
     */
    public static Integer[][] preformRGBConvolutionPadded(Integer[][][] img, Double[][] filter) {
        int p = (filter.length - 1) / 2;
        p = 1;
        Integer[][][] m = new Integer[3][img[0].length][img[0][0].length];
        for (int i = 0; i < 3; i++) {
            m[i] = MatrixUtils.padMatrix(img[i], p);
        }
        return preformRGBConvolution(m, filter);
    }

    /**
     * Preforms RGB convolution using the given matrix and a
     * seperate filterfor each channel.
     * Matrix is padded to fit the original size.
     * @param img to pad.
     * @param filter
     * @return
     */
    public static Integer[][] preformRGBConvolutionPadded(Integer[][][] img, Double[][][] filter) {
        int p = (filter.length - 1) / 2;
        Integer[][][] m = new Integer[3][img[0].length][img[0][0].length];
        for (int i = 0; i < 3; i++) {
            m[i] = MatrixUtils.padMatrix(img[i], p);
        }
        return preformRGBConvolution(m, filter);
    }

    /**
     * Preforms a strided convolution for the given image and single filter
     * for all RGB channels.
     * @param img image to convolute.
     * @param filter to use during convolution.
     * @return the new image.
     */
    public static Integer[][] preformRGBConvolutionStrided(Integer[][][] img, Double[][] filter, int s) {
        Integer[][] r = operationConvolution(img[0], filter, s);
        Integer[][] g = operationConvolution(img[1], filter, s);
        Integer[][] b = operationConvolution(img[2], filter, s);
        Integer[][] output = new Integer[r.length][r[0].length];
        for (int y = 0; y < output.length; y++) {
            for (int x = 0; x < output[0].length; x++) {
                output[y][x] = r[y][x] + g[y][x] + b[y][x];
            }
        }
        return output;
    }

    /**
     * Preforms a strided convolution for the given image and 3 filters
     * for each RGB channel.
     * @param img image to convolute.
     * @param filter to use during convolution.
     * @return the new image.
     */
    public static Integer[][] preformRGBConvolutionStrided(Integer[][][] img, Double[][][] filter, int s) {
        Integer[][] r = operationConvolution(img[0], filter[0], s);
        Integer[][] g = operationConvolution(img[1], filter[1], s);
        Integer[][] b = operationConvolution(img[2], filter[2], s);
        Integer[][] output = new Integer[r.length][r[0].length];
        for (int y = 0; y < output.length; y++) {
            for (int x = 0; x < output[0].length; x++) {
                output[y][x] = r[y][x] + g[y][x] + b[y][x];
            }
        }
        return output;
    }

    /**
     * Preforms a convolution on an image, a given filter and for a
     * given stride and padding.
     * @param img imabge to convolute.
     * @param filter to use during convolution
     * @param s stride of the convolution
     * @param p padding to use.
     * @return
     */
    public static Integer[][] preformRGBConvolutionStridedPadded(Integer[][][] img, Double[][] filter, int s, int p) {
        Integer[][][] m = new Integer[3][img[0].length][img[0][0].length];
        for (int i = 0; i < 3; i++) {
            m[i] = MatrixUtils.padMatrix(img[i], p);
        }
        img = m;
        Integer[][] r = operationConvolution(img[0], filter, s);
        Integer[][] g = operationConvolution(img[1], filter, s);
        Integer[][] b = operationConvolution(img[2], filter, s);
        Integer[][] output = new Integer[r.length][r[0].length];
        for (int y = 0; y < output.length; y++) {
            for (int x = 0; x < output[0].length; x++) {
                output[y][x] = r[y][x] + g[y][x] + b[y][x];
            }
        }
        return output;
    }

    /**
     * Preforms a convolution on an image, a given filter and for a
     * given stride and padding.
     * @param img imabge to convolute.
     * @param filter to use during convolution
     * @param s stride of the convolution
     * @param p padding to use.
     * @return
     */
    public static Integer[][] preformRGBConvolutionStridedPadded(Integer[][][] img, Double[][][] filter, int s, int p) {
        Integer[][][] m = new Integer[3][img[0].length][img[0][0].length];
        for (int i = 0; i < 3; i++) {
            m[i] = MatrixUtils.padMatrix(img[i], p);
        }
        img = m;
        Integer[][] r = operationConvolution(img[0], filter[0], s);
        Integer[][] g = operationConvolution(img[1], filter[1], s);
        Integer[][] b = operationConvolution(img[2], filter[2], s);
        Integer[][] output = new Integer[r.length][r[0].length];
        for (int y = 0; y < output.length; y++) {
            for (int x = 0; x < output[0].length; x++) {
                output[y][x] = r[y][x] + g[y][x] + b[y][x];
            }
        }
        return output;
    }

    /**
     * Preforms a convolution operation for each RGB channel with
     * the same given img an filter.
     * @param img RGB matrix representing three channels of FxK matrix's.
     * @param filter given Kernal/Image filter (is usually 3 x 3)
     * @return output matrix. The size will be (img.w - filter.w + 1)
     */
    public static Integer[][] preformRGBConvolution(Integer[][][] img, Double[][] filter) {
        Integer[][] r = operationConvolution(img[0], filter);
        Integer[][] g = operationConvolution(img[1], filter);
        Integer[][] b = operationConvolution(img[2], filter);
        Integer[][] output = new Integer[r.length][r[0].length];
        for (int y = 0; y < output.length; y++) {
            for (int x = 0; x < output[0].length; x++) {
                output[y][x] = r[y][x] + g[y][x] + b[y][x];
            }
        }
        return output;
    }

    /**
     * Preforms a convolution operation for each RGB channel with
     * the three given filters on a given image.
     * @param img RGB matrix representing three channels of FxK matrix's.
     * @param filter given Kernal/Image filters, one for each channel (is usually 3 x 3)
     * @return output matrix. The size will be (img.w - filter.w + 1)
     */
    public static Integer[][] preformRGBConvolution(Integer[][][] img, Double[][][] filter) {
        Integer[][] r = operationConvolution(img[0], filter[0]);
        Integer[][] g = operationConvolution(img[1], filter[1]);
        Integer[][] b = operationConvolution(img[2], filter[2]);
        Integer[][] output = new Integer[r.length][r[0].length];
        for (int y = 0; y < output.length; y++) {
            for (int x = 0; x < output[0].length; x++) {
                output[y][x] = r[y][x] + g[y][x] + b[y][x];
            }
        }
        return output;
    }

    /**
     * Preforms a convolution operation for a gray-scaled matrix and
     * a given filter.
     * @param img matrix of gray-scaled values to represent a single-chanelled image.
     * @param filter to preform convolution with. Should be odd numbered, is usually 3 x 3.
     * @return resulting array from convolution (size is f - k + 1).
     */
    public static Integer[][] operationConvolution(Integer[][] img, Double[][] filter) {
        //(F x F) * (K x K) = F - K + 1
        //(6 x 6) * (3 x 3) = 4 x 4
        int i, j;
        Integer[][] output = new Integer[i = img.length - filter.length + 1][j = img[0].length - filter[0].length + 1];
        for (int y = 0; y < i; y = y + 1) {
            for (int x = 0; x < j; x = x + 1) {
                //preform convoltion as follows:
                //start at (0, 0) look (kSize, kSize) on img
                //(x, y) ----> (x + kSize, y + kSize) on img
                //add to sum: for each (x, y) multiply by filter[y][x]
                //output[y][x] = sum
                int sum = 0;
                for (int yy = 0; yy < filter.length; yy++) {
                    for (int xx = 0; xx < filter[0].length; xx++) {
                        sum += img[y + yy][x + xx] * filter[yy][xx];
                    }
                }
                output[y][x] = sum;
            }
        }
        return output;
    }

    /**
     * Preforms a convolution operation for a gray-scaled matrix and
     * a given filter and stride.
     * @param img matrix of gray-scaled values to represent a single-chanelled image.
     * @param filter to preform convolution with. Should be odd numbered, is usually 3 x 3.
     * @param s stride of convolution, or the length of movement on each convolution step.
     * @return resulting array from convolution (size is f - k + 1).
     */
    public static Integer[][] operationConvolution(Integer[][] img, Double[][] filter, int s) {
        if (s < 1) s = 1;
        int fSizeY = img.length;
        int kSizeY = filter.length;
        int fSizeX = img[0].length;
        int kSizeX = filter[0].length;
        int p = 0;
        int i, j;
        Integer[][] output = new Integer[i = ((img.length - filter.length) / s) + 1][j = ((img[0].length - filter[0].length) / s) + 1];

        //(F x F) * (K x K) = F - K + 1
        //(6 x 6) * (3 x 3) = 4 x 4
        for (int y = 0; y < i; y = y + 1) {
            for (int x = 0; x < j; x = x + 1) {
                //preform convoltion as follows:
                //start at (0, 0) look (kSize, kSize) on img
                //(x, y) ----> (x + kSize, y + kSize) on img
                //add to sum: for each (x, y) multiply by filter[y][x]
                //output[y][x] = sum
                int sum = 0;
                for (int yy = 0; yy < kSizeY; yy++) {
                    for (int xx = 0; xx < kSizeX; xx++) {
                        sum += img[(y * 2) + yy][(x * 2) + xx] * filter[yy][xx];
                    }
                }
                output[y][x] = sum;
            }
        }
        return output;
    }

    /**
     * Checks all values in a matrix and fixes teh values so that
     * 0 < v < 255
     * @param input matrix with gray-scaled values.
     * @return matrix of same size with fixes values;
     */
    public static Integer[][] fixRGBValues(Integer[][] input) {
        Integer[][] output = new Integer[input.length][input.length];
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input.length; x++) {
                int k = input[y][x];
                if (k < 0) k = -k;
                if (k > 255) k = 255;
                output[y][x] = k;
            }
        }
        return output;
    }

    /**
     * Fixes a given RGB value to be wthin the range 0 -> 255.
     * @param v value to check given length of.
     * @return new value that is in range.
     */
    public static int fixRGBValue(int v) {
        if (v < 0) v = -v;
        if (v > 255) v = 255;
        return v;
    }

    /**
     * Preforms the max pooling operation with a given w and h.
     * @param img image to preform max pooling on.
     * @param w with of da man
     * @param h height of da man
     * @return
     */
    public static Integer[][] preformRGBMaxPooling(Integer[][][] img, int w, int h) {
        Integer[][] r = operationMaxPooling(img[0], w, h);
        Integer[][] g = operationMaxPooling(img[1], w, h);
        Integer[][] b = operationMaxPooling(img[2], w, h);
        Integer[][] output = new Integer[r.length][r[0].length];
        for (int y = 0; y < output.length; y++) {
            for (int x = 0; x < output[0].length; x++) {
                output[y][x] = r[y][x] + g[y][x] + b[y][x];
            }
        }
        return output;
    }

    /**
     * Preforms the max pooling operation with a given w and h.
     * @param img image to preform max pooling on.
     * @param w with of da man
     * @param h height of da man
     * @param s stride
     * @return
     */
    public static Integer[][] preformRGBMaxPooling(Integer[][][] img, int w, int h, int s) {
        Integer[][] r = operationMaxPooling(img[0], w, h, s);
        Integer[][] g = operationMaxPooling(img[1], w, h, s);
        Integer[][] b = operationMaxPooling(img[2], w, h, s);
        Integer[][] output = new Integer[r.length][r[0].length];
        for (int y = 0; y < output.length; y++) {
            for (int x = 0; x < output[0].length; x++) {
                output[y][x] = r[y][x] + g[y][x] + b[y][x];
            }
        }
        return output;
    }

    /**
     * Preforms the max pooling operation with a given w and h.
     * @param img image to preform max pooling on.
     * @param w with of da man
     * @param h height of da man
     * @return
     */
    public static Double[][] preformRGBMaxPooling(Double[][][] img, double w, double h) {
        Double[][] r = operationMaxPooling(img[0], w, h);
        Double[][] g = operationMaxPooling(img[1], w, h);
        Double[][] b = operationMaxPooling(img[2], w, h);
        Double[][] output = new Double[r.length][r[0].length];
        for (double y = 0; y < output.length; y++) {
            for (double x = 0; x < output[0].length; x++) {
                output[y][x] = r[y][x] + g[y][x] + b[y][x];
            }
        }
        return output;
    }

    /**
     * Preforms the max pooling operation with a given w and h.
     * @param img image to preform max pooling on.
     * @param w with of da man
     * @param h height of da man
     * @param s stride
     * @return
     */
    public static Double[][] preformRGBMaxPooling(Double[][][] img, int w, int h, int s) {
        Double[][] r = operationMaxPooling(img[0], w, h, s);
        Double[][] g = operationMaxPooling(img[1], w, h, s);
        Double[][] b = operationMaxPooling(img[2], w, h, s);
        Double[][] output = new Double[r.length][r[0].length];
        for (double y = 0; y < output.length; y++) {
            for (double x = 0; x < output[0].length; x++) {
                output[y][x] = r[y][x] + g[y][x] + b[y][x];
            }
        }
        return output;
    }

    /**
     * Preforms the max pooling operation on a given Matrix.
     * @param matrix
     * @param w
     * @param h
     * @return
     */
    public static Integer[][] operationMaxPooling(Integer[][] matrix, int w, int h) {
        int i, j;
        Integer[][] output = new Integer[i = matrix.length - h + 1][j = matrix[0].length - w + 1];
        for (int y = 0; y < i; y = y + 1) {
            for (int x = 0; x < j; x = x + 1) {
                //preform convoltion as follows:
                //start at (0, 0) look (kSize, kSize) on img
                //(x, y) ----> (x + kSize, y + kSize) on img
                //add to sum: for each (x, y) multiply by filter[y][x]
                //output[y][x] = sum
                int largest = 0;
                for (int yy = 0; yy < h; yy++) {
                    for (int xx = 0; xx < w; xx++) {
                        int l = matrix[y + yy][x + xx];
                        if (l > largest) largest = l;
                    }
                }
                output[y][x] = largest;
            }
        }
        return output;
    }

    /**
     * Preforms the max pooling operation on a given Matrix and stride.
     * @param matrix
     * @param w
     * @param h
     * @return
     */
    public static Integer[][] operationMaxPooling(Integer[][] matrix, int w, int h, int s) {
        if (s < 1) s = 1;
        int fSizeY = matrix.length;
        int kSizeY = h;
        int fSizeX = matrix[0].length;
        int kSizeX = w;
        int p = 0;
        int i, j;
        Integer[][] output = new Integer[i = ((matrix.length - h) / s) + 1][j = ((matrix[0].length - w) / s) + 1];

        //(F x F) * (K x K) = F - K + 1
        //(6 x 6) * (3 x 3) = 4 x 4
        for (int y = 0; y < i; y = y + 1) {
            for (int x = 0; x < j; x = x + 1) {
                //preform convoltion as follows:
                //start at (0, 0) look (kSize, kSize) on img
                //(x, y) ----> (x + kSize, y + kSize) on img
                //add to sum: for each (x, y) multiply by filter[y][x]
                //output[y][x] = sum
                int largest = 0;
                for (int yy = 0; yy < h; yy++) {
                    for (int xx = 0; xx < w; xx++) {
                        int l = matrix[y + yy][x + xx];
                        if (l > largest) largest = l;
                    }
                }
                output[y][x] = largest;
            }
        }
        return output;
    }

    /**
     * Preforms the max pooling operation on a given Matrix and stride.
     * @param matrix
     * @param w
     * @param h
     * @return
     */
    public static Double[][] operationMaxPooling(Double[][] matrix, int w, int h, int s) {
        if (s < 1) s = 1;
        double i, j;
        Double[][] output = new Double[i = ((matrix.length - h) / s) + 1][j = ((matrix[0].length - w) / s) + 1];

        //(F x F) * (K x K) = F - K + 1
        //(6 x 6) * (3 x 3) = 4 x 4
        for (int y = 0; y < i; y = y + 1) {
            for (int x = 0; x < j; x = x + 1) {
                //preform convoltion as follows:
                //start at (0, 0) look (kSize, kSize) on img
                //(x, y) ----> (x + kSize, y + kSize) on img
                //add to sum: for each (x, y) multiply by filter[y][x]
                //output[y][x] = sum
                double largest = 0;
                for (int yy = 0; yy < h; yy++) {
                    for (int xx = 0; xx < w; xx++) {
                        double l = matrix[y + yy][x + xx];
                        if (l > largest) largest = l;
                    }
                }
                output[y][x] = largest;
            }
        }
        return output;
    }

    /**
     * Preforms the max pooling operation on a given Matrix.
     * @param matrix
     * @param w
     * @param h
     * @return
     */
    public static Double[][] operationMaxPooling(Double[][] matrix, int w, int h) {
        int i, j;
        Double[][] output = new Double[i = matrix.length - h + 1][j = matrix[0].length - w + 1];
        for (int y = 0; y < i; y = y + 1) {
            for (int x = 0; x < j; x = x + 1) {
                //preform convoltion as follows:
                //start at (0, 0) look (kSize, kSize) on img
                //(x, y) ----> (x + kSize, y + kSize) on img
                //add to sum: for each (x, y) multiply by filter[y][x]
                //output[y][x] = sum
                double largest = 0;
                for (int yy = 0; yy < h; yy++) {
                    for (int xx = 0; xx < w; xx++) {
                        double l = matrix[y + yy][x + xx];
                        if (l > largest) largest = l;
                    }
                }
                output[y][x] = largest;
            }
        }
        return output;
    }


    /**
     * Preforms the average pooling operation on a given Matrix.
     * @param matrix
     * @param w
     * @param h
     * @return
     */
    public static Double[][] operationAvgPooling(Double[][] matrix, int w, int h) {
        int i, j;
        Double[][] output = new Double[i = matrix.length - h + 1][j = matrix[0].length - w + 1];
        for (int y = 0; y < i; y = y + 1) {
            for (int x = 0; x < j; x = x + 1) {
                double avg = 0;
                for (int yy = 0; yy < h; yy++) {
                    for (int xx = 0; xx < w; xx++) {
                        double l = matrix[y + yy][x + xx];
                        avg += l;
                    }
                }
                output[y][x] = avg / (h * w);
            }
        }
        return output;
    }

    /**
     * Preforms the max pooling operation on a given Matrix and stride.
     * @param matrix
     * @param w
     * @param h
     * @param s
     * @return
     */
    public static Double[][] operationAvgPooling(Double[][] matrix, int w, int h, int s) {
        if (s < 1) s = 1;
        int fSizeY = matrix.length;
        int kSizeY = h;
        int fSizeX = matrix[0].length;
        int kSizeX = w;
        int p = 0;
        int i, j;
        Double[][] output = new Double[i = ((matrix.length - h) / s) + 1][j = ((matrix[0].length - w) / s) + 1];
        for (int y = 0; y < i; y = y + 1) {
            for (int x = 0; x < j; x = x + 1) {
                double avg = 0;
                for (int yy = 0; yy < h; yy++) {
                    for (int xx = 0; xx < w; xx++) {
                        double l = matrix[y + yy][x + xx];
                        avg += l;
                    }
                }
                output[y][x] = avg / (h * w);
            }
        }
        return output;
    }

    /**
     * HashMap of String (filter name) and 3x3 matrix (kernal filter)
     */
    private static HashMap<String, Double[][]> kernalMap;


    /**
     * Retrieve's a 3x3 matrix based on a given String.
     * @param filterName name of filter.
     * @return filter associated with name (if any)
     */
    public final static Double[][] getFiler(String filterName) {
        //setup kernals
        if (kernalMap == null) {
            kernalMap = new HashMap<>();
            //vertical
            Double[][] vertical = {{1.0, 0.0, -1.0}, {1.0, 0.0, -1.0}, {1.0, 0.0, -1.0}};
            kernalMap.put("v", vertical);
            //horizontal
            Double[][] horizontal = {{1.0, 1.0, 1.0}, {0.0, 0.0, 0.0}, {-1.0, -1.0, -1.0}};
            kernalMap.put("h", horizontal);
            //sobel vertical
            Double[][] sobel_v = {{1.0, 0.0, -1.0}, {2.0, 0.0, -2.0}, {1.0, 0.0, -1.0}};
            kernalMap.put("sobel v", sobel_v);
            //sobel horizontal
            Double[][] sobel_h = {{0.0, 2.0, 1.0}, {0.0, 0.0, 0.0}, {-1.0, -2.0, -1.0}};
            kernalMap.put("sobel h", sobel_h);
            //scharr vertical
            Double[][] scharr_v = {{3.0, 0.0, -3.0}, {10.0, 0.0, -10.0}, {3.0, 0.0, -3.0}};
            kernalMap.put("scharr v", scharr_v);
            //scharr horizontal
            Double[][] scharr_h = {{3.0, 10.0, 3.0}, {0.0, 0.0, 0.0}, {-3.0, -10.0, -3.0}};
            kernalMap.put("scharr h", scharr_h);
        }
        if (!kernalMap.containsKey(filterName)) return null;
        return kernalMap.get(filterName);
    }

    /**
     * Adds the given filter and String to the filter map.
     * @param str name of filter.
     * @param filter matrix of doubles that represent a filter (3 x 3)
     */
    public static void addFilter(String str, Double[][] filter) {
        //setup kernals
        if (kernalMap == null) {
            kernalMap = new HashMap<>();
            //vertical
            Double[][] vertical = {{1.0, 0.0, -1.0}, {1.0, 0.0, -1.0}, {1.0, 0.0, -1.0}};
            kernalMap.put("v", vertical);
            //horizontal
            Double[][] horizontal = {{1.0, 1.0, 1.0}, {0.0, 0.0, 0.0}, {-1.0, -1.0, -1.0}};
            kernalMap.put("h", horizontal);
            //sobel vertical
            Double[][] sobel_v = {{1.0, 0.0, -1.0}, {2.0, 0.0, -2.0}, {1.0, 0.0, -1.0}};
            kernalMap.put("sobel v", sobel_v);
            //sobel horizontal
            Double[][] sobel_h = {{0.0, 2.0, 1.0}, {0.0, 0.0, 0.0}, {-1.0, -2.0, -1.0}};
            kernalMap.put("sobel h", sobel_h);
            //scharr vertical
            Double[][] scharr_v = {{3.0, 0.0, -3.0}, {10.0, 0.0, -10.0}, {3.0, 0.0, -3.0}};
            kernalMap.put("scharr v", scharr_v);
            //scharr horizontal
            Double[][] scharr_h = {{3.0, 10.0, 3.0}, {0.0, 0.0, 0.0}, {-3.0, -10.0, -3.0}};
            kernalMap.put("scharr h", scharr_h);
        }
        if (!kernalMap.containsKey(str)) {
            kernalMap.put(str, filter);
        }
    }

    /**
     * Creates a File of the given path as a .png from any
     * given Matrix.
     * @param fileName to write the corresponding image to.
     * @param imageRGB matrix, this is the data of the image.
     * @return File corresponding with image.
     * @throws IOException
     */
    public static final File writeFileFromRGBMatrix(String fileName, Integer[][] imageRGB) throws IOException {
        BufferedImage writeBackImage = new BufferedImage(imageRGB[0].length, imageRGB.length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < imageRGB.length; i++) {
            for (int j = 0; j < imageRGB[i].length; j++) {
                Color color = new Color(fixRGBValue(imageRGB[i][j]),
                        fixRGBValue(imageRGB[i][j]),
                        fixRGBValue(imageRGB[i][j]));
                writeBackImage.setRGB(j, i, color.getRGB());
            }
        }
        File outputFile = new File(fileName);
        ImageIO.write(writeBackImage, "png", outputFile);
        return outputFile;
    }

}
