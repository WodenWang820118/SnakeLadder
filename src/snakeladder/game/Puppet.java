package snakeladder.game;

import ch.aplu.jgamegrid.*;
import java.awt.Point;

public class Puppet extends Actor {

  // fields
  private boolean isAuto;
  private int cellIndex = 0;
  private int nbSteps;
  private int y;
  private int dy;
  private String puppetName;
  
  // components
  private Connection currentCon = null;
  private NavigationPaneModel npModel;

  Puppet(NavigationPaneModel npModel, String puppetImage) {
    super(puppetImage);
    this.npModel = npModel;
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

  void go(int nbSteps) {
    if (cellIndex == 100) {
      cellIndex = 0;
      setLocation(npModel.gp.startLocation);
    }
    this.nbSteps = nbSteps;
    setActEnabled(true);
  }

  void resetToStartingPoint() {
    cellIndex = 0;
    setLocation(npModel.gp.startLocation);
    setActEnabled(true);
  }

  int getCellIndex() {
    return cellIndex;
  }

  private void moveToNextCell() {
    int tens = cellIndex / 10;
    int ones = cellIndex - tens * 10;
    // Cells starting left 01, 21, .. 81
    if (tens % 2 == 0) {
      if (ones == 0 && cellIndex > 0) {
        setLocation(new Location(getX(), getY() - 1));
      } else {
        setLocation(new Location(getX() + 1, getY()));
      } 
    } else {
      // Cells starting left 20, 40, .. 100
      if (ones == 0) {
        setLocation(new Location(getX(), getY() - 1));
      } else {
        setLocation(new Location(getX() - 1, getY()));
      } 
    }
    cellIndex++;
  }

  public void act() {
    if ((cellIndex / 10) % 2 == 0) {
      if (isHorzMirror()) {
        setHorzMirror(false);
      }
    } else {
      if (!isHorzMirror()) {
        setHorzMirror(true);
      }
    }

    // Animation: Move on connection
    if (currentCon != null) {
      int x = npModel.gp.x(y, currentCon);
      setPixelLocation(new Point(x, y));
      y += dy;

      // Check end of connection
      if ((dy > 0 && (y - npModel.gp.toPoint(currentCon.locEnd).y) > 0)
        || (dy < 0 && (y - npModel.gp.toPoint(currentCon.locEnd).y) < 0)) {
        npModel.gp.setSimulationPeriod(100);
        setActEnabled(false);
        setLocation(currentCon.locEnd);
        cellIndex = currentCon.cellEnd;
        setLocationOffset(new Point(0, 0));
        currentCon = null;
        npModel.prepareRoll(cellIndex);
      }
      return;
    }
  
    // Normal movement
    if (nbSteps > 0) {
      moveToNextCell();

      // Game over
      if (cellIndex == 100) {
        setActEnabled(false);
        npModel.prepareRoll(cellIndex);
        return;
      }

      nbSteps--;
      if (nbSteps == 0) {
        // Check if on connection start
        if ((currentCon = npModel.gp.getConnectionAt(getLocation())) != null) {
          npModel.gp.setSimulationPeriod(50);
          y = npModel.gp.toPoint(currentCon.locStart).y;

          if (currentCon.locEnd.y > currentCon.locStart.y) {
            dy = npModel.gp.animationStep;
          } else {
            dy = npModel.gp.animationStep;
          }
            
          if (currentCon instanceof Snake) {
            npModel.statusModel.showStatus(npModel.np.getStatusField(), "Digesting...");
            npModel.playSound(GGSound.MMM);
          } else {
            npModel.statusModel.showStatus(npModel.np.getStatusField(), "Climbing...");
            npModel.playSound(GGSound.BOING);
          }
        } else {
          setActEnabled(false);
          npModel.prepareRoll(cellIndex);
        }
      }
    }
  }
}
