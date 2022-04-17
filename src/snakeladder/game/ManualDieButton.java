package snakeladder.game;

import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.GGButtonListener;
import snakeladder.game.custom.CustomGGButton;

public class ManualDieButton implements GGButtonListener {
  private NavigationPaneModel npModel;

  public ManualDieButton(NavigationPaneModel npModel) {
    this.npModel = npModel;
  }

  @Override
  public void buttonClicked(GGButton ggButton) {
    System.out.println("manual die button clicked");

    if (ggButton instanceof CustomGGButton) {
        CustomGGButton customGGButton = (CustomGGButton) ggButton;
        int tag = customGGButton.getTag();
        System.out.println("manual die button clicked - tag: " + tag);
        npModel.prepareBeforeRoll();
        npModel.roll(tag);
    }
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
