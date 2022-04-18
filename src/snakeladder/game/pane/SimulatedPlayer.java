package snakeladder.game.pane;

import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.util.Monitor;
import snakeladder.game.pane.navigationpane.NavigationPane;
import snakeladder.game.pane.navigationpane.die.DieModel;

public class SimulatedPlayer extends Thread {

  private PaneController paneController;
  private NavigationPane np = paneController.getNavigationPane();
  private DieModel dieModel = paneController.getDieModel();

  public SimulatedPlayer() {}

  public void setPaneController(PaneController paneController) {
    this.paneController = paneController;
  }
  
  public void run() {
    while (true) {
      Monitor.putSleep();
      np.getHandBtn().show(1);
      paneController.roll(this.dieModel.getDieValue());
      GameGrid.delay(1000);
      np.getHandBtn().show(0);
    }
  }
}
