package snakeladder.game.pane.navigationpane;

import ch.aplu.jgamegrid.GameGrid;

public class NavigationPaneModel extends GameGrid {

  private GamePlayCallback gamePlayCallback;
  public NavigationPaneModel() {}

  public void setGamePlayCallback(GamePlayCallback gamePlayCallback) {
    this.gamePlayCallback = gamePlayCallback;
  }

}