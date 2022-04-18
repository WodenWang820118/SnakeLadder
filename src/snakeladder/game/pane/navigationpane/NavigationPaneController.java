package snakeladder.game.pane.navigationpane;

import java.util.Properties;

import snakeladder.game.pane.navigationpane.die.DieModel;
import snakeladder.game.pane.navigationpane.status.StatusModel;

public class NavigationPaneController {

  // view
  private NavigationPane np;
  
  // models
  private NavigationPaneModel npModel;
  private StatusModel statusModel;
  private DieModel dieModel;
  private ManualDieButton manualDieButton;

  public NavigationPaneController(
    NavigationPane np,
    NavigationPaneModel npModel,
    StatusModel statusModel,
    DieModel dieModel,
    ManualDieButton manualDieButton) {
    this.np = np;
    this.npModel = npModel;
    this.statusModel = statusModel;
    this.dieModel = dieModel;
    this.manualDieButton = manualDieButton;
  }

  public void initNavigationPane(Properties properties) {
    np.initNavigationPane(properties);
  }

  public void setupDieValues() {
    dieModel.setupDieValues();
  }

  public NavigationPane getNavigationPane() {
    return np;
  }

  public NavigationPaneModel getNavigationPaneModel() {
    return npModel;
  }

  public StatusModel getStatusModel() {
    return statusModel;
  }

  public DieModel getDieModel() {
    return dieModel;
  }

  public ManualDieButton getManualDieButton() {
    return manualDieButton;
  }

}
