package agents.inGame;

import engine.helper.MarioActions;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Config {
    public static final int[] ACTIONS = {
            MarioActions.DOWN.getValue(),
            MarioActions.SPEED.getValue(),
            MarioActions.LEFT.getValue(), 
            MarioActions.RIGHT.getValue(),
            MarioActions.JUMP.getValue()};
    public static final boolean[] DEFAULT_ACTIONS = {false,true,false,true,false};
    public static final int LENGTH = 10;
    public static final int WIDTH = (int)Math.pow(2, ACTIONS.length);
    
    public enum ScoreType{
        Win, Death, X, Jump
    }
    
    public static final int[] SCORE = {(int) 1E6, -Integer.MAX_VALUE, 1, 5};

    public static final int RANDOM_SEED = 42;
    public static Random rand = new Random(RANDOM_SEED);
    
    public static final String LEVEL_STRING = "1-1";
    
    public static final int POPULATION_SIZE = 20;
    public static final int MAX_GENERATIONS = 100;
    public static final int TOURNAMENT_SIZE = 8;
    public static final int ELITISM_SIZE = 4;
    public static final double CROSSOVER_PROBABILITY = 0.9;
    public static final double MUTATION_PROBABILITY = 0.1;
    
    public static final boolean VISUALS = true;
    public static final boolean RUN_ALL_LEVELS = false;
    public static final String LOAD_DATA_PATH = "log/520-4.json";
    
    public static final int FLOOR_Y = 207;
}