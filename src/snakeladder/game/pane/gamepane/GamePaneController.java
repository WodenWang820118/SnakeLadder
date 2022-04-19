package snakeladder.game.pane.gamepane;

import java.util.Properties;

public class GamePaneController {

  // view
  private GamePane gp;

  // model
  private GamePaneModel gpModel;

  public GamePaneController (GamePane gp, GamePaneModel gpModel, Properties properties) {
    this.gp = gp;
    this.gpModel = gpModel;
  }

  public GamePane getGp() {
    return gp;
  }

  public GamePaneModel getGpModel() {
    return gpModel;
  }
}
