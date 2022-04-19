package snakeladder.game.pane.navigationpane;

import java.util.Properties;

import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.GGButtonListener;
import snakeladder.game.custom.CustomGGButton;

public class NavigationPaneController {
  
  // views
  private NavigationPane np;
  private DieBoard dieBoard;
  private StatusBoard statusBoard;

  // models
  private NavigationPaneModel npModel;
  private ManualDieButton manualDieButton = new ManualDieButton();

  public NavigationPaneController(
    NavigationPane np,
    DieBoard dieBoard,
    StatusBoard statusBoard,
    NavigationPaneModel npModel,
    Properties properties) {
    
    this.np = np;
    this.dieBoard = dieBoard;
    dieBoard.setManualDieButton(manualDieButton);
    this.statusBoard = statusBoard;
    this.npModel = npModel;
  }
    
  public NavigationPane getNp() {
    return np;
  }

  public NavigationPaneModel getNpModel() {
    return npModel;
  }

  public DieBoard getDieBoard() {
    return dieBoard;
  }

  public StatusBoard getStatusBoard() {
    return statusBoard;
  }

  public ManualDieButton getManualDieButton() {
    return manualDieButton;
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
        np.prepareBeforeRoll();
        np.roll(tag);
      }
    }
  }
}
