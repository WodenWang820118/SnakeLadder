package snakeladder.game.pane.gamepane;
import snakeladder.game.pane.Puppet;
import snakeladder.game.pane.navigationpane.NavigationPane;
import ch.aplu.jgamegrid.Location;

public class ChangeConnection implements ChangeConnectionStrategy{

    @Override
    public boolean checkChange(GamePaneController gpc, NavigationPane np) {
        // Determine whether to change all connection when the player automates

        int numberOfDice = np.getNumberOfDice();
        int numberOfCell = numberOfDice * 6;

        int currentPlayer = gpc.getGpModel().getCurrentPuppetIndex();
        int nextPlayerIndex;
        Puppet nextPuppet;
        Location nextPlayerLocation;

        Connection currentCon = null;
        int numberOfUp = 0;
        int numberOfDown = 0;

        if(currentPlayer == gpc.getGpModel().getNumberOfPlayers() - 1){
            nextPlayerIndex = 0;
        }else{
            nextPlayerIndex = currentPlayer + 1;
        }
        
        nextPuppet = gpc.getGpModel().getPuppets().get(nextPlayerIndex);

        // Determine all cells with connections where nextPlayer will go next.
        // if up, numberOfUp ++; if down, numberOfDown ++;
        for(int i = 0; i < numberOfCell; i++){
            nextPlayerLocation = GamePane.cellToLocation(nextPuppet.getCellIndex() + i);
            currentCon = gpc.getGp().getConnectionAt(nextPlayerLocation);
            if(currentCon != null){
                // down
                if(currentCon.getCellEnd() - currentCon.getCellStart() < 0){
                    numberOfDown ++;
                }else if(currentCon.getCellEnd() - currentCon.getCellStart() > 0){
                    numberOfUp ++;
                }
            }
        }
        System.out.println(numberOfUp);
        System.out.println(numberOfDown);
        
        if(numberOfUp >= numberOfDown){
            return true;
        }else{
            return false;
        }
    }
}