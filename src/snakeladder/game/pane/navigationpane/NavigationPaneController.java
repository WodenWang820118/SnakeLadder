package snakeladder.game.pane.navigationpane;

import java.util.Properties;

public class NavigationPaneController {
  
  // view
  private NavigationPane np;
  private DieBoard dieBoard;
  private StatusBoard statusBoard;

  // model
  private NavigationPaneModel npModel;

  public NavigationPaneController(
    NavigationPane np,
    DieBoard dieBoard,
    StatusBoard statusBoard,
    NavigationPaneModel npModel,
    Properties properties) {
    
    this.np = np;
    this.dieBoard = dieBoard;
    this.statusBoard = statusBoard;
    this.npModel = npModel;
  }
    
  public NavigationPane getNp() {
    return np;
  }

  public NavigationPaneModel getNpModel() {
    return npModel;
  }

  public DieBoard getDieBoard() {
    return dieBoard;
  }

  public StatusBoard getStatusBoard() {
    return statusBoard;
  }
}
