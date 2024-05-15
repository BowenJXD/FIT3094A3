package agents.EAController;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

import java.util.*;
import java.util.function.Function;

public class Agent implements MarioAgent {
    private boolean[] actions = null;
    FeedForwardNetwork network;

    public Agent () {
        this.network = new FeedForwardNetwork(Config.LAYER_NODES, 
                FeedForwardNetwork.getActivationByName(Config.HIDDEN_ACTIVATION), 
                FeedForwardNetwork.getActivationByName(Config.OUTPUT_ACTIVATION), 
                "uniform", Config.RANDOM_SEED, null);
    }
    
    public Agent(Map<String, double[][]> chromosome){
        this.network = new FeedForwardNetwork(Config.LAYER_NODES, 
                FeedForwardNetwork.getActivationByName(Config.HIDDEN_ACTIVATION), 
                FeedForwardNetwork.getActivationByName(Config.OUTPUT_ACTIVATION), 
                "uniform", Config.RANDOM_SEED, chromosome);
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
        int[][] map = model.getMarioSceneObservation(2);
        
        double[] inputs = getInputs(map);
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
        
        return actions;
    }

    @Override
    public String getAgentName() {
        return "EAController";
    }

    @Override
    public void train(MarioForwardModel model) {
    }
    
    public double[] getInputs(int[][] map) {
        int startRow = Config.INPUT_DIMENSIONS[0];
        int width = Config.INPUT_DIMENSIONS[1];
        int height = Config.INPUT_DIMENSIONS[2];
        double[] inputs = new double[width * height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                inputs[i * height + j] = (double) map[startRow + i][j] / 100;
            }
        }
        return inputs;
    }
}
