package snakeladder.game.pane;

import java.util.Properties;

import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.util.Monitor;
import snakeladder.game.pane.gamepane.ChangeConnectionStrategy;
import snakeladder.game.pane.gamepane.GamePane;
import snakeladder.game.pane.gamepane.GamePaneController;
import snakeladder.game.pane.gamepane.GamePaneModel;
import snakeladder.game.pane.navigationpane.NavigationPane;
import snakeladder.game.pane.navigationpane.NavigationPaneController;
import snakeladder.game.pane.navigationpane.NavigationPaneModel;

public class PaneController extends GameGrid {
  
  public GamePaneController gpController;
  public NavigationPaneController npController;
  private Cup cup;
  private ChangeConnectionStrategy cc;
  

  public PaneController(GamePaneController gpController, NavigationPaneController npController, Cup cup, ChangeConnectionStrategy cc, Properties properties) {
    this.gpController = gpController;
    this.npController = npController;
    this.cup = cup;
    this.cc = cc;
    cup.setPaneController(this);
    new SimulatedPlayer().start();

    gpController.getGpModel().createSnakesLadders(properties);
    gpController.getGpModel().setupPlayers(properties);
    gpController.getGp().setGamePaneSnakeLadderImg("sprites/gamepane_snakeladder.png");
  }

  public void createGpGui(PaneController pc) {
    gpController.getGp().createGui(this);
  }

  public void createNpGui() {
    npController.getNp().createGui();
  }

  public GamePaneModel getGpModel() {
    return gpController.getGpModel();
  }

  public NavigationPaneModel getNpModel() {
    return npController.getNpModel();
  }

  public GamePane getGp() {
    return gpController.getGp();
  }

  public NavigationPane getNp() {
    return npController.getNp();
  }

  public Cup getCup(){
    return cup;
  }

  public ChangeConnectionStrategy getCC(){
    return cc;
  }

  private class SimulatedPlayer extends Thread {
    public void run() {
      while (true) {
        Monitor.putSleep();
        getNp().getHandBtn().show(1);
        // Roll the dice continuously
        for(int i = 0; i < getNp().getNumberOfDice(); i++){
          getNp().roll(getNp().getDieValue(getGpModel()));
          delay(1000);
        }
        getNp().getHandBtn().show(0);
      }
    }
  }
}
