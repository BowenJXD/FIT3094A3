package agents.EAController;

import util.Matrix;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class FeedForwardNetwork {
    private List<Integer> layerNodes;
    private Function<double[], double[]> hiddenActivation;
    private Function<double[], double[]> outputActivation;
    private Map<String, double[][]> params;
    private double[] out;

    public FeedForwardNetwork(List<Integer> layerNodes,
                              Function<double[], double[]> hiddenActivation,
                              Function<double[], double[]> outputActivation,
                              String initMethod,
                              Integer seed,
                              Map<String, double[][]> chromosome) {
        this.layerNodes = layerNodes;
        this.hiddenActivation = hiddenActivation;
        this.outputActivation = outputActivation;
        this.out = new double[6];

        Random rand = (seed != null) ? new Random(seed) : Config.rand;

        if (chromosome != null) {
            this.params = new HashMap<>(chromosome);
            if (!params.keySet().stream().anyMatch(k -> k.startsWith("A"))) {
                int numLayers = params.size() / 2;
                IntStream.range(0, numLayers).forEach(i -> {
                    params.put("A" + (i + 1), new double[params.get("b" + (i + 1)).length][1]);
                });
            }
        } else {
            this.params = new HashMap<>();
            for (int l = 1; l < layerNodes.size(); l++) {
                if (initMethod.equals("uniform")) {
                    params.put("W" + l, Matrix.randomUniformMatrix(layerNodes.get(l), layerNodes.get(l - 1), rand));
                    params.put("b" + l, Matrix.randomUniformMatrix(layerNodes.get(l), 1, rand));
                } else {
                    throw new RuntimeException("Please extend more chromosome initialization methods");
                }
                params.put("A" + l, new double[0][0]);
            }
        }
    }

    public double[] feedForward(double[] X) {
        double[] A_prev = X;
        int L = layerNodes.size() - 1;

        for (int l = 1; l < L; l++) {
            double[][] W = params.get("W" + l);
            double[][] b = params.get("b" + l);
            double[] Z = Matrix.add(Matrix.dot(W, A_prev), b);
            A_prev = hiddenActivation.apply(Z);
            params.put("A" + l, new double[][]{A_prev});
        }

        double[][] W = params.get("W" + L);
        double[][] b = params.get("b" + L);
        double[] Z = Matrix.add(Matrix.dot(W, A_prev), b);
        double[] out = outputActivation.apply(Z);
        params.put("A" + L, new double[][]{out});

        this.out = out;
        return out;
    }

    public double[] softmax(double[] X) {
        double sumExp = Arrays.stream(X).map(Math::exp).sum();
        return Arrays.stream(X).map(x -> Math.exp(x) / sumExp).toArray();
    }

    public static Function<double[], double[]> getActivationByName(String name) {
        Map<String, Function<double[], double[]>> activations = new HashMap<>();
        activations.put("relu", x -> Arrays.stream(x).map(v -> Math.max(0, v)).toArray());
        activations.put("sigmoid", x -> Arrays.stream(x).map(v -> 1.0 / (1.0 + Math.exp(-v))).toArray());
        activations.put("linear", x -> x);
        activations.put("leaky_relu", x -> Arrays.stream(x).map(v -> v > 0 ? v : 0.01 * v).toArray());
        activations.put("tanh", x -> Arrays.stream(x).map(Math::tanh).toArray());

        Function<double[], double[]> func = activations.get(name.toLowerCase());
        if (func == null) throw new IllegalArgumentException("Unknown activation function: " + name);
        return func;
    }
    
    public Map<String, double[][]> getParams() {
        return params;
    }
}
