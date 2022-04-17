package snakeladder.game.pane.navigationpane;

import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.GGButtonListener;
import snakeladder.game.custom.CustomGGButton;
import snakeladder.game.pane.PaneController;

public class ManualDieButton implements GGButtonListener {
  
  private PaneController paneController;
  
  public ManualDieButton() {}

  @Override
  public void buttonClicked(GGButton ggButton) {
    System.out.println("manual die button clicked");

    if (ggButton instanceof CustomGGButton) {
      CustomGGButton customGGButton = (CustomGGButton) ggButton;
      int tag = customGGButton.getTag();
      System.out.println("manual die button clicked - tag: " + tag);
      paneController.prepareBeforeRoll();
      paneController.roll(tag);
    }
  }

  public void setPaneController(PaneController paneController) {
    this.paneController = paneController;
  }

  @Override
  public void buttonPressed(GGButton arg0) {
      // TODO Auto-generated method stub
      
  }

  @Override
  public void buttonReleased(GGButton arg0) {
      // TODO Auto-generated method stub
      
  }
    
}