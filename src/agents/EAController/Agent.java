package agents.EAController;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

import java.util.*;
import java.util.function.Function;

public class Agent extends Individual implements MarioAgent {
    private boolean[] actions = null;
    boolean[] actionCache = null;
    FeedForwardNetwork network;
    MarioForwardModel modelCache;
    
    public int generation = 0;
    public int index = 0;
    public String[] parents = new String[2];

    public Agent(){
        this.network = new FeedForwardNetwork(Config.LAYER_NODES, 
                FeedForwardNetwork.getActivationByName(Config.HIDDEN_ACTIVATION), 
                FeedForwardNetwork.getActivationByName(Config.OUTPUT_ACTIVATION), 
                "uniform", Config.RANDOM_SEED, null);
        chromosome = network.getParams();
    }
    
    public Agent(Map<String, double[][]> chromosome){
        this.network = new FeedForwardNetwork(Config.LAYER_NODES, 
                FeedForwardNetwork.getActivationByName(Config.HIDDEN_ACTIVATION), 
                FeedForwardNetwork.getActivationByName(Config.OUTPUT_ACTIVATION), 
                "uniform", Config.RANDOM_SEED, chromosome);
        this.chromosome = chromosome;
    } 

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        actions = new boolean[MarioActions.numberOfActions()];
        actionCache = actions.clone();
        actions[MarioActions.RIGHT.getValue()] = true;
        actions[MarioActions.SPEED.getValue()] = true;
    }

    double[] mapCache = null;
    
    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        int[][] map = model.getMarioSceneObservation(2);
        int[] marioPos = model.getMarioScreenTilePos();
        double[] inputs = getInputs(map, marioPos);
        double[] outputs = network.feedForward(inputs);
        
        if (!Arrays.equals(outputs, mapCache)) {
            if (mapCache == null) {
                mapCache = outputs;
            }
            else{
                mapCache = outputs;
            }
        }
        
        for (int i = 0; i < outputs.length; i++) {
            actions[Config.OUTPUT_ACTIONS[i]] = outputs[i] > 0.5;
        }
        
        // if Mario is on the ground and the jump action is enabled, disable it
        if (model.isMarioOnGround() && actionCache[MarioActions.JUMP.getValue()]) {
            actions[MarioActions.JUMP.getValue()] = false;
        }

        modelCache = model;
        actionCache = actions.clone();
        return actions;
    }

    public void init(int generation, int index, Agent[] parents) {
        this.generation = generation;
        this.index = index;
        if (parents != null) {
            this.parents[0] = parents[0] == null ? "null" : parents[0].getAgentId();
            this.parents[1] = parents[1] == null ? "null" : parents[1].getAgentId();
        }
    }
    
    @Override
    public String getAgentName() {
        return "EAController";
    }
    
    public String getAgentId() {
        return generation + "-" + index;
    }

    @Override
    public void train(MarioForwardModel model) {
    }
    
    public double[] getInputs(int[][] map, int[] marioPos) {
        int startCol = Config.INPUT_DIMENSIONS[0];
        int width = Config.INPUT_DIMENSIONS[2];
        int height = Config.INPUT_DIMENSIONS[1];
        double[] inputs = new double[width * height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // consider the size of the map being smaller than the input dimensions
                if (marioPos[0] + i >= map.length || startCol + j >= map[0].length) {
                    inputs[i * width + j] = 0;
                    continue;
                }
                inputs[i * width + j] = map[marioPos[0] + i][startCol + j];
            }
        }
        return inputs;
    }

    @Override
    public void calculateFitness() {
        
    }
}
