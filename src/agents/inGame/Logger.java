package agents.inGame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import engine.core.MarioResult;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static Logger instance;
    private static final String LEVEL_FILE = "log/LevelLogs.csv";
    private static final String LEVEL_HEADER = "Level,JsonFileName,GameStatus,CompletionPercentage,RemainingTime,MarioMode,KillsTotal,KillsByFire,KillsByStomp,KillsByShell,MarioNumHurts,NumBumpQuestionBlock,NumBumpBrick,KillsByFall,NumJumps,MaxXJump,MaxJumpAirTime,CurrentLives,CurrentCoins,NumCollectedMushrooms,NumCollectedFireflower,NumCollectedTileCoins,NumDestroyedBricks,GameEvents,AgentEvents";
    
    private static String jsonFileName = "log/InGame.json";
    private List<String> bestAgents;
    
    private Logger() {
        // Private constructor to prevent instantiation from outside
        checkAndWriteHeader(LEVEL_FILE, LEVEL_HEADER);
        bestAgents = new ArrayList<>();
        // create a new json file to store chromosomes, the name of the file is the datetime now
        if (Config.LOAD_DATA_PATH.equals("")) {
            jsonFileName = "log/" + LocalDateTime.now().toString().replace(":", "-").replace(".", "-") + ".json";
            checkAndWriteHeader(jsonFileName, "");
        }
        else {
            jsonFileName = Config.LOAD_DATA_PATH;
        }
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public static class IndividualData {
        public String name;
        public float fitness;
        public int[] chromosome;
    }
    
    public void logIndividual(Individual individual) {
        String newLine = "-";
        if (bestAgents.size() < 2 || 
                !bestAgents.subList(bestAgents.size() - 2, bestAgents.size()).contains(individual.getName())) {
            bestAgents.add(individual.getName());
            
            IndividualData data = new IndividualData();
            data.name = individual.getName();
            data.fitness = individual.getFitness();
            data.chromosome = individual.getChromosome();
            Gson gson = new GsonBuilder().create();
            newLine = gson.toJson(data) + "\n";
        }

        try (FileWriter writer = new FileWriter(jsonFileName, true)) {
            writer.append(newLine);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception according to your application's requirements
        }
    }

    public List<Individual> readIndividual(String filename) {
        List<Individual> individualList = new ArrayList<>();
        Gson gson = new Gson();
        Type individualType = new TypeToken<IndividualData>() {}.getType();

        int counter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    if (line.isEmpty()) continue;
                    IndividualData data = gson.fromJson(line, individualType);
                    if (data == null) continue;
                    Individual individual = new Individual(data.chromosome);
                    String name = data.name;
//                    int generation = Integer.parseInt(name.substring(0, name.indexOf("-")));
//                    int id = Integer.parseInt(name.substring(name.indexOf("-") + 1));
//                    individual.init(generation, id, new String[]{});
                    individual.init(0, counter++, null);
                    individualList.add(individual);
                    bestAgents.add(individual.getName());
                } catch (Exception ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception according to your application's requirements
        }

        return individualList;
    }
    
    public List<Individual> loadPopulation(int size){
        List<Individual> agents = readIndividual(jsonFileName);
        agents.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
        if (agents.size() > size) {
            agents = agents.subList(0, size);
        }
        
        return agents;
    }
    
    public void logLevelResult(MarioResult result, String level) {
        String data = getResultAsCSVLine(result);
        logCSV(LEVEL_FILE, level + "," + jsonFileName + "," + data);
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

    public void logCSV(String filename, String data) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.append(data).append("\n");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    private static void checkAndWriteHeader(String filename, String header) {
        File file = new File(filename);
        boolean writeHeader = true;

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String firstLine = reader.readLine();
                if (header.equals(firstLine)) {
                    writeHeader = false;
                }
            } catch (IOException e) {
                System.err.println("Error reading the file: " + e.getMessage());
            }
        }

        if (writeHeader) {
            try (FileWriter writer = new FileWriter(filename)) {
                writer.write(header);
                writer.write("\n");
            } catch (IOException e) {
                System.err.println("Error writing header to file: " + e.getMessage());
            }
        }
    }
}
