/*
 * NAME - MAYURI GUPTA
 * ROLL NUMBER- 1710110209
 * 
 * ALGORITHM:
 * Given a particular state of the board. It verifies if the sudoku can be solved with a given state or not.
My sudoku GUI gives two flexibility to the user. 
1.	Enter the sudoku puzzle manually and then click on the solve button.
2.	Upload the csv file as an input to the sudoku grid by clicking on upload option. 
Clear Option to clear the sudoku grid to enter the new numbers. 
It first of all selects a blank or basically unassigned cell in a given state of the sudoku. The first cell found will be returned. 
Then I have used various heuristics:
1.	Heuristic of the domain reduction of the cell. This heuristic function would be to reduce the domain of each cell so that it can take only particular values by analyzing through constraint satisfaction. It will check through all the rows, columns and 3*3 grid to make a list of all the numbers that can be assigned to a particular cell. This is called domain reduction. The function used in this domain_reduction.
2.	Heuristic of the selecting best cell. This particular selects a best cell based on the smaller domain among all the domains of the cell and acts as a tiebreaker if multiple cells are found. The function used in this best_Cell.
3.	Heuristic of selecting the best value that can be assigned to the best cell, highest constraining cell using constraint satisfaction problem. The most appropriate value is being assigned to the cell that has a smaller domain using constraint satisfaction. Apply the constraint inference rules to the selected value and the cell to generate all possible constraints as defined above. The function used is best_value.
If the set of constraints contains a contraction, then report that this path is a dead end. Else assign the value to the cell.
Then we do forward checking to find out how this assignment affects other blank or unassigned cells by recursively calling all the heuristics. 
If the assignment affects the constraints of any cell, then the domain and rules of that cell are returned and, in that case, backtracking needs to be done in order to update the value of the cell whose assignment affects other cells.
This process is repeated again and again until there is no way to go forward or solution has been found.
 */

