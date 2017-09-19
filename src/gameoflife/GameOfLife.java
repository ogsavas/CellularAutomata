
package gameoflife;

import java.io.*;
import java.util.Scanner;
import java.awt.*; //needed for graphics
import javax.swing.*; //needed for graphics
import static javax.swing.JFrame.EXIT_ON_CLOSE; //needed for graphics

public class GameOfLife extends JFrame {

    //FIELDS
    int numGenerations = 100000;
    int currGeneration = 1;
    
    Color aliveColor = Color.black;
    Color deadColor = Color.white;
    Color gridColor = Color.gray;
    
    String fileName = "PlantGOL.txt";
    int width = 800; //width of the window in pixels
    int height = 800;
    int borderWidth = 40;
    int numCellsX = 100; //width of the grid (in cells)
    int numCellsY = 100;
        
    boolean alive[][] = new boolean[numCellsX][numCellsY];
    boolean aliveNext[][] = new boolean[numCellsX][numCellsY];
    
    int cellSizeX = (width - borderWidth * 2)/numCellsX; //replace with the correct formula that uses width, borderWidth and numCellsX
    int cellSizeY = (height - borderWidth * 2)/numCellsY; //replace with the correct formula that uses height, borderWidth and numCellsY
    
    int labelX = width / 2;
    int labelY = borderWidth;
 
    
    //METHODS
    
    //Sets all cells to dead
    public void makeEveryoneDead() {
        for (int i = 0; i < numCellsX; i++) {
            for (int j = 0; j < numCellsY; j++) {
                alive[i][j] = false;
            }
        }
    }

    
    //reads the first generations' alive cells from a file
    public void plantFromFile(String nameOfFile) throws IOException {     
        
        FileReader f = new FileReader( nameOfFile );
        Scanner s = new Scanner(f);
        int x,y;
        
        while ( s.hasNext() ) {
            x = s.nextInt();
            y = s.nextInt();            
            alive[x][y] = true;                        
        }    
    }
    
    public void plantFirstGeneration() throws IOException {
        makeEveryoneDead();
        plantFromFile( fileName );
        
        //OR plant the first generation systematically using a pattern, using
        //one of these methods, for example:
        
        //plantBlock ( 20, 20, 40, 40 );
        //plantGlider(1, 2, 4);
        //plantGlider(26, 2, 1);
        //plantGlider(26, 26, 2);
        //plantGlider(1, 26, 3);
    }
        
    

    
    //Plants a solid rectangle of alive cells.  Would be used in place of plantFromFile()
//    public void plantBlock(int startX, int startY, int numColumns, int numRows) {
//        
//        int endCol = Math.min(startX + numColumns, numCellsX);
//        int endRow = Math.min(startY + numRows, numCellsY);
//
//        for (int i = startX; i < endCol; i++) {
//            for (int j = startY; j < endRow; j++) {
//                alive[i][j] = true;
//            }
//        }
//    }

    
//    //Plants a "glider" group, which is a cluster of living cells that migrates across the grid from 1 generation to the next
//    public void plantGlider(int startX, int startY, int direction) { //direction can be "SW", "NW", "SE", or "NE"
//
//        //fill this in at the very end.  It should be one of the last things you do.
//    }

    
    

    


    
    //Counts the number of living cells adjacent to cell (i, j)
    public int countLivingNeighbors(int m, int n) {
        
        int neighbourCounter = 0;
        int xStart = m;
        int yStart = n;
        int xEnd = m;
        int yEnd = n;
                
       
        if (m!=0 && m!=numCellsX-1 && n!=0 && n!=numCellsY-1) {
            xStart --;
            yStart --;
            xEnd ++;
            yEnd ++;
        }
        
        else if (m==0) {            
            if (n==0) {                
                xEnd ++;
                yEnd ++;
            }
            
            else if (n==numCellsY-1) {
                xEnd ++;
                yEnd --;
            }
            
            else {
                yStart --;
                xEnd ++;
                yEnd ++;                                   
            }
        }
        
        else if (m==numCellsX-1) {
            if (n==0) {
                xStart --;
                yEnd ++;
            }
            
            else if (n==numCellsY-1) {
                xStart --;
                yStart --;
            }
            
            else {
                xStart --;
                yStart --;
                yEnd ++;
            }
        }
        
        else if (n==0) {
            xStart --;
            xEnd ++;
            yEnd ++;
        }
        
        else if (n==numCellsY-1) {
            xStart --;
            xEnd ++;
            yStart --;
            
        }
 
            
        for (int a = xStart; a <= xEnd; a++) {
            for (int b = yStart; b <= yEnd; b++) {
                if (alive[a][b] == true) {
                    neighbourCounter ++;
                if (a==m && b==n){
                    neighbourCounter--;
                }
                }
            }
        }
                     
         return neighbourCounter;
    }

    
    //Applies the rules of The Game of Life to set the true-false values of the aliveNext[][] array,
    //based on the current values in the alive[][] array
    public void computeNextGeneration() {
        for (int a = 0; a < numCellsX; a++) {
            for (int b = 0; b < numCellsY; b++) {                
                if (alive[a][b] == true) {
                    if (countLivingNeighbors(a,b) < 2 || countLivingNeighbors(a,b) > 3){
                        aliveNext[a][b] = false;
                    }   
                    else {
                        aliveNext[a][b] = true;
                    }
                }
                                
                
                else if (alive[a][b] == false) {
                    if (countLivingNeighbors(a,b) == 3) {
                        aliveNext[a][b] = true;
                    }
                }             
            }            
        }
    }
    
//Overwrites the current generation's 2-D array with the values from the next generation's 2-D array
    public void plantNextGeneration() {
        for (int i = 0; i < numCellsX; i++) {
            for (int j = 0; j < numCellsY; j++) {
                alive[i][j]=aliveNext[i][j];
            }
            
        }
    }
        
        
        
