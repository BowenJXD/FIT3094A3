package agents.inGame;

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
        if (bestCache != null && bestCache.step < step - Config.BESTCACHE_AGE) {
            bestCache = null;
        }
        boolean[] actionCache = actions.clone();

        Individual best = null;
        for (int g = 0; g < 100; g++) {
            List<Individual> individuals = new ArrayList<>();
            for (int i = 0; i < Config.NUM_POPULATION; i++) {
                Population pop = new Population();
                var config = pop.getConfig();
                config.SCORE = i == 0? new int[]{(int) 1E6, -Integer.MAX_VALUE, 100, 1} : new int[]{(int) 1E6, -Integer.MAX_VALUE, 1, 100};
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
                break;
            }
            bestCache = best;
        }
        
        if (bestCache != null && best.getFitness() < bestCache.getFitness()) {
            best = bestCache;
        }
        actions = best.nextActions(step);
        Logger.getInstance().logIndividual(best); // 
        log(best);

        // if Mario is on the ground and the jump action is enabled, disable it
//        if (model.isMarioOnGround() && actionCache[MarioActions.JUMP.getValue()]) {
//            actions[MarioActions.JUMP.getValue()] = false;
//        }
        bestCache = best;
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
    public void train(MarioForwardModel model) {}
}
