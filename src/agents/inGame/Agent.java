package agents.inGame;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

import java.util.*;

public class Agent implements MarioAgent {
    private boolean[] actions = null;
    
    public int generation = 0;
    private Population[] populations = new Population[Config.NUM_POPULATION];

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        Config.rand = new Random(Config.RANDOM_SEED);
        actions = new boolean[MarioActions.numberOfActions()];
        actions[MarioActions.SPEED.getValue()] = true;
        actions[MarioActions.RIGHT.getValue()] = true;
        for (int i = 0; i < Config.NUM_POPULATION; i++) {
            Population pop = new Population();
            var config = pop.getConfig();
            config.VARIABLES.replace('p', new float[]{(float) (1 + i * 0.1), (float) (1 + i * 0.1), 0.2f});
            pop.setConfig(config);
            populations[i] = pop;
            if (Config.LOAD_DATA_PATH.equals("")) {
                populations[i].populate(generation);
            }
//        else {
//            population = Logger.getInstance().loadPopulation(Config.POPULATION_SIZE); // 
//        }
            populations[i].setUp(populations[i].getIndividuals(), model);
        }
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        generation++;
        boolean[] actionCache = actions.clone();

        List<Individual> individuals = new ArrayList<>();
        for (int i = 0; i < Config.NUM_POPULATION; i++) {
            populations[i] = new Population();
            populations[i].populate(generation);
            populations[i].setUp(populations[i].getIndividuals(), model);
            for (int j = 0; j < Config.NUM_GENERATION; j++) {
                populations[i].evolve(model);
            }
            individuals.addAll(populations[i].getIndividuals());
        }
        individuals.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
        Individual best = individuals.getFirst();
        if (best.fitness < 0) {
            actions = new boolean[]{false, false, false, false, false};
        }
        else{
            actions = best.nextActions(best.getGeneration());
        }
        Logger.getInstance().logIndividual(best); // 
        log(best);

        // if Mario is on the ground and the jump action is enabled, disable it
//        if (model.isMarioOnGround() && actionCache[MarioActions.JUMP.getValue()]) {
//            actions[MarioActions.JUMP.getValue()] = false;
//        }
        
        return actions;
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
