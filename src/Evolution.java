import agents.EAController.*;
import engine.core.MarioForwardModel;

import java.lang.reflect.Executable;
import java.util.*;
import java.util.function.Function;

public class Evolution {
    private boolean[] actions = null;
    FeedForwardNetwork network;

    private Population population;
    private int populationSize = 100;
    private int maxGenerations = 100;
    private List<Integer> layerNodes = Arrays.asList(8, 4, 2); // Example: 4 inputs, 1 hidden layer with 5 neurons, and 3 outputs
    private Function<double[], double[]> hiddenActivation = FeedForwardNetwork.getActivationByName("relu");
    private Function<double[], double[]> outputActivation = FeedForwardNetwork.getActivationByName("sigmoid");

    private static Evolution instance;
    private Evolution() {
        
    }

    public static Evolution getInstance() {
        if (instance == null) {
            instance = new Evolution();
        }
        return instance;
    }
    
    public void run() {
        
    }
    
/*    public void train(MarioForwardModel model) {
        // Evolutionary algorithm training loop
        int maxGenerations = 1000;
        int populationSize = 10;
        for (int generation = 0; generation < maxGenerations; generation++) {
            // Evaluate fitness of individuals
            for (Individual individual : population.getIndividuals()) {
                double[] chromosome = individual.getChromosome();
                individual
                individual.setFitness(evaluateFitness(new FeedForwardNetwork(layerNodes, hiddenActivation, outputActivation, "uniform", 42, chromosome), model, timer));
            }

            // Selection
            List<Individual> parents = Selection.tournamentSelection(population, populationSize, 5);

            // Crossover and mutation
            List<Individual> offspring = new ArrayList<>();
            for (int i = 0; i < parents.size(); i += 2) {
                Individual parent1 = parents.get(i);
                Individual parent2 = parents.get(i + 1);
                double[] child1Chromosome, child2Chromosome;
                child1Chromosome = Crossover.simulatedBinaryCrossover(parent1.getChromosome().get("W1"), parent2.getChromosome().get("W1"), 1.0);
                child2Chromosome = Crossover.simulatedBinaryCrossover(parent2.getChromosome().get("W1"), parent1.getChromosome().get("W1"), 1.0);
                Map<String, double[][]> child1Params = new HashMap<>(parent1.getChromosome());
                child1Params.put("W1", child1Chromosome);
                Map<String, double[][]> child2Params = new HashMap<>(parent2.getChromosome());
                child2Params.put("W1", child2Chromosome);
                Mutation.gaussianMutation(child1Params.get("W1"), 0.1, null, null, 0.5);
                Mutation.gaussianMutation(child2Params.get("W1"), 0.1, null, null, 0.5);
                offspring.add(new Individual(child1Params));
                offspring.add(new Individual(child2Params));
            }

            // Survivor selection
            population.getIndividuals().addAll(offspring);
            population.getIndividuals().sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
            population.getIndividuals().subList(populationSize, population.getIndividuals().size()).clear();
        }
    }
    }*/
    
    

}
