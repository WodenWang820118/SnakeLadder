package snakeladder.game.pane.navigationpane;

public interface GamePlayCallback {
    void finishGameWithResults(int winningPlayerIndex, java.util.List<String> playerCurrentPositions);
}
