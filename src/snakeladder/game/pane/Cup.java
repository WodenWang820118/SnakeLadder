package snakeladder.game.pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cup {
  private List<Die> dice;
  private PaneController pc;
  private int numRolled;

  public Cup(){
    this.dice = new ArrayList<>();
    this.numRolled = 0;
  }

  // the player on the paneController roll a number of dice
  // according to the property setting
  public void roll(int nb) {
    int len = dice.size();
    Die die = new Die(nb, this, len + 1);
    dice.add(die);
    numRolled += nb;
    updatePlayerStats(numRolled);
  }

  public void updatePlayerStats(int nb) {
    int puppetIndex = pc.getGpModel().getCurrentPuppetIndex();
    Map<Integer, HashMap<Integer, Integer>> diceRollingRecord =
      pc.getNpModel().getDiceRollingRecord();
    Map<Integer, Integer> playerRecord = diceRollingRecord.get(puppetIndex);
    // update the rolling record
    playerRecord.put(nb, playerRecord.get(nb) + 1);
  }

  // when roll finish start moving
  public void endRoll(int numberOfDiceUsed){
    if (numberOfDiceUsed == pc.getNp().getNumberOfDice()){
      pc.getNp().startMoving(numRolled);
      // reset
      this.numRolled = 0;
      this.dice.clear();
    }
  }

  // for addActor()
  public List<Die> getDice(){
    return this.dice;
  }

  public void setPaneController(PaneController pc){
    this.pc = pc;
  }
}
