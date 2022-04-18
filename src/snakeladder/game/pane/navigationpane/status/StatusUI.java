package snakeladder.game.pane.navigationpane.status;

import java.awt.Font;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGTextField;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import snakeladder.game.pane.navigationpane.NavigationPane;

public class StatusUI extends GameGrid {
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

  NavigationPane np;

  public StatusUI(NavigationPane np) {
    this.np = np;
  }

  public void addStatusFields() {
    pipsField = new GGTextField(np, "", pipsLocation, false);
    pipsField.setFont(new Font("Arial", Font.PLAIN, 16));
    pipsField.setTextColor(YELLOW);
    pipsField.show();

    addActor(new Actor("sprites/linedisplay.gif"), statusDisplayLocation);
    statusField = new GGTextField(np, "Click the hand!", statusLocation, false);
    statusField.setFont(new Font("Arial", Font.PLAIN, 16));
    statusField.setTextColor(YELLOW);
    statusField.show();

    addActor(new Actor("sprites/linedisplay.gif"), scoreDisplayLocation);
    scoreField = new GGTextField(np, "# Rolls: 0", scoreLocation, false);
    scoreField.setFont(new Font("Arial", Font.PLAIN, 16));
    scoreField.setTextColor(YELLOW);
    scoreField.show();

    addActor(new Actor("sprites/linedisplay.gif"), resultDisplayLocation);
    resultField = new GGTextField(np, "Current pos: 0", resultLocation, false);
    resultField.setFont(new Font("Arial", Font.PLAIN, 16));
    resultField.setTextColor(YELLOW);
    resultField.show();
  }

  public GGTextField getPipField() {
    return pipsField;
  }

  public GGTextField getStatusField() {
    return statusField;
  }

  public GGTextField getResultField() {
    return resultField;
  }

  public GGTextField getScoreField() {
    return scoreField;
  }
}
