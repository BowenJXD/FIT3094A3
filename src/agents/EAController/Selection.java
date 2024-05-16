package agents.EAController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Selection {
    public static List<Agent> elitism(Population population, int numAgents) {
        List<Agent> agents = new ArrayList<>(population.agents);
        agents.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
        return agents.subList(0, numAgents);
    }

    public static List<Agent> fitnessRouletteWheel(Population population, int numAgents) {
        List<Agent> selection = new ArrayList<>();
        double wheel = population.agents.stream().mapToDouble(Agent::getFitness).sum();
        Random rand = Config.rand;
        for (int i = 0; i < numAgents; i++) {
            double pick = rand.nextDouble() * wheel;
            double current = 0;
            for (Agent agent : population.agents) {
                current += agent.getFitness();
                if (current > pick) {
                    selection.add(agent);
                    break;
                }
            }
        }
        return selection;
    }
    
    public static List<Agent> rankRouletteWheel(Population population, int numAgents, int rankPower) {
        List<Agent> selection = new ArrayList<>();
        List<Agent> agents = population.agents;
        Random rand = Config.rand;
        for (int i = 0; i < numAgents; i++) {
            int wheel = getSigmaPowered(agents.size(), rankPower);
            int pick = rand.nextInt(wheel);
            int current = 0;
            for (int j = 0; j < agents.size(); j++) {
                current += (int) Math.pow(agents.size() - j, rankPower);
                if (current > pick) {
                    selection.add(agents.get(j));
                    break;
                }
            }
        }
        selection.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
        return selection;
    }
    
    public static int getSigmaPowered(int n, int p){
        int result = 0;
        for (int i = 1; i <= n; i++) {
            result += (int) Math.pow(i, p);
        }
        return result;
    }

    public static List<Agent> tournamentSelection(Population population, int numAgents) {
        int tournamentSize = Config.TOURNAMENT_SIZE;
        List<Agent> agents = new ArrayList<>(population.agents);
        List<Agent> selection = new ArrayList<>();
        Random rand = Config.rand;
        for (int i = 0; i < numAgents; i++) {
            List<Agent> tournament = new ArrayList<>();
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