package minesweeper;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.Border;

import static java.awt.Font.BOLD;
import static java.awt.Image.SCALE_SMOOTH;

/**
 * Minesweeper --- main class for the minesweeper game
 * @author ramit.kataria
 */
public class Minesweeper {
    private ArrayList<Integer> mineNums;
    
    int numMines = 10;
    int numRows = 8;
    int numCols = 8;
    
    private final Random rand = new Random();
    private int[][]grid;

    boolean isOn = false;

    int tilesLeft;
    int minesLeft = numMines;

    File HSCORES = new File("./data/high_scores.txt");
    
    int clickNum;
    private int time;
    Timer timer;
    
    Color grey;
    Font buttonFont;
    
    private final JFrame frame;
    MButton[][] buttons;
    
    JPanel topPanel;
    JPanel gridPanel;
    JPanel mainPanel;
    JPanel leftPanel;
    JPanel rightPanel;
    JPanel newGamePanel;
    
    JButton difficultyB;
    JButton highScoresB;
    JButton newGameB;
    JLabel timerIcon;
    JLabel stopwatch = new JLabel("0");
    JLabel minesLeftIcon;
    JLabel minesLeftLabel = new JLabel(Integer.toString(minesLeft));

    ImageIcon MINES_LEFT_ICON = new ImageIcon("./data/mines_left.png");
    ImageIcon HAPPY_FACE_ICON = new ImageIcon("./data/happy_face.png");
    ImageIcon SAD_FACE_ICON = new ImageIcon("./data/sad_face.png");
    ImageIcon MENU_ICON = new ImageIcon("./data/menu.png");
    ImageIcon SCORES_ICON = new ImageIcon("./data/high_scores.png");
    ImageIcon TIMER_ICON = new ImageIcon("./data/timer.png");
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Minesweeper game = new Minesweeper();
        /*
        System.out.println();
        for (int[] row: game.grid){
            for (int cell: row){
                System.out.print(cell + "   ");
                
                // add extra space for non-mine cells to align the columns in 
                // the output screen
                if (cell != -1){System.out.print(" ");}
            }
            System.out.println();
            System.out.println();
        }
        */
    }
    
    /**
     * constructor for the class, to perform necessary actions
     */
    private Minesweeper() {
        // create a frame that exits on close
        frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        grey = new Color(45, 45, 45);
        buttonFont = new Font("bFont", BOLD, 20);

        mainPanel = new JPanel(new BorderLayout());
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(grey);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 25));
        leftPanel = new JPanel();
        leftPanel.setBackground(grey);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
        rightPanel = new JPanel();
        rightPanel.setBackground(grey);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 0));
        JPanel newGamePanel = new JPanel();
        newGamePanel.setBackground(grey);

        // create buttons and labels fot the top panel
        difficultyB = new JButton();
        MENU_ICON = new ImageIcon(MENU_ICON.getImage().getScaledInstance(40,40, SCALE_SMOOTH));
        difficultyB.setIcon(MENU_ICON);
        difficultyB.addMouseListener(new CommandMouseListener("changedifficulty", this));
        difficultyB.setBackground(grey);
        difficultyB.setFocusPainted(false);
        difficultyB.setBorderPainted(false);
        leftPanel.add(difficultyB);

        highScoresB = new JButton();
        SCORES_ICON = new ImageIcon(SCORES_ICON.getImage().getScaledInstance(40,40, SCALE_SMOOTH));
        highScoresB.setIcon(SCORES_ICON);
        highScoresB.addMouseListener(new CommandMouseListener("highscores", this));
        highScoresB.setBackground(grey);
        highScoresB.setFocusPainted(false);
        highScoresB.setBorderPainted(false);
        leftPanel.add(highScoresB);

        newGameB = new JButton();
        newGameB.addMouseListener(new CommandMouseListener("newgame", this));
        HAPPY_FACE_ICON = new ImageIcon(HAPPY_FACE_ICON.getImage().getScaledInstance(40,40, SCALE_SMOOTH));
        SAD_FACE_ICON = new ImageIcon(SAD_FACE_ICON.getImage().getScaledInstance(40, 40, SCALE_SMOOTH));
        newGameB.setIcon(HAPPY_FACE_ICON);
        newGameB.setBackground(grey);
        newGameB.setFocusPainted(false);
        newGameB.setBorderPainted(false);
        newGamePanel.add(newGameB);

        timerIcon = new JLabel();
        TIMER_ICON = new ImageIcon(TIMER_ICON.getImage().getScaledInstance(32, 32, SCALE_SMOOTH));
        timerIcon.setIcon(TIMER_ICON);
        rightPanel.add(timerIcon);
        
        stopwatch.setForeground(Color.WHITE);
        stopwatch.setFont (buttonFont);
        stopwatch.setPreferredSize(new Dimension(44, 26));
        rightPanel.add(stopwatch);

        minesLeftIcon = new JLabel();
        MINES_LEFT_ICON = new ImageIcon(MINES_LEFT_ICON.getImage().getScaledInstance(32, 32, SCALE_SMOOTH));
        minesLeftIcon.setIcon(MINES_LEFT_ICON);
        rightPanel.add(minesLeftIcon);

        minesLeftLabel.setForeground(Color.WHITE);
        minesLeftLabel.setFont (buttonFont);
        minesLeftLabel.setPreferredSize(new Dimension(22, 26));
        rightPanel.add(minesLeftLabel);
        


        createTiles();


        topPanel.add(newGamePanel, BorderLayout.CENTER);
        topPanel.add(leftPanel, BorderLayout.LINE_START);
        topPanel.add(rightPanel, BorderLayout.LINE_END);

        mainPanel.add(topPanel, BorderLayout.PAGE_START);
        

        frame.add(mainPanel);
        
        




        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time++;
                stopwatch.setText(Integer.toString(time));
            }
        });

        frame.pack();
        frame.setVisible(true);
        newGame();
    }
    
    private void createTiles(){
        gridPanel = new JPanel(new GridLayout(numRows, numCols, 0, 0));
        gridPanel.setBackground(grey);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // an array for easy access of buttons
        buttons = new MButton[numRows][numCols];

        // create buttons for each cell in the grid and add them to the buttons
        // array and the panel
        for (int row = 0; row < numRows; row++){
            for (int col = 0; col < numCols; col++){
                int[] location = {row, col};
                buttons[row][col] = new MButton(location, numRows);
                MButton b = buttons[row][col];
                b.addMouseListener(new GameMouseListener(this));
                gridPanel.add(b);
            }
        }
        mainPanel.add(gridPanel, BorderLayout.PAGE_END);
        
    }
    
    /**
     * create grid of random mines and add those to the main panel
     */
    private void createGrid(){
        mineNums = new ArrayList<>();
        grid = new int[numRows][numCols];

        // add random numbers, for location of Mines, to an ArrayList 
        // until it contains the desired amount
        while (mineNums.size() < numMines){
            
            int randNum = rand.nextInt(numRows*numCols);
            
            // check if the number is repeated before adding it to the ArrayList
            if (!mineNums.contains(randNum)){
                mineNums.add(randNum);
            }
        }

        // set values for mines and surrounding cells in the grid
        for(int num:mineNums){ // go through each number in the previously 
                               // created ArrayList for mines
                               
            // the random number divided by number of columns gives the row
            // the remainder of the division gives the column
            // store the values as a coordinate for the mine
            int[] mine = {num/numCols, num%numCols};
            
            // set the mine locations to -1
            grid[mine[0]][mine[1]] = -1; 
            
            // go through the 3x3 grid around the mine
            for (int row = -1; row < 2; row++){
                for (int col = -1; col < 2; col++){
                    
                    // assign a variable for the coordinates of the cell in 
                    // question to avoid repetition of the same operation
                    int[] cell = {mine[0]+row, mine[1]+col};
                    
                    // check if the cell exists and is not a mine, and then 
                    // add 1 to the cell
                    if (cell[0] >= 0 && cell[0] < numRows && cell[1] >= 0 && cell[1] < numCols && grid[cell[0]][cell[1]] != -1){
                        grid[cell[0]][cell[1]] += 1;
                    }
                }
            }
        }
        
        for (int row = 0; row < numRows; row++){
            for (int col = 0; col < numCols; col++){
                int value = grid[row][col];
                buttons[row][col].setValue(value);
                buttons[row][col].reset();
            }
        }
    }
    
    /**
     * Perform necessary tasks when the game ends, including exposing all the
     * tiles
     */
    void gameOver(){
        for (MButton[] row: buttons){
            for (MButton b: row) {
                b.expose();
            }
        }
        timer.stop();
        newGameB.setIcon(SAD_FACE_ICON);
    }

    void showScores(){
        // show a dialog box that displays the high scores
        JOptionPane.showMessageDialog(frame,
                readScores(), "High Scores", JOptionPane.PLAIN_MESSAGE);
    }

    void changeDifficulty(){
        Object[] possibilities = {"Easy", "Medium", "Hard"};
        String s = (String)JOptionPane.showInputDialog(
                frame,
                "Choose difficulty:",
                "Difficulty",
                JOptionPane.PLAIN_MESSAGE,
                SCORES_ICON,
                possibilities,
                "Easy");

        //If a string was returned, say so.
        if ((s != null) && (s.length() > 0)) {
            if (s == "Easy"){
                numMines = 10;
                numRows = 8;
                numCols = 8;
            }else if(s == "Medium"){
                numMines = 40;
                numRows = 16;
                numCols = 16;
            }else{
                numMines = 99;
                numRows = 16;
                numCols = 30;
            }
            mainPanel.remove(gridPanel);
            
            createTiles();
            frame.pack();
            frame.repaint();
            newGame();
        }
    }

    /**
     * clear all the cells around a cell with 0 mines around it and repeat until
     * all the adjacent ones have been cleared
     * @param button 
     */
    void clearCells(MButton button){
        if (button.getValue() == 0){
            
            // go through the 3x3 grid around the "0" cell
            for (int row = -1; row < 2; row++){
                for (int col = -1; col < 2; col++){
                    int[] loc = {button.getLoc()[0] + row, button.getLoc()[1] + col};
                    
                    // check if the cell exists
                    if (loc[0] >= 0 && loc[0] < numRows && loc[1] >= 0 && loc[1] < numCols){
                        MButton b = buttons[loc[0]][loc[1]];
                        if (!b.isExposed() && !b.isFlagged()){
                            b.expose();
                            tilesLeft--;
                            
                            // repeat recursively until all the adjacent "0" boxes have been cleared
                            if (b.getValue() == 0){
                                clearCells(b);
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * reveal all the mines
     */
    void win(){
        timer.stop();
        for (int mine: mineNums){
            buttons[mine/numCols][mine%numCols].flag();
        }
        String message = "Congratulations!!!\nYou completed the game in " + time + " seconds.\nEnter your name below to save your score:  ";
        String inputName = JOptionPane.showInputDialog(frame, message, "Victory", JOptionPane.PLAIN_MESSAGE);
        if (inputName!= null){
            saveScore(inputName);
        }
    }
    
    /**
     * 
     */
    void newGame(){
        tilesLeft = numRows * numCols - numMines;
        clickNum = 0;
        time = 0;
        stopwatch.setText("0");
        createGrid();
        isOn = true;
        newGameB.setIcon(HAPPY_FACE_ICON);
        minesLeft = numMines;
        minesLeftLabel.setText(Integer.toString(minesLeft));
        timer.stop();
    }

    /**
     * 
     */
    void start(){
        // add 1 to the time for stopwatch every second
        timer.restart();
        timer.start();
    }
    
    /**
     * 
     * @return 
     */
    private String readScores(){
        try
        {
            Scanner input = new Scanner(HSCORES);
            input.useDelimiter("");
            String data = "";
            
            // save all the data in a variable
            while (input.hasNext()){
                data += input.next();
            }
            input.close();
            return data;
        }
        
        catch(IOException e)
        {
            System.out.println(e);
            
            // Because the the function has to return a string
            return "error";
        }
        
    }
    
    /**
     * 
     * @param name 
     */
    private void saveScore(String name){
        // string to be saved
        String entry = time + ": " + name;
        try
        {
            // make an array consisting of lines in the file
            String dataArr[] = readScores().split("\\n");
            
            // boolean to keep track of whether the score has added
            boolean added = false;
            
            for (int i = 0; i < dataArr.length; i ++){
                if (dataArr[i].contains(name)){
                    dataArr[i] = entry;
                    added = true;
                    break;
                }
            }
            
            PrintWriter writer = new PrintWriter(HSCORES);

            for(String line:dataArr){
                writer.println(line);
            }

            if (!added){
                writer.println(entry);
            }
            
            writer.close();
        }
        
        catch(IOException e)
        {
            System.out.println(e);
        }
    }
    
    int firstClickMineReset(MButton b){
        createGrid();
        return buttons[b.getLoc()[0]][b.getLoc()[1]].getValue();
    }
}