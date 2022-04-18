package snakeladder.game.pane.gamepane;

import java.util.Properties;

import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.GameGrid;

@SuppressWarnings("serial")
public class GamePane extends GameGrid {
  
  private final Location startLocation = new Location(-1, 9);  // outside grid
  public static final int NUMBER_HORIZONTAL_CELLS = 10;
  public static final int NUMBER_VERTICAL_CELLS = 10;

  public GamePane(Properties properties) {}

  public void initGamePane(Properties properties) {
    setSimulationPeriod(100);
    setBgImagePath("sprites/gamepane_blank.png");
    setCellSize(60);
    setNbHorzCells(NUMBER_HORIZONTAL_CELLS);
    setNbHorzCells(NUMBER_VERTICAL_CELLS);
    doRun();
  }

  public void setGamePaneSnakeLadderImg(String imgPath) {
    setBgImagePath(imgPath);
  }

  public static Location cellToLocation(int cellIndex) {
    int index = cellIndex - 1;  // 0..99

    int tens = index / NUMBER_HORIZONTAL_CELLS;
    int ones = index - tens * NUMBER_HORIZONTAL_CELLS;

    int y = 9 - tens;
    int x;

    if (tens % 2 == 0)     // Cells starting left 01, 21, .. 81
      x = ones;
    else     // Cells starting left 20, 40, .. 100
      x = 9 - ones;

    return new Location(x, y);
  }

  public int x(int y, Connection con) {
    int x0 = toPoint(con.getLocStart()).x;
    int y0 = toPoint(con.getLocStart()).y;
    int x1 = toPoint(con.getLocEnd()).x;
    int y1 = toPoint(con.getLocEnd()).y;
    // Assumption y1 != y0
    double a = (double)(x1 - x0) / (y1 - y0);
    double b = (double)(y1 * x0 - y0 * x1) / (y1 - y0);
    return (int)(a * y + b);
  }

  public Location getStartLoction() {
    return startLocation;
  }
}
