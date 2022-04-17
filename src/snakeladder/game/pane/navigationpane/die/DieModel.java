package snakeladder.game.pane.navigationpane.die;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import snakeladder.game.pane.gamepane.GamePaneModel;

public class DieModel {

  private Properties properties;
  private final int RANDOM_ROLL_TAG = -1;
  private List<List<Integer>> dieValues = new ArrayList<>();
  private int nbRolls = 0;

  private GamePaneModel gpModel;

  public DieModel(Properties properties, GamePaneModel gpModel) {
    this.properties = properties;
    this.gpModel = gpModel;
  }

  public void setupDieValues() {
    for (int i = 0; i < gpModel.getNumberOfPlayers(); i++) {
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
    int currentRound = nbRolls / gpModel.getNumberOfPlayers();
    int playerIndex = nbRolls % gpModel.getNumberOfPlayers();
    if (dieValues.get(playerIndex).size() > currentRound) {
      return dieValues.get(playerIndex).get(currentRound);
    }
    return RANDOM_ROLL_TAG;
  }
}
