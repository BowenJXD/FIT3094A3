package agents.EAController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Selection {
    public static List<Agent> elitismSelection(Population population, int numAgents) {
        List<Agent> agents = new ArrayList<>(population.agents);
        agents.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
        return agents.subList(0, numAgents);
    }

    public static List<Agent> rouletteWheelSelection(Population population, int numAgents) {
        List<Agent> selection = new ArrayList<>();
        double wheel = population.agents.stream().mapToDouble(Agent::getFitness).sum();
        Random rand = new Random();
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

    public static List<Agent> tournamentSelection(Population population, int numAgents, int tournamentSize) {
        List<Agent> selection = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < numAgents; i++) {
            List<Agent> tournament = new ArrayList<>();
            for (int j = 0; j < tournamentSize; j++) {
                tournament.add(population.agents.get(rand.nextInt(population.getNumAgents())));
            }
            tournament.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
            selection.add(tournament.get(0));
        }
        return selection;
    }
}