/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import minesweeper.Board.CellState;
import minesweeper.Window.CellContainer;

/**
 *
 * @author ruben
 */

public class Game implements MouseListener{

    private String difficulty;
    private Board board;
    private Window window;
    private boolean gameOver;
    
    public Game(int c, int r, int m){
        board = new Board(c, r, m);   
        window = new Window(board.getBoard(), m, this);
    }
    
    public Game(String difficulty){
        
        switch(difficulty){
            case "easy":
                board = new Board(9, 9, 10);
                window = new Window(board.getBoard(), 9, this);
            break;
            
            case "medium":
                board = new Board(16, 16, 40);
                window = new Window(board.getBoard(), 16, this);
            break;
            
            case "hard":
                board = new Board(16, 30, 99);
                window = new Window(board.getBoard(), 99, this);
            break;
            
            default:
                System.out.println("Invalid entry.");
            break;
        } 
        
        printBoard();
    }
    
    
    
    private void printBoard(){
        int[][] values = board.getBoard();
        CellState[][] states = board.getCellStates();
        
        for(int i=0; i<values.length; i++){
            String line = "";
            String boardLine = "";
            
            for(int j=0; j<values[i].length; j++){ 
                if(states[i][j] == CellState.MARKED)                    
                    line += (values[i][j] == -1 ? "*" : values[i][j]) + " ";
                else{
                    if(states[i][j] == CellState.FLAGGED)
                        line += "F ";
                    else if(states[i][j] == CellState.WRONG_FLAG)
                        line += "W ";
                    else
                        line += "X ";
                }
                    
                
                boardLine += (values[i][j] == -1 ? "*" : values[i][j]) + " "; 
            }
            
            System.out.println(line + "\t" + boardLine);
        }
    }
    
    
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        
        if(!gameOver){
            CellContainer source = (CellContainer) e.getSource();
            int row = source.getRowIndex();
            int col = source.getColIndex();


            if(SwingUtilities.isLeftMouseButton(e)){
                if(board.markCell(row, col) == -1)
                    gameOver = true;
            }

            if(SwingUtilities.isRightMouseButton(e)){
                board.flagCell(row, col);
            }

            System.out.println("Ready for next move");
            printBoard();
        }
        
        window.update(board.getCellStates(),gameOver);

        
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

}


