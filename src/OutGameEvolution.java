import agents.EAController.*;
import com.google.gson.Gson;
import engine.core.MarioAgent;
import engine.core.MarioGame;
import engine.core.MarioResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class OutGameEvolution {
    
    public static final String configPath = "log/gameConfigs.json";
    
    public List<GameConfig> population;
    
    public static final int POPULATION_SIZE = 6;
    public static final int NUM_GENERATION = 10;
    public static final float MUTATION_RATE = 0.1f;
    public static final boolean ALL_LEVELS = false;
    
    public Random random = new Random();
    
    public void evolve(){
        GameConfig best = population.getFirst();
        for (int i = 0; i < NUM_GENERATION; i++) {
            crossover();
            mutateTowardsBest(MUTATION_RATE, best);
            mutate(MUTATION_RATE);
            populate();
            population.sort((a, b) -> Float.compare(b.fitness, a.fitness));
            select();
            best = population.getFirst();
        }
    }
    
    public void populate(){
        for (GameConfig currentConfig : population) {
            System.out.println(currentConfig);
            currentConfig.applyConfig();
            Agent agent = new Agent();
            if (ALL_LEVELS) {
                var results = PlayLevel.runLevels(agent, true);
                for (int j = 0; j < results.size(); j++) {
                    population.get(j).fitness = results.get(j).getCompletionPercentage() * results.get(j).getRemainingTime();
                }
            }
            else {
                var result = PlayLevel.runLevel(agent, "", false); 
                currentConfig.fitness = result.getCompletionPercentage() * result.getRemainingTime();
            }
            System.out.println("Fitness: " + currentConfig.fitness);
        }
    }
    
    public void crossover() {
        List<GameConfig> newPopulation = new ArrayList<>();
        for (int j = 0; j < POPULATION_SIZE - 1; j++) {
            int[] gene1 = population.get(j).getGene();
            int[] gene2 = population.get(j + 1).getGene();
            int[] offspring1 = new int[gene1.length];
            int[] offspring2 = new int[gene2.length];
            for (int i = 0; i < gene1.length; i++) {
                boolean randomBool = random.nextBoolean();
                offspring1[i] = randomBool ? gene1[i] : gene2[i];
                offspring2[i] = randomBool ? gene2[i] : gene1[i];
            }
            newPopulation.add(new GameConfig(offspring1));
            newPopulation.add(new GameConfig(offspring2));
        }
        population = newPopulation;
    }
    
    public void mutate(float rate) {
        for (GameConfig currentConfig : population) {
            currentConfig.mutate(rate);
        }
    }
    
    public void mutateTowardsBest(float rate, GameConfig best) {
        for (GameConfig currentConfig : population) {
            if (random.nextFloat() > rate) {
                continue;
            }
            for (int i = 0; i < currentConfig.getGene().length; i++) {
                int diff = best.getGene()[i] - currentConfig.getGene()[i];
                int delta = random.nextInt(Math.abs(diff) + 1);
                if (diff > 0) {
                    currentConfig.setGene(i, currentConfig.getGene()[i] + delta);
                }
                else if (diff < 0) {
                    currentConfig.setGene(i, currentConfig.getGene()[i] - delta);
                }
            }
        }
    }
    
    public void select() {
        population.sort((a, b) -> Float.compare(b.fitness, a.fitness));
        population = population.subList(0, POPULATION_SIZE);
    }
    
    public void loadConfigs() {
        population = new ArrayList<>();
        Gson gson = new Gson();
        // load from the last 10 lines of the file as json
        try {
            String content = new String(Files.readAllBytes(Paths.get(configPath)));
            String[] lines = content.split("\n");
            if (lines.length > 10) {
                lines = new String[]{lines[lines.length - 10]};
            }
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].isEmpty()) {
                    continue;
                }
                population.add(gson.fromJson(lines[i], GameConfig.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveConfigs() {
        Gson gson = new Gson();
        try {
            for (GameConfig currentConfig : population) {
                Files.write(Paths.get(configPath), (gson.toJson(currentConfig) + "\n").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        OutGameEvolution evolution = new OutGameEvolution();
        evolution.loadConfigs();
        if (evolution.population.isEmpty()) {
            for (int i = 0; i < POPULATION_SIZE; i++) {
                evolution.population.add(new GameConfig());
            }
            evolution.populate();
        }
        evolution.evolve();
        evolution.saveConfigs();
    }
}