    //Makes the pause between generations
    public static void sleep(int duration) {
        try {
            Thread.sleep(duration);
        } 
        catch (Exception e) {}
    }

    
    //Displays the statistics at the top of the screen
//    void drawLabel(Graphics g, int state) {
//        g.setColor(Color.black);
//        g.fillRect(0, 0, width, borderWidth);
//        g.setColor(Color.yellow);
//        g.drawString("Generation: " + state, labelX, labelY);
//    }

    
    //Draws the current generation of living cells on the screen
    public void paint(Graphics g) {
        int i, j;        
        
        int xPos = borderWidth;
        
        
        //drawLabel(g, currGeneration);

        for (i = 0; i < numCellsX; i++) {  
            int yPos = borderWidth;
            
            for (j = 0; j < numCellsY; j++) {                
                if ( alive[i][j] == true ) {
                    g.setColor( aliveColor ); //"ON" COLOUR
                } 
                else {
                    g.setColor( deadColor ); //"OFF" COLOUR
                }

                g.fillRect(xPos, yPos, cellSizeX, cellSizeY);
                g.setColor ( gridColor );
                g.drawRect(xPos, yPos, cellSizeX, cellSizeY);
                
                yPos += cellSizeY;                
                }
            xPos += cellSizeX;            
        }
    }


    //Sets up the JFrame screen
    public void initializeWindow() {
        setTitle("Game of Life Simulator");
        setSize(height, width);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.white);
        setVisible(true); //calls paint() for the first time
        
        
    }
    
    
    //Main algorithm
    public static void main(String args[]) throws IOException {

        GameOfLife currGame = new GameOfLife();

        currGame.initializeWindow();
        currGame.plantFirstGeneration(); //Sets the initial generation of living cells, either by reading from a file or creating them algorithmically
                
        for (int i = 1; i <= currGame.numGenerations; i++) {
            currGame.repaint();
            sleep(150);
            currGame.computeNextGeneration();
            currGame.plantNextGeneration();
            sleep(150);
                    
        }
        
    } //end of main()
    
} //end of class
