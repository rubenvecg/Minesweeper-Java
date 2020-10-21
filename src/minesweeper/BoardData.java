/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.util.Random;
import java.util.Stack;

/**
 *
 * @author ruben
 */
public class BoardData {
    private int cols, rows, mines, flaggedMines, markedCells, flaggedCells;
    private Cell[][] cells;
    
    
    public BoardData(int r, int c, int m){
        //Initialize attributes
        rows = r;
        cols = c;
        mines = m;
        cells = new Cell[r][c];
        
                
        for(int i=0; i<r; i++)
            for(int j=0; j<c; j++)
                cells[i][j] = new Cell();
        
        //Place mines randomly on board
        int mineCount = 0;
        Random rand = new Random();
        
        while(mineCount < mines){        
            int newRow, newCol;
            //Calculate new valid coordinate for mine
            do{
                newRow = rand.nextInt(rows);
                newCol = rand.nextInt(cols);
            }while(cellHasMine(newRow, newCol)); //Repeat if coordinate already has a mine on it
            
            placeMine(newRow, newCol);
            mineCount++;
        }
    }
    
    public int[][] getBoard(){
        //Return only the values of the cells of the board. -1 represents a cell with a mine
        int[][] values = new int[rows][cols];
        
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++)
                values[i][j] = cells[i][j].value;
        }
        return values;
    }
    
    public int getFlaggedMines(){ return flaggedMines;}
    private boolean allMinesFlagged(){ return flaggedMines == mines; }
    
    private boolean allCellsMarked(){ return markedCells == (cols * rows) - mines; }
    
    public boolean boardIsClear(){ return allMinesFlagged() && allCellsMarked(); }
    
    public int flaggedCells(){ return flaggedCells; }
    
    public CellState[][] getCellStates(){
        CellState[][] values = new CellState[rows][cols];
        
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                values[i][j] = cells[i][j].state;
            }
        }                
        
        return values;
    }
    
    public CellState getCellState(int r, int c){
        return this.cells[r][c].state;
    }
        
    private void placeMine(int r, int c){
            cells[r][c].placeMine();
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
            cells[r][c].update();
    }
    
    public int markCell(int row, int col) {
        
        if(!coordIsValid(row, col) || cellIsFlagged(row,col))
            return 0;
        
        if(cellHasMine(row,col)){
            showMines();
            return -1;
        }
        
        if(cells[row][col].isMarked)
            return 0;
        
        cells[row][col].mark();  
        markedCells++; 
        
        if(cells[row][col].value != 0)
            return 0;
        
        return  markCell(row-1, col)+     //west
                markCell(row, col-1)+     //north
                markCell(row, col+1)+     //south
                markCell(row+1, col)+     //east
                markCell(row-1, col-1)+   //northeast
                markCell(row+1, col-1)+   //northwest
                markCell(row-1, col+1)+   //southeast
                markCell(row+1, col+1);   //southwest
    }
    
    public void flagCell(int row, int col){
        if(coordIsValid(row, col) && !cells[row][col].isMarked){
            cells[row][col].flag();            
            
            if(cells[row][col].hasMine())
                flaggedMines += cells[row][col].isFlagged ? 1 : -1;

            flaggedCells += cells[row][col].isFlagged ? 1 : -1;
            
        }
    }
    
    private void showMines(){
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                if(cellHasMine(i, j) && (!cells[i][j].isMarked) && (!cells[i][j].isFlagged)) cells[i][j].mark();
            }
        }
    }
    
    //Boolean functions   
    private boolean coordIsValid(int r, int c){
        return (r >= 0 && r < rows) && (c >= 0 && c < cols); 
    }
    
    private boolean cellIsMarked(int r, int c){
        return cells[r][c].state == CellState.MARKED;
    }
    
    private boolean cellHasMine(int r, int c){
        return cells[r][c].hasMine();
    }
    
    private boolean cellIsFlagged(int r, int c){
        return (cells[r][c].state == CellState.FLAGGED || cells[r][c].state == CellState.WRONG_FLAG);
    }
    
     
    private class Cell {
        private boolean isMarked, isFlagged;
        private CellState state;
        private int value;
    
        public Cell(){
            value = 0;
            state = CellState.NONE;
        }
        
        void placeMine(){
            value = -1;
        }
        
        boolean hasMine(){
            return value == -1;
        }
        
        void update(){
            value++;
        }
        
        void mark(){
            isMarked = true;
            isFlagged = false;
            state = CellState.MARKED;
        }
        
        void flag(){
            isFlagged = !isFlagged;
            if(isFlagged)
                state = (value != -1) ? CellState.WRONG_FLAG : CellState.FLAGGED;
            else
                state = CellState.NONE;
        }      
    }
    
    public enum CellState{
        MARKED,
        FLAGGED,
        WRONG_FLAG,
        NONE
    }
}
