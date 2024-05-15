package agents.EAController;

import java.util.ArrayList;
import java.util.List;

public class Population {
    public List<Individual> individuals;

    public Population(List<Individual> individuals) {
        this.individuals = individuals;
    }

    public int getNumIndividuals() {
        return individuals.size();
    }

    public int getNumGenes() {
        return individuals.get(0).getChromosome().length;
    }

    public double getAverageFitness() {
        double sum = 0;
        for (Individual individual : individuals) {
            sum += individual.getFitness();
        }
        return sum / individuals.size();
    }

    public Individual getFittestIndividual() {
        Individual fittest = individuals.get(0);
        for (Individual individual : individuals) {
            if (individual.getFitness() > fittest.getFitness()) {
                fittest = individual;
            }
        }
        return fittest;
    }

    public void calculateFitness() {
        for (Individual individual : individuals) {
            individual.calculateFitness();
        }
    }

    public double getFitnessStd() {
        double sum = 0;
        double mean = getAverageFitness();
        for (Individual individual : individuals) {
            sum += Math.pow(individual.getFitness() - mean, 2);
        }
        return Math.sqrt(sum / individuals.size());
    }
}