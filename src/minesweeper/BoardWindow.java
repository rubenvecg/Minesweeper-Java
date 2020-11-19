/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;
import java.awt.BorderLayout;
import java.awt.Color;
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
public class BoardWindow extends JPanel implements MouseListener{
    
    private CellContainer[][] buttons;
    private int flags;
    private int rowCount, colCount;
    private BoardData data;
    private boolean gameOver;
    
    public BoardWindow(BoardData data){
        super(); 
        this.data = data;
        initLayout();
    } 
    
    public void reset(BoardData data){        
        this.data = data;
        gameOver = false;
        initLayout();
    }
    
    private void initLayout(){
        rowCount = data.getRows();
        colCount = data.getCols();
        
        removeAll();
        setLayout(new GridLayout(rowCount, colCount));              
        buttons = new CellContainer[rowCount][colCount];
        
        for(int i=0; i<rowCount; i++){
            for(int j=0; j<colCount; j++){ 
                buttons[i][j] = new CellContainer(i, j, this);
                add(buttons[i][j]);
            }
        }
    }
    
    private void fillBoard(int r0, int c0){ 
        data.fillBoard(r0, c0);
        int[][] values = data.getBoard();        
                        
        for(int i=0; i<rowCount; i++){
            for(int j=0; j<colCount; j++){                
                String cellValue = "";
                                        
                if(values[i][j] == -1) cellValue = "*";
                else{
                    cellValue = (values[i][j] == 0 ) ? "" : values[i][j]+"";                    
                }
                
                buttons[i][j].changeValue(cellValue);
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
                            if(state == CellState.WRONG_FLAG)
                                buttons[i][j].wrongFlag();
                            if(state == CellState.MINE_PICKED)
                                buttons[i][j].explode();
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
        CellContainer source = (CellContainer) e.getSource();
        int row = source.getRowIndex();
        int col = source.getColIndex();
        
        if(data.isEmpty())
            fillBoard(row, col);
        
        if(!gameOver){
            
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
        printBoard();
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
        private Color hiddenColor, revealedColor, revealedTextColor, wrongFlagColor, borderColor;
        
        public CellContainer(int r, int c, MouseListener listener){
            super();
            rowIndex = r;
            colIndex = c;
            hiddenColor = new Color(97, 155, 138);
            revealedColor = new Color(35, 61, 77);
            revealedTextColor = new Color(230, 230, 230);
            wrongFlagColor = new Color(254, 127, 45);
            borderColor = revealedColor.darker();
            
            valueLabel = new JLabel();
            valueLabel.setVisible(false);
            valueLabel.setFont(new Font("Arial", Font.BOLD, 12));
            valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(30, 30));
            setBackground(hiddenColor);
            setBorder(BorderFactory.createLineBorder(borderColor));
            addMouseListener(listener);
            add(valueLabel, BorderLayout.CENTER);
        }
        
        private void changeValue(String newValue){
            value = newValue;
            valueLabel.setText(value);
        }
        
        public int getRowIndex(){ return rowIndex; }
        public int getColIndex(){ return colIndex; }
        
        public void reset(){
            this.setBackground(hiddenColor);
            valueLabel.setVisible(false);
            valueLabel.setText(this.value);
        }
        
        public void show(){
            this.setBackground(revealedColor);
            valueLabel.setVisible(true);   
            valueLabel.setForeground(revealedTextColor);
        }   
        
        public void flag(){                 
            valueLabel.setText("^");
            valueLabel.setVisible(true);
        }
        
        public void wrongFlag(){
            valueLabel.setText("X");
            valueLabel.setForeground(Color.black);
            valueLabel.setVisible(true);
            this.setBackground(wrongFlagColor);            
        }
        
        public void explode(){
            valueLabel.setText("*");
            valueLabel.setForeground(Color.black);
            valueLabel.setVisible(true);
            this.setBackground(wrongFlagColor);
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
        
        System.out.println("");
    }
}
