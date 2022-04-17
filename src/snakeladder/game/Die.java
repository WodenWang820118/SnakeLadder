package snakeladder.game;

import ch.aplu.jgamegrid.Actor;

public class Die extends Actor {
  private NavigationPaneModel npModel;
  private int nb;

  Die(int nb, NavigationPaneModel npModel) {
    super("sprites/pips" + nb + ".gif", 7);
    this.nb = nb;
    this.npModel = npModel;
  }

  public void act() {
    showNextSprite();
    if (getIdVisible() == 6) {
      setActEnabled(false);
      this.npModel.startMoving(nb);
    }
  }
}
