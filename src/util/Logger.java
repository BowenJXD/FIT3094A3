package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import engine.core.MarioEvent;
import engine.core.MarioAgentEvent;
import engine.core.MarioResult;

public class Logger {
    private static Logger instance;
    private static final String FILE_NAME = "logs.csv";
    private static final String HEADER = "Level,GameStatus,CompletionPercentage,RemainingTime,MarioMode,KillsTotal,KillsByFire,KillsByStomp,KillsByShell,MarioNumHurts,NumBumpQuestionBlock,NumBumpBrick,KillsByFall,NumJumps,MaxXJump,MaxJumpAirTime,CurrentLives,CurrentCoins,NumCollectedMushrooms,NumCollectedFireflower,NumCollectedTileCoins,NumDestroyedBricks,GameEvents,AgentEvents";

    private Logger() {
        // Private constructor to prevent instantiation from outside
        checkAndWriteHeader();
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void logResult(MarioResult result, String level) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.append(level).append(",");
            writer.append(getResultAsCSVLine(result));
            writer.append("\n");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    private String getResultAsCSVLine(MarioResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append(result.getGameStatus().toString()).append(",");
        sb.append(result.getCompletionPercentage()).append(",");
        sb.append(result.getRemainingTime()).append(",");
        sb.append(result.getMarioMode()).append(",");
        sb.append(result.getKillsTotal()).append(",");
        sb.append(result.getKillsByFire()).append(",");
        sb.append(result.getKillsByStomp()).append(",");
        sb.append(result.getKillsByShell()).append(",");
        sb.append(result.getMarioNumHurts()).append(",");
        sb.append(result.getNumBumpQuestionBlock()).append(",");
        sb.append(result.getNumBumpBrick()).append(",");
        sb.append(result.getKillsByFall()).append(",");
        sb.append(result.getNumJumps()).append(",");
        sb.append(result.getMaxXJump()).append(",");
        sb.append(result.getMaxJumpAirTime()).append(",");
        sb.append(result.getCurrentLives()).append(",");
        sb.append(result.getCurrentCoins()).append(",");
        sb.append(result.getNumCollectedMushrooms()).append(",");
        sb.append(result.getNumCollectedFireflower()).append(",");
        sb.append(result.getNumCollectedTileCoins()).append(",");
        sb.append(result.getNumDestroyedBricks()).append(",");
        /*sb.append(getGameEventsAsCSV(result.getGameEvents())).append(",");
        sb.append(getAgentEventsAsCSV(result.getAgentEvents()));*/
        return sb.toString();
    }

    private String getGameEventsAsCSV(ArrayList<MarioEvent> gameEvents) {
        StringBuilder sb = new StringBuilder();
        for (MarioEvent event : gameEvents) {
            sb.append(event.getEventType()).append(",");
            sb.append(event.getEventParam()).append(",");
            sb.append(event.getMarioX()).append(",");
            sb.append(event.getMarioY()).append(",");
            sb.append(event.getMarioState()).append(",");
            sb.append(event.getTime()).append(";");
        }
        return sb.toString();
    }

    private String getAgentEventsAsCSV(ArrayList<MarioAgentEvent> agentEvents) {
        StringBuilder sb = new StringBuilder();
        for (MarioAgentEvent event : agentEvents) {
            sb.append(event.toString()).append(",");
            sb.append(event.getTime()).append(";");
        }
        return sb.toString();
    }

    private void checkAndWriteHeader() {
        File file = new File(FILE_NAME);
        boolean writeHeader = true;

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String firstLine = reader.readLine();
                if (HEADER.equals(firstLine)) {
                    writeHeader = false;
                }
            } catch (IOException e) {
                System.err.println("Error reading the file: " + e.getMessage());
            }
        }

        if (writeHeader) {
            try (FileWriter writer = new FileWriter(FILE_NAME)) {
                writer.write(HEADER);
                writer.write("\n");
            } catch (IOException e) {
                System.err.println("Error writing header to file: " + e.getMessage());
            }
        }
    }
}
