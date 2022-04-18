package snakeladder.game.pane;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGSound;
import ch.aplu.jgamegrid.Location;
import snakeladder.game.pane.gamepane.Connection;
import snakeladder.game.pane.gamepane.GamePane;
import snakeladder.game.pane.gamepane.GamePaneModel;
import snakeladder.game.pane.gamepane.Snake;
import snakeladder.game.pane.navigationpane.NavigationPane;
import snakeladder.game.pane.navigationpane.NavigationPaneModel;
import snakeladder.game.pane.navigationpane.StatusModel;

import java.awt.Point;

public class Puppet extends Actor {

  // fields
  final int animationStep = 10;
  private boolean isAuto;
  private int cellIndex = 0;
  private int nbSteps;
  private int y;
  private int dy;
  private String puppetName;

  // components
  private Connection currentCon = null;

  // TODO: because of act method, for now, we need to pass the PaneController
  // it needs to be refactored after
  private PaneController paneController;
  private GamePaneModel gpModel = this.paneController.getGamePaneModel();
  private NavigationPaneModel npModel = this.paneController.getNavigationPaneModel();
  private GamePane gp = this.paneController.getGamePane();
  private NavigationPane np = this.paneController.getNavigationPane();
  private StatusModel statusModel = this.paneController.getStatusModel();

  public Puppet(String puppetImage) {
    super(puppetImage);
  }

  public void setPaneController(PaneController paneController) {
    this.paneController = paneController;
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
    if (cellIndex == 100) {
      cellIndex = 0;
      setLocation(gp.getStartLoction());
    }
    this.nbSteps = nbSteps;
    setActEnabled(true);
  }

  public void resetToStartingPoint() {
    cellIndex = 0;
    setLocation(gp.getStartLoction());
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

  // TODO: might need to refactor the method
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
        paneController.prepareRoll(cellIndex);
      }
      return;
    }
  
    // Normal movement
    if (nbSteps > 0) {
      moveToNextCell();

      // Game over
      if (cellIndex == 100) {
        setActEnabled(false);
        paneController.prepareRoll(cellIndex);
        return;
      }

      nbSteps--;
      if (nbSteps == 0) {
        // Check if on connection start
        if ((currentCon = gpModel.getConnectionAt(getLocation())) != null) {
          gp.setSimulationPeriod(50);
          y = gp.toPoint(currentCon.getLocStart()).y;

          if (currentCon.getLocEnd().y > currentCon.getLocStart().y) {
            dy = animationStep;
          } else {
            dy = animationStep;
          }
            
          if (currentCon instanceof Snake) {
            statusModel.showStatus(np.getStatusField(), "Digesting...");
            npModel.playSound(GGSound.MMM);
          } else {
            statusModel.showStatus(np.getStatusField(), "Climbing...");
            npModel.playSound(GGSound.BOING);
          }
        } else {
          setActEnabled(false);
          paneController.prepareRoll(cellIndex);
        }
      }
    }
  }
}
