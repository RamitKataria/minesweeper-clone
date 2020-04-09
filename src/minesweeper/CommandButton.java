/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author ramit.kataria
 */
public class CommandButton extends JButton{
    CommandButton(ImageIcon ICON, Color grey){
        setIcon(ICON);
        setBackground(grey);
        setFocusPainted(false);
    }
}
