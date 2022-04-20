package snakeladder.game;

import snakeladder.game.pane.GamePlayCallback;
import snakeladder.game.pane.PaneController;
import snakeladder.game.pane.gamepane.GamePane;
import snakeladder.game.pane.gamepane.GamePaneController;
import snakeladder.game.pane.gamepane.GamePaneModel;
import snakeladder.game.pane.navigationpane.DieBoard;
import snakeladder.game.pane.navigationpane.NavigationPane;
import snakeladder.game.pane.navigationpane.NavigationPaneController;
import snakeladder.game.pane.navigationpane.NavigationPaneModel;
import snakeladder.game.pane.navigationpane.StatusBoard;
import snakeladder.utility.PropertiesLoader;
import snakeladder.utility.ServicesRandom;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("serial")
public class FrameTutorial extends JFrame {
  private final String version = "1.01";
  
  public FrameTutorial(Properties properties) {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setResizable(false);
    setLocation(10, 10);
    setTitle("snakeladder.game.FrameTutorial V" + version +
      ", (Design: Carlo Donzelli, Implementation: Aegidius Pluess)");

    // instantiate views
    // low couping: the dieBoard and statusBoard are instantiated without dependencies
    DieBoard dieBoard = new DieBoard();
    StatusBoard statusBoard = new StatusBoard();
    GamePane gp = new GamePane(properties);
    // dependency injection: constructor injection
    NavigationPane np = new NavigationPane(properties, dieBoard, statusBoard);

    // instantiate models
    GamePaneModel gpModel = new GamePaneModel(properties);
    NavigationPaneModel npModel = new NavigationPaneModel(properties);

    // instantiate controllers
    GamePaneController gpController = new GamePaneController(gp, gpModel, properties);
    NavigationPaneController npController = new NavigationPaneController(np, dieBoard, statusBoard, npModel, properties);
    PaneController pc = new PaneController(gpController, npController, properties);

    getContentPane().add(pc.getGp(), BorderLayout.WEST);
    getContentPane().add(pc.getNp(), BorderLayout.EAST);

    pc.getNp().setGamePlayCallback(new GamePlayCallback() {
      @Override
      public void finishGameWithResults(int winningPlayerIndex, List<String> playerCurrentPositions) {
       System.out.println("DO NOT CHANGE THIS LINE---WINNING INFORMATION: " + winningPlayerIndex + "-" + String.join(",", playerCurrentPositions));
      }
    });

    pack();  // Must be called before actors are added!

    // setter injection
    // the order of injection makes sure the thread does not point to Null
    pc.getNp().setPaneController(pc);
    pc.createNpGui();
    pc.getGp().setPaneController(pc);
    pc.createGpGui(pc);
    pc.getNp().checkAuto();

  }

  public static void main(String[] args) {
    final Properties properties;
    if (args == null || args.length == 0) {
      properties = PropertiesLoader.loadPropertiesFile(null);
    } else {
      properties = PropertiesLoader.loadPropertiesFile(args[0]);
    }
    String seedProp = properties.getProperty("seed");
    Long seed = null;
    if (seedProp != null) seed = Long.parseLong(seedProp);
    ServicesRandom.initServicesRandom(seed);
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        new snakeladder.game.FrameTutorial(properties).setVisible(true);
      }
    });
  }
}