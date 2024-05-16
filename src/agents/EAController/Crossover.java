package agents.EAController;

import java.util.Random;

public class Crossover {

    public static double[][] uniformBinaryCrossover(double[][] parent1, double[][] parent2) {
        Random rand = Config.rand;
        double[][] offspring1 = parent1.clone();
        double[][] offspring2 = parent2.clone();

        for (int i = 0; i < parent1.length; i++) {
            for (int j = 0; j < parent1[i].length; j++) {
                if (rand.nextDouble() > 0.5) {
                    double temp = offspring1[i][j];
                    offspring1[i][j] = offspring2[i][j];
                    offspring2[i][j] = temp;
                }
            }
        }

        return offspring1;
    }
}