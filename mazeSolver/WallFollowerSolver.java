package mazeSolver;

import maze.Cell;
import maze.Maze;

/**
 * Implements WallFollowerSolver
 */

public class WallFollowerSolver implements MazeSolver {
	
	private int steps = 0; 
	private Cell currentCell;
	private Integer currentDirection;
	private Boolean solved = true;
	
	@Override
	public void solveMaze(Maze maze) {

		 currentCell = maze.entrance;
		 Cell nextCell = null;

		 // Find current direction
		 for(int i=0; i<currentCell.neigh.length; i++){
			 if(currentCell.neigh[i] != null){
				 if(currentCell.wall[i].present == false){
					 currentDirection = i;
					 break;
				 }
			 }
		 }

		 
		 while(currentCell != maze.exit){
			 if(currentCell.tunnelTo == null){
				 nextCell = findNext(currentDirection, currentCell.neigh.length, currentCell);
				 if(nextCell == null){
					 nextCell = findNext(0, currentDirection, currentCell);
				 }
				 
			 }else{
				 nextCell = currentCell.tunnelTo;
			 }

			 currentCell.visited = true;
			 maze.drawFtPrt(currentCell);
			 currentCell = nextCell;
			 steps++;
			 
			 // Checking if we are back at the start with no new pathways to take.
			 if(currentCell == maze.entrance){
				 Boolean endPath = true;
				 // If there are any new paths, continue the loop
				 for(int i=0; i<currentCell.neigh.length; i++){
					 if(currentCell.neigh[i] != null){
						 if(currentCell.neigh[i].visited == false){
							 endPath = false;
						 }
					 }
				 }
				 
				 // Otherwise we didn't find the solution.
				 if(endPath == true){
					 solved = false;
					 break;
				 }
			 }
		 }

	} // end of solveMaze()
    
	

	private Cell findNext(int startDirection, int endDirection, Cell current){
		
		for(int i=startDirection; i<=endDirection; i++){
			if(current.neigh[i] != null){
				if(current.wall[i].present == false){
					currentDirection = i;
					return current.neigh[i];
				}
			}
		}
		
		return null;
	}
	
    
	@Override
	public boolean isSolved() {
		
		return solved;
	} // end if isSolved()
    
    
	@Override
	public int cellsExplored() {
		return steps;
	} // end of cellsExplored()

} // end of class WallFollowerSolver
