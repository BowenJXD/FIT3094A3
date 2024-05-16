import agents.EAController.*;
import engine.core.MarioForwardModel;
import engine.core.MarioRender;
import engine.core.MarioResult;
import util.Logger;

import java.lang.reflect.Executable;
import java.util.*;
import java.util.function.Function;

public class Evolution {
    private Population population;
    Random rand = new Random();

    private static Evolution instance;
    private Evolution() {
        rand = new Random(Config.RANDOM_SEED);
    }

    public static Evolution getInstance() {
        if (instance == null) {
            instance = new Evolution();
        }
        return instance;
    }
    
    public static void main(String[] args) {
        Evolution evolution = Evolution.getInstance();
        evolution.run();
    }
    
    public void run() {
        // population
        population = new Population();
        for (int g = 1; g < Config.MAX_GENERATIONS; g++) {
            for (Agent agent : population.getAgents()) {
                MarioResult result = PlayLevel.runLevel(agent, Config.LEVEL_STRING);
                double percentage = result.getCompletionPercentage();
                if (agent.getFitness() < percentage) {
                    agent.setFitness(percentage);
                }
            }

            population.agents.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));

            // Selection
            List<Agent> parents = Selection.elitismSelection(population, Config.POPULATION_SIZE);
            population.agents = parents;
            printPopulation(g);
            Logger.getInstance().logChromosome(parents.getFirst());
            
            // Crossover and mutation
            List<Agent> offspring = new ArrayList<>();
            for (int i = 0; i < parents.size(); i += 2) {
                Agent parent1 = parents.get(i);
                Agent parent2 = parents.get(i + 1);
                
                Map<String, double[][]> child1Params = deepCopyMap(parent1.getChromosome());
                Map<String, double[][]> child2Params = deepCopyMap(parent2.getChromosome());
                for (String key : parent1.getChromosome().keySet()) {
                    double[][] child1Chromosome, child2Chromosome;
                    child1Chromosome = Crossover.uniformBinaryCrossover(child1Params.get(key), child1Params.get(key));
                    child2Chromosome = Crossover.uniformBinaryCrossover(child1Params.get(key), child1Params.get(key));
                    /*Mutation.uniformMutationWithRespectToBestIndividual(rand, child1Params.get(key), bestAgent.getChromosome().get(key), Config.MUTATION_PROBABILITY);
                    Mutation.uniformMutationWithRespectToBestIndividual(rand, child2Params.get(key), bestAgent.getChromosome().get(key), Config.MUTATION_PROBABILITY);*/
                    Mutation.randomUniformMutation(rand, child1Chromosome, Config.MUTATION_PROBABILITY, -1, 1);
                    Mutation.randomUniformMutation(rand, child2Chromosome, Config.MUTATION_PROBABILITY, -1, 1);
                    child1Params.put(key, child1Chromosome);
                    child2Params.put(key, child2Chromosome);
                }
                Agent child1 = new Agent(child1Params);
                Agent child2 = new Agent(child2Params);
                child1.init(g, offspring.size(), new Agent[]{parent1, parent2});
                child2.init(g, offspring.size() + 1, new Agent[]{parent1, parent2});
                offspring.add(child1);
                offspring.add(child2);
            }

            // Survivor selection
            // if no improvement, remove all parents
            if (Math.abs(parents.getFirst().getFitness() - parents.getLast().getFitness()) < 0.01) {
                population.agents.clear();
            } else {
                population.agents.subList(Config.POPULATION_SIZE / 2, Config.POPULATION_SIZE).clear();
            }
            population.agents.addAll(offspring);
        }
        
    }
    
    public void printPopulation(int g) {
        StringBuilder sb = new StringBuilder();
        sb.append("Generation: ").append(g).append(": ");
        for (Agent agent : population.getAgents()) {
            sb.append("(");
            sb.append(agent.getAgentId()).append(": ");
            sb.append(String.format("%.2f", agent.getFitness()));
            sb.append(") ");
            sb.append("> ");
        }
        System.out.println(sb.toString());
    }
    
/*    public void train(MarioForwardModel model) {
        // Evolutionary algorithm training loop
        int maxGenerations = 1000;
        int populationSize = 10;
        for (int generation = 0; generation < maxGenerations; generation++) {
            // Evaluate fitness of agents
            for (Agent agent : population.getAgents()) {
                double[] chromosome = agent.getChromosome();
                agent
                agent.setFitness(evaluateFitness(new FeedForwardNetwork(layerNodes, hiddenActivation, outputActivation, "uniform", 42, chromosome), model, timer));
            }

            // Selection
            List<Agent> parents = Selection.tournamentSelection(population, populationSize, 5);

            // Crossover and mutation
            List<Agent> offspring = new ArrayList<>();
            for (int i = 0; i < parents.size(); i += 2) {
                Agent parent1 = parents.get(i);
                Agent parent2 = parents.get(i + 1);
                double[] child1Chromosome, child2Chromosome;
                child1Chromosome = Crossover.simulatedBinaryCrossover(parent1.getChromosome().get("W1"), parent2.getChromosome().get("W1"), 1.0);
                child2Chromosome = Crossover.simulatedBinaryCrossover(parent2.getChromosome().get("W1"), parent1.getChromosome().get("W1"), 1.0);
                Map<String, double[][]> child1Params = new HashMap<>(parent1.getChromosome());
                child1Params.put("W1", child1Chromosome);
                Map<String, double[][]> child2Params = new HashMap<>(parent2.getChromosome());
                child2Params.put("W1", child2Chromosome);
                Mutation.gaussianMutation(child1Params.get("W1"), 0.1, null, null, 0.5);
                Mutation.gaussianMutation(child2Params.get("W1"), 0.1, null, null, 0.5);
                offspring.add(new Agent(child1Params));
                offspring.add(new Agent(child2Params));
            }

            // Survivor selection
            population.getAgents().addAll(offspring);
            population.getAgents().sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
            population.getAgents().subList(populationSize, population.getAgents().size()).clear();
        }
    }
    }*/

    public static Map<String, double[][]> deepCopyMap(Map<String, double[][]> originalMap) {
        if (originalMap == null) {
            return null;
        }

        Map<String, double[][]> newMap = new HashMap<>();
        for (Map.Entry<String, double[][]> entry : originalMap.entrySet()) {
            String key = entry.getKey();
            double[][] value = entry.getValue();
            double[][] copiedValue = deepCopyDoubleArray(value);
            newMap.put(key, copiedValue);
        }
        return newMap;
    }

    public static double[][] deepCopyDoubleArray(double[][] original) {
        if (original == null) {
            return null;
        }

        double[][] copiedArray = new double[original.length][];
        for (int i = 0; i < original.length; i++) {
            copiedArray[i] = original[i].clone();
        }
        return copiedArray;
    }

}
