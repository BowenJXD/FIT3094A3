package agents.inGame;

import agents.EAController.Config;

import java.util.Random;

public class Crossover {

    public static int[][] uniformBinaryCrossover(int[] parent1, int[] parent2) {
        Random rand = Config.rand;
        int[] offspring1 = new int[parent1.length];
        int[] offspring2 = new int[parent1.length];
        for (int i = 0; i < parent1.length; i++) {
            if (rand.nextBoolean()) {
                offspring1[i] = parent1[i];
                offspring2[i] = parent2[i];
            } else {
                offspring1[i] = parent2[i];
                offspring2[i] = parent1[i];
            }
        }
        return new int[][]{offspring1, offspring2};
    }
}