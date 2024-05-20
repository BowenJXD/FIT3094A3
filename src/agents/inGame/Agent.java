package agents.inGame;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

import java.util.*;

public class Agent implements MarioAgent {
    private boolean[] actions = null;
    
    public int generation = 0;
    private List<Individual> population;

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        Config.rand = new Random(Config.RANDOM_SEED);
        actions = new boolean[MarioActions.numberOfActions()];
        actions[MarioActions.SPEED.getValue()] = true;
        actions[MarioActions.RIGHT.getValue()] = true;
        population = new ArrayList<>();
        if (Config.LOAD_DATA_PATH.equals("")) {
            populate();
        }
        else {
            population = Logger.getInstance().loadPopulation(Config.POPULATION_SIZE); // 
        }
        setUp(population, model);
    }

    private void setUp(List<Individual> population, MarioForwardModel model) {
        for (Individual individual : population) {
            individual.advanceModel(model);
            individual.calculateFitness();
        }
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        generation++;
        boolean[] actionCache = actions.clone();
        
        List<Individual> offspring = crossover(population);
        mutate(offspring);
        setUp(offspring, model);
        passAway();
        population.addAll(offspring);
        population = select(population);
        Individual best = population.getFirst();
        Logger.getInstance().logIndividual(best); // 
        actions = best.nextActions(generation);
        log(best);

        // if Mario is on the ground and the jump action is enabled, disable it
        if (model.isMarioOnGround() && actionCache[MarioActions.JUMP.getValue()]) {
            actions[MarioActions.JUMP.getValue()] = false;
        }
        
        return actions;
    }
    
    public void populate(){
        Random rand = Config.rand;
        for (int i = 0; i < Config.POPULATION_SIZE; i++) {
            int[] chromosome = new int[Config.LENGTH];
            for (int j = 0; j < Config.LENGTH; j++) {
                chromosome[j] = rand.nextInt(Config.WIDTH);
            }
            Individual individual = new Individual(chromosome);
            individual.init(generation, i, new String[]{});
            population.add(individual);
        }
    }
    
    public List<Individual> crossover(List<Individual> parent){
        List<Individual> offspring = new ArrayList<>();
        for (int i = 0; i < parent.size(); i += 2) {
            Individual parent1 = parent.get(i);
            Individual parent2 = parent.get(i + 1);
            int[][] offspringChromosomes = Crossover.uniformBinaryCrossover(parent1.getChromosome(), parent2.getChromosome());
            Individual offspring1 = new Individual(offspringChromosomes[0]);
            Individual offspring2 = new Individual(offspringChromosomes[1]);
            offspring1.init(generation, i, new String[]{parent1.getName(), parent2.getName()});
            offspring2.init(generation, i + 1, new String[]{parent2.getName(), parent1.getName()});
            offspring.add(offspring1);
            offspring.add(offspring2);
        }
        return offspring;
    }
    
    public List<Individual> mutate(List<Individual> population){
        for (Individual individual : population) {
            Mutation.randomUniformMutation(individual.getChromosome(), Config.MUTATION_PROBABILITY, 0, Config.WIDTH);
        }
        return population;
    }
    
    public List<Individual> select(List<Individual> population){
        population = Selection.tournamentSelection(population, Config.POPULATION_SIZE);
        return population;
    }
    
    public void passAway(){
        for (int i = population.size() - 1; i >= 0; i--) {
            if (population.get(i).getGeneration() <= generation - Config.LENGTH) {
                population.remove(i);
            }
        }
    }
    
    public void sort(){
        population.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
    }
    
    public void log(Individual individual){
        StringBuilder sb = new StringBuilder();
        sb.append(" | G: ").append(generation);
        sb.append(" | I: ").append(individual.getName());
        sb.append(" | F: ").append(individual.getFitness());
        sb.append(" | Actions: ");
        var actions = individual.getActions(0);
        for (int i = 0; i < actions.length; i++) {
            if (actions[i]) {
                sb.append(MarioActions.values()[i].toString()).append(" ");
            }
        }
        System.out.println(sb.toString());
    }
    
    @Override
    public String getAgentName() {
        return "EAController";
    }

    @Override
    public void train(MarioForwardModel model) {}
}
