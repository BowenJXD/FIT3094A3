package agents.EAController;

import engine.core.MarioForwardModel;

import java.util.Random;

public class Individual {
    private double fitness;
    private double[] chromosome;

    public void calculateFitness(MarioForwardModel model){
        fitness = model.getCompletionPercentage();
    }

    public double getFitness() {
        return fitness;
    }

    public double[] getChromosome() {
        return chromosome;
    }
}