package agents.EAController;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

import java.util.*;
import java.util.function.Function;

public class Agent extends Individual implements MarioAgent {
    private boolean[] actions = null;
    FeedForwardNetwork network;
    MarioForwardModel modelCache;
    
    public int generation = 0;
    public int index = 0;
    public Agent[] parents = new Agent[2];

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
        actions[MarioActions.RIGHT.getValue()] = true;
        actions[MarioActions.SPEED.getValue()] = true;
    }

    double[] mapCache = null;
    
    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
/*        int[][] array = new int[16][16];

        for (int r = 1; r <= 16; r++) {
            for (int c = 1; c <= 16; c++) {
                array[r - 1][c - 1] = c * 100 + r;
            }
        }*/
        
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
        
        modelCache = model;
        return actions;
    }

    public void init(int generation, int index, Agent[] parents) {
        this.generation = generation;
        this.index = index;
        this.parents = parents;
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
                inputs[i * width + j] = map[marioPos[0] + i][startCol + j];
            }
        }
        return inputs;
    }

    @Override
    public void calculateFitness() {
        
    }
}
