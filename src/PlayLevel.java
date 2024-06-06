import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import agents.EAController.Agent;
import agents.EAController.Config;
import agents.EAController.Logger;
import engine.core.MarioAgent;
import engine.core.MarioGame;
import engine.core.MarioResult;
import engine.helper.GameStatus;

public class PlayLevel {
    public static void printResults(MarioResult result) {
        System.out.println("****************************************************************");
        System.out.println("Game Status: " + result.getGameStatus().toString() +
                " Percentage Completion: " + result.getCompletionPercentage());
        System.out.println("Lives: " + result.getCurrentLives() + " Coins: " + result.getCurrentCoins() +
                " Remaining Time: " + (int) Math.ceil(result.getRemainingTime() / 1000f));
        System.out.println("Mario State: " + result.getMarioMode() +
                " (Mushrooms: " + result.getNumCollectedMushrooms() + " Fire Flowers: " + result.getNumCollectedFireflower() + ")");
        System.out.println("Total Kills: " + result.getKillsTotal() + " (Stomps: " + result.getKillsByStomp() +
                " Fireballs: " + result.getKillsByFire() + " Shells: " + result.getKillsByShell() +
                " Falls: " + result.getKillsByFall() + ")");
        System.out.println("Bricks: " + result.getNumDestroyedBricks() + " Jumps: " + result.getNumJumps() +
                " Max X Jump: " + result.getMaxXJump() + " Max Air Time: " + result.getMaxJumpAirTime());
        System.out.println("****************************************************************");
    }

    public static String getLevel(String filepath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException e) {
        }
        return content;
    }
    
    public static String[] getAllLevels(String folderpath) {
        String[] levels = null;
        try {
            levels = Files.walk(Paths.get(folderpath))
                    .filter(Files::isRegularFile)
                    .map(x -> x.toString())
                    .toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return levels;
    }
    
    public static List<MarioResult> runLevels(MarioAgent agent, boolean visual, boolean quitOnFail){
        String[] levels = getAllLevels("./levels/SuperMarioBros");

        MarioGame game = new MarioGame();
        List<MarioResult> results = new ArrayList<>();
        for (String level : levels) {
            MarioResult result = game.runGame(agent, getLevel(level), 60, 0, visual, visual ? 60 : 1000);
            results.add(result);
            game.CloseWindow();
            String levelName = level.substring(level.lastIndexOf("-") - 1, level.lastIndexOf(".")).replace("-", ".");
            Logger.getInstance().logLevelResult(result, levelName);
            if (quitOnFail && result.getGameStatus() != engine.helper.GameStatus.WIN) {
                return results;
            }
            // printResults(result);
        }
        return results;
    }

    public static MarioResult runLevel(MarioAgent agent, String inputLevel, boolean visual){
        String level;
        if (Objects.equals(inputLevel, "")){
            level = "./levels/SuperMarioBros/mario-1-1.txt";
        }
        else {
            level = "./levels/SuperMarioBros/mario-" + inputLevel + ".txt";
        }

        MarioGame game = new MarioGame();
        MarioResult result;
        result = game.runGame(agent, getLevel(level), 60, 0, visual, visual ? 60 : 1000);
        game.CloseWindow();
        String levelName = level.substring(level.lastIndexOf("-") - 1, level.lastIndexOf(".")).replace("-", ".");
        // Logger.getInstance().logLevelResult(result, levelName);
        // printResults(result);
        return result;
    }
    
    public static void randomnessTest(){
        int winCount = 0;
        int NUM_GENERATION = 10;
        Random random = new Random();
        Agent agent = new Agent();
        for (int i = 0; i < NUM_GENERATION; i++) {
            int randSeed = random.nextInt(100);
            Config.rand = new Random(randSeed);
            System.out.println("Random Seed: " + randSeed);
            float totalCompletion = 0;

            float fitness = 0;
            if (Config.RUN_ALL_LEVELS) {
                var results = PlayLevel.runLevels(agent, true, true);
                for (int j = 0; j < results.size(); j++) {
                    fitness += OutGameEvolution.getFitness(results.get(j));
                    totalCompletion += results.get(j).getGameStatus() == GameStatus.WIN ? 1 : 0;
                }
                winCount += totalCompletion == 15 ? 1 : 0;
            }
            else {
                var result = PlayLevel.runLevel(agent, Config.LEVEL_STRING, Config.VISUALS);
                fitness = OutGameEvolution.getFitness(result);
                totalCompletion += result.getGameStatus() == GameStatus.WIN ? 1 : 0;
            }
            System.out.println("Fitness: " + fitness + " Completion: " + totalCompletion);
            System.out.println("Win Count: " + winCount + " Success Rate: " + (float) winCount / (i+1));
        }
        System.out.println("Success Rate: " + (float) winCount / NUM_GENERATION);
    }

    public static void main(String[] args) {
        
        var agent = new Agent(); // Change this to your own agent
        
        randomnessTest();
//        if (Config.RUN_ALL_LEVELS){
//            var result = runLevels(agent, Config.VISUALS, false);
//            float fitness = 0;
//            for (var r : result){
//                fitness += r.getCompletionPercentage() * r.getRemainingTime();
//            }
//        }
//        else {
//            MarioResult result = runLevel(agent, Config.LEVEL_STRING, Config.VISUALS);
//            printResults(result);
//            Logger.getInstance().logLevelResult(result, Config.LEVEL_STRING);
//        }
    }
}
