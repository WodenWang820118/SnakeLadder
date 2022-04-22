package snakeladder.game.pane;

import ch.aplu.jgamegrid.Actor;

public class Die extends Actor {
  private int nb;
  // create cup
  private Cup cup;
  private int index;

  public Die(int nb, Cup cup, int index) {
    super("sprites/pips" + nb + ".gif", 7);
    this.nb = nb;
    this.cup = cup;
    this.index = index;
  }

  public void act() {
    showNextSprite();
    if (getIdVisible() == 6) {
      setActEnabled(false);

      // let cup know that the dice are rolled
      cup.endRoll(index);
    }
  }
}
