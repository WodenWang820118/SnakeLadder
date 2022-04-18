package snakeladder.game.pane.navigationpane;

import java.util.Properties;

import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.GGButtonListener;
import ch.aplu.jgamegrid.GGCheckButton;
import ch.aplu.jgamegrid.GGCheckButtonListener;
import ch.aplu.jgamegrid.GGTextField;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import ch.aplu.util.Monitor;
import snakeladder.game.pane.PaneController;
import snakeladder.game.pane.navigationpane.die.DieModel;
import snakeladder.game.pane.navigationpane.die.DieUI;
import snakeladder.game.pane.navigationpane.status.StatusUI;

@SuppressWarnings("serial")
public class NavigationPane extends GameGrid implements GGButtonListener {

  // fields
  private boolean isAuto;
  private boolean isToggle = false;

  // regarding hand animation
  private final Location handBtnLocation = new Location(110, 70);
  private GGButton handBtn = new GGButton("sprites/handx.gif");

  // regarding checkboxes and toggle buttons
  private final Location autoChkLocation = new Location(15, 375);
  private final Location toggleModeLocation = new Location(95, 375);
  private GGCheckButton autoChk;
  private GGCheckButton toggleCheck =
  new GGCheckButton("Toggle Mode", YELLOW, TRANSPARENT, isToggle);

  // controller, model and UI views
  private PaneController paneController;
  private ManualDieButton manualDieButton = paneController.getManualDieButton();
  private DieModel dieModel = paneController.getDieModel();
  private DieUI dieUI = new DieUI(manualDieButton);
  private StatusUI statusUI = new StatusUI(this);

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
    dieUI.addDieButtons();
    statusUI.addStatusFields();

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
  }

  public void setPaneController(PaneController paneController) {
    this.paneController = paneController;
  }

  private int getDieValue() {
    return this.dieModel.getDieValue();
  }

  public GGTextField getPipField() {
    return statusUI.getPipField();
  }

  public GGTextField getStatusField() {
    return statusUI.getStatusField();
  }

  public GGTextField getResultField() {
    return statusUI.getResultField();
  }

  public GGTextField getScoreField() {
    return statusUI.getScoreField();
  }

  public GGButton getHandBtn() {
    return handBtn;
  }

  public Location getDieBoardLocation() {
    return dieUI.getDieBoardLocation();
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
