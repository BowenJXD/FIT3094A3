package agents.inGame;

import engine.helper.MarioActions;

import java.util.Map;
import java.util.Random;

public class Config {

    public enum ScoreType{
        Win, Death, X, Y
    }
    
    // PopulationConfig
    
    public static final String INDIVIDUAL_CLASS = "CosIndividual";
    
    public static final int[] ACTIONS = {
//            MarioActions.DOWN.getValue(),
//            MarioActions.SPEED.getValue(),
            MarioActions.LEFT.getValue(), 
//            MarioActions.RIGHT.getValue(),
            MarioActions.JUMP.getValue()};
    public static final boolean[] DEFAULT_ACTIONS = {false,true,false,true,false};
    public static final int LENGTH = 20;
    public static final Map<Character, float[]> VARIABLES = Map.of(
            'a', new float[]{0, 1, 0},
            'h', new float[]{-LENGTH, LENGTH, 0},
            'k', new float[]{-1, 1, 0},
            'p', new float[]{0, 2, 0.2f});
    
    public static final int[] SCORE = {(int) 1E6, -Integer.MAX_VALUE, 100, 1};
    
    public static final int POPULATION_SIZE = 40;
    public static final int TOURNAMENT_SIZE = Math.round(POPULATION_SIZE * 0.4f);
    public static final int ELITISM_SIZE = 4;
    public static final double MUTATION_PROBABILITY = 0.1;

    // ------------------------------------------------

    public static final int NUM_POPULATION = 2;
    public static final int NUM_GENERATION = 20;
    public static final int BESTCACHE_AGE = LENGTH / 4;
    
    public static final int RANDOM_SEED = 42;
    public static Random rand = new Random(RANDOM_SEED);

    public static final String LEVEL_STRING = "5-3";
    public static final boolean VISUALS = true;
    public static final boolean RUN_ALL_LEVELS = true;
    public static final String LOAD_DATA_PATH = "";
    
    public static final int FLOOR_Y = 207;
}