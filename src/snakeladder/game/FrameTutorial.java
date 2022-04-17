package snakeladder.game;

import snakeladder.game.pane.PaneController;
import snakeladder.game.pane.SimulatedPlayer;
import snakeladder.game.pane.gamepane.GamePane;
import snakeladder.game.pane.gamepane.GamePaneController;
import snakeladder.game.pane.gamepane.GamePaneModel;
import snakeladder.game.pane.navigationpane.GamePlayCallback;
import snakeladder.game.pane.navigationpane.ManualDieButton;
import snakeladder.game.pane.navigationpane.NavigationPane;
import snakeladder.game.pane.navigationpane.NavigationPaneController;
import snakeladder.game.pane.navigationpane.NavigationPaneModel;
import snakeladder.game.pane.navigationpane.StatusModel;
import snakeladder.game.pane.navigationpane.die.DieModel;
import snakeladder.utility.PropertiesLoader;
import snakeladder.utility.ServicesRandom;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("serial")
// the FrameTutorial is the controller to give specific instructions to the system
public class FrameTutorial extends JFrame {
  private final String version = "1.01";
  public FrameTutorial(Properties properties) {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setResizable(false);
    setLocation(10, 10);
    setTitle("snakeladder.game.FrameTutorial V" + version +
      ", (Design: Carlo Donzelli, Implementation: Aegidius Pluess)");

    // view instantiation
    GamePane gp = new GamePane(properties);
    NavigationPane np = new NavigationPane(properties);

    // model instantiation
    GamePaneModel gpModel = new GamePaneModel(properties);
    StatusModel statusModel = new StatusModel();
    NavigationPaneModel npModel = new NavigationPaneModel();
    DieModel dieModel = new DieModel(properties, gpModel);
    ManualDieButton manualDieButton = new ManualDieButton();
    SimulatedPlayer player = new SimulatedPlayer();

    // controller instantiation
    GamePaneController gpController = new GamePaneController(gp, gpModel);
    NavigationPaneController npController = new NavigationPaneController(np, npModel, statusModel, dieModel, manualDieButton);
    PaneController paneController = new PaneController(gpController, npController);

    // set controller
    np.setPaneController(paneController);
    manualDieButton.setPaneController(paneController);
    player.setPaneController(paneController);

    // instructions
    gpController.initGamePane(properties);
    getContentPane().add(gp, BorderLayout.WEST);

    npController.initNavigationPane(properties);
    npController.setupDieValues();
    getContentPane().add(np, BorderLayout.EAST);

    npModel.setGamePlayCallback(new GamePlayCallback() {
      @Override
      public void finishGameWithResults(int winningPlayerIndex, List<String> playerCurrentPositions) {
       System.out.println("DO NOT CHANGE THIS LINE---WINNING INFORMATION: " + winningPlayerIndex + "-" + String.join(",", playerCurrentPositions));
      }
    });

    pack();  // Must be called before actors are added!

    np.createGui();
    paneController.createGamePaneGui();
    np.checkAuto();
    player.start();
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
    if (seedProp != null) {
      seed = Long.parseLong(seedProp);
    }

    ServicesRandom.initServicesRandom(seed);
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        new snakeladder.game.FrameTutorial(properties).setVisible(true);
      }
    });
  }
}