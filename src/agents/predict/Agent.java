package agents.predict;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.GameStatus;
import engine.helper.MarioActions;

import java.util.ArrayList;
import java.util.List;

public class Agent implements MarioAgent {
    private boolean[] actions = null;
    boolean[] actionCache = null;
    public static final int PREDICTION_STEPS = 20;

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        actions = new boolean[MarioActions.numberOfActions()];
        actions[MarioActions.RIGHT.getValue()] = true;
        actions[MarioActions.SPEED.getValue()] = true;
    }

    @Override
    public void train(MarioForwardModel model) {    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        // if Mario is on the ground and the jump action is enabled, disable it
        if (model.isMarioOnGround() && actionCache[MarioActions.JUMP.getValue()]) {
            actions[MarioActions.JUMP.getValue()] = false;
            actionCache = actions.clone();
            return actions;
        }
        
        // first is go with alter immediately, last is go with alter starting after PREDICTION_STEPS steps
        List<MarioForwardModel> predictions = new ArrayList<>();
        var alternateActions = actions.clone();
        alternateActions[MarioActions.JUMP.getValue()] = true;
        List<MarioForwardModel> firstPredictions = new ArrayList<>();
        
        // Nested loops for (m, n) pairs with m < n
        for (int m = 0; m < PREDICTION_STEPS; m++) {
            for (int n = m + 1; n <= PREDICTION_STEPS; n++) {
                MarioForwardModel prediction = model.clone();
                boolean isSuccessful = true;
                for (int j = 0; j < PREDICTION_STEPS; j++) {
                    if (j >= m && j < n) {
                        prediction.advance(alternateActions);
                    } else {
                        prediction.advance(actions);
                    }
                    if (prediction.getGameStatus() == GameStatus.WIN) {
                        break;
                    } else if (prediction.getNumLives() < model.getNumLives() || prediction.getGameStatus() != GameStatus.RUNNING) {
                        isSuccessful = false;
                        break;
                    }
                }
                if (isSuccessful) {
                    predictions.add(prediction);
                } else {
                    predictions.add(null);
                }
                if (m == 0) {
                    firstPredictions.add(prediction);
                }
            }
        }
        
        // Sort the predictions by Mario's x position
        List<MarioForwardModel> sortedPredictions = new ArrayList<>(predictions);
        sortedPredictions.sort((a, b) -> {
            if (a == null && b == null) {
                return 0;
            } else if (a == null) {
                return 1;
            } else if (b == null) {
                return -1;
            } else {
                return Integer.compare(Math.round(b.getMarioFloatPos()[0]), Math.round(a.getMarioFloatPos()[0]));
            }
        });
        
        boolean[] result = null;
        if (firstPredictions.contains(sortedPredictions.getFirst())) {
            result = alternateActions;
        }
        else {
            result = actions;
        }
        actionCache = result.clone();
        return result;
    }

    @Override
    public String getAgentName() {
        return "Predict";
    }
}
