package agents.EAController;

import java.util.Random;

public class Mutation {
    public static void gaussianMutation(double[] chromosome, double probMutation, double[] mu, double[] sigma, Double scale) {
        Random rand = new Random();
        for (int i = 0; i < chromosome.length; i++) {
            if (rand.nextDouble() < probMutation) {
                double gaussianMutation;
                if (mu != null && sigma != null) {
                    gaussianMutation = rand.nextGaussian() * sigma[i] + mu[i];
                } else {
                    gaussianMutation = rand.nextGaussian();
                }
                if (scale != null) {
                    gaussianMutation *= scale;
                }
                chromosome[i] += gaussianMutation;
            }
        }
    }

    public static void randomUniformMutation(double[] chromosome, double probMutation, double low, double high) {
        Random rand = new Random();
        for (int i = 0; i < chromosome.length; i++) {
            if (rand.nextDouble() < probMutation) {
                chromosome[i] = rand.nextDouble() * (high - low) + low;
            }
        }
    }

    public static void uniformMutationWithRespectToBestIndividual(double[] chromosome, double[] bestChromosome, double probMutation) {
        Random rand = new Random();
        for (int i = 0; i < chromosome.length; i++) {
            if (rand.nextDouble() < probMutation) {
                double uniformMutation = rand.nextDouble();
                chromosome[i] += uniformMutation * (bestChromosome[i] - chromosome[i]);
            }
        }
    }

    public static void cauchyMutation(double[] chromosome, double scale) {
        // Implementation goes here
    }

    public static void exponentialMutation(double[] chromosome, double xi, double probMutation) {
        Random rand = new Random();
        for (int i = 0; i < chromosome.length; i++) {
            if (rand.nextDouble() < probMutation) {
                double y = rand.nextDouble();
                double x;
                if (y <= 0.5) {
                    x = (1.0 / xi) * Math.log(2 * y);
                } else {
                    x = -(1.0 / xi) * Math.log(2 * (1.0 - y));
                }
                double delta = (xi / 2.0) * Math.exp(-xi * Math.abs(x));
                delta = (1.0 / xi) * delta;
                chromosome[i] += delta;
            }
        }
    }

    public static void mmoMutation(double[] chromosome, double probMutation) {
        Random rand = new Random();
        for (int i = 0; i < chromosome.length; i++) {
            if (rand.nextDouble() < probMutation) {
                double normal = rand.nextGaussian();
                double cauchy = getCauchyRandom();
                double delta = normal + cauchy;
                chromosome[i] += delta;
            }
        }
    }

    private static double getCauchyRandom() {
        Random rand = new Random();
        double u1 = rand.nextDouble();
        double u2 = rand.nextDouble();
        return Math.tan(Math.PI * (u1 - 0.5)) * Math.sqrt(-2 * Math.log(u2));
    }
}