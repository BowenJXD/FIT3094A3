package agents.EAController;

import engine.core.MarioForwardModel;
import engine.helper.MarioActions;

import java.util.List;

public class Config {
    
    public static final int[] INPUT_DIMENSIONS = {4, 8, 10}; // (start_row, width, height) where width and height are in number of tiles
    public static final int[] OUTPUT_ACTIONS = {MarioActions.RIGHT.getValue(), MarioActions.JUMP.getValue()}; // Actions to be taken by the agent
    public static final int[] HIDDEN_LAYERS = {5};
    public static final List<Integer> LAYER_NODES = List.of(INPUT_DIMENSIONS[1] * INPUT_DIMENSIONS[2], HIDDEN_LAYERS[0], OUTPUT_ACTIONS.length);
    
    public static final int RANDOM_SEED = 42;
    
    public static final String HIDDEN_ACTIVATION = "relu";
    public static final String OUTPUT_ACTIVATION = "sigmoid";
    
    public static final String LEVEL_STRING = "1-1";
    
    public static final int POPULATION_SIZE = 10;
    public static final int MAX_GENERATIONS = 100;
    public static final int TOURNAMENT_SIZE = 5;
    public static final double CROSSOVER_PROBABILITY = 0.9;
    public static final double MUTATION_PROBABILITY = 0.1;
}