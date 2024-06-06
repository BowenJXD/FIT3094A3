package agents.EAController;

import engine.helper.MarioActions;

import java.util.Map;
import java.util.Random;

public class Config {

    public enum ScoreType{
        Win, Death, X, Y
    }
    
    // PopulationConfig
    
    public static String INDIVIDUAL_CLASS = "CosIndividual";
    
    public static int[] ACTIONS = {
//            MarioActions.DOWN.getValue(),
//            MarioActions.SPEED.getValue(),
            MarioActions.LEFT.getValue(), 
//            MarioActions.RIGHT.getValue(),
            MarioActions.JUMP.getValue()};
    public static boolean[] DEFAULT_ACTIONS = {false,true,false,true,false};
    public static int LENGTH = 40;
    public static Map<Character, float[]> VARIABLES = Map.of(
            'a', new float[]{0, 1, 0},
            'h', new float[]{-LENGTH, LENGTH, 0},
            'k', new float[]{-1, 1, 0},
            'p', new float[]{0, 2, 0.2f});
    
    public static int[] SCORE = {(int) 1E6, -Integer.MAX_VALUE, 100, 1};
    
    public static int POPULATION_SIZE = 20;
    public static int TOURNAMENT_SIZE = Math.round(POPULATION_SIZE * 0.4f);
    public static int OFFSPRING_NUM = (int) (POPULATION_SIZE * 0.7f);
    public static int ELITISM_SIZE = POPULATION_SIZE / 2;
    public static double MUTATION_PROBABILITY = 0.2;

    // ------------------------------------------------

    public static int NUM_POPULATION = 2;
    public static int INTERVAL(){ return 100 / (NUM_POPULATION - 1);}
    public static int NUM_GENERATION = 40;
    public static int BESTCACHE_AGE = LENGTH / 2;
    
    public static int RANDOM_SEED = 99;
    public static Random rand = new Random(RANDOM_SEED);

    public static String LEVEL_STRING = "8-1";
    public static boolean VISUALS = true;
    public static boolean RUN_ALL_LEVELS = false;
    public static String LOAD_DATA_PATH = "";
    
    public static int FLOOR_Y = 207;
}