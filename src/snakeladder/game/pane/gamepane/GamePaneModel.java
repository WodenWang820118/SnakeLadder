package snakeladder.game.pane.gamepane;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import ch.aplu.jgamegrid.Location;
import snakeladder.game.pane.PaneController;
import snakeladder.game.pane.Puppet;
import snakeladder.utility.PropertiesLoader;

public class GamePaneModel {

  final int MAX_PUPPET_SPRITES = 4;
  private int numberOfPlayers = 1;
  private int currentPuppetIndex = 0;
  private List<Boolean> playerManualMode;
  private ArrayList<Connection> connections = new ArrayList<Connection>();
  private List<Puppet> puppets =  new ArrayList<>();

  public GamePaneModel(Properties properties){}

  public void createGui(PaneController pc) {
    for (int i = 0; i < getNumberOfPlayers(); i++) {
      boolean isAuto = getPlayerManualMode().get(i);
      int spriteImageIndex = i % MAX_PUPPET_SPRITES;
      String puppetImage = "sprites/cat_" + spriteImageIndex + ".gif";
      Puppet puppet = new Puppet(pc, puppetImage);
      puppet.setAuto(isAuto);
      puppet.setPuppetName("Player " + (i + 1));
      pc.getGp().addActor(puppet, pc.getGp().startLocation);
      getPuppets().add(puppet);
    }
  }

  public void createSnakesLadders(Properties properties) {
    connections.addAll(PropertiesLoader.loadSnakes(properties));
    connections.addAll(PropertiesLoader.loadLadders(properties));
  }

  public void setupPlayers(Properties properties) {
    numberOfPlayers = Integer.parseInt(properties.getProperty("players.count"));
    playerManualMode = new ArrayList<>();
    for (int i = 0; i < numberOfPlayers; i++) {
      playerManualMode.add(Boolean.parseBoolean(properties.getProperty("players." + i + ".isAuto")));
    }
    
    System.out.println("playerManualMode = " + playerManualMode);
  }

  public void setNumberOfPlayers(int numberOfPlayers) {
    this.numberOfPlayers = numberOfPlayers;
  }

  public int getNumberOfPlayers() {
    return numberOfPlayers;
  }

  public void setCurrentPuppetIndex(int currentPuppetIndex) {
    this.currentPuppetIndex = currentPuppetIndex;
  }

  public int getCurrentPuppetIndex() {
    return currentPuppetIndex;
  }

  public List<Boolean> getPlayerManualMode() {
    return playerManualMode;
  }

  public ArrayList<Connection> getConnections() {
    return connections;
  }

  public List<Puppet> getPuppets() {
    return puppets;
  }

  public Connection getConnectionAt(Location loc) {
    for (Connection con : getConnections())
      if (con.getLocStart().equals(loc))
        return con;
    return null;
  }

  public Puppet getPuppet() {
    return getPuppets().get(getCurrentPuppetIndex());
  }

  public void switchToNextPuppet() {
    setCurrentPuppetIndex((getCurrentPuppetIndex() + 1) % getNumberOfPlayers());
  }

  public void resetAllPuppets() {
    for (Puppet puppet: getPuppets()) {
      puppet.resetToStartingPoint();
    }
  }
}