/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.util.Random;

/**
 *
 * @author ruben
 */
public class Board {
    private int cols, rows, mines, flaggedMines, visitedCells;
    private Cell[][] cells;
    
    public Board(int r, int c, int m){
        //Initialize attributes
        this.rows = r;
        this.cols = c;
        this.mines = m;
        this.cells = new Cell[r][c];
                
        for(int i=0; i<r; i++)
            for(int j=0; j<c; j++)
                this.cells[i][j] = new Cell();
        
        //Place mines randomly on board
        int mineCount = 0;
        Random rand = new Random();
        
        while(mineCount < mines){        
            int newRow, newCol;
            //Calculate new valid coordinate for mine
            do{
                newRow = rand.nextInt(this.rows);
                newCol = rand.nextInt(this.cols);
            }while(this.cells[newRow][newCol].hasMine()); //Repeat if coordinate already has a mine on it
            
            placeMine(newRow, newCol);
            mineCount++;
        }
    }
    
    public int[][] getBoard(){
        //Return only the values of the cells of the board. -1 represents a cell with a mine
        int[][] values = new int[rows][cols];
        
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++)
                values[i][j] = this.cells[i][j].value;
        }
        return values;
    }
    
    public int[][] getCellStates(){
        //Return current cell states. 0 = unvisited, 1 = flagged, 2 = visited
        int[][] values = new int[rows][cols];
        
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                int v;
                
                if(this.cells[i][j].isVisited) v = 2;
                else if(this.cells[i][j].isFlagged) v = 1;
                else v = 0;  
                
                values[i][j] = v;
            }
        }                
        
        return values;
    }
    
    public int getFlaggedMineCount(){ return flaggedMines; }
    public int getVisitedCellCount(){ return visitedCells; }
    
    private void placeMine(int r, int c){
            this.cells[r][c].placeMine();
            updateCell(r-1, c-1);
            updateCell(r-1, c);
            updateCell(r-1, c+1);
            updateCell(r, c-1);
            updateCell(r, c+1);
            updateCell(r+1, c-1);
            updateCell(r+1, c);
            updateCell(r+1, c+1);                
    }
    
    private void updateCell(int r, int c){
        if(coordIsValid(r,c) && !cellHasMine(r,c))
            this.cells[r][c].update();
    }
    
    public int visitCell(int row, int col) {
        
        if(!coordIsValid(row, col) || cellIsFlagged(row,col))
            return 0;
        
                
        if(this.cells[row][col].value != 0 || this.cells[row][col].isVisited){  
            this.cells[row][col].visit();  
            this.visitedCells++;
                if(cellHasMine(row,col)){
                    visitAll();
                    return -1;
                }
            return 0;
        }      
        
        this.cells[row][col].visit();
        this.visitedCells++;
        return  visitCell(row-1, col)+     //west
                visitCell(row, col-1)+     //north
                visitCell(row, col+1)+     //south
                visitCell(row+1, col)+     //east
                visitCell(row-1, col-1)+   //northeast
                visitCell(row+1, col-1)+   //northwest
                visitCell(row-1, col+1)+   //southeast
                visitCell(row+1, col+1);   //southwest
    }
    
    public void flagCell(int row, int col){
        if(coordIsValid(row, col) && !this.cells[row][col].isVisited){
            this.cells[row][col].flag();
            if(this.cells[row][col].hasMine())
                this.flaggedMines++;
        }
    }
    
    private boolean coordIsValid(int r, int c){
        return (r >= 0 && r < this.rows) && (c >= 0 && c < this.cols); 
    }
    
    private boolean cellHasMine(int r, int c){
        return this.cells[r][c].hasMine();
    }
    
    private boolean cellIsFlagged(int r, int c){
        return this.cells[r][c].isFlagged;
    }
    
    private void visitAll(){
        for(int i=0; i<rows; i++)
            for(int j=0; j<cols; j++)
                this.cells[i][j].visit();
    }
    
    
    private class Cell {
        boolean isFlagged, isVisited;  
        int value;
    
        public Cell(){
            value = 0;
            isFlagged = false;
            isVisited = false;
        }
        
        void placeMine(){
            this.value = -1;
        }
        
        boolean hasMine(){
            return this.value == -1;
        }
        
        void update(){
            this.value++;
        }
        
        void visit(){
            this.isVisited = true;
        }
        
        void flag(){
            this.isFlagged = !isFlagged;
        }      
    }
}
