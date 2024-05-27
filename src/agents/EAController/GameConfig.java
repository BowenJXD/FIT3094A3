package agents.EAController;

import java.util.Map;
import java.util.Random;

public class GameConfig {

    public float fitness = 0;
    
    public int predictionLength;
    public static int getPredictionLength(){
        return getRandomBetween(10, 100);
    }
    
    public int populationSize;
    public static int getPopulationSize(){
        return getRandomBetween(10, 100);
    }
    
    public int offspringSizeIndex;
    public static int getOffspringSize(){
        return getRandomBetween(10, 100);
    }
    
    public int elitismSizeIndex;
    public static int getElitismSize(){
        return getRandomBetween(10, 80);
    }
    
    public int mutationProbabilityIndex;
    public static int getMutationProbability(){
        return getRandomBetween(1, 100);
    }
    
    public int numPopulation;
    public static int getNumPopulation(){
        return getRandomBetween(2, 4);
    }
    
    public int numGeneration;
    public static int getNumGeneration(){
        return getRandomBetween(10, 100);
    }
    
    public int bestCacheAgeIndex;
    public static int getBestCacheAge(){
        return getRandomBetween(10, 100);
    }
    
    public GameConfig(){
        predictionLength = getPredictionLength();
        populationSize = getPopulationSize();
        offspringSizeIndex = getOffspringSize();
        elitismSizeIndex = getElitismSize();
        mutationProbabilityIndex = getMutationProbability();
        numPopulation = getNumPopulation();
        numGeneration = getNumGeneration();
        bestCacheAgeIndex = getBestCacheAge();
    }
    
    public GameConfig(int[] gene){
        predictionLength = gene[0];
        populationSize = gene[1];
        offspringSizeIndex = gene[2];
        elitismSizeIndex = gene[3];
        mutationProbabilityIndex = gene[4];
        numPopulation = gene[5];
        numGeneration = gene[6];
        bestCacheAgeIndex = gene[7];
    }
    
    public void applyConfig(){
        Config.LENGTH = predictionLength;
        Config.POPULATION_SIZE = populationSize;
        Config.OFFSPRING_NUM = populationSize * offspringSizeIndex / 100;
        Config.ELITISM_SIZE = populationSize * elitismSizeIndex / 100;
        Config.MUTATION_PROBABILITY = Math.pow(mutationProbabilityIndex / 200.0, 2);
        Config.NUM_POPULATION = numPopulation;
        Config.NUM_GENERATION = numGeneration;
        Config.BESTCACHE_AGE = bestCacheAgeIndex * Config.LENGTH / 100;
        Config.VARIABLES = Map.of(
                'a', new float[]{0, 1, 0},
                'h', new float[]{-predictionLength, predictionLength, 0},
                'k', new float[]{-1, 1, 0},
                'p', new float[]{0, 2, 0.2f});
    }
    
    public int[] getGene(){
        return new int[]{predictionLength, populationSize, offspringSizeIndex, elitismSizeIndex, mutationProbabilityIndex, numPopulation, numGeneration, bestCacheAgeIndex};
    }
    
    public void setGene(int index, int value){
        switch (index) {
            case 0:
                predictionLength = value;
                break;
            case 1:
                populationSize = value;
                break;
            case 2:
                offspringSizeIndex = value;
                break;
            case 3:
                elitismSizeIndex = value;
                break;
            case 4:
                mutationProbabilityIndex = value;
                break;
            case 5:
                numPopulation = value;
                break;
            case 6:
                numGeneration = value;
                break;
            case 7:
                bestCacheAgeIndex = value;
                break;
        }
    }
    
    public void mutate(float rate){
        for (int i = 0; i < getGene().length; i++) {
            if (rand.nextFloat() < rate) {
                switch (i) {
                    case 0:
                        predictionLength = getPredictionLength();
                        break;
                    case 1:
                        populationSize = getPopulationSize();
                        break;
                    case 2:
                        offspringSizeIndex = getOffspringSize();
                        break;
                    case 3:
                        elitismSizeIndex = getElitismSize();
                        break;
                    case 4:
                        mutationProbabilityIndex = getMutationProbability();
                        break;
                    case 5:
                        numPopulation = getNumPopulation();
                        break;
                    case 6:
                        numGeneration = getNumGeneration();
                        break;
                    case 7:
                        bestCacheAgeIndex = getBestCacheAge();
                        break;
                }
            }
        }
    }
    
    public static final int RANDOM_SEED = 42;
    public static Random rand = new Random(RANDOM_SEED);
    
    public static int getRandomBetween(int min, int max){
        return rand.nextInt(max + 1 - min) + min;
    }
    
    public static float getRandomBetween(float min, float max){
        return rand.nextFloat() * (max - min) + min;
    }

    @Override
    public String toString() {
        return  "len " + predictionLength +
                " | psz " + populationSize +
                " | osz " + offspringSizeIndex +
                " | esz " + elitismSizeIndex +
                " | mpi " + mutationProbabilityIndex +
                " | npp " + numPopulation +
                " | gen " + numGeneration +
                " | bca " + bestCacheAgeIndex;
    }
}