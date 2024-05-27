package agents.EAController;

import engine.core.MarioForwardModel;

import java.util.ArrayList;
import java.util.List;

public class Population {
    protected List<Individual> individuals = new ArrayList<>();
    protected int generation = 0;
    protected int step = 0;
    protected PopulationConfig config = new PopulationConfig();

    public Population() {
    }

    public Population(List<Individual> agents) {
        this.individuals = agents;
    }
    
    public void populate(int step){
        this.step = step;
        for (int i = 0; i < config.POPULATION_SIZE; i++) {
            Individual individual = null;
            if (config.INDIVIDUAL_CLASS.equals("UniformIndividual")) {
                individual = new UniformIndividual(config);
            }
            else if (config.INDIVIDUAL_CLASS.equals("CosIndividual")) {
                individual = new CosIndividual(config);
            }
            individual.init(step, generation, i, new String[]{});
            individual.setConfig(config);
            individuals.add(individual);
        }
    }
    
    public void evolve(MarioForwardModel model){
        generation++;
        
        List<Individual> offspring = crossover(individuals);
        mutate(offspring);
        setUp(offspring, model);
        passAway();
        individuals.addAll(offspring);
        individuals = select(individuals);
//        individuals = individuals.subList(0, 1);
//        individuals.addAll(offspring.subList(0, config.POPULATION_SIZE - 1));
//        individuals.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
    }

    public List<Individual> crossover(List<Individual> parent){
        List<Individual> offspring = new ArrayList<>();
        for (int i = 0; i < parent.size() - 1; i ++) {
            Individual parent1 = parent.get(i);
            Individual parent2 = parent.get(i + 1);
            Individual[] offsprings = parent1.crossover(parent2);
            for (int j = 0; j < offsprings.length; j++) {
                offsprings[j].init(step, generation, i + j, new String[]{parent1.getName(), parent2.getName()});
                offsprings[j].setConfig(config);
                offspring.add(offsprings[j]);
            }
        }
        return offspring;
    }

    public List<Individual> mutate(List<Individual> individuals){
        for (Individual individual : individuals) {
            individual.mutate();
        }
        return individuals;
    }

    public List<Individual> select(List<Individual> individuals){
        individuals = Selection.elitism(individuals, config.ELITISM_SIZE);
        return individuals;
    }

    public void passAway(){
        for (int i = individuals.size() - 1; i >= 0; i--) {
            if (individuals.get(i).getGeneration() <= generation - config.LENGTH) {
                individuals.remove(i);
            }
        }
    }

    public void setUp(List<Individual> individuals, MarioForwardModel model) {
        for (Individual individual : individuals) {
            individual.advanceModel(model);
            individual.calculateFitness();
        }
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }
    
    public int getGeneration() {
        return generation;
    }
    
    public Individual getBest(){
        return individuals.getFirst();
    }
    
    public PopulationConfig getConfig(){
        return config;
    }

    public void setConfig(PopulationConfig config) {
        this.config = config;
    }
}