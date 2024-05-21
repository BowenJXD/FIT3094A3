package agents.inGame;

import engine.core.MarioForwardModel;
import engine.helper.GameStatus;

import java.util.*;

public abstract class Individual {
    protected int generation;
    protected int index;
    protected String[] parents = new String[2];
    
    protected float fitness;
    protected MarioForwardModel model;
    protected Random rand;
    protected PopulationConfig config;

    public Individual() {
        rand = Config.rand;
    }

    public abstract boolean[] getActions(int index);

    public abstract void generate();

    public abstract Individual[] crossover(Individual otherIndividual);

    public abstract void mutate();

    public abstract String logGene();
    
    public void advanceModel(MarioForwardModel newModel){
        model = newModel.clone();
        for (int i = 0; i < config.LENGTH; i++) {
            if (model.getGameStatus() != GameStatus.RUNNING) return;
            model.advance(getActions(i));
        }
    }
    
    public void calculateFitness(){
        Map<Config.ScoreType, Float> scoreMap = new EnumMap<>(Config.ScoreType.class);
        scoreMap.put(Config.ScoreType.Win, (float) (model.getGameStatus() == GameStatus.WIN 
                ? config.SCORE[Config.ScoreType.Win.ordinal()] + model.getRemainingTime() : 0));
        scoreMap.put(Config.ScoreType.Death, (float) ((
                        model.getGameStatus() == GameStatus.LOSE ||
                        model.getMarioFloatPos()[1] > Config.FLOOR_Y) 
                ? config.SCORE[Config.ScoreType.Death.ordinal()] : 0));
        scoreMap.put(Config.ScoreType.X, model.getMarioFloatPos()[0] * config.SCORE[Config.ScoreType.X.ordinal()]);
        fitness = scoreMap.values().stream().reduce(0f, Float::sum);
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
                ", gene=" + logGene() +
                '}';
    }

    public float getFitness() { return fitness; }
    public void setFitness(float fitness) { this.fitness = fitness; }
    public void setModel(MarioForwardModel model) { this.model = model; }
    public MarioForwardModel getModel() { return model; }
    public int getGeneration() { return generation; }
    public PopulationConfig getConfig() { return config; }
    public void setConfig(PopulationConfig config) { this.config = config; }
}