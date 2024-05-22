package agents.EAController;

import com.google.gson.Gson;
import engine.helper.MarioActions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CosIndividual extends Individual {
    protected float[] chromosome;
    
    public CosIndividual(PopulationConfig config){
        chromosome = new float[config.ACTIONS.length * config.VARIABLES.size()];
        this.config = config;
        generate();
    }

    public CosIndividual(float[] chromosome){
        this.chromosome = chromosome;
    }
    
    public CosIndividual(String gene){
        Gson gson = new Gson();
        chromosome = gson.fromJson(gene, float[].class);
    }

    @Override
    public boolean[] getActions(int index){
        boolean[] actions = config.DEFAULT_ACTIONS.clone();
        int v = config.VARIABLES.size();
        for (int i = 0; i < config.ACTIONS.length; i++) {
            if (config.ACTIONS[i] == MarioActions.LEFT.getValue()) {
                double left = getAction(index, Arrays.copyOfRange(chromosome, i*v, i*v+v));
                int j = MarioActions.RIGHT.getValue();
                double right = getAction(index, Arrays.copyOfRange(chromosome, j*v, j*v+v));
                actions[MarioActions.LEFT.getValue()] = left > right;
                actions[MarioActions.RIGHT.getValue()] = right > left;
            }
            actions[config.ACTIONS[i]] = getAction(index, Arrays.copyOfRange(chromosome, i*v, i*v+v)) > 0;
        }

        return actions;
    }

    public double getAction(float x, float[] chromosomeOfAction){
        Map<Character, Float> values = new HashMap<>(Map.of('a', 0f, 'h', 0f, 'k', 0f, 'p', 0f));
        for (int i = 0; i < config.VARIABLES.size(); i++) {
            values.put(config.KEYS[i], chromosomeOfAction[i]);
        }
        float a = values.get('a'), h = values.get('h'), k = values.get('k'), p = values.get('p');
        double y = Math.cos(Math.pow(a * (x + h),p)) + k;
        return y;
    }

    @Override
    public void generate() {
        var vars = config.VARIABLES;
        Character[] keys = config.KEYS;
        for (int i = 0; i < config.ACTIONS.length; i++) {
            for (int j = 0; j < vars.size(); j++) {
                chromosome[i*vars.size() + j] = vars.get(keys[j])[0] + (vars.get(keys[j])[1] - vars.get(keys[j])[0]) * rand.nextFloat();
                // TODO: snap the value to the nearest value in the range using vars.get(keys[j])[2]
            }
        }
    }

    @Override
    public Individual[] crossover(Individual otherIndividual) {
        CosIndividual other = (CosIndividual) otherIndividual;
        float[] offspring1 = new float[chromosome.length];
        float[] offspring2 = new float[chromosome.length];
        for (int i = 0; i < chromosome.length; i++) {
            if (rand.nextBoolean()) {
                offspring1[i] = chromosome[i];
                offspring2[i] = other.getChromosome()[i];
            } else {
                offspring1[i] = other.getChromosome()[i];
                offspring2[i] = chromosome[i];
            }
        }
        return new CosIndividual[]{new CosIndividual(offspring1), new CosIndividual(offspring2)};
    }

    @Override
    public void mutate() {
        var vars = config.VARIABLES;
        Character[] keys = config.KEYS;
        for (int i = 0; i < chromosome.length; i++) {
            int j = i % keys.length;
            if (rand.nextDouble() < config.MUTATION_PROBABILITY) {
                chromosome[i] = vars.get(keys[j])[0] + (vars.get(keys[j])[1] - vars.get(keys[j])[0]) * rand.nextFloat();
            }
        }
    }

    @Override
    public String logGene() {
        Gson gson = new Gson();
        return gson.toJson(chromosome);
    }

    public float[] getChromosome() {
        return chromosome;
    }
}