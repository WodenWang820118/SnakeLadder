package snakeladder.game.pane.gamepane;

import java.util.List;
import java.util.Properties;

import ch.aplu.jgamegrid.GameGrid;
import snakeladder.game.pane.Puppet;

public class GamePaneController extends GameGrid {

  // view
  private GamePane gp;

  // model
  private GamePaneModel gpModel;

  public GamePaneController (
    GamePane gp,
    GamePaneModel gpModel) {
    this.gp = gp;
    this.gpModel = gpModel;
  }

  public int getNumberOfPlayers() {
    return this.gpModel.getNumberOfPlayers();
  }

  public Puppet getPuppet() {
    return gpModel.getPuppet();
  }

  public void switchToNextPuppet() {
    gpModel.switchToNextPuppet();
  }

  public List<Puppet> getAllPuppets() {
    return gpModel.getPuppets();
  }

  public void resetAllPuppets() {
    for (Puppet puppet: this.gpModel.getPuppets()) {
      puppet.resetToStartingPoint();
    }
  }

  public void createSnakesLadders(Properties properties) {
    this.gpModel.createSnakesLadders(properties);
  }

  public void setupPlayers(Properties properties) {
    this.gpModel.setupPlayers(properties);
  }

  public void initGamePane(Properties properties) {
    gp.initGamePane(properties);
    createSnakesLadders(properties);
    setupPlayers(properties);
    gp.setGamePaneSnakeLadderImg("sprites/gamepane_snakeladder.png");
  }

  public GamePane getGamePane() {
    return gp;
  }

  public GamePaneModel getGpModel() {
    return gpModel;
  }
}
