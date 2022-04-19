package snakeladder.game.pane;

import ch.aplu.jgamegrid.*;
import snakeladder.game.pane.gamepane.Connection;
import snakeladder.game.pane.gamepane.GamePane;
import snakeladder.game.pane.gamepane.Snake;
import snakeladder.game.pane.navigationpane.NavigationPane;

import java.awt.Point;

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

  private void moveToNextCell() {
    int tens = cellIndex / 10;
    int ones = cellIndex - tens * 10;
    // Cells starting left 01, 21, .. 81
    if (tens % 2 == 0) {
      if (ones == 0 && cellIndex > 0)
        setLocation(new Location(getX(), getY() - 1));
      else
        setLocation(new Location(getX() + 1, getY()));
    } else {
      // Cells starting left 20, 40, .. 100
      if (ones == 0)
        setLocation(new Location(getX(), getY() - 1));
      else
        setLocation(new Location(getX() - 1, getY()));
    }
    cellIndex++;
  }

  public void act() {
    if ((cellIndex / 10) % 2 == 0) {
      if (isHorzMirror())
        setHorzMirror(false);
    } else {
      if (!isHorzMirror())
        setHorzMirror(true);
    }

    // Animation: Move on connection
    if (currentCon != null) {
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
    if (nbSteps > 0) {
      moveToNextCell();

      // Game over
      if (cellIndex == 100) {
        setActEnabled(false);
        np.prepareRoll(cellIndex);
        return;
      }

      nbSteps--;
      if (nbSteps == 0) {
        // Check if on connection start
        if ((currentCon = gp.getConnectionAt(getLocation())) != null) {
          gp.setSimulationPeriod(50);
          y = gp.toPoint(currentCon.getLocStart()).y;
          if (currentCon.getLocEnd().y > currentCon.getLocStart().y)
            dy = gp.animationStep;
          else
            dy = -gp.animationStep;
          if (currentCon instanceof Snake) {
            np.showStatus("Digesting...");
            np.playSound(GGSound.MMM);
          }
          else {
            np.showStatus("Climbing...");
            np.playSound(GGSound.BOING);
          }
        } else {
          setActEnabled(false);
          np.prepareRoll(cellIndex);
        }
      }
    }
  }
}
