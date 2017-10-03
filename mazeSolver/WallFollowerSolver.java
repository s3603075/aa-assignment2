package mazeSolver;

import java.util.*;

import maze.Cell;
import maze.Maze;
import maze.Wall;

/**
 * Implements WallFollowerSolver
 */

public class WallFollowerSolver implements MazeSolver {
	
	private int steps = 0; 
	private Cell currentCell;
	private Integer currentDirection;
	private ArrayList<Cell> path = new ArrayList<Cell>();
	private Maze map = null;
	
	@Override
	public void solveMaze(Maze maze) {

		map = maze;
		
		currentCell = map.entrance;
		path.add(currentCell);
		Cell nextCell;
		
		 // Find current direction
		for(int i=0; i<currentCell.neigh.length; i++){
			if(currentCell.neigh[i] != null){
				if(currentCell.wall[i].present == false){
					currentDirection = i;
					break;
				 }
			 }
		 }

		 while(currentCell != map.exit){
			 
			 if(endOfPath(currentCell) == true){
				 break;
			 }
			 
			 nextCell = findNext(currentDirection, currentCell.neigh.length, currentCell);
			 if(nextCell == null){
				 nextCell = findNext(0, currentDirection, currentCell);
			 }
			 
			 if(nextCell == null){
				 break;
			 }

			 currentCell.visited = true;
			 path.add(currentCell);
			 maze.drawFtPrt(currentCell);
			 currentCell = nextCell;
			 steps++;
			 
		 }

	} // end of solveMaze()

	
	private Boolean endOfPath(Cell current){
		
		if(currentCell == map.entrance){
			for(int i=0; i<currentCell.neigh.length; i++){
				 if(currentCell.neigh[i] != null){
					 if(currentCell.neigh[i].visited == false){
						 return true;
					 }
				 }
			 }
		}
		return false;
	}
	

	private Cell findNext(int startDirection, int endDirection, Cell current){
		
		for(int i=startDirection; i<endDirection; i++){
			if(current.neigh[i] != null){
				if(current.wall[i].present == false){
					currentDirection = i;
					return current.neigh[i];
				}
			}
		}
		
		if(current.tunnelTo != null){
			return current.tunnelTo;
		}
		
		return null;
	}
	
    
	@Override
	public boolean isSolved() {
		// If the path is empty, then we didn't solve the maze.
		if(path.isEmpty() == true){
			return false;
		}

		// If the 1st part of the path is the entrance...
		if(path.get(0) == map.entrance){
					
			Cell current;
			Cell next;
			Wall wall;
			
			// Loop through each element in the path,
			for(int i=0; i<path.size() - 1; i++){
				
				current = path.get(i);
				next = path.get(i+1);
						
				// Check if we can reach the next item in the path
				if(Arrays.asList(current.neigh).contains(next) || current.tunnelTo == next){
					
					if(current.tunnelTo != next){
						wall = current.wall[Arrays.asList(current.neigh).indexOf(next)];
						if(wall.present == true){
							return false;
						}
					}
					
				}else{
					return false;
				}
			}
			
			// Check if the last element in the path is the exit
			if(path.get(path.size() - 1) == map.exit){
				// If it passed all of these checks, then the maze was solved.
				return true;
			}
					
		}		
				
		return false;
	} // end if isSolved()
    
    
	@Override
	public int cellsExplored() {
		return steps;
	} // end of cellsExplored()

} // end of class WallFollowerSolver
