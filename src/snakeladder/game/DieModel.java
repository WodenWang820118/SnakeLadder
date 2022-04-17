package snakeladder.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DieModel {
  private GamePaneModel gamePaneModel;
  private Properties properties;
  private List<List<Integer>> dieValues = new ArrayList<>();
  private final int RANDOM_ROLL_TAG = -1;
  private int nbRolls = 0;

  public DieModel(Properties properties) {
      this.properties = properties;
      this.gamePaneModel = new GamePaneModel(properties);
  }

  public void setupDieValues() {
    for (int i = 0; i < this.gamePaneModel.getNumberOfPlayers(); i++) {
      // an arrayList for storing the die values for each player
      java.util.List<Integer> dieValuesForPlayer = new ArrayList<>();
      // get the properties from the test2.properties file
      // the array of string will be split by "," and elements will be parsed to integer, stored in dieValuesForPlayer
      if (properties.getProperty("die_values." + i) != null) {
        String dieValuesString = properties.getProperty("die_values." + i);
        String[] dieValueStrings = dieValuesString.split(",");
        for (int j = 0; j < dieValueStrings.length; j++) {
          dieValuesForPlayer.add(Integer.parseInt(dieValueStrings[j]));
        }
        // the dieValues add the dieValuesForPlayer to the dieValues list for the testing purpose
        dieValues.add(dieValuesForPlayer);
      } else {
        System.out.println("All players need to be set a die value for the full testing mode to run. " +
                "Switching off the full testing mode");
        dieValues = null;
        break;
      }
    }
    System.out.println("dieValues = " + dieValues);
  }

  public int getDieValue() {
    if (dieValues == null) {
      return RANDOM_ROLL_TAG;
    }
    int currentRound = nbRolls / this.gamePaneModel.getNumberOfPlayers();
    int playerIndex = nbRolls % this.gamePaneModel.getNumberOfPlayers();
    if (dieValues.get(playerIndex).size() > currentRound) {
      return dieValues.get(playerIndex).get(currentRound);
    }
    return RANDOM_ROLL_TAG;
  }
}
