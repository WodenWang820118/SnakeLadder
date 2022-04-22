package snakeladder.game.pane.gamepane;

import ch.aplu.jgamegrid.Location;

public abstract class Connection {
  private Location locStart;
  private Location locEnd;
  private int cellStart;
  private int cellEnd;
  private String imagePath;

  Connection(int cellStart, int cellEnd) {
    this.cellStart = cellStart;
    this.cellEnd = cellEnd;
    locStart = GamePane.cellToLocation(cellStart);
    locEnd = GamePane.cellToLocation(cellEnd);
  }

  public Location getLocStart() {
    return locStart;
  }

  public Location getLocEnd() {
    return locEnd;
  }

  public int getCellStart() {
    return cellStart;
  }

  public int getCellEnd() {
    return cellEnd;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public double xLocationPercent(int locationCell) {
    return (double) locationCell / GamePane.NUMBER_HORIZONTAL_CELLS;
  }
  public double yLocationPercent(int locationCell) {
    return (double) locationCell / GamePane.NUMBER_VERTICAL_CELLS;
  }

  public void reverseConnection(){
    //调换start和end
    Location locTmp = locStart;
    locStart = locEnd;
    locEnd = locTmp;

    int cellTmp = cellStart;
    cellStart = cellEnd;
    cellEnd = cellTmp;
  }
}
