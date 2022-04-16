package snakeladder.game;
// java utility modules
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
// game utility modules
import snakeladder.utility.PropertiesLoader;

/**
 * The class is served as the GamePane's mechanism model
 */
public class GamePaneModel {
    private NavigationPane np;
    private int numberOfPlayers = 1;
    private int currentPuppetIndex = 0;
    private List<Boolean> playerManualMode;
    private ArrayList<Connection> connections = new ArrayList<Connection>();
    private List<Puppet> puppets =  new ArrayList<>();

    GamePaneModel(Properties properties){}

    public void createSnakesLadders(Properties properties) {
        connections.addAll(PropertiesLoader.loadSnakes(properties));
        connections.addAll(PropertiesLoader.loadLadders(properties));
    }

    public void setupPlayers(Properties properties) {
        numberOfPlayers = Integer.parseInt(properties.getProperty("players.count"));
        playerManualMode = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            playerManualMode.add(Boolean.parseBoolean(properties.getProperty("players." + i + ".isAuto")));
        }
        System.out.println("playerManualMode = " + playerManualMode);
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setCurrentPuppetIndex(int currentPuppetIndex) {
        this.currentPuppetIndex = currentPuppetIndex;
    }

    public int getCurrentPuppetIndex() {
        return currentPuppetIndex;
    }

    public List<Boolean> getPlayerManualMode() {
        return playerManualMode;
    }

    public void setNavigationPane(NavigationPane np) {
        this.np = np;
    }

    public NavigationPane getNavigationPane() {
        return np;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public List<Puppet> getPuppets() {
        return puppets;
    }
}
