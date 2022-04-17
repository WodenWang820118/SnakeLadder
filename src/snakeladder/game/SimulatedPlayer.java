package snakeladder.game;

import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.util.Monitor;

public class SimulatedPlayer extends Thread {
    private NavigationPane np;
    private NavigationPaneModel npModel;
    private DieModel dieModel;

    public SimulatedPlayer(NavigationPane np, NavigationPaneModel npModel, DieModel dieModel) {
        this.np = np;
        this.npModel = npModel;
        this.dieModel = dieModel;
    }
    public void run() {
      while (true) {
        Monitor.putSleep();
        this.np.getHandBtn().show(1);
        this.npModel.roll(this.dieModel.getDieValue());
        GameGrid.delay(1000);
        this.np.getHandBtn().show(0);
      }
    }
}
