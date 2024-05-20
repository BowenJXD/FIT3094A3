package agents.inGame;

import agents.EAController.Config;

import java.util.Random;

public class Mutation {
    public static void randomUniformMutation(int[] chromosome, double probMutation, double low, double high) {
        Random rand = agents.EAController.Config.rand;
        for (int i = 0; i < chromosome.length; i++) {
            if (rand.nextDouble() < probMutation) {
                chromosome[i] = (int) (low + (high - low) * rand.nextDouble());
            }
        }
    }

    public static void uniformMutationWithRespectToBestIndividual(int[] chromosome, int[] bestChromosome, double probMutation) {
        Random rand = Config.rand;
        for (int i = 0; i < chromosome.length; i++) {
            if (rand.nextDouble() < probMutation) {
                chromosome[i] = bestChromosome[i];
            }
        }
    }
}