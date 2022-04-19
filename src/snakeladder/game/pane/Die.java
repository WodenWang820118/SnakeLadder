package snakeladder.game.pane;

import ch.aplu.jgamegrid.Actor;
import snakeladder.game.pane.navigationpane.NavigationPane;

public class Die extends Actor {
  private NavigationPane np;
  private int nb;

  public Die(int nb, NavigationPane np) {
    super("sprites/pips" + nb + ".gif", 7);
    this.nb = nb;
    this.np = np;
  }

  public void act() {
    showNextSprite();
    if (getIdVisible() == 6) {
      setActEnabled(false);
      np.startMoving(nb);
    }
  }
}
