package snakeladder.game.pane.navigationpane;

import ch.aplu.jgamegrid.*;
import java.awt.*;
import ch.aplu.util.*;
import snakeladder.game.custom.CustomGGButton;
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
  private final int DIE1_BUTTON_TAG = 1;
  private final int DIE2_BUTTON_TAG = 2;
  private final int DIE3_BUTTON_TAG = 3;
  private final int DIE4_BUTTON_TAG = 4;
  private final int DIE5_BUTTON_TAG = 5;
  private final int DIE6_BUTTON_TAG = 6;
  private final int RANDOM_ROLL_TAG = -1;

  private final Location dieBoardLocation = new Location(100, 180);
  private final Location die1Location = new Location(20, 270);
  private final Location die2Location = new Location(50, 270);
  private final Location die3Location = new Location(80, 270);
  private final Location die4Location = new Location(110, 270);
  private final Location die5Location = new Location(140, 270);
  private final Location die6Location = new Location(170, 270);

  private GGButton die1Button = new CustomGGButton(DIE1_BUTTON_TAG, "sprites/Number_1.png");
  private GGButton die2Button = new CustomGGButton(DIE2_BUTTON_TAG, "sprites/Number_2.png");
  private GGButton die3Button = new CustomGGButton(DIE3_BUTTON_TAG, "sprites/Number_3.png");
  private GGButton die4Button = new CustomGGButton(DIE4_BUTTON_TAG, "sprites/Number_4.png");
  private GGButton die5Button = new CustomGGButton(DIE5_BUTTON_TAG, "sprites/Number_5.png");
  private GGButton die6Button = new CustomGGButton(DIE6_BUTTON_TAG, "sprites/Number_6.png");

  // status related
  private GGTextField pipsField;
  private GGTextField statusField;
  private GGTextField resultField;
  private GGTextField scoreField;

  private final Location pipsLocation = new Location(70, 230);
  private final Location statusLocation = new Location(20, 330);
  private final Location statusDisplayLocation = new Location(100, 320);
  private final Location scoreLocation = new Location(20, 430);
  private final Location scoreDisplayLocation = new Location(100, 430);
  private final Location resultLocation = new Location(20, 495);
  private final Location resultDisplayLocation = new Location(100, 495);

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

  public NavigationPane(Properties properties) {
    this.properties = properties;

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

  void addDieButtons() {
    ManualDieButton manualDieButton = new ManualDieButton();

    addActor(die1Button, die1Location);
    addActor(die2Button, die2Location);
    addActor(die3Button, die3Location);
    addActor(die4Button, die4Location);
    addActor(die5Button, die5Location);
    addActor(die6Button, die6Location);

    die1Button.addButtonListener(manualDieButton);
    die2Button.addButtonListener(manualDieButton);
    die3Button.addButtonListener(manualDieButton);
    die4Button.addButtonListener(manualDieButton);
    die5Button.addButtonListener(manualDieButton);
    die6Button.addButtonListener(manualDieButton);
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

    addDieButtons();
    addStatusFields();
  }

  void addStatusFields() {
    pipsField = new GGTextField(this, "", pipsLocation, false);
    pipsField.setFont(new Font("Arial", Font.PLAIN, 16));
    pipsField.setTextColor(YELLOW);
    pipsField.show();

    addActor(new Actor("sprites/linedisplay.gif"), statusDisplayLocation);
    statusField = new GGTextField(this, "Click the hand!", statusLocation, false);
    statusField.setFont(new Font("Arial", Font.PLAIN, 16));
    statusField.setTextColor(YELLOW);
    statusField.show();

    addActor(new Actor("sprites/linedisplay.gif"), scoreDisplayLocation);
    scoreField = new GGTextField(this, "# Rolls: 0", scoreLocation, false);
    scoreField.setFont(new Font("Arial", Font.PLAIN, 16));
    scoreField.setTextColor(YELLOW);
    scoreField.show();

    addActor(new Actor("sprites/linedisplay.gif"), resultDisplayLocation);
    resultField = new GGTextField(this, "Current pos: 0", resultLocation, false);
    resultField.setFont(new Font("Arial", Font.PLAIN, 16));
    resultField.setTextColor(YELLOW);
    resultField.show();
  }

  public void showStatus(String text) {
    statusModel.showStatus(statusField, text);
  }

  public void prepareRoll(int currentIndex) {
    // Game over
    if (currentIndex == 100) {
      playSound(GGSound.FADE);
      statusModel.showStatus(statusField, "Click the hand!");
      statusModel.showResult(resultField, "Game over");
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
      statusModel.showStatus(statusField, "Click the hand!");
      String result = gpModel.getPuppet().getPuppetName() + " - pos: " + currentIndex;
      statusModel.showResult(resultField, result);
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

  void startMoving(int nb) {
    statusModel.showStatus(statusField, "Moving...");
    statusModel.showPips(pipsField, "Pips: " + nb);
    int newScore = npModel.getNbRolls() + 1;
    npModel.setNbRolls(newScore);
    statusModel.showScore(scoreField, "# Rolls: " + newScore);
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
    statusModel.showStatus(statusField, "Rolling...");
    statusModel.showPips(pipsField, "");

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

  class ManualDieButton implements GGButtonListener {
    @Override
    public void buttonPressed(GGButton ggButton) {

    }

    @Override
    public void buttonReleased(GGButton ggButton) {

    }

    @Override
    public void buttonClicked(GGButton ggButton) {
      System.out.println("manual die button clicked");
      if (ggButton instanceof CustomGGButton) {
        CustomGGButton customGGButton = (CustomGGButton) ggButton;
        int tag = customGGButton.getTag();
        System.out.println("manual die button clicked - tag: " + tag);
        prepareBeforeRoll();
        roll(tag);
      }
    }
  }
}
