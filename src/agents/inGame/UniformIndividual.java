package agents.inGame;

import com.google.gson.Gson;
import engine.core.MarioForwardModel;
import engine.helper.GameStatus;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class UniformIndividual extends Individual {
    protected int[] chromosome = new int[Config.LENGTH];
    
    public UniformIndividual(){
        generate();
    }
    
    public UniformIndividual(int[] chromosome){
        this.chromosome = chromosome;        
    }
    
    public UniformIndividual(String gene){
        Gson gson = new Gson();
        chromosome = gson.fromJson(gene, int[].class);
    }
    
    @Override
    public boolean[] getActions(int index){
        boolean[] actions = Config.DEFAULT_ACTIONS.clone();
        var partialActions = intToBooleanArray(chromosome[index], Config.ACTIONS.length);
        for (int i = 0; i < partialActions.length; i++) {
            actions[Config.ACTIONS[i]] = partialActions[i];
        }

        return actions;
    }

    @Override
    public void generate() {
        chromosome = new int[Config.LENGTH];
        for (int j = 0; j < Config.LENGTH; j++) {
            chromosome[j] = rand.nextInt(Config.WIDTH);
        }
    }

    @Override
    public Individual[] crossover(Individual otherIndividual) {
        UniformIndividual other = (UniformIndividual) otherIndividual;
        int[] offspring1 = new int[chromosome.length];
        int[] offspring2 = new int[chromosome.length];
        for (int i = 0; i < chromosome.length; i++) {
            if (rand.nextBoolean()) {
                offspring1[i] = chromosome[i];
                offspring2[i] = other.getChromosome()[i];
            } else {
                offspring1[i] = other.getChromosome()[i];
                offspring2[i] = chromosome[i];
            }
        }
        return new UniformIndividual[]{new UniformIndividual(offspring1), new UniformIndividual(offspring2)};
    }

    @Override
    public void mutate() {
        for (int i = 0; i < chromosome.length; i++) {
            if (rand.nextDouble() < Config.MUTATION_PROBABILITY) {
                chromosome[i] = rand.nextInt(Config.WIDTH);
            }
        }
    }

    @Override
    public String logGene() {
        Gson gson = new Gson();
        return gson.toJson(chromosome);
    }

    public int[] getChromosome() {
        return chromosome;
    }

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