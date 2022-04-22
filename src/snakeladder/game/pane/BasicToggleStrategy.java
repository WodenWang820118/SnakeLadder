package snakeladder.game.pane;

import snakeladder.game.pane.gamepane.GamePane;
import snakeladder.game.pane.navigationpane.NavigationPane;

public class BasicToggleStrategy implements ToggleStrategy{

    @Override
    public boolean checkIfToggle(NavigationPane np, GamePane gp){

        int numberOfDice = np.getNumberOfDice();
        int nextPlayer = gp.getCurrentPuppetIndex() == 0;//如果是0下一个就是1，反之则为0 
        //通过gamepane去检查
        //计算下一个人在下一轮都可能出现在什么位置
        //在这些位置里向上connection是不是比向下connection要多
        //如果多就return True、
        //otherwise return false
        return false;
        }
        
    }