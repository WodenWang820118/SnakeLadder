package snakeladder.game.pane.navigationpane.die;

import ch.aplu.jgamegrid.Actor;
import snakeladder.game.pane.PaneController;

public class Die extends Actor {

  private int nb;
  private PaneController paneController;

  public Die(int nb, PaneController paneController) {
    super("sprites/pips" + nb + ".gif", 7);
    this.nb = nb;
    this.paneController = paneController;
  }

  public void act() {
    showNextSprite();
    if (getIdVisible() == 6) {
      setActEnabled(false);
      paneController.startMoving(nb);
    }
  }
}
