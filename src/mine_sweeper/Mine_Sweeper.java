/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mine_sweeper;

/**
 *
 * @author Numan
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Mine_Sweeper implements ActionListener{

    JFrame frame;
    JPanel textPanel;
    JPanel buttonPanel;
    int[][] solution;
    JButton[][] button;
    JLabel textfield;
    
    Random random;
    
    int size = 9;
    int bombs = 10;
    
    ArrayList<Integer> xPositions;
    ArrayList<Integer> yPositions;
    
    public Mine_Sweeper(){
        
        xPositions = new ArrayList<>();
        yPositions = new ArrayList<>();
        
        random = new Random();
        for(int i=0;i<bombs;i++){
            xPositions.add(random.nextInt(size));
            yPositions.add(random.nextInt(size));
        }
        
        for(int i=0;i<bombs;i++){
            for(int j=i+1;j<bombs;j++){
                if(xPositions.get(i) == xPositions.get(j) && yPositions.get(i) == yPositions.get(j)){
                    xPositions.set(j,random.nextInt(size));
                    yPositions.set(j,random.nextInt(size));
                    
                    i=0;
                    j=0;
                }
            }
        }
        
        for(int i=0;i<bombs;i++){
            System.out.println(xPositions.get(i)+" "+yPositions.get(i));
        }
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        
        textPanel = new JPanel();
        textPanel.setVisible(true);
        textPanel.setBackground(Color.BLACK);
        
        buttonPanel = new JPanel();
        buttonPanel.setVisible(true);
        buttonPanel.setLayout(new GridLayout(size,size));
        
        textfield = new JLabel();
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setFont(new Font("MV Boli",Font.BOLD,20));
        textfield.setForeground(Color.BLUE);
        textfield.setText(bombs+" Bombs");
        
        solution = new int[size][size];
        button = new JButton[size][size];
        for(int i=0;i<button.length;i++){
            for(int j=0;j<button[0].length;j++){
                button[i][j] = new JButton();
                button[i][j].setFocusable(false);
                button[i][j].setFont(new Font("MV Boli",Font.BOLD,12));
                button[i][j].addActionListener(this);
                button[i][j].setText("");
                buttonPanel.add(button[i][j]);
            }
        }
        
        textPanel.add(textfield);
        frame.add(textPanel, BorderLayout.NORTH);
        frame.add(buttonPanel);
        
        frame.setSize(570,570);
        frame.validate();
        frame.setLocationRelativeTo(null);
        
        getSolution();
    }
    
    public void getSolution(){
        for(int y=0;y<solution.length;y++){
            for(int x=0;x<solution[0].length;x++){
                boolean changed = false;
                int bombsAround = 0;
                
                for(int i=0;i<xPositions.size();i++){
                    if(x == xPositions.get(i) && y == yPositions.get(i)){
                        solution[y][x] = size+1;
                        changed = true;
                    }
                }
                
                if(!changed){
                    for(int i=0;i<xPositions.size();i++){
                        if(x-1 == xPositions.get(i) && y == yPositions.get(i)){
                            bombsAround++;
                        }
                        if(x+1 == xPositions.get(i) && y == yPositions.get(i)){
                            bombsAround++;
                        }
                        if(x == xPositions.get(i) && y+1 == yPositions.get(i)){
                            bombsAround++;
                        }
                        if(x == xPositions.get(i) && y-1 == yPositions.get(i)){
                            bombsAround++;
                        }
                        if(x+1 == xPositions.get(i) && y+1 == yPositions.get(i)){
                            bombsAround++;
                        }
                        if(x-1 == xPositions.get(i) && y-1 == yPositions.get(i)){
                            bombsAround++;
                        }
                        if(x-1 == xPositions.get(i) && y+1 == yPositions.get(i)){
                            bombsAround++;
                        }
                        if(x+1 == xPositions.get(i) && y-1 == yPositions.get(i)){
                            bombsAround++;
                        }
                    }
                    solution[y][x] = bombsAround;
                }
            }
        }
        
        for(int i=0;i<solution.length;i++){
            for(int j=0;j<solution[0].length;j++){
                System.out.print(solution[i][j]+" ");
            }
            System.out.println();
        }
    }
    
    public void check(int y,int x){
        boolean over = false;
        if(solution[y][x] == size+1){
            gameOver(false);
            over = true;
        }
        
        if(!over){
            button[y][x].setText(String.valueOf(solution[y][x]));
            checkWinner();
        }
    }
    
    public void checkWinner(){
        int buttonLeft = 0;
        for(int i=0;i<button.length;i++){
            for(int j=0;j<button[0].length;j++){
                if(button[i][j].getText() == ""){
                    buttonLeft++;
                }
            }
        }
        
        if(buttonLeft == bombs){
            gameOver(true);
        }
    }
    
    public void gameOver(boolean won){
        if(won){
            textfield.setForeground(Color.GREEN);
            textfield.setText("YOU WIN!!");
        }
        else{
            textfield.setForeground(Color.RED);
            textfield.setText("GAME OVER!!");
        }
        
        for(int i=0;i<button.length;i++){
            for(int j=0;j<button[0].length;j++){
                button[i][j].setEnabled(false);
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        for(int i=0;i<button.length;i++){
            for(int j=0;j<button[0].length;j++){
                if(e.getSource() == button[i][j])
                    check(i,j);
            }
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        new Mine_Sweeper();
    }
}
