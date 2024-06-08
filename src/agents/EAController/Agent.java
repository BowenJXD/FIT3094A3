package agents.EAController;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

import java.util.*;

public class Agent implements MarioAgent {
    private boolean[] actions = null;
    
    public int step = 0;
    private Population[] populations = new Population[Config.NUM_POPULATION];
    private Individual bestCache = null;
    private MarioForwardModel modelCache = null;
    public float tickRate = 2;
    public float tick;

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        step = 0;
        bestCache = null;
        tickRate = model.getLevelFloatDimensions()[0] / 1500;
        
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
                populations[i].populate(step);
            }
//        else {
//            population = Logger.getInstance().loadPopulation(Config.POPULATION_SIZE); // 
//        }
            populations[i].setUp(populations[i].getIndividuals(), model);
        }
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        step++;
        if (bestCache != null && tick < tickRate) {
            tick++;
            actions = bestCache.nextActions(step);
            return actions;
        }
        tick -= tickRate;
        
        Individual best = null;
        for (int g = 0; g < 10; g++) {
            List<Individual> individuals = new ArrayList<>();
            for (int i = 0; i < Config.NUM_POPULATION; i++) {
                Population pop = new Population();
                var config = pop.getConfig();
                int x = Math.max(100 - i * Config.INTERVAL(), 1);
                int y = Math.max(100 - x, 1);
                config.SCORE = new int[]{(int) 1E6, -Integer.MAX_VALUE, x, y};
                pop.setConfig(config);
                populations[i] = pop;
                populations[i].populate(step);
                populations[i].setUp(populations[i].getIndividuals(), model);
                for (int j = 0; j < Config.NUM_GENERATION; j++) {
                    populations[i].evolve(model);
                }
                individuals.add(populations[i].getIndividuals().getFirst());
            }
            individuals.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
            best = individuals.getFirst();
            if (best.getFitness() > 0) {
/*                if (g > 0){
                    System.out.println("Retry successful at generation: " + g);
                }*/
                if (bestCache != null && bestCache.step < step - Config.BESTCACHE_AGE) {
                    bestCache = null;
                }
                break;
            }
        }
        
        if (bestCache != null && best.getFitness() < bestCache.getFitness()) {
            best = bestCache;
        }
        else{
            bestCache = best;
        }
        actions = best.nextActions(step);
        // Logger.getInstance().logIndividual(best); // 
        // log(best);
        modelCache = model;

        return actions;
    }
    
    public void log(Individual individual){
        StringBuilder sb = new StringBuilder();
        sb.append("Step ").append(step);
        sb.append(" : ").append(individual.getName());
        sb.append(" | Fitness: ").append(individual.getFitness());
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
    public void train(MarioForwardModel model) {
        
    }
}
