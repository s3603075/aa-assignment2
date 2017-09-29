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
	
	@Override
	public void solveMaze(Maze maze) {

		 currentCell = maze.entrance;
		 Cell nextCell = maze.entrance;

		 for(int i=0; i<currentCell.neigh.length; i++){
			 if(currentCell.wall[i] != null){
				 currentDirection = i;
				 break;
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
			 currentCell = nextCell;
			 steps++;
			 
		 }

	} // end of solveMaze()
    

	private Cell findNext(int startDirection, int endDirection, Cell current){
		
		for(int i=startDirection; i<endDirection; i++){
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
		// Is it even possible to determine if the maze was solved without knowing the maze exit?
		
		// Can't change parameters of this.
		
		return false;
	} // end if isSolved()
    
    
	@Override
	public int cellsExplored() {
		return steps;
	} // end of cellsExplored()

} // end of class WallFollowerSolver
