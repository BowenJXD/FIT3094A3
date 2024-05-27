package agents.EAController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Selection {
    public static List<Individual> elitism(List<Individual> population, int numAgents) {
        List<Individual> agents = new ArrayList<>(population);
        agents.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
        if (numAgents == agents.size())
            System.out.println("Warning: Elitism selection size is equal to population size");
        if (numAgents > agents.size())
            System.out.println("Warning: Elitism selection size is greater than population size");
        return agents.subList(0, numAgents);
    }

    public static List<Individual> fitnessRouletteWheel(List<Individual> population, int numAgents) {
        List<Individual> selection = new ArrayList<>();
        double wheel = population.stream().mapToDouble(Individual::getFitness).sum();
        Random rand = Config.rand;
        for (int i = 0; i < numAgents; i++) {
            double pick = rand.nextDouble() * wheel;
            double current = 0;
            for (Individual agent : population) {
                current += agent.getFitness();
                if (current > pick) {
                    selection.add(agent);
                    break;
                }
            }
        }
        return selection;
    }
    
    public static Individual rankRouletteWheel(List<Individual> population){
        double wheel = population.stream().mapToDouble(Individual::getFitness).sum();
        Random rand = Config.rand;
        double pick = rand.nextDouble() * wheel;
        double current = 0;
        for (Individual agent : population) {
            current += agent.getFitness();
            if (current > pick) {
                return agent;
            }
        }
        return null;
    }
    
/*    public static List<Individual> rankRouletteWheel(List<Individual> population, int numAgents, int rankPower) {
        List<Individual> selection = new ArrayList<>();
        Random rand = Config.rand;
        for (int i = 0; i < numAgents; i++) {
            int wheel = getSigmaPowered(population.size(), rankPower);
            int pick = rand.nextInt(wheel);
            int current = 0;
            for (int j = 0; j < population.size(); j++) {
                current += (int) Math.pow(population.size() - j, rankPower);
                if (current > pick) {
                    selection.add(population.get(j));
                    break;
                }
            }
        }
        selection.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
        return selection;
    }*/
    
    public static int getSigmaPowered(int n, int p){
        int result = 0;
        for (int i = 1; i <= n; i++) {
            result += (int) Math.pow(i, p);
        }
        return result;
    }

    public static List<Individual> tournamentSelection(List<Individual> population, int numAgents) {
        int tournamentSize = agents.EAController.Config.TOURNAMENT_SIZE;
        List<Individual> agents = new ArrayList<>(population);
        List<Individual> selection = new ArrayList<>();
        Random rand = Config.rand;
        for (int i = 0; i < numAgents; i++) {
            List<Individual> tournament = new ArrayList<>();
            for (int j = 0; j < tournamentSize; j++) {
                tournament.add(agents.get(rand.nextInt(agents.size())));
            }
            tournament.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
            selection.add(tournament.getFirst());
            agents.remove(tournament.getFirst());
        }
        selection.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
        return selection;
    }
}