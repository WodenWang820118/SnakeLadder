package snakeladder.game;
// java utility modules
import java.util.List;
import java.util.Properties;
// game utility modules
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.GameGrid;

/**
 * The class is served as a view
 */
@SuppressWarnings("serial")
public class GamePane extends GameGrid {
  
  final Location startLocation = new Location(-1, 9);  // outside grid
  public static final int NUMBER_HORIZONTAL_CELLS = 10;
  public static final int NUMBER_VERTICAL_CELLS = 10;
  private final int MAX_PUPPET_SPRITES = 4;
  final int animationStep = 10; // TODO: shouldn't be here. It's used in the Puppet class

  // the controller uses the model to display the view
  private GamePaneModel model;

  GamePane(Properties properties) { 
    // instantiate the model
    this.model = new GamePaneModel(properties);
    this.initGame(properties);
  }

  public void initGame(Properties properties) {
    setSimulationPeriod(100);
    setBgImagePath("sprites/gamepane_blank.png");
    setCellSize(60);
    setNbHorzCells(NUMBER_HORIZONTAL_CELLS);
    setNbHorzCells(NUMBER_VERTICAL_CELLS);
    doRun();
    this.model.createSnakesLadders(properties);
    this.model.setupPlayers(properties);
    setBgImagePath("sprites/gamepane_snakeladder.png");
  }

  static Location cellToLocation(int cellIndex) {
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

  int x(int y, Connection con) {
    int x0 = toPoint(con.locStart).x;
    int y0 = toPoint(con.locStart).y;
    int x1 = toPoint(con.locEnd).x;
    int y1 = toPoint(con.locEnd).y;
    // Assumption y1 != y0
    double a = (double)(x1 - x0) / (y1 - y0);
    double b = (double)(y1 * x0 - y0 * x1) / (y1 - y0);
    return (int)(a * y + b);
  }

  void setNavigationPane(NavigationPane np) {
    this.model.setNavigationPane(np);
  }

  void createGui() {
    for (int i = 0; i < this.model.getNumberOfPlayers(); i++) {
      boolean isAuto = this.model.getPlayerManualMode().get(i);
      int spriteImageIndex = i % MAX_PUPPET_SPRITES;
      String puppetImage = "sprites/cat_" + spriteImageIndex + ".gif";
      Puppet puppet = new Puppet(this, this.model.getNavigationPane(), puppetImage);
      puppet.setAuto(isAuto);
      puppet.setPuppetName("Player " + (i + 1));
      addActor(puppet, startLocation);
      this.model.getPuppets().add(puppet);
    }
  }

  Connection getConnectionAt(Location loc) {
    for (Connection con : this.model.getConnections())
      if (con.locStart.equals(loc))
        return con;
    return null;
  }

  public int getNumberOfPlayers() {
    return this.model.getNumberOfPlayers();
  }

  Puppet getPuppet() {
    return this.model.getPuppets().get(this.model.getCurrentPuppetIndex());
  }

  void switchToNextPuppet() {
    this.model.setCurrentPuppetIndex((this.model.getCurrentPuppetIndex() + 1) % this.model.getNumberOfPlayers());
  }

  List<Puppet> getAllPuppets() {
    return this.model.getPuppets();
  }

  void resetAllPuppets() {
    for (Puppet puppet: this.model.getPuppets()) {
      puppet.resetToStartingPoint();
    }
  }
}
