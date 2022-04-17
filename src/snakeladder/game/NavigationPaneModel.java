package snakeladder.game;

import java.util.ArrayList;
import java.util.List;

import ch.aplu.jgamegrid.GGSound;
import ch.aplu.jgamegrid.GameGrid;
import snakeladder.utility.ServicesRandom;
import ch.aplu.util.Monitor;

public class NavigationPaneModel extends GameGrid {
  public StatusModel statusModel;
  public NavigationPane np;
  public GamePane gp;
  private GamePlayCallback gamePlayCallback;

  private volatile boolean isGameOver = false;
  private int nbRolls = 0;
  private boolean isAuto;
  private final int RANDOM_ROLL_TAG = -1;
  

  public NavigationPaneModel() {
    this.statusModel = np.getStatusModel();
  }
  // TODO: decouple the model from the view
  public void connectNavigationPane(NavigationPane np) {
    this.np = np;
  }

  // TODO: decouple the model from the view
  public void connectGamePane(GamePane gp) {
    this.gp = gp;
  }

  public void prepareRoll(int currentIndex) {
    if (currentIndex == 100) {
      playSound(GGSound.FADE);
      this.statusModel.showStatus(np.getStatusField(), "Click the hand!");
      this.statusModel.showResult(np.getResultField(), "Game over");
      isGameOver = true;
      np.getHandBtn().setEnabled(true);

      List<String> playerPositions = new ArrayList<>();

      for (Puppet puppet: gp.getAllPuppets()) {
        playerPositions.add(puppet.getCellIndex() + "");
      }
      gamePlayCallback.finishGameWithResults(nbRolls % gp.getNumberOfPlayers(), playerPositions);
      gp.resetAllPuppets();
    } else {
      playSound(GGSound.CLICK);
      this.statusModel.showStatus(np.getStatusField(), "Done. Click the hand!");
      String result = gp.getPuppet().getPuppetName() + " - pos: " + currentIndex;
      this.statusModel.showResult(np.getResultField(), result);
      gp.switchToNextPuppet();
      // System.out.println("current puppet - auto: " + gp.getPuppet().getPuppetName() + "  " + gp.getPuppet().isAuto() );

      if (isAuto) {
        Monitor.wakeUp();
      } else if (gp.getPuppet().isAuto()) {
        Monitor.wakeUp();
      } else {
        np.getHandBtn().setEnabled(true);
      }
    }
  }

  public void startMoving(int nb) {
    this.statusModel.showStatus(np.getStatusField(), "Moving...");
    this.statusModel.showPips(np.getStatusField(), "Pips: " + nb);
    this.statusModel.showScore(np.getScoreField(), "# Rolls: " + (++nbRolls));
    gp.getPuppet().go(nb);
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
    this.statusModel.showStatus(np.getStatusField(), "Rolling...");
    this.statusModel.showPips(np.getPipField(),"");

    removeActors(Die.class);
    Die die = new Die(nb, this);
    addActor(die, np.getDieBoardLocation());
  }

  public void setGamePlayCallback(GamePlayCallback gamePlayCallback) {
    this.gamePlayCallback = gamePlayCallback;
  }
}