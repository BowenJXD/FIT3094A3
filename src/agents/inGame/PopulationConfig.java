package agents.inGame;

import engine.helper.MarioActions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PopulationConfig {
    public String INDIVIDUAL_CLASS;
    
    public int[] ACTIONS;
    public boolean[] DEFAULT_ACTIONS;
    public int LENGTH;
    public Map<Character, float[]> VARIABLES;
    
    public int[] SCORE;
    
    public int POPULATION_SIZE;
    public int TOURNAMENT_SIZE;
    public int ELITISM_SIZE;
    public double MUTATION_PROBABILITY;
    
    public int WIDTH() {
        return (int)Math.pow(2, ACTIONS.length);
    }
    
    public Character[] KEYS;
    
    public PopulationConfig(){
        INDIVIDUAL_CLASS = Config.INDIVIDUAL_CLASS;
        ACTIONS = Config.ACTIONS.clone();
        DEFAULT_ACTIONS = Config.DEFAULT_ACTIONS.clone();
        LENGTH = Config.LENGTH;
        VARIABLES = new HashMap<>(Config.VARIABLES);
        SCORE = Config.SCORE.clone();
        POPULATION_SIZE = Config.POPULATION_SIZE;
        TOURNAMENT_SIZE = Config.TOURNAMENT_SIZE;
        ELITISM_SIZE = Config.ELITISM_SIZE;
        MUTATION_PROBABILITY = Config.MUTATION_PROBABILITY;
        
        KEYS = VARIABLES.keySet().toArray(new Character[0]);
    }
}