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
import minesweeper.Board.CellState;
/**
 *
 * @author ruben
 */
public class Window extends JFrame{
    
    private CellContainer[][] buttons;
    private int flags;
    private MouseListener listener;
    private int rowCount, colCount;
    
        
    public Window(int[][] values, int flags, MouseListener l){
        
        super("Minesweeper");
        initBoard(values, flags, l);
        
        this.add(new JButton("Reset"));
        this.pack();       
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
        this.setVisible(true);
    } 
    
    private void initBoard(int[][] values, int flags, MouseListener l){
        this.listener = l;        
        this.rowCount = values.length;
        this.colCount = values[0].length;
        
        buttons = new CellContainer[rowCount][colCount];
                
        Container cont = this.getContentPane();
        JPanel boardPanel = new JPanel(new GridLayout(rowCount, colCount));
                
        for(int i=0; i<rowCount; i++){
            for(int j=0; j<colCount; j++){
                
                String cellValue = "";
                if(values[i][j] != 0) cellValue = (values[i][j] == -1 ? "*" : values[i][j] + ""); 
                
                buttons[i][j] = new CellContainer(cellValue, i, j, listener); 
                boardPanel.add(buttons[i][j]);
            }
        } 
        cont.add(boardPanel, BorderLayout.SOUTH);
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
    
}
