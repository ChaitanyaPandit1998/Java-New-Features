import java.util.Random;

public class MatrixMultiplication {

    public static void main(String[] args) {
        int size = 128;
        double[][] A = generateRandomMatrix(size);
        double[][] B = generateRandomMatrix(size);
        // Start the timer
        long startTime = System.nanoTime();
        multiplyMatrices(A, B);
        long endTime = System.nanoTime();
        double timeTaken = (endTime - startTime) / 1e9;
        // Calculate GFLOPS
        double flops = 2.0 * size * size * size; // 2 floating-point operations per iteration
        double gflops = (flops / timeTaken) / 1e9;
        System.out.println("Time Taken(s): " + timeTaken);
        System.out.println("GFLOPS: " + gflops);
    }

    public static void multiplyMatrices(double[][] A, double[][] B) {
        int rowsA = A.length;
        int colsA = A[0].length;
        int rowsB = B.length;
        int colsB = B[0].length;

        if (colsA != rowsB) {
            throw new IllegalArgumentException("Matrix dimensions are incompatible for multiplication.");
        }

        double[][] result = new double[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }

    }

    private static double[][] generateRandomMatrix(int range){
        Random random = new Random();
        double[][] matrix = new double[range][range];

        // Generate 10 random integers between 1 and 100
        for (int i = 0; i < range; i++) {
            for(int j = 0 ; j < range; j++){
                matrix[i][j] = random.nextDouble();
            }
        }

        return matrix;
    }
}