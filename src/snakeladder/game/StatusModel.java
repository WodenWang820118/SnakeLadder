package snakeladder.game;

import ch.aplu.jgamegrid.GGTextField;

public class StatusModel {
  public StatusModel() {
  }

  public void showPips(GGTextField pipsField, String text) {
    pipsField.setText(text);
    if (text != "") System.out.println(text);
  }

  public void showStatus(GGTextField statusField, String text) {
    statusField.setText(text);
    System.out.println("Status: " + text);
  }

  public void showScore(GGTextField scoreField, String text) {
    scoreField.setText(text);
    System.out.println(text);
  }

  void showResult(GGTextField resultField, String text) {
    resultField.setText(text);
    System.out.println("Result: " + text);
  }
}
