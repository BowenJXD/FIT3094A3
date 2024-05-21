package agents.inGame;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CosIndividual extends Individual {
    protected float[] chromosome = new float[Config.VARIABLES.size() * Config.ACTIONS.length];

    public CosIndividual() {
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
        boolean[] actions = Config.DEFAULT_ACTIONS.clone();
        int v = Config.VARIABLES.size();
        for (int i = 0; i < Config.ACTIONS.length; i++) {
            actions[Config.ACTIONS[i]] = getAction(index, Arrays.copyOfRange(chromosome, i*v, i*v+v));
        }

        return actions;
    }

    public boolean getAction(float x, float[] chromosomeOfAction){
        Map<Character, Float> values = new HashMap<>(Map.of('a', 0f, 'h', 0f, 'k', 0f, 'p', 0f));
        for (int i = 0; i < Config.VARIABLES.size(); i++) {
            values.put(Config.KEYS[i], chromosomeOfAction[i]);
        }
        float a = values.get('a'), h = values.get('h'), k = values.get('k'), p = values.get('p');
        double y = Math.cos(Math.pow(a * (x + h),p)) + k;
        return y > 0;
    }

    @Override
    public void generate() {
        var vars = Config.VARIABLES;
        Character[] keys = Config.KEYS;
        for (int i = 0; i < Config.ACTIONS.length; i++) {
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
        var vars = Config.VARIABLES;
        Character[] keys = Config.KEYS;
        for (int i = 0; i < chromosome.length; i++) {
            int j = i % keys.length;
            if (rand.nextDouble() < Config.MUTATION_PROBABILITY) {
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