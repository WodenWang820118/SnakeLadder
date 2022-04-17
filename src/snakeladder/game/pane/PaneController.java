package snakeladder.game.pane;

import java.util.ArrayList;
import java.util.List;

import ch.aplu.jgamegrid.GGSound;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.util.Monitor;
import snakeladder.game.pane.gamepane.GamePane;
import snakeladder.game.pane.gamepane.GamePaneController;
import snakeladder.game.pane.gamepane.GamePaneModel;
import snakeladder.game.pane.navigationpane.GamePlayCallback;
import snakeladder.game.pane.navigationpane.ManualDieButton;
import snakeladder.game.pane.navigationpane.NavigationPane;
import snakeladder.game.pane.navigationpane.NavigationPaneController;
import snakeladder.game.pane.navigationpane.NavigationPaneModel;
import snakeladder.game.pane.navigationpane.StatusModel;
import snakeladder.game.pane.navigationpane.die.Die;
import snakeladder.game.pane.navigationpane.die.DieModel;
import snakeladder.utility.ServicesRandom;

public class PaneController extends GameGrid implements GamePlayCallback {
  
  // fields
  private final int RANDOM_ROLL_TAG = -1;
  private final int MAX_PUPPET_SPRITES = 4;
  final int animationStep = 10;
  private volatile boolean isGameOver = false;
  private int nbRolls = 0;
  private boolean isAuto;

  // components
  private GamePaneController gpController;
  private NavigationPaneController npController;
  
  // models from two controllers
  private GamePaneModel gpModel = gpController.getGpModel();
  private NavigationPaneModel npModel = npController.getNavigationPaneModel();
  private GamePane gp = gpController.getGamePane();
  private NavigationPane np = npController.getNavigationPane();
  private StatusModel statusModel = npController.getStatusModel();
  private DieModel dieModel = npController.getDieModel();
  private ManualDieButton manualDieButton = npController.getManualDieButton();
  private GamePlayCallback gamePlayCallback; // TODO: need to implement this interface

  public PaneController(GamePaneController gpController, NavigationPaneController npController) {
    this.gpController = gpController;
    this.npController = npController;
  }

  public void createGamePaneGui() {
    for (int i = 0; i < gpModel.getNumberOfPlayers(); i++) {
      boolean isAuto = gpModel.getPlayerManualMode().get(i);
      int spriteImageIndex = i % MAX_PUPPET_SPRITES;
      String puppetImage = "sprites/cat_" + spriteImageIndex + ".gif";
      Puppet puppet = new Puppet(puppetImage);
      puppet.setPaneController(this); // set the paneController to use the act method
      puppet.setAuto(isAuto);
      puppet.setPuppetName("Player " + (i + 1));
      addActor(puppet, gp.getStartLoction());
      gpModel.getPuppets().add(puppet);
    }
  }

  public void startMoving(int nb) {
    statusModel.showStatus(np.getStatusField(), "Moving...");
    statusModel.showPips(np.getStatusField(), "Pips: " + nb);
    statusModel.showScore(np.getScoreField(), "# Rolls: " + (++nbRolls));
    gpModel.getPuppet().go(nb);
  }

  public void prepareBeforeRoll() {
    np.getHandBtn().setEnabled(false);
    if (isGameOver) {
      isGameOver = false;
      nbRolls = 0;
    }
  }

  public void roll(int rollNumber) {
    int nb = rollNumber;
    if (rollNumber == RANDOM_ROLL_TAG) {
      nb = ServicesRandom.get().nextInt(6) + 1;
    }
    statusModel.showStatus(np.getStatusField(), "Rolling...");
    statusModel.showPips(np.getPipField(),"");

    removeActors(Die.class);
    Die die = new Die(nb, this);
    addActor(die, np.getDieBoardLocation());
  }

  public void prepareRoll(int currentIndex) {
    if (currentIndex == 100) {
      playSound(GGSound.FADE);
      statusModel.showStatus(np.getStatusField(), "Click the hand!");
      statusModel.showResult(np.getResultField(), "Game over");
      isGameOver = true;
      np.getHandBtn().setEnabled(true);

      List<String> playerPositions = new ArrayList<>();

      for (Puppet puppet: gpModel.getPuppets()) {
        playerPositions.add(puppet.getCellIndex() + "");
      }
      gamePlayCallback.finishGameWithResults(nbRolls % gpModel.getNumberOfPlayers(), playerPositions);
      gpModel.resetAllPuppets();
    } else {
      playSound(GGSound.CLICK);
      statusModel.showStatus(np.getStatusField(), "Done. Click the hand!");
      String result = gpModel.getPuppet().getPuppetName() + " - pos: " + currentIndex;
      statusModel.showResult(np.getResultField(), result);
      gpModel.switchToNextPuppet();
      // System.out.println("current puppet - auto: " + gp.getPuppet().getPuppetName() + "  " + gp.getPuppet().isAuto() );

      if (isAuto) {
        Monitor.wakeUp();
      } else if (gpModel.getPuppet().isAuto()) {
        Monitor.wakeUp();
      } else {
        np.getHandBtn().setEnabled(true);
      }
    }
  }

  public GamePaneController getGpController() {
    return gpController;
  }

  public NavigationPaneController getNpController() {
    return npController;
  }

  public GamePane getGamePane() {
    return gp;
  }

  public NavigationPane getNavigationPane() {
    return np;
  }

  public GamePaneModel getGamePaneModel() {
    return gpModel;
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

  @Override
  public void finishGameWithResults(int winningPlayerIndex, List<String> playerCurrentPositions) {
    // TODO Auto-generated method stub
    
  }
}
