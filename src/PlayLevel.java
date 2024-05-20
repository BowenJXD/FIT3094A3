import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import agents.inGame.Agent;
import agents.inGame.Config;
import engine.core.MarioAgent;
import engine.core.MarioGame;
import engine.core.MarioResult;
import util.Logger;

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
    
    public static List<MarioResult> runLevels(MarioAgent agent, boolean visual){
        String[] levels = getAllLevels("./levels/SuperMarioBros");

        MarioGame game = new MarioGame();
        List<MarioResult> results = new ArrayList<>();
        for (String level : levels) {
            MarioResult result = game.runGame(agent, getLevel(level), 20, 0, visual, visual ? 60 : 1000);
            results.add(result);
            game.CloseWindow();
            String levelName = level.substring(level.lastIndexOf("-") - 1, level.lastIndexOf(".")).replace("-", ".");
            agents.inGame.Logger.getInstance().logLevelResult(result, levelName);
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
        result = game.runGame(agent, getLevel(level), 30, 0, visual, visual ? 60 : 1000);
        game.CloseWindow();
        String levelName = level.substring(level.lastIndexOf("-") - 1, level.lastIndexOf(".")).replace("-", ".");
        // Logger.getInstance().logLevelResult(result, levelName);
        // printResults(result);
        return result;
    }

    public static void main(String[] args) {
        
        var agent = new agents.inGame.Agent(); // Change this to your own agent
        
        if (Config.RUN_ALL_LEVELS){
            runLevels(agent, Config.VISUALS);
        }
        else {
            MarioResult result = runLevel(agent, Config.LEVEL_STRING, Config.VISUALS);
            printResults(result);
            agents.inGame.Logger.getInstance().logLevelResult(result, "1-1");
        }
    }
}
