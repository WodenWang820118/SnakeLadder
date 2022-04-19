package snakeladder.game.pane.navigationpane;

import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.Location;
import snakeladder.game.custom.CustomGGButton;
import snakeladder.game.pane.navigationpane.NavigationPaneController.ManualDieButton;

public class DieBoard {
  // dice related
  private final int DIE1_BUTTON_TAG = 1;
  private final int DIE2_BUTTON_TAG = 2;
  private final int DIE3_BUTTON_TAG = 3;
  private final int DIE4_BUTTON_TAG = 4;
  private final int DIE5_BUTTON_TAG = 5;
  private final int DIE6_BUTTON_TAG = 6;

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

  private ManualDieButton manualDieButton;

  void addDieButtons(NavigationPane np) {
    // ManualDieButton manualDieButton = new ManualDieButton();

    np.addActor(die1Button, die1Location);
    np.addActor(die2Button, die2Location);
    np.addActor(die3Button, die3Location);
    np.addActor(die4Button, die4Location);
    np.addActor(die5Button, die5Location);
    np.addActor(die6Button, die6Location);

    die1Button.addButtonListener(manualDieButton);
    die2Button.addButtonListener(manualDieButton);
    die3Button.addButtonListener(manualDieButton);
    die4Button.addButtonListener(manualDieButton);
    die5Button.addButtonListener(manualDieButton);
    die6Button.addButtonListener(manualDieButton);
  }

  void setManualDieButton(ManualDieButton manualDieButton) {
    this.manualDieButton = manualDieButton;
  }
}
