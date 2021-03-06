package snakeladder.game.pane.navigationpane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import snakeladder.game.pane.gamepane.GamePaneModel;

public class NavigationPaneModel {

  private final int RANDOM_ROLL_TAG = -1;
  private int nbRolls = 0;
  private volatile boolean isGameOver = false;
  private List<List<Integer>> dieValues = new ArrayList<>();
  private Properties properties;
  // the first element of the hashmap is the puppetIndex
  // the second element as a hashmap stores the die records
  private Map<Integer, HashMap<Integer, Integer>> diceRollingRecord = new HashMap<>();
  private Map<Integer, Integer> playerRecord;
  // traversal records
  private Map<Integer, HashMap<String, Integer>> traversalRecord = new HashMap<>();
  private Map<String, Integer> personalTraversal;
  

  public NavigationPaneModel(Properties properties) {
    this.properties = properties;
  }

  // adds the initial record stats to each player by the counter
  // note the counter is also used as the puppetIndex to track the puppet
  // called after setupDieValues()
  public void setupPlayerRecords(GamePaneModel gpModel) {
    for (int i = 0; i < gpModel.getNumberOfPlayers(); i++) {
      diceRollingRecord.put(i, (HashMap<Integer, Integer>) this.initRecordStats());
    }
  }

  public void setupPlayerTraversalRecord(GamePaneModel gpModel) {
    for (int i = 0; i < gpModel.getNumberOfPlayers(); i++) {
      traversalRecord.put(i, (HashMap<String, Integer>) this.initTraversalStats());
    }
  }

  // initializes the record stats according to the number of dice
  private Map<Integer, Integer> initRecordStats() {
    int numberOfDice =  //Number of six-sided dice
            (properties.getProperty("dice.count") == null)
                    ? 1  // default
                    : Integer.parseInt(properties.getProperty("dice.count"));
    int counter = numberOfDice * 6;
    // the minimum value is the number of dice
    this.playerRecord = new HashMap<>();
    for (int i = numberOfDice; i <= counter; i++) {
      playerRecord.put(i, 0);
    }
    return playerRecord;
  }

  private Map<String, Integer> initTraversalStats() {
    this.personalTraversal = new HashMap<>();
    personalTraversal.put("up", 0);
    personalTraversal.put("down", 0);
    return personalTraversal;
  }

  public void setupDieValues(GamePaneModel gpModel) {
    for (int i = 0; i < gpModel.getNumberOfPlayers(); i++) {
      java.util.List<Integer> dieValuesForPlayer = new ArrayList<>();
      if (properties.getProperty("die_values." + i) != null) {
        String dieValuesString = properties.getProperty("die_values." + i);
        String[] dieValueStrings = dieValuesString.split(",");
        for (int j = 0; j < dieValueStrings.length; j++) {
          dieValuesForPlayer.add(Integer.parseInt(dieValueStrings[j]));
        }
        getDieValues().add(dieValuesForPlayer);
      } else {
        System.out.println("All players need to be set a die value for the full testing mode to run. " +
                "Switching off the full testing mode");
        // dieValues = null;
        setDieValues(null);
        break;
      }
    }
    System.out.println("dieValues = " + getDieValues());
  }

  public int getDieValue(GamePaneModel gpModel) {
    if (getDieValues() == null) {
      return RANDOM_ROLL_TAG;
    }
    int currentRound = getNbRolls() / gpModel.getNumberOfPlayers();
    int playerIndex = getNbRolls() % gpModel.getNumberOfPlayers();
    if (getDieValues().get(playerIndex).size() > currentRound) {
      return getDieValues().get(playerIndex).get(currentRound);
    }
    return RANDOM_ROLL_TAG;
  }

  public List<List<Integer>> getDieValues() {
    return dieValues;
  }

  public void setDieValues(List<List<Integer>> dieValues) {
    this.dieValues = dieValues;
  }

  public int getNbRolls() {
    return nbRolls;
  }

  public void setNbRolls(int nbRolls) {
    this.nbRolls = nbRolls;
  }

  public boolean isGameOver() {
    return isGameOver;
  }

  public void setGameOver(boolean isGameOver) {
    this.isGameOver = isGameOver;
  }

  public HashMap<Integer, HashMap<Integer, Integer>> getDiceRollingRecord() {
    return (HashMap<Integer, HashMap<Integer, Integer>>) diceRollingRecord;
  }

  public HashMap<Integer, HashMap<String, Integer>> getTraversalRecord() {
    return (HashMap<Integer, HashMap<String, Integer>>) traversalRecord;
  }
}
