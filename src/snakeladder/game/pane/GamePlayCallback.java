package snakeladder.game.pane;

public interface GamePlayCallback {
  void finishGameWithResults(int winningPlayerIndex, java.util.List<String> playerCurrentPositions);
}
