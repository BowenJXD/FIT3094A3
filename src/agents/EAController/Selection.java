package agents.EAController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Selection {
    public static List<Individual> elitismSelection(Population population, int numIndividuals) {
        List<Individual> individuals = new ArrayList<>(population.individuals);
        individuals.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
        return individuals.subList(0, numIndividuals);
    }

    public static List<Individual> rouletteWheelSelection(Population population, int numIndividuals) {
        List<Individual> selection = new ArrayList<>();
        double wheel = population.individuals.stream().mapToDouble(Individual::getFitness).sum();
        Random rand = new Random();
        for (int i = 0; i < numIndividuals; i++) {
            double pick = rand.nextDouble() * wheel;
            double current = 0;
            for (Individual individual : population.individuals) {
                current += individual.getFitness();
                if (current > pick) {
                    selection.add(individual);
                    break;
                }
            }
        }
        return selection;
    }

    public static List<Individual> tournamentSelection(Population population, int numIndividuals, int tournamentSize) {
        List<Individual> selection = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < numIndividuals; i++) {
            List<Individual> tournament = new ArrayList<>();
            for (int j = 0; j < tournamentSize; j++) {
                tournament.add(population.individuals.get(rand.nextInt(population.getNumIndividuals())));
            }
            tournament.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
            selection.add(tournament.get(0));
        }
        return selection;
    }
}