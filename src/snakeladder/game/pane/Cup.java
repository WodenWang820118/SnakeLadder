package snakeladder.game.pane;

import snakeladder.game.pane.navigationpane.NavigationPane;
import java.util.ArrayList;
import java.util.List;

public class Cup {
    private List<Die> dice;
    private NavigationPane np;
    private int numRolled;

    public Cup(NavigationPane np){
        this.np = np;
        this.dice = new ArrayList<>();
        this.numRolled = 0;
    }

    public void roll(int nb){
        int len = dice.size();
        Die die = new Die(nb, this, len+1);
        dice.add(die);
        numRolled += nb;
    }

    // when roll finish start moving
    public void endRoll(int index){
        if (index == np.getNumberOfDice()){
            np.startMoving(numRolled);
            // reset
            this.numRolled = 0;
            this.dice.clear();
        }
    }

    // for addActor()
    public List<Die> getDice(){
        return this.dice;
    }
}
