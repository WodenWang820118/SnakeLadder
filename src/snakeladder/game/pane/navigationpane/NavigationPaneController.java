package snakeladder.game.pane.navigationpane;

import java.util.Properties;

public class NavigationPaneController {
  
  // view
  private NavigationPane np;

  // model
  private NavigationPaneModel npModel;
  private StatusModel statusModel;

  public NavigationPaneController(
    NavigationPane np,
    NavigationPaneModel npModel,
    StatusModel statusModel,
    Properties properties) {
    
    this.np = np;
    this.npModel = npModel;
    this.statusModel = statusModel;
  }
    
  public NavigationPane getNp() {
    return np;
  }

  public NavigationPaneModel getNpModel() {
    return npModel;
  }

  public StatusModel getStatusModel() {
    return statusModel;
  }
}
