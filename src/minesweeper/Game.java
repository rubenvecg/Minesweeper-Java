/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author ruben
 */

public class Game extends JFrame implements ActionListener{

    private String difficulty;
    private BoardData data;
    private BoardWindow board;
    private JButton reset;
    
    int c, r, m;
    
    public Game(int c, int r, int m){
        super("Minesweeper");
        this.initGame(c, r, m);
        this.initWindow();
    }
    
    public Game(String difficulty){
        super("Minesweeper");
                
        switch(difficulty){
            case "easy":
                this.initGame(9, 9, 10);                
            break;
            
            case "medium":
                this.initGame(16, 16, 40);
            break;
            
            case "hard":
                this.initGame(16, 30, 99);
            break;
            
            default:
                JOptionPane.showMessageDialog(null, "Invalid Entry");
            break;
        } 
        
        initWindow();
    } 
      
    private void initGame(int c, int r, int m){
        this.c = c; this.r = r; this.m = m;        
        data = new BoardData(c, r, m);        
    }
    
    private void initWindow(){ 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        board = new BoardWindow(data);
        reset = new JButton("Reset");
        reset.addActionListener(this);
        
        
        this.add(reset, BorderLayout.NORTH);
        this.add(board, BorderLayout.SOUTH);
        this.pack();
        
        this.setVisible(true);
    }      

    @Override
    public void actionPerformed(ActionEvent e) {
        initGame(c, r, m);
        board.reset(data);
        pack();
    }

    
    
    
    
}


