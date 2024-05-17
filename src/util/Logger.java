package util;

import agents.EAController.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import agents.EAController.Agent;
import com.google.gson.reflect.TypeToken;
import engine.core.MarioEvent;
import engine.core.MarioAgentEvent;
import engine.core.MarioResult;

// TODO: allow outputting the statistic of each run
public class Logger {
    private static Logger instance;
    private static final String LEVEL_FILE = "log/logs.csv";
    private static final String LEVEL_HEADER = "Level,GameStatus,CompletionPercentage,RemainingTime,MarioMode,KillsTotal,KillsByFire,KillsByStomp,KillsByShell,MarioNumHurts,NumBumpQuestionBlock,NumBumpBrick,KillsByFall,NumJumps,MaxXJump,MaxJumpAirTime,CurrentLives,CurrentCoins,NumCollectedMushrooms,NumCollectedFireflower,NumCollectedTileCoins,NumDestroyedBricks,GameEvents,AgentEvents";
    
    private static String jsonFileName = "log/515-before.json";
    private List<String> bestAgents;
    
    private static final String BESTFIT_FILE = "log/bestfits.csv";
    
    List<Double> bestfits;
    
    private Logger() {
        // Private constructor to prevent instantiation from outside
        checkAndWriteHeader(LEVEL_FILE, LEVEL_HEADER);
        bestfits = new ArrayList<>();
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

    public static class AgentData {
        public String agentName;
        public double fitness;
        public Map<String, double[][]> data;

        public AgentData(String agentName, double fitness, Map<String, double[][]> data) {
            this.agentName = agentName;
            this.fitness = fitness;
            this.data = data;
        }

        // Serialization method
        public String toJson() {
            Gson gson = new GsonBuilder().create();
            return gson.toJson(this);
        }
    }

    public void logChromosome(Agent agent) {
        bestfits.add(agent.getFitness());
        String newLine = "-";
        if (bestAgents.size() < 2 || 
                !bestAgents.subList(bestAgents.size() - 2, bestAgents.size()).contains(agent.getAgentId())) {
            bestAgents.add(agent.getAgentId());
            AgentData agentData = new AgentData(agent.getAgentId(), agent.getFitness(), agent.getChromosome());
            newLine = agentData.toJson() + "\n";
        }

        try (FileWriter writer = new FileWriter(jsonFileName, true)) {
            writer.append(newLine);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception according to your application's requirements
        }
    }

    public List<AgentData> readChromosome(String filename) {
        List<AgentData> agentDataList = new ArrayList<>();
        Gson gson = new Gson();
        Type agentDataType = new TypeToken<AgentData>() {}.getType();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    if (line.isEmpty()) continue;
                    AgentData agentData = gson.fromJson(line, agentDataType);
                    if (agentData == null) continue;
                    agentDataList.add(agentData);
                    bestAgents.add(agentData.agentName);
                } catch (Exception ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception according to your application's requirements
        }

        return agentDataList;
    }
    
    public void logBestfits(){
        StringBuilder data = new StringBuilder();
        for (Double bestfit : bestfits) {
            // format to 2 decimal places
            data.append(String.format("%.2f", bestfit)).append(",");
        }
        logCSV(BESTFIT_FILE, data.toString());
    }
    
    public List<Agent> loadAgents(int size){
        List<Agent> agents = new ArrayList<>();
        List<AgentData> agentDataList = readChromosome(jsonFileName);
        for (AgentData agentData : agentDataList) {
            Agent agent = new Agent(agentData.data);
            int generation = Integer.parseInt(agentData.agentName.substring(0, agentData.agentName.indexOf("-")));
            int id = Integer.parseInt(agentData.agentName.substring(agentData.agentName.indexOf("-") + 1));
            agent.init(generation, id, null);
            agent.setFitness(agentData.fitness);
            agents.add(agent);
        }
        agents.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
        if (agents.size() > size) {
            agents = agents.subList(0, size);
        }
        
        return agents;
    }
    
    public void logLevelResult(MarioResult result, String level) {
        String data = getResultAsCSVLine(result);
        logCSV(LEVEL_FILE, data);
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
