package snakeladder.game.pane.gamepane;

import ch.aplu.jgamegrid.Location;

public abstract class Connection {
  private Location locStart;
  private Location locEnd;
  private int cellStart;
  private int cellEnd;
  private String imagePath;

  // for reset connection
  private Location reLocStart;
  private Location reLocEnd;
  private int reCellStart;
  private int reCellEnd;


  Connection(int cellStart, int cellEnd) {
    this.cellStart = cellStart;
    this.cellEnd = cellEnd;
    locStart = GamePane.cellToLocation(cellStart);
    locEnd = GamePane.cellToLocation(cellEnd);

    reCellStart = cellStart;
    reCellEnd = cellEnd;
    reLocStart = locStart;
    reLocEnd = locEnd;
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

  // change connection, switch the starting and ending points
  private Location change1;
  public void changeConnection(){
    change1 = locEnd;
    locEnd = locStart;
    locStart = change1;

    int change2 = cellEnd;
    cellEnd = cellStart;
    cellStart = change2;
  }

  public void resetConnection(){
    locEnd = reLocEnd;
    locStart = reLocStart;

    cellEnd = reCellEnd;
    cellStart = reCellStart;
  }
}
