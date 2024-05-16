package util;

import engine.core.MarioAgentEvent;
import engine.core.MarioEvent;
import engine.core.MarioResult;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class Matrix {
    public static double[][] randomUniformMatrix(int rows, int cols, Random rand) {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rand.nextDouble() * 2 - 1; // Uniform distribution between -1 and 1
            }
        }
        return matrix;
    }

    public static double[] dot(double[][] matrix, double[] vector) {
        return Arrays.stream(matrix).mapToDouble(row -> {
            return IntStream.range(0, row.length).mapToDouble(i -> row[i] * vector[i]).sum();
        }).toArray();
    }

    public static double[] add(double[] vector1, double[][] vector2) {
        return IntStream.range(0, vector1.length).mapToDouble(i -> vector1[i] + vector2[i][0]).toArray();
    }
}
