package snakeladder.game.pane;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGSound;
import ch.aplu.jgamegrid.Location;
import snakeladder.game.pane.gamepane.Connection;
import snakeladder.game.pane.gamepane.GamePane;
import snakeladder.game.pane.gamepane.Snake;
import snakeladder.game.pane.navigationpane.NavigationPane;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class Puppet extends Actor {

  private PaneController pc;
  private GamePane gp;
  private NavigationPane np;
  private int cellIndex = 0;
  private int nbSteps;
  private Connection currentCon = null;
  private int y;
  private int dy;
  private boolean isAuto;
  private String puppetName;

  // task2
  private boolean notDown = false;
  // task3
  private boolean ifGoBack = false;

  public Puppet(PaneController pc,  String puppetImage) {
    super(puppetImage);
    this.pc = pc;
    this.gp = pc.gpController.getGp();
    this.np = pc.npController.getNp();
  }

  public boolean isAuto() {
    return isAuto;
  }

  public void setAuto(boolean auto) {
    isAuto = auto;
  }

  public String getPuppetName() {
    return puppetName;
  }

  public void setPuppetName(String puppetName) {
    this.puppetName = puppetName;
  }

  public void go(int nbSteps) {
    // after game over
    if (cellIndex == 100) {
      cellIndex = 0;
      setLocation(gp.startLocation);
    }
    this.nbSteps = nbSteps;

    // Check if a die roll a “1”
    if (nbSteps == np.getNumberOfDice()) {
      notDown = true;
    } else {
      notDown = false;
    }

    setActEnabled(true);
  }

  public void resetToStartingPoint() {
    cellIndex = 0;
    setLocation(gp.startLocation);
    setActEnabled(true);
  }

  public int getCellIndex() {
    return cellIndex;
  }
  
  public void act() {
    if ((cellIndex / 10) % 2 == 0) {
      if (isHorzMirror())
        setHorzMirror(false);
    } else {
      if (!isHorzMirror())
        setHorzMirror(true);
    }

    // task5 update the traversals
    HashMap<Integer, HashMap<String, Integer>> traversalRecords = pc.getNpModel().getTraversalRecord();
    int puppetIndex = pc.getGpModel().getCurrentPuppetIndex();
    Map<String, Integer> personalRecord = traversalRecords.get(puppetIndex);
    
    // Animation: Move on connection
    // end-start < 0 means met the head of the snake
    // if met the head of the snake but notDown == true, not go down
    // if met the head of the snake and notDown == false, can go down
    if (currentCon != null && 
        !(notDown && (currentCon.getCellEnd() - currentCon.getCellStart()) < 0)) {
      int x = gp.x(y, currentCon);
      setPixelLocation(new Point(x, y));
      y += dy;

      // Check end of connection
      if ((dy > 0 && (y - gp.toPoint(currentCon.getLocEnd()).y) > 0)
        || (dy < 0 && (y - gp.toPoint(currentCon.getLocEnd()).y) < 0)) {
        gp.setSimulationPeriod(100);
        setActEnabled(false);
        setLocation(currentCon.getLocEnd());
        cellIndex = currentCon.getCellEnd();
        setLocationOffset(new Point(0, 0));
        currentCon = null;
        np.prepareRoll(cellIndex);
      }
      return;
    }

    // Normal movement
    if (nbSteps != 0) {
      moveToCell(nbSteps);

      // Game over
      if (cellIndex == 100) {
        setActEnabled(false);
        np.prepareRoll(cellIndex);
        return;
      }

      if(nbSteps > 0){
        nbSteps--;
      }
      if(nbSteps < 0){
        nbSteps++;
      }

      if (nbSteps == 0) {
        // Check if on connection start
        // check notDown and if met a head of the snake
        if ((currentCon = gp.getConnectionAt(getLocation())) != null && 
            !(notDown && (currentCon.getCellEnd() - currentCon.getCellStart()) < 0)) {

          gp.setSimulationPeriod(50);
          y = gp.toPoint(currentCon.getLocStart()).y;
          if (currentCon.getLocEnd().y > currentCon.getLocStart().y)
            dy = gp.animationStep;
          else
            dy = -gp.animationStep;
          if (currentCon instanceof Snake) {
            // update the traversal record
            // TODO: update the traversal counter correctly, but cannot distinguish the unique players
            personalRecord.put("down", personalRecord.get("down") + 1);
            pc.npController.getStatusBoard().showStatus("Digesting...");
            np.playSound(GGSound.MMM);
          }
          else {
            // update the traversal record
            personalRecord.put("up", personalRecord.get("up") + 1);
            pc.npController.getStatusBoard().showStatus("Climbing...");
            np.playSound(GGSound.BOING);
          }
        } else {
          setActEnabled(false);
          np.prepareRoll(cellIndex);
        }

        // task4
        if(isAuto == true){
          boolean Toggle = pc.getCC().checkChange(pc.gpController, np);
          if(Toggle){
            // change connection
            np.getToggleCheck().setChecked(true);
          }else{
            np.getToggleCheck().setChecked(false);
          }
        }
        
      }
    }
  }

  // task3 
  public void setGoBack(boolean ifGoBack){
    this.ifGoBack = ifGoBack;
  }

  public boolean getGoBack(){
    return ifGoBack;
  }

  public void moveToCell(int nbSteps){
    if(nbSteps > 0){
      cellIndex ++;
    }else if(nbSteps < 0){
      cellIndex --;
    }
    Location loc = cellToLocation(cellIndex);
    setLocation(loc);
  }

  // from GamePame， a better way to get location.
  public static Location cellToLocation(int cellIndex) {
    int index = cellIndex - 1;

    int tens = index / 10;
    int ones = index - tens * 10;

    int y = 9 - tens;
    int x;

    if (tens % 2 == 0)    // Cells starting left 01, 21, .. 81
      x = ones;
    else     // Cells starting left 20, 40, .. 100
      x = 9 - ones;

    return new Location(x, y);
  }
}