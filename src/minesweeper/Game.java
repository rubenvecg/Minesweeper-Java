/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ruben
 */

public class Game {

    private String difficulty;
    private Board board;
    
    public Game(int c, int r, int m){
        board = new Board(c, r, m);               
    }
    
    public Game(String difficulty){
        switch(difficulty){
            case "easy":
                board = new Board(9, 9, 10);
            break;
            
            case "medium":
                board = new Board(16, 16, 40);
            break;
            
            case "hard":
                board = new Board(16, 30, 99);
            break;               
        }        
    }
    
    public void start() throws IOException{
                
        boolean openedMine = false;
        
        while(!openedMine){
            printBoard();
            Command command = readCommand();
            
            if(command == null)
                System.out.println("That is an invaid command, please try again.");
            else{
                int row = command.coord[0];            
                int col = command.coord[1];
                
                if(command.type.equals("flag"))
                    board.flagCell(row, col);
                else if(command.type.equals("visit"))
                    openedMine = (board.visitCell(row, col) == -1);
                else 
                    System.out.println("This is not an existing command. Please try again.");
            }            
        }
        
        printBoard();
        System.out.println("Game over");       
    }
    
    private void printBoard(){
        int[][] values = board.getBoard();
        int[][] states = board.getCellStates();
        
        for(int i=0; i<values.length; i++){
            String line = "";
            String boardLine = "";
            
            for(int j=0; j<values[i].length; j++){ 
                if(states[i][j] == 2)                    
                    line += (values[i][j] == -1 ? "*" : values[i][j]) + " ";
                else{
                    if(states[i][j] == 1)
                        line += "F ";
                    else
                        line += "X ";
                }
                    
                
                boardLine += (values[i][j] == -1 ? "*" : values[i][j]) + " "; 
            }
            
            System.out.println(line + "\t" + boardLine);
        }
    }
    
    //TODO: Testing-run mark and flag commands simultaneously
    //Keep track of flagged mines
    private Command readCommand(){
    //Command examples: flag 5 5, visit 3, 2
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));        
        
        try {
            
            String s = reader.readLine();
            String[] split = s.split(" ");
            
            if(split.length != 3) return null;
            
            String type = split[0];
            int row = Integer.parseInt(split[1]);
            int col = Integer.parseInt(split[2]);
            
            return new Command(type, --row, --col);
            
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NumberFormatException ex) {
            return null;
        }
        
        return null;
    }
    
    private class Command{
        String type;
        int[] coord;
        
        public Command(String type, int r, int c){
            this.type = type;
            this.coord = new int[2];
            this.coord[0] = r;
            this.coord[1] = c;
        }       
    }   
}


