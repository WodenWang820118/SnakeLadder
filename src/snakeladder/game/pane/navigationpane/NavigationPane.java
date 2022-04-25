package snakeladder.game.pane.navigationpane;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.GGButtonListener;
import ch.aplu.jgamegrid.GGCheckButton;
import ch.aplu.jgamegrid.GGCheckButtonListener;
import ch.aplu.jgamegrid.GGSound;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import ch.aplu.util.*;
import snakeladder.game.pane.*;
import snakeladder.game.pane.gamepane.GamePaneModel;
import snakeladder.utility.ServicesRandom;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

@SuppressWarnings("serial")
public class NavigationPane extends GameGrid implements GGButtonListener{

  // fields
  private boolean isAuto;
  private boolean isToggle = false;

  // dice related
  private final int RANDOM_ROLL_TAG = -1;
  private final Location dieBoardLocation = new Location(100, 180);
  private int numberOfDice;
  private int numberOfDiceUsed = 0;

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
  private DieBoard dieBoard;
  private StatusBoard statusBoard;

  public NavigationPane(Properties properties, DieBoard dieBoard, StatusBoard statusBoard) {
    this.properties = properties;
    this.dieBoard = dieBoard;
    this.statusBoard = statusBoard;

    this.numberOfDice =  //Number of six-sided dice
            (properties.getProperty("dice.count") == null)
                    ? 1  // default
                    : Integer.parseInt(properties.getProperty("dice.count"));
    System.out.println("numberOfDice = " + numberOfDice);
    isAuto = Boolean.parseBoolean(properties.getProperty("autorun"));
    autoChk = new GGCheckButton("Auto Run", YELLOW, TRANSPARENT, isAuto);
    System.out.println("autorun = " + isAuto);
    initNavigationPane();
  }

  private void initNavigationPane() {
    setSimulationPeriod(200);
    setBgImagePath("sprites/navigationpane.png");
    setCellSize(1);
    setNbHorzCells(200);
    setNbVertCells(600);
    doRun();
  }

  public void setupDieValues(GamePaneModel gpModel) {
    npModel.setupDieValues(gpModel);
  }

  public void setGamePlayCallback(GamePlayCallback gamePlayCallback) {
    this.gamePlayCallback = gamePlayCallback;
  }
  
  // setter injection
  public void setPaneController(PaneController pc) {
    this.pc = pc;
    this.gpModel = pc.gpController.getGpModel();
    this.npModel = pc.npController.getNpModel();
    setupDieValues(gpModel);
    // setup the record stats for tracking later
    this.npModel.setupPlayerRecords(gpModel);
  }

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

  public void prepareRoll(int currentIndex) {
    // Game over
    if (currentIndex == 100) {
      playSound(GGSound.FADE);
      statusBoard.showStatus("Click the hand!");
      statusBoard.showResult("Game over");
      //TODO: show all the player statistics
      statusBoard.showStats(pc);
      npModel.setGameOver(true);
      handBtn.setEnabled(true);

      List<String> playerPositions = new ArrayList<>();
      for (Puppet puppet: gpModel.getPuppets()) {
        playerPositions.add(puppet.getCellIndex() + "");
      }
      gamePlayCallback.finishGameWithResults(npModel.getNbRolls() % gpModel.getNumberOfPlayers(), playerPositions);
      gpModel.resetAllPuppets();
    } else {
      playSound(GGSound.CLICK);
      statusBoard.showStatus("Click the hand!");
      String result = gpModel.getPuppet().getPuppetName() + " - pos: " + currentIndex;
      statusBoard.showResult(result);

      // task3 Check if Puppet are on the same grid
      int index = gpModel.getCurrentPuppetIndex();
      int n = 0;
      for(Puppet puppet : gpModel.getPuppets()){
        if(n != index && puppet.getCellIndex() == currentIndex){
          puppet.setGoBack(true);
          puppet.go(-1);
        }
        n ++;
      }

      if(gpModel.getPuppet().getGoBack() == true){
        gpModel.switchToNextPuppet();
        gpModel.switchToNextPuppet();
        gpModel.getPuppet().setGoBack(false);
      } else {
        gpModel.switchToNextPuppet();
      }
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
    statusBoard.showStatus("Moving...");
    statusBoard.showPips("Pips: " + nb);
    int newScore = npModel.getNbRolls() + 1;
    npModel.setNbRolls(newScore);
    statusBoard.showScore("# Rolls: " + newScore);
    gpModel.getPuppet().go(nb);
  }

  public void prepareBeforeRoll() {
    // enable the hand animation
    if (numberOfDiceUsed == numberOfDice){
      handBtn.setEnabled(false);
      numberOfDiceUsed = 0;
    } else {
      numberOfDiceUsed ++;
    }
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
    statusBoard.showStatus("Rolling...");
    statusBoard.showPips("");

    removeActors(Die.class);
    pc.getCup().roll(nb);
    addActor(pc.getCup().getDice().get(pc.getCup().getDice().size()-1), dieBoardLocation);
  }

  public void buttonPressed(GGButton btn) {
  }

  public void buttonReleased(GGButton btn) {
  }

  public void checkAuto() {
    if (isAuto) Monitor.wakeUp();
  }

  public GGButton getHandBtn() {
    return handBtn;
  }

  // get total num of the dice
  public int getNumberOfDice(){
    return numberOfDice;
  }
}
