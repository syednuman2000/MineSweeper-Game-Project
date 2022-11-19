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
    boolean[][] flagged;
    JButton resetButton;
    JButton flag;
    JLabel textfield;
    
    Random random;
    
    int size = 9;
    int bombs = 10;
    
    ArrayList<Integer> xPositions;
    ArrayList<Integer> yPositions;
    
    boolean flagging;
    int count = 0;
    int lastXChecked;
    int lastYChecked;
    int xZero;
    int yZero;
    
    public Mine_Sweeper(){
        
        lastXChecked = size+1;
        lastYChecked = size+1;
        
        xPositions = new ArrayList<>();
        yPositions = new ArrayList<>();
        
        flagged = new boolean[size][size];
        
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
        /*
        for(int i=0;i<bombs;i++){
            System.out.println(xPositions.get(i)+" "+yPositions.get(i));
        }*/
        
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
        
        resetButton = new JButton();
        resetButton.setForeground(Color.BLUE);
        resetButton.setBackground(Color.WHITE);
        resetButton.setText("Reset");
        resetButton.setFont(new Font("MV Boli",Font.BOLD,20));
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);
        
        flag = new JButton();
        flag.setForeground(Color.ORANGE);
        flag.setBackground(Color.WHITE);
        flag.setText("|>");
        flag.setFont(new Font("MV Boli",Font.BOLD,20));
        flag.setFocusable(false);
        flag.addActionListener(this);
        
        solution = new int[size][size];
        button = new JButton[size][size];
        for(int i=0;i<button.length;i++){
            for(int j=0;j<button[0].length;j++){
                button[i][j] = new JButton();
                button[i][j].setFocusable(false);
                button[i][j].setFont(new Font("MV Boli",Font.BOLD,12));
                button[i][j].addActionListener(this);
                button[i][j].setText("");
                //button[i][j].setBackground(Color.DARK_GRAY);
                buttonPanel.add(button[i][j]);
            }
        }
        
        textPanel.add(textfield);
        frame.add(textPanel, BorderLayout.NORTH);
        frame.add(resetButton,BorderLayout.SOUTH);
        frame.add(flag,BorderLayout.WEST);
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
        /*
        for(int i=0;i<solution.length;i++){
            for(int j=0;j<solution[0].length;j++){
                System.out.print(solution[i][j]+" ");
            }
            System.out.println();
        }*/
    }
    
    public void check(int y,int x){
        boolean over = false;
        if(solution[y][x] == size+1){
            gameOver(false);
            over = true;
        }
        else
            getColor(y,x);
        //button[y][x].setBackground(Color.WHITE);
        if(!over){
            if(solution[y][x] == 0){
                xZero = x;
                yZero = y;
                
                count = 0;
                
                display();
            }
            checkWinner();
        }
    }
    
    public void checkWinner(){
        int buttonLeft = 0;
        for(int i=0;i<button.length;i++){
            for(int j=0;j<button[0].length;j++){
                if(button[i][j].getText().equals("") || button[i][j].getText().equals("|>")){
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
                button[i][j].setBackground(null);
                button[i][j].setEnabled(false);
                
                for(int count=0;count<xPositions.size();count++){
                    if(j==xPositions.get(count) && i==yPositions.get(count)){
                        button[i][j].setEnabled(true);
                        button[i][j].setBackground(Color.BLACK);
                        button[i][j].setForeground(Color.RED);
                        button[i][j].setFont(new Font("MV Boli",Font.BOLD,18));
                        button[i][j].setText("B");
                    }
                }
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == flag){
            if(flagging){
                flag.setBackground(Color.WHITE);
                flagging = false;
            }
            else{
                flag.setBackground(Color.RED);
                flagging = true;
            }
        }
        if(e.getSource() == resetButton){
            frame.dispose();
            new Mine_Sweeper();
        }
        for(int i=0;i<button.length;i++){
            for(int j=0;j<button[0].length;j++){
                if(e.getSource() == button[i][j])
                    if(flagging && button[i][j].getText().equals("") || flagging && button[i][j].getText().equals("|>")){
                        if(flagged[i][j]){
                            button[i][j].setText("");
                            button[i][j].setBackground(null);
                            flagged[i][j]=false;
                        }
                        else{
                            button[i][j].setText("|>");
                            button[i][j].setBackground(Color.RED);
                            button[i][j].setForeground(Color.ORANGE);
                            flagged[i][j]=true;
                        }
                    }else{
                        if(!flagged[i][j]){
                            check(i,j);
                        }
                    }
            }
        }
    }
    
    public void display()
    {
	if(count<1){
            if((xZero-1)>=0)
                getColor(yZero,xZero-1);
            if((xZero+1)<size)
                getColor(yZero,xZero+1);
            if((yZero-1)>=0)
                getColor(yZero-1,xZero);
            if((yZero+1)<size)
                getColor(yZero+1,xZero);
            if((yZero+1)<size && (xZero+1)<size)
                getColor(yZero+1,xZero+1);
            if((yZero-1)>=0 && (xZero-1)>=0)
                getColor(yZero-1,xZero-1);
            if((yZero+1)<size && (xZero-1)>=0)
                getColor(yZero+1,xZero-1);
            if((yZero-1)>=0 && (xZero+1)<size)
                getColor(yZero-1, xZero+1);

            count++;		
            display();	
	}
        else{
            for(int y=0; y<button.length; y++){
                for(int x=0; x<button[0].length; x++){
                    if(button[y][x].getText().equals("0")){
                        if(y-1>=0){
                            if(button[y-1][x].getText().equals("") || button[y-1][x].getText().equals("1>")){
                                lastXChecked=x;
                                lastYChecked=y;
                            }
                        }
                        if(y+1<size){
                            if(button[y+1][x].getText().equals("") || button[y+1][x].getText().equals("1>")){
                                lastXChecked=x;
                                lastYChecked=y;
                            }
                        }
                        if(x-1>=0){
                            if(button[y][x-1].getText().equals("") || button[y][x-1].getText().equals("1>")){
                                lastXChecked=x;
                                lastYChecked=y;
                            }
                        }
                        if(x+1<size){
                            if(button[y][x+1].getText().equals("") || button[y][x+1].getText().equals("1>")){
                                lastXChecked=x;
                                lastYChecked=y;
                            }
                        }
                        if(x-1>=0 && y-1>=0){
                            if(button[y-1][x-1].getText().equals("") || button[y-1][x-1].getText().equals("1>")){
                                lastXChecked=x;
                                lastYChecked=y;
                            }
                        }
                        if(x+1<size && y+1<size){
                            if(button[y+1][x+1].getText().equals("") || button[y+1][x+1].getText().equals("1>")){
                                lastXChecked=x;
                                lastYChecked=y;
                            }
                        }
                        if(x+1<size && y-1>=0){
                            if(button[y-1][x+1].getText().equals("") || button[y-1][x+1].getText().equals("1>")){
                                lastXChecked=x;
                                lastYChecked=y;
                            }
                        }
                        if(x-1>=0 && y+1<size){
                            if(button[y+1][x-1].getText().equals("") || button[y+1][x-1].getText().equals("1>")){
                                lastXChecked=x;
                                lastYChecked=y;
                            }
                        }
                    }
                }
            }

            if(lastXChecked<size+1 && lastYChecked<size+1)
            {				
                    xZero=lastXChecked;
                    yZero=lastYChecked;

                    count=0;

                    lastXChecked=size+1;
                    lastYChecked=size+1;

                    display();			
            }
        }
    }
    
    public void getColor(int y,int x){
        if(solution[y][x] == 0)
            button[y][x].setEnabled(false);
        if(solution[y][x] == 1)
            button[y][x].setForeground(Color.BLUE);
        if(solution[y][x] == 2)
            button[y][x].setForeground(Color.GREEN);
        if(solution[y][x] == 3)
            button[y][x].setForeground(Color.RED);
        if(solution[y][x] == 4)
            button[y][x].setForeground(Color.MAGENTA);
        if(solution[y][x] == 5)
            button[y][x].setForeground(new Color(128,0,128));
        if(solution[y][x] == 6)
            button[y][x].setForeground(Color.CYAN);
        if(solution[y][x] == 7)
            button[y][x].setForeground(new Color(42,13,93));
        if(solution[y][x] == 8)
            button[y][x].setForeground(Color.LIGHT_GRAY);
            
        button[y][x].setBackground(null);
        button[y][x].setText(String.valueOf(solution[y][x]));
        
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        new Mine_Sweeper();
    }
}
