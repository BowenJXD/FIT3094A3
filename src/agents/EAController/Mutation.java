package agents.EAController;

import java.util.Random;

public class Mutation {
    public static void randomUniformMutation(Random rand, double[][] chromosome, double probMutation, double low, double high) {
        for (int i = 0; i < chromosome.length; i++) {
            for (int j = 0; j < chromosome[i].length; j++) {
                if (rand.nextDouble() < probMutation) {
                    chromosome[i][j] = rand.nextDouble() * (high - low) + low;
                }
            }
        }
    }

    public static void uniformMutationWithRespectToBestIndividual(Random rand, double[][] chromosome, double[][] bestChromosome, double probMutation) {
        for (int i = 0; i < chromosome.length; i++) {
            for (int j = 0; j < chromosome[i].length; j++) {
                if (rand.nextDouble() < probMutation) {
                    chromosome[i][j] = bestChromosome[i][j];
                }
            }
        }
    }
}