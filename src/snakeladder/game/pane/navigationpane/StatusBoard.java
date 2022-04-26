package snakeladder.game.pane.navigationpane;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGTextField;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import snakeladder.game.pane.PaneController;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StatusBoard extends GameGrid {
  private GGTextField pipsField;
  private GGTextField statusField;
  private GGTextField resultField;
  private GGTextField scoreField;

  private final Location pipsLocation = new Location(70, 230);
  private final Location statusLocation = new Location(20, 330);
  private final Location statusDisplayLocation = new Location(100, 320);
  private final Location scoreLocation = new Location(20, 430);
  private final Location scoreDisplayLocation = new Location(100, 430);
  private final Location resultLocation = new Location(20, 495);
  private final Location resultDisplayLocation = new Location(100, 495);

  void addStatusFields(NavigationPane np) {
    pipsField = new GGTextField(np, "", pipsLocation, false);
    pipsField.setFont(new Font("Arial", Font.PLAIN, 16));
    pipsField.setTextColor(YELLOW);
    pipsField.show();

    np.addActor(new Actor("sprites/linedisplay.gif"), statusDisplayLocation);
    statusField = new GGTextField(np, "Click the hand!", statusLocation, false);
    statusField.setFont(new Font("Arial", Font.PLAIN, 16));
    statusField.setTextColor(YELLOW);
    statusField.show();

    np.addActor(new Actor("sprites/linedisplay.gif"), scoreDisplayLocation);
    scoreField = new GGTextField(np, "# Rolls: 0", scoreLocation, false);
    scoreField.setFont(new Font("Arial", Font.PLAIN, 16));
    scoreField.setTextColor(YELLOW);
    scoreField.show();

    np.addActor(new Actor("sprites/linedisplay.gif"), resultDisplayLocation);
    resultField = new GGTextField(np, "Current pos: 0", resultLocation, false);
    resultField.setFont(new Font("Arial", Font.PLAIN, 16));
    resultField.setTextColor(YELLOW);
    resultField.show();
  }

  public void showPips(String text) {
    pipsField.setText(text);
    if (text != "") System.out.println(text);
  }

  public void showStatus(String text) {
    statusField.setText(text);
    System.out.println("Status: " + text);
  }

  public void showScore(String text) {
    scoreField.setText(text);
    System.out.println(text);
  }

  public void showResult(String text) {
    resultField.setText(text);
    System.out.println("Result: " + text);
  }
  
  public void showStats(PaneController pc) {
    Map<Integer, HashMap<Integer, Integer>> diceRollingRecord
      = pc.getNpModel().getDiceRollingRecord();

    for (Entry<Integer, HashMap<Integer, Integer>> entry
          : diceRollingRecord.entrySet()) {
      int puppetIndex = entry.getKey();
      Map<Integer, Integer> playerRecord = entry.getValue();
      System.out.print("Player " + (puppetIndex + 1) + " rolled: ");
      
      // get the maximum value of keys for printing purpose
      List<Integer> diceValues = new ArrayList<Integer>();
      for (Integer key: playerRecord.keySet()) {
        diceValues.add(key);
      }

      int maxValue = Collections.max(diceValues);

      // printing stats
      for (Entry<Integer, Integer> entry2 : playerRecord.entrySet()) {
        int diceValue = entry2.getKey();
        int diceCount = entry2.getValue();
       
        if (entry2.getKey() == maxValue) {
          System.out.print(diceValue + "-" + diceCount);
        } else {
          System.out.print(diceValue + "-" + diceCount + ", ");
        }
      }
      System.out.println();
    }
  }

  public void showTraversals(PaneController pc) {
    HashMap<Integer, HashMap<String, Integer>> traversals = pc.getNpModel().getTraversalRecord();
    for (Entry<Integer, HashMap<String, Integer>> entry : traversals.entrySet()) {
      int playerIndex = entry.getKey();
      Map<String, Integer> playerTraversals = entry.getValue();

      System.out.print("Player " + (playerIndex + 1) + " traversed: ");
      for (Entry<String, Integer> entry2 : playerTraversals.entrySet()) {
        String traversal = entry2.getKey();
        int count = entry2.getValue();
        if (traversal.equals("up")) {
          System.out.print(traversal + "-" + count + ", ");
        } else if (traversal.equals("down")) {
          System.out.print(traversal + "-" + count);
        }
      }
      System.out.println();
    }
  }
}
