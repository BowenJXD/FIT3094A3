package agents.EAController;

import engine.core.MarioForwardModel;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class Population {
    public List<Agent> agents;

    public Population(){
        agents = new ArrayList<>();
        for (int i = 0; i < Config.POPULATION_SIZE; i++) {
            Agent agent = new Agent();
            agent.init(0, i, null);
            agents.add(agent);
        }
    }
    
    public Population(List<Agent> agents) {
        this.agents = agents;
        if (agents.size() < Config.POPULATION_SIZE) {
            for (int i = agents.size(); i < Config.POPULATION_SIZE; i++) {
                Agent agent = new Agent();
                agent.init(0, i, null);
                agents.add(agent);
            }
        }
    }

    public int getNumAgents() {
        return agents.size();
    }

/*    public int getNumGenes() {
        return agents.get(0).getChromosome().length;
    }*/

    public double getAverageFitness() {
        double sum = 0;
        for (Agent agent : agents) {
            sum += agent.getFitness();
        }
        return sum / agents.size();
    }

    public Agent getFittestAgent() {
        Agent fittest = agents.get(0);
        for (Agent agent : agents) {
            if (agent.getFitness() > fittest.getFitness()) {
                fittest = agent;
            }
        }
        return fittest;
    }

    public void calculateFitness() {
        for (Agent agent : agents) {
            agent.calculateFitness();
        }
    }

    public double getFitnessStd() {
        double sum = 0;
        double mean = getAverageFitness();
        for (Agent agent : agents) {
            sum += Math.pow(agent.getFitness() - mean, 2);
        }
        return Math.sqrt(sum / agents.size());
    }

    public List<Agent> getAgents() {
        return agents;
    }
}