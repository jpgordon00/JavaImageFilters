import java.util.Random;

/**
 * @author Jacob Gordon
 * @version 1.0
 * @date 5/31/19
 **/
public class MatrixUtils <E> {

    /**
     * Copies the given matrix to a new Matrix.
     *
     * @param matrix to copy contents from.
     * @return new matrix with the same contents.
     */
    public static Integer[][] copyMatrixInt(Integer[][] matrix) {
        Integer[][] newM = new Integer[matrix.length][matrix[0].length];
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                newM[y][x] = matrix[y][x];
            }
        }
        return newM;
    }

    /**
     * Copies the given matrix to a new Matrix.
     *
     * @param matrix to copy contents from.
     * @return new matrix with the same contents.
     */
    public static Double[][] copyMatrixDouble(Double[][] matrix) {
        Double[][] newM = new Double[matrix.length][matrix[0].length];
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                newM[y][x] = matrix[y][x];
            }
        }
        return newM;
    }

    /**
     * Copies the given matrix to a new Matrix.
     *
     * @param matrix to copy contents from.
     * @return new matrix with the same contents.
     */
    public static Float[][] copyMatrixFloat(Float[][] matrix) {
        Float[][] newM = new Float[matrix.length][matrix[0].length];
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                newM[y][x] = matrix[y][x];
            }
        }
        return newM;
    }

    /**
     * Returns the largest dimension of any n x f matrix.
     *
     * @param matrix to check matrix of.
     * @return length of the matrix.
     */
    public static int getMatrixLength(Integer[][] matrix) {
        int q = 0;
        for (int y = 0; y < matrix.length; y++) {
            if (matrix[y].length > q) q = matrix[y].length;
        }
        return q;
    }

    /**
     * Gets a string to represent the given matrix.
     *
     * @param matrix to print to console.
     * @return String printed.
     */
    public static String getStringFromMatrix(Integer[][] matrix) {
        StringBuilder sb = new StringBuilder("");
        int l = getMatrixLength(matrix);
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < l; x++) {
                sb.append(matrix[y][x]);
                if (x + 1 < matrix[0].length) sb.append(", ");
            }
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Prints the given matrix of any size to the console.
     *
     * @param matrix to print.
     */
    public static void printMatrix(Integer[][] matrix) {
        int l = getMatrixLength(matrix);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < l; i++) sb.append("*");
        System.out.println(sb.toString());
        System.out.println(getStringFromMatrix(matrix));
        System.out.println(sb.toString());
    }

    /**
     * Prints the given matrix of any size to the console.
     *
     * @param matrix to print.
     * @param str    additional string to preface the log with.
     */
    public static void printMatrix(Integer[][] matrix, String str) {
        int l = getMatrixLength(matrix);
        StringBuilder sb = new StringBuilder();
        System.out.println(str);
        for (int i = 0; i < l; i++) sb.append("*");
        System.out.println(sb.toString());
        System.out.println(getStringFromMatrix(matrix));
        System.out.println(sb.toString());
    }

    /**
     * Builds a matrix that is f x n with 0's.
     * @param f
     * @param n
     * @return
     */
    public static Integer[][] build(int f, int n) {
        Integer[][] matrix = new Integer[f][n];
        for (int y = 0; y < matrix.length; y++) {
            for (int x =0; x < matrix[0].length; x++) {
                matrix[y][x] = 0;
            }
        }
        return matrix;
    }

    /**
     * Builds a f x n matrix with the inputted number.
     * @param f
     * @param n
     * @param q
     * @return
     */
    public static Integer[][] build(int f, int n, int q) {
        Integer[][] matrix = new Integer[f][n];
        for (int y = 0; y < matrix.length; y++) {
            for (int x =0; x < matrix[0].length; x++) {
                matrix[y][x] = q;
            }
        }
        return matrix;
    }

    /**
     * Builds a f x n matrix with random numbers from min -> max.
     * @param f
     * @param n
     * @param min
     * @param max
     * @return
     */
    public static Integer[][] build(int f, int n, int min, int max) {
        Random r = new Random();
        Integer[][] matrix = new Integer[f][n];
        for (int y = 0; y < matrix.length; y++) {
            for (int x =0; x < matrix[0].length; x++) {
                matrix[y][x] = r.nextInt(max - min) + min;
            }
        }
        return matrix;
    }

    /**
     * Padds the matrix in 0's of 'p' padding.
     * @param matrix to rebuild and pad.
     * @param padding how many layers  of padding in the matrix.
     * @return new matrix.
     */
    public static Integer[][] padMatrix(Integer[][] matrix, int padding) {
        if (padding <= 0) return matrix;
        Integer[][] m = MatrixUtils.copyMatrixInt(matrix);

       /*
       Each time we pad we need to make a matrix to fit the current layer
       of padding only.
       After padding for this layer is done, we consider this now our matrix
       and go again.
        */
        Integer[][] newMatrix = new Integer[m.length + 2][m[0].length + 2];
        for (int i = 0; i < padding; i++) {
            //set 1 layer of padding for matrix 'm'
            //do top and bottom rows of padding
            int l = getMatrixLength(m);
            for (int x = 0; x < newMatrix[0].length; x++) {
                newMatrix[0][x] = 0;
                newMatrix[newMatrix.length - 1][x] = 0;
            }
            //do left and right columns of padding
            for (int y = 0; y < newMatrix.length; y++) {
                newMatrix[y][0] = 0;
                newMatrix[y][newMatrix.length - 1] = 0;
            }
            //fill new matrix
            for (int y = 0; y < m.length; y++) {
                for (int x = 0; x < l; x++) {
                    newMatrix[y + 1][x + 1] = m[y][x];
                }
            }
            //fix null values
            for (int y = 0; y < newMatrix.length; y++) {
                for (int x = 0; x < newMatrix[0].length; x++) {
                    if (newMatrix[y][x] == null) newMatrix[y][x] = 0;
                }
            }

            //if has one morel loop (i + 1 < padding) then:
            // make m = newMatrix
            //rebuild newMatrix
            if (i + 1 < padding) {
                m = newMatrix;
                newMatrix = new Integer[m.length + 2][m[0].length + 2];
            }
        }
        return newMatrix;
    }

    /**
     * Flattens the 2-dimensional matrix into a single dimension.
     * @param matrix to flatten.
     * @return new array of all values.
     */
    public Integer[] flattenMatrix(Integer[][] matrix) {
        Integer[] d = new Integer[matrix.length * matrix[0].length];
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                d[x * y] = matrix[y][x];
            }
        }
        return d;
    }
}
