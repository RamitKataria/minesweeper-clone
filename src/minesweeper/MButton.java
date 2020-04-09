package minesweeper;

import java.awt.Dimension;
import java.awt.Font;
import static java.awt.Font.BOLD;
import static java.awt.Image.SCALE_SMOOTH;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 * MButton --- class to manage buttons
 * @author ramit.kataria
 */
class MButton extends JButton{
    final Dimension tileSize;
    private final int tileMargin;
    private int value;
    private int[] location;
    private boolean exposed;
    private boolean flagged;
    
    ImageIcon TILE_CLICKED = new ImageIcon("./data/tile_clicked.jpg");
    ImageIcon TILE_HIDDEN = new ImageIcon("./data/tile_hidden.jpg");
    ImageIcon TILE_FLAGGED = new ImageIcon("./data/tile_flag.jpg");
    ImageIcon MINE_EXPLODED = new ImageIcon("./data/mine_exploded.jpg");
    ImageIcon MINE_FLAGGED = new ImageIcon("./data/mine_flagged.jpg");
    
    /**
     * constructor for the button class
     * @param location
     * @param numRows
     */
    MButton(int[] location, int numRows){
        this.location = location;
        exposed = false;
        flagged = false;
        tileSize = new Dimension(25+250/numRows, 25+250/numRows);
        
        // to avoid lines around rounded corners
        setBorderPainted(false);
        
        tileMargin = (int) (tileSize.height*0.2);
        setPreferredSize(tileSize);
        setFont(new Font("myFont", BOLD, (int)(tileSize.height*0.5)));
        //System.out.println(tileSize.height*0.4);
        setVerticalTextPosition(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setMargin(new Insets(tileMargin, tileMargin, tileMargin, tileMargin));
        
        TILE_CLICKED = new ImageIcon(TILE_CLICKED.getImage().getScaledInstance(tileSize.width, tileSize.height, SCALE_SMOOTH));
        TILE_HIDDEN = new ImageIcon(TILE_HIDDEN.getImage().getScaledInstance(tileSize.width, tileSize.height, SCALE_SMOOTH));
        TILE_FLAGGED = new ImageIcon(TILE_FLAGGED.getImage().getScaledInstance(tileSize.width, tileSize.height, SCALE_SMOOTH));
        MINE_EXPLODED = new ImageIcon(MINE_EXPLODED.getImage().getScaledInstance(tileSize.width, tileSize.height, SCALE_SMOOTH));
        MINE_FLAGGED  = new ImageIcon(MINE_FLAGGED.getImage().getScaledInstance(tileSize.width, tileSize.height, SCALE_SMOOTH));
        
        setIcon(TILE_HIDDEN);  
    }
    
    /**
     * getter for value
     * @return value
     */
    int getValue(){
        return value;
    }
    
    /**
     * setter for value
     * @param value new value
     */
    void setValue(int value){
        this.value = value;
    }
    
    /**
     * getter for location
     * @return
     */
    int[] getLoc(){
        return location;
    }
    
    /**
     * setter for location
     * @param location 
     */
    void setLoc(int[] location){
        this.location = location;
    }
    
    /**
     * check if the button is exposed
     * @return 
     */
    boolean isExposed(){
        return exposed;
    }
    
    /**
     * expose the button
     */
    void expose(){
        if (value!=-1){
            setIcon(TILE_CLICKED);
            if (value!=0) {
                setText(Integer.toString(value));
            }
        }else{
            if (isFlagged()){
                setIcon(MINE_FLAGGED);
            }else{
                setIcon(MINE_EXPLODED);
            }
        }
        exposed = true;
    }

    void unexpose(){
        exposed = false;
        setIcon(TILE_HIDDEN);
        setText("");
    }
    
    void flag(){
        flagged = true;
        setIcon(TILE_FLAGGED);
    }
    
    void unFlag(){
        flagged = false;
        setIcon(TILE_HIDDEN);
    }
    
    boolean isFlagged(){
        return flagged;
    }

    void reset(){
        unFlag();
        unexpose();
    }
}