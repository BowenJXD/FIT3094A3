package agents.EAController;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

import java.util.*;
import java.util.function.Function;

public class Agent implements MarioAgent {
    private boolean[] actions = null;
    FeedForwardNetwork network;
    
    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        actions = new boolean[MarioActions.numberOfActions()];
        actions[MarioActions.RIGHT.getValue()] = true;
        actions[MarioActions.SPEED.getValue()] = true;

        // Define layer sizes (input layer, hidden layers, output layer)
        List<Integer> layerNodes = Arrays.asList(8, 4, 2); // Example: 4 inputs, 1 hidden layer with 5 neurons, and 3 outputs

        // Define activation functions
        Function<double[], double[]> hiddenActivation = FeedForwardNetwork.getActivationByName("relu");
        Function<double[], double[]> outputActivation = FeedForwardNetwork.getActivationByName("sigmoid");

        // Initialize the neural network
        network = new FeedForwardNetwork(layerNodes, hiddenActivation, outputActivation, "uniform", 42, null);
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        var map = model.getMarioSceneObservation(0);
        
        double[] inputs = {map[10][10], map[10][11], map[11][10], map[11][11], map[12][10], map[12][11], map[13][10], map[13][11]};
        double[] outputs = network.feedForward(inputs);
        actions[MarioActions.RIGHT.getValue()] = outputs[0] > 0.5;
        actions[MarioActions.JUMP.getValue()] = outputs[1] > 0.5;
        
        return actions;
    }

    @Override
    public String getAgentName() {
        return "EAController";
    }

    @Override
    public void train(MarioForwardModel model) {
        
    }

}
