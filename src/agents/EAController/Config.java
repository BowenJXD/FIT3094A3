package agents.EAController;

import engine.core.MarioForwardModel;
import engine.helper.MarioActions;

import java.util.List;
import java.util.Random;

public class Config {
    
    public static final int[] INPUT_DIMENSIONS = {5, 8, 10}; // (start_row, width, height) where width and height are in number of tiles
    public static final int[] OUTPUT_ACTIONS = {
            // MarioActions.DOWN.getValue(),
            // MarioActions.SPEED.getValue(),
            // MarioActions.LEFT.getValue(), 
            MarioActions.RIGHT.getValue(),
            MarioActions.JUMP.getValue()}; // Actions to be taken by the agent
    public static final List<Integer> LAYER_NODES = List.of(INPUT_DIMENSIONS[1] * INPUT_DIMENSIONS[2], 4, 2, OUTPUT_ACTIONS.length);
    
    public static final int RANDOM_SEED = 42;
    public static Random rand = new Random(RANDOM_SEED);
    
    public static final String HIDDEN_ACTIVATION = "relu";
    public static final String OUTPUT_ACTIVATION = "sigmoid";
    
    public static final String LEVEL_STRING = "1-1";
    
    public static final int POPULATION_SIZE = 10;
    public static final int MAX_GENERATIONS = 100;
    public static final int TOURNAMENT_SIZE = 4;
    public static final int ELITISM_SIZE = 4;
    public static final double CROSSOVER_PROBABILITY = 0.9;
    public static final double MUTATION_PROBABILITY = 0.1;
    
    public static final boolean VISUALS = true;
    public static final boolean RUN_ALL_LEVELS = false;
    public static final String LOAD_DATA_PATH = "";
}