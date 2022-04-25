package snakeladder.game.pane.gamepane;

import snakeladder.game.pane.navigationpane.NavigationPane;

// NERDI games will want to change this strategy in the future –  use interface
public interface ChangeConnectionStrategy {
    boolean checkChange(GamePaneController pc, NavigationPane np);
}