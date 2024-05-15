package agents.EAController;

import java.util.Random;

public class Crossover {
    public static double[] simulatedBinaryCrossover(double[] parent1, double[] parent2, double eta) {
        Random rand = new Random();
        double[] gamma = new double[parent1.length];
        double[] chromosome1 = new double[parent1.length];
        double[] chromosome2 = new double[parent1.length];

        for (int i = 0; i < parent1.length; i++) {
            double randVal = rand.nextDouble();
            if (randVal <= 0.5) {
                gamma[i] = Math.pow(2 * randVal, 1.0 / (eta + 1));
            } else {
                gamma[i] = Math.pow(1.0 / (2.0 * (1.0 - randVal)), 1.0 / (eta + 1));
            }
            chromosome1[i] = 0.5 * ((1 + gamma[i]) * parent1[i] + (1 - gamma[i]) * parent2[i]);
            chromosome2[i] = 0.5 * ((1 - gamma[i]) * parent1[i] + (1 + gamma[i]) * parent2[i]);
        }

        return joinArrays(chromosome1, chromosome2);
    }

    public static double[] uniformBinaryCrossover(double[] parent1, double[] parent2) {
        Random rand = new Random();
        double[] offspring1 = parent1.clone();
        double[] offspring2 = parent2.clone();

        for (int i = 0; i < parent1.length; i++) {
            if (rand.nextDouble() > 0.5) {
                double temp = offspring1[i];
                offspring1[i] = offspring2[i];
                offspring2[i] = temp;
            }
        }

        return joinArrays(offspring1, offspring2);
    }

    public static double[] singlePointBinaryCrossover(double[] parent1, double[] parent2, String major) {
        Random rand = new Random();
        double[] offspring1 = parent1.clone();
        double[] offspring2 = parent2.clone();

        int rows = parent1.length;
        int cols = parent2.length / rows;
        int row = rand.nextInt(rows);
        int col = rand.nextInt(cols);

        if (major.equalsIgnoreCase("r")) {
            System.arraycopy(parent2, 0, offspring1, 0, row * cols);
            System.arraycopy(parent1, 0, offspring2, 0, row * cols);

            System.arraycopy(parent2, row * cols, offspring1, row * cols, col + 1);
            System.arraycopy(parent1, row * cols, offspring2, row * cols, col + 1);
        } else if (major.equalsIgnoreCase("c")) {
            System.arraycopy(parent2, 0, offspring1, 0, col);
            System.arraycopy(parent1, 0, offspring2, 0, col);

            System.arraycopy(parent2, col, offspring1, col, row + 1);
            System.arraycopy(parent1, col, offspring2, col, row + 1);
        }

        return joinArrays(offspring1, offspring2);
    }

    public static double[] joinArrays(double[] array1, double[] array2) {
        int length1 = array1.length;
        int length2 = array2.length;
        double[] result = new double[length1 + length2];

        System.arraycopy(array1, 0, result, 0, length1);
        System.arraycopy(array2, 0, result, length1, length2);

        return result;
    }
}