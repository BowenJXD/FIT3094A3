package agents.EAController;

import engine.core.MarioForwardModel;

import java.util.Map;
import java.util.Random;

public abstract class Individual {
    protected double fitness;
    protected Map<String, double[][]> chromosome;

    public abstract void calculateFitness();

    public double getFitness() {
        return fitness;
    }
    
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public Map<String, double[][]> getChromosome() {
        return chromosome;
    }
}