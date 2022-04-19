package snakeladder.game.pane.navigationpane;

import ch.aplu.jgamegrid.*;
import java.awt.*;
import ch.aplu.util.*;
import snakeladder.game.pane.Die;
import snakeladder.game.pane.GamePlayCallback;
import snakeladder.game.pane.PaneController;
import snakeladder.game.pane.Puppet;
import snakeladder.game.pane.gamepane.GamePaneModel;
import snakeladder.utility.ServicesRandom;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

@SuppressWarnings("serial")
public class NavigationPane extends GameGrid implements GGButtonListener {

  // fields
  private boolean isAuto;
  private boolean isToggle = false;

  // dice related
  private final int RANDOM_ROLL_TAG = -1;
  private final Location dieBoardLocation = new Location(100, 180);

  // animation and toggle location
  private final Location handBtnLocation = new Location(110, 70);
  private final Location autoChkLocation = new Location(15, 375);
  private final Location toggleModeLocation = new Location(95, 375);

  private GGButton handBtn = new GGButton("sprites/handx.gif");
  private GGCheckButton autoChk;
  private GGCheckButton toggleCheck =
          new GGCheckButton("Toggle Mode", YELLOW, TRANSPARENT, isToggle);

  // components
  private Properties properties;
  private GamePlayCallback gamePlayCallback;
  private PaneController pc;
  private GamePaneModel gpModel;
  private NavigationPaneModel npModel;
  private StatusModel statusModel;
  private DieBoard dieBoard;
  private StatusBoard statusBoard;

  public NavigationPane(Properties properties, DieBoard dieBoard, StatusBoard statusBoard) {
    this.properties = properties;
    this.dieBoard = dieBoard;
    this.statusBoard = statusBoard;

    int numberOfDice =  //Number of six-sided dice
            (properties.getProperty("dice.count") == null)
                    ? 1  // default
                    : Integer.parseInt(properties.getProperty("dice.count"));
    System.out.println("numberOfDice = " + numberOfDice);
    isAuto = Boolean.parseBoolean(properties.getProperty("autorun"));
    autoChk = new GGCheckButton("Auto Run", YELLOW, TRANSPARENT, isAuto);
    System.out.println("autorun = " + isAuto);
    initNavigationPane();
  }

  void initNavigationPane() {
    setSimulationPeriod(200);
    setBgImagePath("sprites/navigationpane.png");
    setCellSize(1);
    setNbHorzCells(200);
    setNbVertCells(600);
    doRun();
  }

  void setupDieValues(GamePaneModel gpModel) {
    npModel.setupDieValues(gpModel);
  }

  public void setGamePlayCallback(GamePlayCallback gamePlayCallback) {
    this.gamePlayCallback = gamePlayCallback;
  }

  public void setPaneController(PaneController pc) {
    this.pc = pc;
    this.gpModel = pc.gpController.getGpModel();
    this.npModel = pc.npController.getNpModel();
    this.statusModel = pc.npController.getStatusModel();
    setupDieValues(gpModel);
  }

  // TODO: should be in dieboard specifically
  public int getDieValue(GamePaneModel gpModel) {
    return npModel.getDieValue(gpModel);
  }

  public void createGui() {
    addActor(new Actor("sprites/dieboard.gif"), dieBoardLocation);

    handBtn.addButtonListener(this);
    addActor(handBtn, handBtnLocation);
    addActor(autoChk, autoChkLocation);
    autoChk.addCheckButtonListener(new GGCheckButtonListener() {
      @Override
      public void buttonChecked(GGCheckButton button, boolean checked)
      {
        isAuto = checked;
        if (isAuto)
          Monitor.wakeUp();
      }
    });

    addActor(toggleCheck, toggleModeLocation);
    toggleCheck.addCheckButtonListener(new GGCheckButtonListener() {
      @Override
      public void buttonChecked(GGCheckButton ggCheckButton, boolean checked) {
        isToggle = checked;
      }
    });

    dieBoard.addDieButtons(this);
    statusBoard.addStatusFields(this);
  }

  public void showStatus(String text) {
    statusModel.showStatus(statusBoard.statusField, text);
  }

  public void prepareRoll(int currentIndex) {
    // Game over
    if (currentIndex == 100) {
      playSound(GGSound.FADE);
      statusModel.showStatus(statusBoard.statusField, "Click the hand!");
      statusModel.showResult(statusBoard.resultField, "Game over");
      npModel.setGameOver(true);
      // isGameOver = true;
      handBtn.setEnabled(true);

      List<String> playerPositions = new ArrayList<>();
      for (Puppet puppet: gpModel.getPuppets()) {
        playerPositions.add(puppet.getCellIndex() + "");
      }
      gamePlayCallback.finishGameWithResults(npModel.getNbRolls() % gpModel.getNumberOfPlayers(), playerPositions);
      gpModel.resetAllPuppets();
    } else {
      playSound(GGSound.CLICK);
      statusModel.showStatus(statusBoard.statusField, "Click the hand!");
      String result = gpModel.getPuppet().getPuppetName() + " - pos: " + currentIndex;
      statusModel.showResult(statusBoard.resultField, result);
      gpModel.switchToNextPuppet();
      // System.out.println("current puppet - auto: " + gp.getPuppet().getPuppetName() + "  " + gp.getPuppet().isAuto() );

      if (isAuto) {
        Monitor.wakeUp();
      } else if (gpModel.getPuppet().isAuto()) {
        Monitor.wakeUp();
      } else {
        handBtn.setEnabled(true);
      }
    }
  }

  public void startMoving(int nb) {
    statusModel.showStatus(statusBoard.statusField, "Moving...");
    statusModel.showPips(statusBoard.pipsField, "Pips: " + nb);
    int newScore = npModel.getNbRolls() + 1;
    npModel.setNbRolls(newScore);
    statusModel.showScore(statusBoard.scoreField, "# Rolls: " + newScore);
    gpModel.getPuppet().go(nb);
  }

  void prepareBeforeRoll() {
    handBtn.setEnabled(false);
    // First click after game over
    if (npModel.isGameOver()) {
      npModel.setGameOver(false);
      npModel.setNbRolls(0);
    }
  }

  public void buttonClicked(GGButton btn) {
    System.out.println("hand button clicked");
    prepareBeforeRoll();
    roll(getDieValue(gpModel));
  }

  public void roll(int rollNumber) {
    int nb = rollNumber;
    if (rollNumber == RANDOM_ROLL_TAG) {
      nb = ServicesRandom.get().nextInt(6) + 1;
    }
    statusModel.showStatus(statusBoard.statusField, "Rolling...");
    statusModel.showPips(statusBoard.pipsField, "");

    removeActors(Die.class);
    Die die = new Die(nb, this);
    addActor(die, dieBoardLocation);
  }

  public void buttonPressed(GGButton btn)
  {
  }

  public void buttonReleased(GGButton btn)
  {
  }

  public void checkAuto() {
    if (isAuto) Monitor.wakeUp();
  }

  public GGButton getHandBtn() {
    return handBtn;
  }
}
