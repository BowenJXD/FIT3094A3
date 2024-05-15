package agents.EAController;

import java.util.Random;

public abstract class Individual {
    private double fitness;
    private double[] chromosome;

    public abstract void calculateFitness();

    public double getFitness() {
        return fitness;
    }

    public double[] getChromosome() {
        return chromosome;
    }

    public abstract void encodeChromosome();

    public abstract void decodeChromosome();
}