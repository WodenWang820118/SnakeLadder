package snakeladder.game;

import ch.aplu.jgamegrid.*;
import java.awt.*;
import ch.aplu.util.*;
import snakeladder.game.custom.CustomGGButton;
import snakeladder.utility.ServicesRandom;

import java.util.ArrayList;
import java.util.Properties;

@SuppressWarnings("serial")
public class NavigationPane extends GameGrid
  implements GGButtonListener
{
  // TODO: the class should be put in a separate class
  // the class connects to Monitor controller
  // the class connects to the Die class
  // the class connects to the ManualDieButton class
  private class SimulatedPlayer extends Thread
  {
    public void run()
    {
      while (true)
      {
        Monitor.putSleep();
        handBtn.show(1);
       roll(getDieValue());
        delay(1000);
        handBtn.show(0);
      }
    }

  }

  // TODO: the die tags and locations should be put in a separate class
  private final int DIE1_BUTTON_TAG = 1;
  private final int DIE2_BUTTON_TAG = 2;
  private final int DIE3_BUTTON_TAG = 3;
  private final int DIE4_BUTTON_TAG = 4;
  private final int DIE5_BUTTON_TAG = 5;
  private final int DIE6_BUTTON_TAG = 6;
  private final int RANDOM_ROLL_TAG = -1;

  private final Location handBtnLocation = new Location(110, 70);
  private final Location dieBoardLocation = new Location(100, 180);
  private final Location pipsLocation = new Location(70, 230);
  private final Location statusLocation = new Location(20, 330);
  private final Location statusDisplayLocation = new Location(100, 320);
  private final Location scoreLocation = new Location(20, 430);
  private final Location scoreDisplayLocation = new Location(100, 430);
  private final Location resultLocation = new Location(20, 495);
  private final Location resultDisplayLocation = new Location(100, 495);

  private final Location autoChkLocation = new Location(15, 375);
  private final Location toggleModeLocation = new Location(95, 375);

  private final Location die1Location = new Location(20, 270);
  private final Location die2Location = new Location(50, 270);
  private final Location die3Location = new Location(80, 270);
  private final Location die4Location = new Location(110, 270);
  private final Location die5Location = new Location(140, 270);
  private final Location die6Location = new Location(170, 270);

  private GamePane gp;
  private GGButton handBtn = new GGButton("sprites/handx.gif");

  private GGButton die1Button = new CustomGGButton(DIE1_BUTTON_TAG, "sprites/Number_1.png");
  private GGButton die2Button = new CustomGGButton(DIE2_BUTTON_TAG, "sprites/Number_2.png");
  private GGButton die3Button = new CustomGGButton(DIE3_BUTTON_TAG, "sprites/Number_3.png");
  private GGButton die4Button = new CustomGGButton(DIE4_BUTTON_TAG, "sprites/Number_4.png");
  private GGButton die5Button = new CustomGGButton(DIE5_BUTTON_TAG, "sprites/Number_5.png");
  private GGButton die6Button = new CustomGGButton(DIE6_BUTTON_TAG, "sprites/Number_6.png");

  // TODO: the board related variables should be put in a separate class
  private GGTextField pipsField;
  private GGTextField statusField;
  private GGTextField resultField;
  private GGTextField scoreField;
  private boolean isAuto;
  private GGCheckButton autoChk;
  private boolean isToggle = false;
  private GGCheckButton toggleCheck =
          new GGCheckButton("Toggle Mode", YELLOW, TRANSPARENT, isToggle);
  private int nbRolls = 0;
  private volatile boolean isGameOver = false;
  private Properties properties;
  private java.util.List<java.util.List<Integer>> dieValues = new ArrayList<>();
  private GamePlayCallback gamePlayCallback;
  
  // TODO: the class is dealing with GUI
  // TODO: potential controller usage
  NavigationPane(Properties properties)
  {
    this.properties = properties;
    int numberOfDice =  //Number of six-sided dice
            (properties.getProperty("dice.count") == null)
                    ? 1  // default
                    : Integer.parseInt(properties.getProperty("dice.count"));
    System.out.println("numberOfDice = " + numberOfDice);
    isAuto = Boolean.parseBoolean(properties.getProperty("autorun"));
    autoChk = new GGCheckButton("Auto Run", YELLOW, TRANSPARENT, isAuto);
    System.out.println("autorun = " + isAuto);
    setSimulationPeriod(200);
    setBgImagePath("sprites/navigationpane.png");
    setCellSize(1);
    setNbHorzCells(200);
    setNbVertCells(600);
    doRun();
    new SimulatedPlayer().start();
  }

  // TODO: the method should be put in the Die class
  void setupDieValues() {
    for (int i = 0; i < gp.getNumberOfPlayers(); i++) {
      // an arrayList for storing the die values for each player
      java.util.List<Integer> dieValuesForPlayer = new ArrayList<>();
      // get the properties from the test2.properties file
      // the array of string will be split by "," and elements will be parsed to integer, stored in dieValuesForPlayer
      if (properties.getProperty("die_values." + i) != null) {
        String dieValuesString = properties.getProperty("die_values." + i);
        String[] dieValueStrings = dieValuesString.split(",");
        for (int j = 0; j < dieValueStrings.length; j++) {
          dieValuesForPlayer.add(Integer.parseInt(dieValueStrings[j]));
        }
        // the dieValues add the dieValuesForPlayer to the dieValues list for the testing purpose
        dieValues.add(dieValuesForPlayer);
      } else {
        System.out.println("All players need to be set a die value for the full testing mode to run. " +
                "Switching off the full testing mode");
        dieValues = null;
        break;
      }
    }
    System.out.println("dieValues = " + dieValues);
  }

  void setGamePlayCallback(GamePlayCallback gamePlayCallback) {
    this.gamePlayCallback = gamePlayCallback;
  }

  void setGamePane(GamePane gp)
  {
    this.gp = gp;
    // when initializing the game, the method will call the setupDieValues() method
    setupDieValues();
  }

  // the class ManualDieButton implements GGButtonListener interface
  // TODO: the ManualDieButton class should be an independent class
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

  // TODO: the method should be in a separate class
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

  // TODO: the method should be put in the Die class
  private int getDieValue() {
    if (dieValues == null) {
      return RANDOM_ROLL_TAG;
    }
    int currentRound = nbRolls / gp.getNumberOfPlayers();
    int playerIndex = nbRolls % gp.getNumberOfPlayers();
    if (dieValues.get(playerIndex).size() > currentRound) {
      return dieValues.get(playerIndex).get(currentRound);
    }
    return RANDOM_ROLL_TAG;
  }

  // TODO: the method createGui() should be put in a separate class
  // TODO: potential controller usage, dealing with GUI
  void createGui()
  {
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

    // TODO: the method should be put in a separate class
    // the method should connect to ManualDieButton class
    // the method should be able to access the gif files
    // TODO: potential protected variations usage
    addActor(toggleCheck, toggleModeLocation);
    toggleCheck.addCheckButtonListener(new GGCheckButtonListener() {
      @Override
      public void buttonChecked(GGCheckButton ggCheckButton, boolean checked) {
        isToggle = checked;
      }
    });

    addDieButtons();

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

  // TODO: should be put in a separate class
  // should relate to the game play statistics
  void showPips(String text)
  {
    pipsField.setText(text);
    if (text != "") System.out.println(text);
  }

  // TODO: should be put in a separate class
  // should relate to the game play statistics
  void showStatus(String text)
  {
    statusField.setText(text);
    System.out.println("Status: " + text);
  }

  // TODO: should be put in a separate class
  // should relate to the game play statistics
  void showScore(String text)
  {
    scoreField.setText(text);
    System.out.println(text);
  }

  // TODO: should be put in a separate class
  // should relate to the game play statistics
  void showResult(String text)
  {
    resultField.setText(text);
    System.out.println("Result: " + text);
  }

  // TODO: the method should be put in the Die class
  // TODO: potential indirection usage
  void prepareRoll(int currentIndex)
  {
    if (currentIndex == 100)  // Game over
    {
      playSound(GGSound.FADE);
      showStatus("Click the hand!");
      showResult("Game over");
      isGameOver = true;
      handBtn.setEnabled(true);

      java.util.List  <String> playerPositions = new ArrayList<>();
      for (Puppet puppet: gp.getAllPuppets()) {
        playerPositions.add(puppet.getCellIndex() + "");
      }
      gamePlayCallback.finishGameWithResults(nbRolls % gp.getNumberOfPlayers(), playerPositions);
      gp.resetAllPuppets();
    }
    else
    {
      playSound(GGSound.CLICK);
      showStatus("Done. Click the hand!");
      String result = gp.getPuppet().getPuppetName() + " - pos: " + currentIndex;
      showResult(result);
      gp.switchToNextPuppet();
      // System.out.println("current puppet - auto: " + gp.getPuppet().getPuppetName() + "  " + gp.getPuppet().isAuto() );

      if (isAuto) {
        Monitor.wakeUp();
      } else if (gp.getPuppet().isAuto()) {
        Monitor.wakeUp();
      } else {
        handBtn.setEnabled(true);
      }
    }
  }

  // TODO: the method should be put in a separate class
  // TODO: the method should connect to a class dealing with the statistics
  void startMoving(int nb)
  {
    showStatus("Moving...");
    showPips("Pips: " + nb);
    showScore("# Rolls: " + (++nbRolls));
    gp.getPuppet().go(nb);
  }

  // TODO: the method should be put in a separate class
  void prepareBeforeRoll() {
    handBtn.setEnabled(false);
    if (isGameOver)  // First click after game over
    {
      isGameOver = false;
      nbRolls = 0;
    }
  }

  // TODO: the method should connect to the ManualDieButton and the Die class
  // because the method is called when the user clicks on a die button and get the value from the die
  public void buttonClicked(GGButton btn)
  {
    System.out.println("hand button clicked");
    prepareBeforeRoll();
    roll(getDieValue());
  }

  // TODO: the method should be put in a separate class
  // TODO: indirection: high cohesion, low coupling
  private void roll(int rollNumber)
  {
    int nb = rollNumber;
    if (rollNumber == RANDOM_ROLL_TAG) {
      nb = ServicesRandom.get().nextInt(6) + 1;
    }
    showStatus("Rolling...");
    showPips("");

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

  // TODO: the method should be put in a separate class
  // TODO: potentil controller usage, dealing with UI stuffs
  public void checkAuto() {
    if (isAuto) Monitor.wakeUp();
  }
}
