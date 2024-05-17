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

    private static Evolution instance;
    private Evolution() {
        Config.rand = new Random(Config.RANDOM_SEED);
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
        Config.rand = new Random(Config.RANDOM_SEED);
        
        // population
        if (!Config.LOAD_DATA_PATH.equals("")) {
            var agents = Logger.getInstance().loadAgents(Config.POPULATION_SIZE);
            if (!agents.isEmpty()) {
                population = new Population(agents);
                printPopulation(0);
            } else {
                population = new Population();
            }
        }
        else {
            population = new Population();
        }
        runAgents(population.getAgents());

        for (int g = 1; g < Config.MAX_GENERATIONS; g++) {
            // Selection
            List<Agent> parents = Selection.tournamentSelection(population, Config.POPULATION_SIZE);
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
                    Mutation.randomUniformMutation(child1Chromosome, Config.MUTATION_PROBABILITY, -1, 1);
                    Mutation.randomUniformMutation(child2Chromosome, Config.MUTATION_PROBABILITY, -1, 1);
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
            runAgents(offspring);
            // population.agents = Selection.elitism(population, Config.ELITISM_SIZE);
            population.agents.addAll(0, offspring);
        }
        
        Logger.getInstance().logBestfits();
    }

    public void runAgents(List<Agent> agents) {
        for (Agent agent : agents) {
            // run 1-1 only
            if (!Config.RUN_ALL_LEVELS) {
                MarioResult result = PlayLevel.runLevel(agent, Config.LEVEL_STRING, Config.VISUALS);
                double percentage = result.getCompletionPercentage();
                double remainingTime = result.getRemainingTime();
                agent.setFitness(percentage * 100 + remainingTime / 1000);
            }
            else {
                // run all levels
                List<MarioResult> result = PlayLevel.runLevels(agent, Config.VISUALS);
                List<Double> fitnesses = new ArrayList<>();
                for (MarioResult r : result) {
                    double percentage = r.getCompletionPercentage();
                    double remainingTime = r.getRemainingTime();
                    fitnesses.add(percentage * 100 + remainingTime / 1000);
                }
                agent.setFitness(fitnesses.stream().mapToDouble(Double::doubleValue).average().orElse(0));
            }
        }
    }

    public void printPopulation(int g) {
        StringBuilder sb = new StringBuilder();
        sb.append("G").append(g).append(": \t");
        for (Agent agent : population.getAgents()) {
            sb.append(agent.getAgentId()).append(": ");
            sb.append(String.format("%.2f", agent.getFitness()));
            sb.append(" | ");
        }
        System.out.println(sb.toString());
    }

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
