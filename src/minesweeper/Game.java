/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import javax.swing.*;
import minesweeper.BoardData.FlagListener;


/**
 *
 * @author ruben
 */

public class Game extends JFrame implements ActionListener, FlagListener{

    private String difficulty;
    private BoardData data;
    private BoardWindow board;
    private JButton reset;
    private JComboBox levelSelector;
    private JLabel flagCount;
    private Color bgColor, textColor;
    
    int c, r, m;
    String[] options = new String[]{"Easy", "Medium", "Hard"};
    
    public Game(String difficulty){
        super("Minesweeper");
                
        if(initGame(difficulty.toLowerCase()))        
            initWindow();
    } 
    
    private boolean initGame(String difficulty){
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
                JOptionPane.showMessageDialog(null, "Invalid Entry. Program will close.");
                System.exit(1);
                return false;            
        }        
        return true;
    }
      
    private void initGame(int c, int r, int m){
        this.c = c; this.r = r; this.m = m;        
        data = new BoardData(c, r, m); 
        data.addFlagListener(this);
    }
    
    private void initWindow(){ 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        bgColor = new Color(35, 61, 77);
        textColor = Color.white;
        
        board = new BoardWindow(data);
        
        JPanel optionsPanel = new JPanel();
        optionsPanel.setOpaque(true);
        optionsPanel.setBackground(bgColor);
        
        levelSelector = new JComboBox(options);
        levelSelector.setSelectedIndex(1);
        optionsPanel.add(levelSelector);
        
        reset = new JButton("Reset");
        reset.addActionListener(this);
        optionsPanel.add(reset);
        
        JPanel flagPanel = new JPanel();
        flagPanel.setBackground(bgColor);
        flagCount = new JLabel("Flags: " + m);
        flagCount.setHorizontalAlignment(SwingConstants.CENTER);        
        flagCount.setForeground(textColor);
        flagPanel.add(flagCount);        
               
        this.add(optionsPanel, BorderLayout.NORTH);
        this.add(board, BorderLayout.CENTER);
        this.add(flagPanel, BorderLayout.SOUTH);
        this.pack();
        
        this.setVisible(true);
    }      

    @Override
    public void actionPerformed(ActionEvent e) {
        initGame(options[levelSelector.getSelectedIndex()].toLowerCase());        
        board.reset(data);
        flagCount.setText("Flags: " + m);
        pack();
    }

    @Override
    public void onValueChange() {
        flagCount.setText("Flags: " + (m - data.flaggedCells()));
    }

}


