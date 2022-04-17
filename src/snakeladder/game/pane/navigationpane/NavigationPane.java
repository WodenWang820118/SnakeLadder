package snakeladder.game.pane.navigationpane;

import java.awt.Font;
import java.util.Properties;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.GGButtonListener;
import ch.aplu.jgamegrid.GGCheckButton;
import ch.aplu.jgamegrid.GGCheckButtonListener;
import ch.aplu.jgamegrid.GGTextField;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import ch.aplu.util.Monitor;
import snakeladder.game.custom.CustomGGButton;
import snakeladder.game.pane.PaneController;
import snakeladder.game.pane.navigationpane.die.DieModel;

@SuppressWarnings("serial")
public class NavigationPane extends GameGrid implements GGButtonListener {
  
  // regarding dice
  private final int DIE1_BUTTON_TAG = 1;
  private final int DIE2_BUTTON_TAG = 2;
  private final int DIE3_BUTTON_TAG = 3;
  private final int DIE4_BUTTON_TAG = 4;
  private final int DIE5_BUTTON_TAG = 5;
  private final int DIE6_BUTTON_TAG = 6;

  private GGButton die1Button = new CustomGGButton(DIE1_BUTTON_TAG, "sprites/Number_1.png");
  private GGButton die2Button = new CustomGGButton(DIE2_BUTTON_TAG, "sprites/Number_2.png");
  private GGButton die3Button = new CustomGGButton(DIE3_BUTTON_TAG, "sprites/Number_3.png");
  private GGButton die4Button = new CustomGGButton(DIE4_BUTTON_TAG, "sprites/Number_4.png");
  private GGButton die5Button = new CustomGGButton(DIE5_BUTTON_TAG, "sprites/Number_5.png");
  private GGButton die6Button = new CustomGGButton(DIE6_BUTTON_TAG, "sprites/Number_6.png");

  private final Location dieBoardLocation = new Location(100, 180);
  private final Location die1Location = new Location(20, 270);
  private final Location die2Location = new Location(50, 270);
  private final Location die3Location = new Location(80, 270);
  private final Location die4Location = new Location(110, 270);
  private final Location die5Location = new Location(140, 270);
  private final Location die6Location = new Location(170, 270);

  // regarding hand animation
  private GGButton handBtn = new GGButton("sprites/handx.gif");
  private final Location handBtnLocation = new Location(110, 70);

  // regarding textfields
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

  // regarding checkboxes and toggle buttons
  private boolean isAuto;
  private GGCheckButton autoChk;
  private boolean isToggle = false;

  private final Location autoChkLocation = new Location(15, 375);

  private GGCheckButton toggleCheck =
  new GGCheckButton("Toggle Mode", YELLOW, TRANSPARENT, isToggle);
  private final Location toggleModeLocation = new Location(95, 375);

  private PaneController paneController;
  private ManualDieButton manualDieButton = paneController.getManualDieButton();
  private DieModel dieModel = paneController.getDieModel();

  public NavigationPane(Properties properties) {}

  public void initNavigationPane(Properties properties) {
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
  }

  public void createGui() {
    addActor(new Actor("sprites/dieboard.gif"), dieBoardLocation);

    handBtn.addButtonListener(this);
    addActor(handBtn, handBtnLocation);
    addActor(autoChk, autoChkLocation);
    autoChk.addCheckButtonListener(new GGCheckButtonListener() {
      @Override
      public void buttonChecked(GGCheckButton button, boolean checked) {
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

  // public void setManualButtonDie() {
  //   this.manualDieButton = new ManualDieButton();
  // }

  public void setPaneController(PaneController paneController) {
    this.paneController = paneController;
  }

  void addDieButtons() {
    // ManualDieButton manualDieButton = new ManualDieButton();

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

  private int getDieValue() {
    return this.dieModel.getDieValue();
  }

  public GGTextField getPipField() {
    return pipsField;
  }

  public GGTextField getStatusField() {
    return statusField;
  }

  public GGTextField getResultField() {
    return resultField;
  }

  public GGTextField getScoreField() {
    return scoreField;
  }

  public GGButton getHandBtn() {
    return handBtn;
  }

  public Location getDieBoardLocation() {
    return dieBoardLocation;
  }
  
  public void checkAuto() {
    if (isAuto) Monitor.wakeUp();
  }

  public void buttonClicked(GGButton btn) {
    System.out.println("hand button clicked");
    paneController.prepareBeforeRoll();
    paneController.roll(getDieValue());
  }

  public void buttonPressed(GGButton btn) {
  }

  public void buttonReleased(GGButton btn) {
  }
}
