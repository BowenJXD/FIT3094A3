package agents.EAController;

import java.util.Random;

public class Mutation {
    public static void randomUniformMutation(double[][] chromosome, double probMutation, double low, double high) {
        Random rand = Config.rand;
        for (int i = 0; i < chromosome.length; i++) {
            for (int j = 0; j < chromosome[i].length; j++) {
                if (rand.nextDouble() < probMutation) {
                    chromosome[i][j] = rand.nextDouble() * (high - low) + low;
                }
            }
        }
    }

    public static void uniformMutationWithRespectToBestIndividual(double[][] chromosome, double[][] bestChromosome, double probMutation) {
        Random rand = Config.rand;
        for (int i = 0; i < chromosome.length; i++) {
            for (int j = 0; j < chromosome[i].length; j++) {
                if (rand.nextDouble() < probMutation) {
                    chromosome[i][j] = bestChromosome[i][j];
                }
            }
        }
    }
}