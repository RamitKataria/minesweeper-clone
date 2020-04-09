package minesweeper;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static java.awt.event.InputEvent.*;

/**
 * class for mouse listener
 * @author ramit.kataria
 */
public class GameMouseListener implements MouseListener{
    private final Minesweeper game;
    MButton button;

    public GameMouseListener(Minesweeper game) {
        this.game = game;
    }

    @Override
    /**
     * event handler for a mouse click
     */
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    /**
     * event handler for a mouse press
     * @param me 
     */
    public void mousePressed(MouseEvent me) {
        button = (MButton)me.getSource();
        int mouseModifiers = me.getModifiersEx();
        
        if (mouseModifiers == BUTTON1_DOWN_MASK){
            game.clickNum ++;
            int value = button.getValue();
            // if it is the first click, reset
            if (game.clickNum == 1){
                while (value == -1){
                    value = game.firstClickMineReset(button);
                }
                game.start();
            }
            
            if (!button.isExposed()){
                if (game.isOn && !button.isFlagged()){

                    // if the button is a regular tile
                    if (value != -1){
                        // expose it and reduce the number of tiles left
                        button.expose();
                        game.tilesLeft--;

                        // clear surrounding cells if the button is a "0" tile
                        if (value == 0){
                            game.clearCells(button);
                        }

                    // if the button is a mine, the player loses
                    }else{
                        game.gameOver();

                    }
                }
            }
        }else if (mouseModifiers == BUTTON3_DOWN_MASK){
            if (!button.isExposed()){
                if (button.isFlagged()){
                    button.unFlag();
                    game.minesLeft ++;
                }else{
                    button.flag();
                    game.minesLeft --;
                }
                game.minesLeftLabel.setText(Integer.toString(game.minesLeft));
            }
        }else if(mouseModifiers == BUTTON1_DOWN_MASK + BUTTON3_DOWN_MASK || mouseModifiers == BUTTON2_DOWN_MASK){
            // go through the 3x3 grid around the "0" cell
            for (int row = -1; row < 2; row++){
                for (int col = -1; col < 2; col++){
                    int[] loc = {button.getLoc()[0] + row, button.getLoc()[1] + col};

                    // check if the cell exists
                    if (loc[0] >= 0 && loc[0] < game.numRows && loc[1] >= 0 && loc[1] < game.numCols){
                        MButton b = game.buttons[loc[0]][loc[1]];
                        if (!b.isFlagged() && !b.isExposed()){
                            if (b.getValue() == -1){
                                game.gameOver();
                            }else{
                                b.expose();
                                game.tilesLeft--;
                            }
                        }
                    }
                }
            }
        }
        // if no tiles left, then the player wins
        if (game.tilesLeft == 0){
            game.win();
        }
        System.out.println(game.tilesLeft);
    }

    @Override
    /**
     * event handler for a mouse release
     * @param me 
     */
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    /**
     * event handler for the entrance of mouse
     * @param me 
     */
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    /**
     * event handler for a the exit of mouse
     * @param me 
     */
    public void mouseExited(MouseEvent me) {
    }
}