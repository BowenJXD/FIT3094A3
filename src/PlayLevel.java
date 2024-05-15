import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import agents.EAController.Agent;
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
    
    public static void runLevel(MarioAgent agent, String inputLevel){
        String[] levels;
        if (Objects.equals(inputLevel, "")){
            levels = getAllLevels("./levels/SuperMarioBros");
        }
        else {
            levels = new String[]{"./levels/SuperMarioBros/mario-" + inputLevel + ".txt"};
        }

        MarioGame game = new MarioGame();
        MarioResult result;
        for (String level : levels) {
            result = game.runGame(agent, getLevel(level), 20, 0, true);
            String levelName = level.substring(level.lastIndexOf("-") - 1, level.lastIndexOf(".")).replace("-", ".");
            Logger.getInstance().logResult(result, levelName);
            printResults(result);
        }
    }

    public static void main(String[] args) {
        
        var agent = new agents.EAController.Agent(); // Change this to your own agent
        
        runLevel(agent, "1-1");
    }
}
