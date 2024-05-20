package agents.inGame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.core.MarioForwardModel;
import engine.helper.GameStatus;
import engine.helper.MarioActions;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Individual {
    protected int generation;
    protected int index;
    protected String[] parents = new String[2];
    
    protected float fitness;
    protected int[] chromosome = new int[Config.LENGTH];
    protected MarioForwardModel model;

    public Individual() {
        
    }
    
    public Individual(int[] chromosome){
        this.chromosome = chromosome;
    }
    
    public void advanceModel(MarioForwardModel newModel){
        model = newModel.clone();
        for (int i = 0; i < Config.LENGTH; i++) {
            if (model.getGameStatus() != GameStatus.RUNNING) return;
            model.advance(getActions(i));
        }
    }
    
    public void calculateFitness(){
        Map<Config.ScoreType, Float> scoreMap = new EnumMap<>(Config.ScoreType.class);
        scoreMap.put(Config.ScoreType.Win, (float) (model.getGameStatus() == GameStatus.WIN 
                ? Config.SCORE[Config.ScoreType.Win.ordinal()] - model.getRemainingTime() : 0));
        scoreMap.put(Config.ScoreType.Death, (float) ((
                        model.getGameStatus() == GameStatus.LOSE ||
                        model.getMarioFloatPos()[1] > Config.FLOOR_Y) 
                ? Config.SCORE[Config.ScoreType.Death.ordinal()] : 0));
        scoreMap.put(Config.ScoreType.X, model.getMarioFloatPos()[0] * Config.SCORE[Config.ScoreType.X.ordinal()]);
        fitness = scoreMap.values().stream().reduce(0f, Float::sum);
    }
    
    public boolean[] getActions(int index){
        boolean[] actions = Config.DEFAULT_ACTIONS.clone();
        var partialActions = intToBooleanArray(chromosome[index], Config.ACTIONS.length);
        for (int i = 0; i < partialActions.length; i++) {
            actions[Config.ACTIONS[i]] = partialActions[i];
        }
        return actions;
    }
    
    public boolean[] nextActions(int currentGeneration){
        return getActions(currentGeneration - generation);
    }

    public void init(int generation, int index, String[] parents){
        this.generation = generation;
        this.index = index;
        this.parents = parents;
    }
    
    public String getName(){
        return generation + "-" + index;
    }
    
    public String printInfo() {
        return "Individual{" +
                "generation=" + generation +
                ", index=" + index +
                ", parents=" + Arrays.toString(parents) +
                ", fitness=" + fitness +
                ", chromosome=" + Arrays.toString(chromosome) +
                '}';
    }

    public float getFitness() { return fitness; }
    public void setFitness(float fitness) { this.fitness = fitness; }
    public int[] getChromosome() { return chromosome; }
    public void setChromosome(int[] chromosome) { this.chromosome = chromosome; }
    public void setModel(MarioForwardModel model) { this.model = model; }
    public MarioForwardModel getModel() { return model; }
    public int getGeneration() { return generation; }

    public static boolean[] intToBooleanArray(int num, int length) {
        boolean[] boolArray = new boolean[length];
        String binary = Integer.toBinaryString(num);

        // Pad the binary string with leading zeros if necessary
        while (binary.length() < length) {
            binary = "0" + binary;
        }

        // Convert the binary string to a boolean array
        for (int i = 0; i < length; i++) {
            boolArray[i] = binary.charAt(i) == '1';
        }

        return boolArray;
    }
}