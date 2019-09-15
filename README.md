# Sudoku
<b>ALGORITHM</b>
Given a particular state of the board. It verifies if the sudoku can be solved with a given state or not.
My sudoku GUI gives two flexibility to the user. 
1.	Enter the sudoku puzzle manually and then click on the solve button.
2.	Upload the csv file as an input to the sudoku grid by clicking on upload option. 
Clear Option to clear the sudoku grid to enter the new numbers. 
It first of all selects a blank or basically unassigned cell in a given state of the sudoku. The first cell found will be returned. 
Then I have used various heuristics:
1.	Heuristic of the domain reduction of the cell. This heuristic function would be to reduce the domain of each cell so that it can take only particular values by analyzing through constraint satisfaction. It will check through all the rows, columns and 3*3 grid to make a list of all the numbers that can be assigned to a particular cell. This is called domain reduction. It will make a list of all the cells which will contain all the numbers that can come in that cell. The list will be separate for each cell. The function used in this domain_reduction.
2.	Heuristic of the selecting best cell. This particular selects a best cell based on the smaller domain among all the domains of the cell and acts as a tiebreaker if multiple cells are found. The function used in this best_Cell.
3.	Heuristic of selecting the best value that can be assigned to the best cell, highest constraining cell using constraint satisfaction problem. The most appropriate value is being assigned to the cell that has a smaller domain using constraint satisfaction. Apply the constraint inference rules to the selected value and the cell to generate all possible constraints as defined above. The function used is best_value.
If the set of constraints contains a contraction, then report that this path is a dead end. Else assign the value to the cell.
Then we do forward checking to find out how this assignment affects other blank or unassigned cells by recursively calling all the heuristics. 
If the assignment affects the constraints of any cell, then the domain and rules of that cell are returned and, in that case, backtracking needs to be done in order to update the value of the cell whose assignment affects other cells.
This process is repeated again and again until there is no way to go forward or solution has been found.
