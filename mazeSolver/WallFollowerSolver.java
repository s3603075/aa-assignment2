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
		
		 // Find current direction
		for(int i=0; i<currentCell.neigh.length; i++){
			if(currentCell.neigh[i] != null){
				if(currentCell.wall[i].present == false){
					currentDirection = i;
					break;
				 }
			 }
		 }
		
		if(maze.type != Maze.HEX)	{
			getTurnNormal(currentDirection, currentCell);
		}
		else	{
			getTurnHex(currentDirection, currentCell);
		}
		
		

	} // end of solveMaze()
	
	private int getTurnHex(int cellDir, Cell currentCell)	{
		
		map.drawFtPrt(currentCell);
		path.add(currentCell);
		steps++;
		
		if(currentCell == map.exit)	{
			return 1;
		}
		
		switch(cellDir)	{
			case Maze.WEST:
				//Check left turn, then straight, then right turns, then back
				Integer[] westDir = {Maze.SOUTHEAST, Maze.SOUTHWEST, Maze.WEST, Maze.NORTHWEST, Maze.NORTHEAST, Maze.EAST};
				//Iterate for all possibilities
				iterateDirectionsHex(cellDir, currentCell, westDir);
				break;
			case Maze.EAST:
				Integer[] eastDir = {Maze.NORTHWEST, Maze.NORTHEAST, Maze.EAST, Maze.SOUTHEAST, Maze.SOUTHWEST, Maze.WEST};
				iterateDirectionsHex(cellDir, currentCell, eastDir);
				break;
			case Maze.SOUTHEAST:
				Integer[] seDir = {Maze.NORTHEAST, Maze.EAST, Maze.SOUTHEAST, Maze.SOUTHWEST, Maze.WEST, Maze.NORTHWEST};
				iterateDirectionsHex(cellDir, currentCell, seDir);
				break;
			case Maze.SOUTHWEST:
				Integer[] swDir = {Maze.EAST, Maze.SOUTHEAST, Maze.SOUTHWEST, Maze.WEST, Maze.NORTHWEST, Maze.NORTHEAST};
				iterateDirectionsHex(cellDir, currentCell, swDir);
				break;
			case Maze.NORTHEAST:
				Integer[] neDir = {Maze.WEST, Maze.NORTHWEST, Maze.NORTHEAST, Maze.EAST, Maze.SOUTHEAST, Maze.SOUTHWEST};
				iterateDirectionsHex(cellDir, currentCell, neDir);
				break;
			case Maze.NORTHWEST:
				Integer[] nwDir = {Maze.SOUTHWEST, Maze.WEST, Maze.NORTHWEST, Maze.NORTHEAST, Maze.EAST, Maze.SOUTHEAST};
				iterateDirectionsHex(cellDir, currentCell, nwDir);
				break;
			
		}
		
		return 0;
	}
	
	private int iterateDirectionsHex(int cellDir, Cell currentCell, Integer[] dirSet)	{
		
		for(int i=0; i < dirSet.length; i++)	{
			if(currentCell.neigh[dirSet[i]] != null)	{
				if(currentCell.wall[dirSet[i]].present == false)	{
					return getTurnHex(dirSet[i], currentCell.neigh[dirSet[i]]);
				}
			}
		}
		
		return -1;
	}
	
	private void getTurnNormal(int currentDirection, Cell currentCell)	{
		
		path.add(currentCell);
		
		int leftCell;
		int rightCell;
		
		while(currentCell != map.exit)	{
			map.drawFtPrt(currentCell);
			
			if(currentCell.tunnelTo != null)	{
				currentCell = currentCell.tunnelTo;
				map.drawFtPrt(currentCell);
				path.add(currentCell);
			}
			
			leftCell = getLeftTurn(currentDirection);
			//Turn left 90 degrees
			if(currentCell.wall[leftCell].present == false)	{
				currentCell = currentCell.neigh[leftCell];
				path.add(currentCell);
				steps++;
				currentDirection = leftCell;
			}
			//Go forward if there is left wall
			else if(currentCell.wall[currentDirection].present == false) {
				currentCell = currentCell.neigh[currentDirection];
				path.add(currentCell);
				steps++;	
			}
			//Turn right 90 degrees if walls in front and left
			else {
				rightCell = getRightTurn(currentDirection);
				currentDirection = rightCell;
			}
			
		}
		
		map.drawFtPrt(currentCell);
	}
	
	private int getLeftTurn(int cellDir) {
		
		switch(cellDir)	{
			case Maze.NORTH:
				cellDir = Maze.WEST;
				break;
			case Maze.EAST:
				cellDir = Maze.NORTH;
				break;
			case Maze.SOUTH:
				cellDir = Maze.EAST;
				break;
			case Maze.WEST:
				cellDir = Maze.SOUTH;
				break;
		}
		
		return cellDir;
	}
	
	private int getRightTurn(int cellDir) {
		
		switch(cellDir)	{
			case Maze.NORTH:
				cellDir = Maze.EAST;
				break;
			case Maze.EAST:
				cellDir = Maze.SOUTH;
				break;
			case Maze.SOUTH:
				cellDir = Maze.WEST;
				break;
			case Maze.WEST:
				cellDir = Maze.NORTH;
				break;
		}
		
		return cellDir;
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
