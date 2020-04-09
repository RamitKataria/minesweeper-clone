/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author ramit.kataria
 */
public class CommandMouseListener implements MouseListener{
    String action;
    Minesweeper game;

    CommandMouseListener(String action, Minesweeper game) {
        this.action = action;
        this.game = game;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        if (action == "newgame"){
            game.newGame();
        }else if (action == "highscores"){
            game.showScores();
        }else if(action == "changedifficulty"){
            game.changeDifficulty();
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
    
}
