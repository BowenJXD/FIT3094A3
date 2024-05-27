package agents.EAController;

import com.google.gson.Gson;

public class UniformIndividual extends Individual {
    protected int[] chromosome;
    
    public UniformIndividual(PopulationConfig config){
        chromosome = new int[config.LENGTH];
        this.config = config;
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
        boolean[] actions = config.DEFAULT_ACTIONS.clone();
        var partialActions = intToBooleanArray(chromosome[index], config.ACTIONS.length);
        for (int i = 0; i < partialActions.length; i++) {
            actions[config.ACTIONS[i]] = partialActions[i];
        }

        return actions;
    }

    @Override
    public void generate() {
        chromosome = new int[config.LENGTH];
        for (int j = 0; j < config.LENGTH; j++) {
            chromosome[j] = rand.nextInt(config.WIDTH());
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
            if (rand.nextDouble() < config.MUTATION_PROBABILITY) {
                chromosome[i] = rand.nextInt(config.WIDTH());
            }
        }
    }

    @Override
    public void mutateTowards(Individual best) {
        UniformIndividual bestIndividual = (UniformIndividual) best;
        for (int i = 0; i < chromosome.length; i++) {
            if (rand.nextDouble() < config.MUTATION_PROBABILITY) {
                int diff = bestIndividual.getChromosome()[i] - chromosome[i];
                int delta = rand.nextInt(Math.abs(diff) + 1);
                if (diff > 0) {
                    chromosome[i] += delta;
                } else if (diff < 0) {
                    chromosome[i] -= delta;
                }
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