/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import minesweeper.BoardData.CellState;
/**
 *
 * @author ruben
 */
public class BoardWindow extends JPanel  implements MouseListener{
    
    private CellContainer[][] buttons;
    private int flags;
    private int rowCount, colCount;
    private BoardData data;
    private boolean gameOver;
    
        
    public BoardWindow(BoardData data){
        super(); 
        this.data = data;
        initBoard();       
    } 
    
    public void reset(BoardData data){        
        this.data = data;
        gameOver = false;
        initBoard(); 
        System.out.println("");
        printBoard();
    }
    
    private void initBoard(){      
              
        int[][] values = data.getBoard();
        this.rowCount = values.length;
        this.colCount = values[0].length;
        
        removeAll();
        setLayout(new GridLayout(rowCount, colCount));
              
        buttons = new CellContainer[rowCount][colCount];
                        
        for(int i=0; i<rowCount; i++){
            for(int j=0; j<colCount; j++){
                
                String cellValue = "";
                if(values[i][j] != 0) cellValue = (values[i][j] == -1 ? "*" : values[i][j] + ""); 
                
                buttons[i][j] = new CellContainer(cellValue, i, j, this); 
                add(buttons[i][j]);
            }
        }        
    }
    
    public void update(CellState[][] states, boolean gameOver){         
        
        for(int i=0; i<states.length; i++){
            for(int j=0; j<states[0].length; j++){
                
                CellState state = states[i][j];
                
                if(state == CellState.NONE){
                    buttons[i][j].reset();            
                }else{
                    if(state == CellState.MARKED){ 
                        buttons[i][j].show();                    
                    }else{
                        if(!gameOver) buttons[i][j].flag();
                        else{
                            if(state == CellState.FLAGGED)
                                buttons[i][j].flag();
                            else
                                buttons[i][j].wrongFlag();
                        }
                    }
                }              
            }
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
                if(data.markCell(row, col) == -1){
                    gameOver = true;                    
                }                
            }

            if(SwingUtilities.isRightMouseButton(e)){
                data.flagCell(row, col);
                System.out.println(data.getFlaggedMines());
            }                       
        }
        
        this.update(data.getCellStates(), gameOver);
        if(data.boardIsClear()) JOptionPane.showMessageDialog(this, "Good job!");
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
    
    public class CellContainer extends JPanel{
        private String value;
        private JLabel valueLabel;
        private int rowIndex, colIndex;
        private boolean flagged = false; 
        
        public CellContainer(String v, int r, int c, MouseListener listener){
            super();
            this.rowIndex = r;
            this.colIndex = c;
            
            value = v;
            valueLabel = new JLabel(v);
            valueLabel.setVisible(false);
            valueLabel.setFont(new Font("Arial", 12, 12));
            
            this.setPreferredSize(new Dimension(30, 30));
            this.setBackground(Color.blue);
            this.setBorder(BorderFactory.createLineBorder(Color.white));
            this.addMouseListener(listener);
            this.add(valueLabel, BorderLayout.CENTER);
        }
        
        public int getRowIndex(){ return rowIndex; }
        public int getColIndex(){ return colIndex; }
        
        public void reset(){
            this.setBackground(Color.blue);
            valueLabel.setVisible(false);
            valueLabel.setForeground(Color.black);
            valueLabel.setText(this.value);
        }
        
        public void show(){
            this.setBackground(Color.gray);
            valueLabel.setVisible(true);             
        }   
        
        public void flag(){                 
            valueLabel.setText("~");
            valueLabel.setForeground(Color.red);            
            valueLabel.setVisible(true);
        }
        
        public void wrongFlag(){
            valueLabel.setText("X");
            this.setBackground(Color.red);
            valueLabel.setForeground(Color.black);
        }
    }
    
    
    
    private void printBoard(){
        int[][] values = data.getBoard();
        CellState[][] states = data.getCellStates();
        
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
}