package sudoku;
import java.awt.*;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SudokuFrame implements ActionListener{
	// Main Frame which displays sudoku game.
	static JFrame f=new JFrame();
	JLabel l=new JLabel();
	Dimension d=Toolkit.getDefaultToolkit().getScreenSize(); // setting the size of the frame to be same as the size of the screen.
	JLabel l1=new JLabel("SUDOKU PUZZLE");
	JLabel l2=new JLabel("Life is like solving sudoku.....");
	JLabel l3=new JLabel("Select a number:");
	static JLabel l4; // label to display the logical steps dynamically
	JLabel l5=new JLabel("<html>Here you can view<br/>the logical steps used<br/>to solve the sudoku<br/>puzzle after clicking on<br/>solve or upload.</html>");
	static JPanel p=new JPanel(); // panel for displaying logical steps
	static JButton [][]buttons=new JButton[9][9]; // array of buttons for 9*9 sudoku grid
	JButton [] bt=new JButton[10];
	JButton solve=new JButton("Solve");
	JButton upload=new JButton("Upload");
	JButton clear=new JButton("Clear");
	static int counter=1;
	static Box box = Box.createVerticalBox(); 
	int put=0;
	JScrollPane scroll; // scroll pane for displaying logical steps
	SudokuFrame(){
		// setting the features of the frame.
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setSize(d);
		f.getContentPane().setBackground(Color.cyan);
		f.setLayout(null);
		// setting a particular font size
		Font font=new Font("Lucida Handwriting",Font.BOLD,60);
		l1.setFont(font);
		l1.setForeground(Color.BLUE);
		l1.setBounds(240, 10, 600, 60);
		font=new Font("Lucida Handwriting",Font.BOLD,20);
		l2.setFont(font);
		l2.setForeground(Color.BLACK);
		l2.setBounds(850, 20, 600, 60);
		font=new Font("Lucida Handwriting",Font.BOLD,25);
		l3.setFont(font);
		l3.setForeground(Color.red);
		l3.setBounds(1090, 140, 300,50);
		int c=500,h=100,counter=1;
		// for loop for displaying the buttons on the scree, basically the 9*9 grid.
		for(int i=0;i<9;i++) {
			if(i%3==0 &&counter==0)
				counter=1;
			else if(i%3==0 &&counter==1)
				counter=0;
			for(int j=0;j<9;j++) {
				buttons[i][j]=new JButton();
				buttons[i][j].setBounds(c, h, 60,60);
				buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
				c+=60;
				if(j%3==0)
					counter+=1;
				if(counter%2==0)
					buttons[i][j].setBackground(Color.lightGray);
				else
					buttons[i][j].setBackground(Color.WHITE);
				buttons[i][j].addActionListener(this);
				f.add(buttons[i][j]);
			}
			h+=60;
			c=500;
			counter-=3;
		}
		c=1200;h=120;
		// for loop for displying 10 buttons for number selecting
		for(int i=0;i<10;i++)
		{
			if(i%2!=0) {
				c=1200;
			}
			else {
				c=1120;
				h+=80;
			}
			bt[i]=new JButton((i)+"");
			bt[i].setBounds(c, h, 80,80);
			bt[i].setBackground(Color.pink);
			bt[i].setFont(new Font("Arial", Font.BOLD, 40));
			bt[i].addActionListener(this);
			f.add(bt[i]); // adding the button to the frame
		}
		solve.setFont(new Font("Arial", Font.BOLD, 25)); // adding the solve button
		solve.setBounds(695, 660, 150, 60);
		upload.setFont(new Font("Arial", Font.BOLD, 25)); // adding the upload button
		upload.setBounds(880, 660, 150, 60);
		clear.setFont(new Font("Arial", Font.BOLD, 25)); // adding the clear button
		clear.setBounds(510, 660, 150, 60);
		solve.addActionListener(this);  // adding the listener to the solve button
		upload.addActionListener(this); // adding the listener to the upload button
		clear.addActionListener(this);  // adding the listener to the clear button
		p.setBackground(Color.cyan);
		l5.setFont(new Font("Lucida Handwriting", Font.BOLD, 25));
		p.setBounds(20, 250, 430, 540);
		p.add(l5);
		// adding all the values to the frame.
		f.add(l1);
		f.add(l2); 
		f.add(l3);
		f.add(solve);
		f.add(upload);
		f.add(clear);
		f.add(p);
		f.setVisible(true);
	}
public static void main(String args[]) {
	new SudokuFrame(); // calling the constructor of the sudoku
}
@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	// if condition to check whether solve button was clicked.
	if(e.getActionCommand().equals("Solve")) {
		// declaring the board array
		int[][] board = new int[9][9];                 // sudoku matrix for 9*9 
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				if(buttons[i][j].getText().length()!=0)
				board[i][j]=Integer.parseInt(buttons[i][j].getText()); // adding the content of the buttons to the array board
				else
					board[i][j]=0;
			}
		}
		p.remove(l5);
		f.remove(p);
		p.add(box);
		p.setBackground(Color.cyan);
		scroll = new JScrollPane(p);
		f.getContentPane().add(scroll);
		f.pack();
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		scroll.setBounds(20, 100, 430, 540);
		f.add(scroll);
		boolean ans=CalSudoku(board); // calling the actual function which calculates the result 
		int[][] board1 = new int[9][9];                 // sudoku matrix for 9*9 
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				if(buttons[i][j].getText().length()!=0)
				board1[i][j]=Integer.parseInt(buttons[i][j].getText()); // adding the content of the buttons to the array board
				else
					board1[i][j]=0;
			}
		}
		int flag=0;
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				if(board[i][j]!=board1[i][j]) {
					flag=1;
					break;
				}
			}
		}
		if(flag==1)
			JOptionPane.showMessageDialog(null, "Answer is wrong");
		else
			JOptionPane.showMessageDialog(null,"Answer is correct");
		f.repaint();
		f.validate();
	}
	// if the button pressed was upload
	else if(e.getActionCommand().equals("Upload")) {
		try {
			open(); // calling the open function to open the open dialog box to select a file from it.
		} 
		catch (IOException e1) {} // exception to handle it.
	}
	// if the button pressed was clear 
	else if(e.getActionCommand().equals("Clear")) {
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++)
				buttons[i][j].setText(""); // clearing the contents of the sudoku grid
		}
		p.removeAll();
		try {
		f.remove(scroll);
		}
		catch(Exception e1) {}
		p.add(l5);
		p.setBounds(20, 250, 430, 540);
		f.add(p);
		f.repaint(); // refreshing the contents
		f.revalidate();
	}
	// if one of the 10 numbers buttons are clicked, then get the number which was pressed.
	else if(e.getActionCommand().length()!=0) {
	put=Integer.parseInt(e.getActionCommand());
	}
	// if the sudoku cell is pressed, then place the number in the grid.
	else
	{
		JButton clicked = (JButton) e.getSource();
		clicked.setForeground(Color.BLUE.darker()); // get the place where that number needs to be placed in the cell.
		clicked.setText(put+"");
	}
}
// function that checks if sudoku can be solved or not.
public static boolean CalSudoku(int[][] board) {
	// TODO Auto-generated method stub
	boolean check=CheckEmptyLocation(board);
	if(check==true)
		return true;
	else
		return false;
}
// This function take initial state of the board as the input and do forward checking for the remaining cell to check if the heuristics value assigned was correct or not.
private static boolean CheckEmptyLocation(int[][] board) {
	// TODO Auto-generated method stub
	int flag=0;
	int row_i=-1;
	int col_j=-1;
	for(int i=0;i<9;i++)
	{
		// selecting the non empty cell
		for(int j=0;j<9;j++) {
			if(board[i][j]==0)
			{
				row_i=i;  // assigning the row number of that empty cell
				col_j=j;  // assigning the column number of that empty cell
				flag=1;
				break;
			}
		}
		if(flag==1)
			break;
	}
	if(flag==0)
		return true;
	else
	{
		// doing forward checking for the other cells using constraint satisfaction problem
		for(int i=1;i<=9;i++) {
			// checking constraints satisfaction.
			if(allowed(board,row_i,col_j,i)) {
				final int mi=row_i;
				final int mj=col_j;
				final int ii=i;
				//swingerwroker class for adding the delay.
				SwingWorker sw1 = new SwingWorker()  
		        { 
		            @Override
		            protected String doInBackground() throws Exception  
		            { 
		                // define what thread will do here 
		            	if(counter<=500)
		                Thread.sleep(50);
		            	else
		            		Thread.sleep(0);
		                String res = "Finished Execution"; 
		                return res; 
		            } 
		            @Override
		            protected void done()  
		            { 
		            	//buttons[mi][mj].setText(board[mi][mj]+""); /// displaying the result on the board
		              } 
		        }; 
		        // executes the swingworker on worker thread
		        sw1.execute();
		        // swingwroker to add delay for the display of the logical steps
		        SwingWorker sw2 = new SwingWorker()  
		        { 
		  
		            @Override
		            protected String doInBackground() throws Exception  
		            { 
		                // define what thread will do here 
		                Thread.sleep(10);
						
		                String res = "Finished Execution"; 
		                return res; 
		            } 
		            @Override
		            protected void done()  
		            { 
		            	l4=new JLabel("Step: "+"The value "+ii+" is assign to row "+mi+" and column "+mj+"\n"); // adding the label which displays the logical step
						Font font1=new Font("Lucida Handwriting",Font.BOLD,15);
							l4.setOpaque(true);
							Border border1 = BorderFactory.createLineBorder(Color.BLACK, 1);
							l4.setFont(font1);
							l4.setBorder(border1); // setting the border
							if(counter%2==0)
							l4.setBackground(new Color(238,232,170));
							else
								l4.setBackground(new Color(255,255,224));
		            	box.add(l4);
				        counter+=1;
		            	
		              } 
		        }; 
		          
		        // executes the swingworker on worker thread
		        sw2.execute();
				board[row_i][col_j]=i;
				if(CheckEmptyLocation(board)) {
					 sw2 = new SwingWorker()  
			        { 
			  
			            @Override
			            protected String doInBackground() throws Exception  
			            { 
			                // define what thread will do here 
			                Thread.sleep(10);
							
			                String res = "Finished Execution"; 
			                return res; 
			            } 
			            @Override
			            protected void done()  
			            { 
			            	Font font=new Font("Lucida Handwriting",Font.BOLD,15);
							l4=new JLabel("Step: "+"Final. The position of "+ii+" in row "+mi+" and column "+mj+" is final in this cell.\n");
							l4.setOpaque(true);
							Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
							l4.setFont(font);
							l4.setBorder(border);
							if(counter%2==0)
							l4.setBackground(new Color(238,232,170));
							else
								l4.setBackground(new Color(255,255,224));
							box.add(l4);
					        counter+=1;
			            	
			              } 
			        }; 
			          
			        // executes the swingworker on worker thread
			        sw2.execute();
					return true;
				}
				else
				{
					board[row_i][col_j]=0;
					// swingwroker to add delay for the display of the logical steps
					 sw2 = new SwingWorker()  
			        { 
			  
			            @Override
			            protected String doInBackground() throws Exception  
			            { 
			                // define what thread will do here 
			                Thread.sleep(10); // adding the sleep
							
			                String res = "Finished Execution"; 
			                return res; 
			            } 
			            @Override
			            protected void done()  
			            { 
			            	Font font=new Font("Lucida Handwriting",Font.BOLD,15); // displaying the logical steps on the screen
							l4=new JLabel("Step: "+"The constraint satisfaction problem of "+ii+" in row "+mi+" and column "+mj+".\n");
							Border border = BorderFactory.createLineBorder(Color.BLACK, 1); // border factory
							l4.setFont(font);
							l4.setBorder(border);
							l4.setOpaque(true);
							if(counter%2==0)
								l4.setBackground(new Color(238,232,170));
								else
									l4.setBackground(new Color(255,255,224));
							
							box.add(l4);
					        counter+=1;
			            	
			              } 
			        }; 
			          
			        // executes the swingworker on worker thread
			        sw2.execute();
	            	
					
				}
			}
		}
	}
		return false;
}
// function which checks all the constraints in the sudoku problem. It checks in rows, columns, 3*3 grid cell.
private static boolean allowed(int[][] board, int row_i, int col_j, int n){
	// TODO Auto-generated method stub
	// check if the same number exists in the row of row_i
	for(int col=0;col<9;col++) {
		if(board[row_i][col]==n)
			return false;
	}
	// checking for a particular column
	for(int row=0;row<9;row++) {
		if(board[row][col_j]==n)
			return false;
	}
	//checking for 3 by 3 grid
	int start_r=row_i-(row_i%3);
	int start_c=col_j-(col_j%3);
	for(int row=start_r;row<(start_r+3);row++) {
		for(int col=start_c;col<(start_c+3);col++) {
			if(board[row][col]==n)
				return false;
		}
	}
	return true;
}
// the function to open the open dialog box in order to upload the csv file.
public  void open()throws IOException{
	JFileChooser chooser=new JFileChooser();
	Scanner in=null;
	if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
		File selected= chooser.getSelectedFile();
		in=new Scanner(selected); // scanner input
		String [][]data=new String [9][9];
		int [][]board=new int [9][9]; // array of board
		int i=0;
		String row;
		while(in.hasNext()) {
			row = in.nextLine();
			data[i]=row.split(","); // splitting the value that reads the csv file
			i+=1;
		}
		
		for(int k=0;k<9;k++) {
			for(int j=0;j<9;j++) {
				board[k][j]=Integer.parseInt(data[k][j]); 
			}
		}
		p.remove(l5);
		f.remove(p);
		p.add(box);
		p.setBackground(Color.cyan);
		scroll = new JScrollPane(p);
		f.getContentPane().add(scroll);
		f.pack();
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		scroll.setBounds(20, 100, 430, 540);
		f.add(scroll);
		for(i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				if(board[i][j]!=0) {
					buttons[i][j].setForeground(Color.BLUE.darker());
					buttons[i][j].setText(board[i][j]+"");
				}
			}
		}
		// function to find if the sudoku can be solved or not.
		boolean ans=CalSudoku(board);
		if(ans==true) {
			for(i=0;i<9;i++) {
				for(int j=0;j<9;j++) {
					final int mi=i;
					final int mj=j;
			    } 
					
			}
		}
	}
}
// heuristic 1 of the domain reduction of the cell. 1. One heuristic function would be to reduce the domain of each cell.
public ArrayList<Integer> domain_redution(int rr,int cc,int board[][]) {
	ArrayList<Integer> array=new ArrayList();
	for(int i=0;i<9;i++) {
		array.add(i+1);
	}
	for(int i=1;i<=9;i++) {
	for(int col=0;col<9;col++) {
		if(board[rr][col]==i)
			array.remove(i-1);
	}
}
	// checking for a particular column
	for(int i=1;i<=9;i++) {
	for(int row=0;row<9;row++) {
		if(board[row][cc]==i)
			array.remove(i-1);
	}
	}
	//checking for 3 by 3 grid
	int start_r=rr-(rr%3);
	int start_c=cc-(cc%3);
	for(int i=1;i<=9;i++) {
	for(int row=start_r;row<(start_r+3);row++) {
		for(int col=start_c;col<(start_c+3);col++) {
			if(board[row][col]==i)
				array.remove(i-1);
		}
	}
	}
	return array;
}
//heuristic 2 which is called to find the best cell. This heuristic selects the cell which has smaller domain and acts as a tie breaker if multiple cells are found.
public String best_Cell(int board[][],ArrayList<Integer>[][] list) {
	int min=0,indexi=-1,indexj=-1;
	for(int i=0;i<9;i++) {
		for(int j=0;j<9;j++) {
			int ff=list[i][j].size();
			if(ff<min) {
				min=list[i][j].size();
				indexi=i;
				indexj=j;
			}
		}
	}
	return indexi+""+indexj;
}
// heuristic 3 is to find the most suitable value that can be assigned to highest constraining cell using constraint satisfaction problem
public int best_value(int rr,int cc,ArrayList<Integer> list,int board[][]) {
	int flag1=0,flag2=0,flag3=0, ans=0;
	for(int i=0;i<list.size();i++) {
		for(int col=0;col<9;col++) {
			if(board[rr][col]==list.get(i))
				flag1=1;
		}
		// checking for a particular column
		for(int row=0;row<9;row++) {
			if(board[row][cc]==list.get(i))
				flag2=1;
		}
		//checking for 3 by 3 grid
		int start_r=rr-(rr%3);
		int start_c=cc-(cc%3);
		for(int row=start_r;row<(start_r+3);row++) {
			for(int col=start_c;col<(start_c+3);col++) {
				if(board[row][col]==list.get(i))
					flag3=1;
			}
		}
		if(flag1==0&&flag2==0&&flag3==0) // to check if no constraints are conflicting
			ans=list.get(i);
	}
	return ans;
}
}