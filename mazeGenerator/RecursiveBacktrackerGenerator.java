package mazeGenerator;

import java.util.Collections;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import maze.Cell;
import maze.Maze;


/**
 * Uses Recursive Backtracking to generate a maze from a map.
 * 
 * @author Danny Ho			s3603075
 * @author Caleb Turner		s3604744
 */


public class RecursiveBacktrackerGenerator implements MazeGenerator {
	
	// Array for set of valid directions (normal and tunnel mazes).
	Integer dirSetN[] = {Maze.EAST,Maze.NORTH,Maze.WEST,Maze.SOUTH};
	
	// Array for set of valid directions (hex maze).
	Integer dirSetX[] = {Maze.EAST, Maze.NORTHEAST,Maze.NORTHWEST,Maze.WEST,Maze.SOUTHWEST, Maze.SOUTHEAST};
	
	// Stack for backtracking to previous cells.
	Deque<Cell> dfsStack = new ArrayDeque<Cell>();
	
	
	
	/**
	 * Transform input maze into a perfect maze using recursive backtracking.
	 * 
	 * **********************************************************************
	 * ALGORITHM Recursive Backtrack(Maze)
	 * Perform a recursive backtrack to create a path between cells in a maze.
	 * INPUT: Maze maze.
	 * OUTPUT: None.
	 * 
	 * 1. Pick a random starting cell
	 * 2. Pick the method for the following maze types
	 * 
	 * @param maze Input Maze. 
	 */
	
	
	@Override
	public void generateMaze(Maze maze) {
		
		//Pick a random starting cell
		int randC = ThreadLocalRandom.current().nextInt(0, maze.sizeC);
		int randR = ThreadLocalRandom.current().nextInt(0, maze.sizeR);
		
		Cell startCell = maze.map[randR][randC];
		
		switch(maze.type)	{
			case Maze.NORMAL:
				getUnvisited(startCell, dirSetN);
				break;
			case Maze.HEX:
				//Starting cell for hex
				startCell = maze.map[randR][randC + (randR + 1) / 2];
				getUnvisited(startCell, dirSetX);
				break;
			case Maze.TUNNEL:
				getUnvisited(startCell, dirSetN);
				break;
		}
		
	} // end of generateMaze()
	
	/**
	 * Find an unvisited neighbour of the currently selected cell
	 * 
	 * **********************************************************************
	 * ALGORITHM Recursive Backtrack(Maze)
	 * Perform a recursive backtrack to create a path between cells in a maze.
	 * INPUT: Cell c, Integer array dirSet.
	 * OUTPUT: Cell.
	 * 
	 * 1. if c.tunnelTo is not null
	 * 2. 	 set c and c.tunnelTo cells to visited
	 * 3. 	 return c.tunnelTo to function
	 * 4. end if
	 * 5. shuffle direction set
	 * 6. for each neighbour of c
	 * 7. 	 if neighbour is in dirSet
	 * 8:	    set c, neigh to visited
	 * 9:		dfsStack.push(c)
	 * 10:		disable wall in dirSet direction
	 * 11:	 end if 
	 * 12: end for
	 * 13: if dfsStack is empty
	 * 14: 	  return backtrack function
	 * 15: endif
	 * @param Cell c, Integer[] dirSet
	 * @return getUnvisited(Cell c, Integer[] dirSet) when there are unvisited
	 * neighbours.
	 */
	
	private Cell getUnvisited(Cell c, Integer[] dirSet)	{
		
		Cell neighCell;
		
		//Check if cell has a tunnel. If so, add to stack and teleport to other cell.
		if(c.tunnelTo != null && c.tunnelTo.visited == false)	{
			c.visited = true;
			c.tunnelTo.visited = true;
			dfsStack.push(c);
			return getUnvisited(c.tunnelTo, dirSet);
		}
		
		// Randomise set of directions.
		Collections.shuffle(Arrays.asList(dirSet));
		
		for(int i = 0; i < dirSet.length; i++)	{
			// Find if neighbours are visited.
			neighCell = c.neigh[dirSet[i]];
			if(neighCell != null) {
				if(neighCell.visited == false)	{
					// Mark cells as visited.
					c.visited = true;
					neighCell.visited = true;
					
					// Push to stack.
					dfsStack.push(c);
					// Disable wall.
					c.wall[dirSet[i]].present = false;
					// Return neighbour cell to function.
					return getUnvisited(neighCell, dirSet);
				}
			}
		}
		
		// Backtrack if no unvisited neighbours.
		if(!dfsStack.isEmpty()) {
			return backtrack(dfsStack.pop(), dirSet);
		}
		
		return null;
		
	}
	
	/**
	 * Backtrack when all neighbours of a cell have been visited
	 * 
	 * **********************************************************************
	 * ALGORITHM Recursive Backtrack(Maze)
	 * Perform a recursive backtrack to create a path between cells in a maze.
	 * INPUT: Cell c, Integer array dirSet.
	 * OUTPUT: Cell.
	 * 
	 * 1. if c.tunnelTo is not null and dfsStack is not empty
	 * 2. 	 return cell at top of stack to backtrack()
	 * 3. end if
	 * 4. for each neighbour of c
	 * 5. 	 if neighbour is in dirSet
	 * 6:	    return unvisited cell to getUnvisited()
	 * 7:	 end if 
	 * 8: end for
	 * 9: if dfsStack is not empty
	 * 10: 	 return cell at top of stack to backtrack()
	 * 11:end if
	 * 
	 * @param Cell c, Integer[] dirSet
	 * @return getBacktrack(Cell c, Integer[] dirSet) when there are no neighbours.
	 * @return getUnvisited(Cell c, Integer[] dirSet) when there are unvisited
	 * neighbours.
	 */
	
	private Cell backtrack(Cell c, Integer[] dirSet) {
		
		Cell neighCell;
		
		// If there is a tunnel, just backtrack to the previous item
		if(c.tunnelTo != null)	{
			if(!dfsStack.isEmpty()) {
				return backtrack(dfsStack.pop(), dirSet);
			}	
		}
		
		for(int i = 0; i < dirSet.length; i++)	{
			//Find if neighbours are visited
			neighCell = c.neigh[dirSet[i]];
			if(neighCell != null) {
				//Find if visited
				if(neighCell.visited == false)	{
					//Return neighbour cell to function
					return getUnvisited(c, dirSet);
				}
			}
		}
		
		//Backtrack if no unvisited neighbours
		if(!dfsStack.isEmpty()) {
			return backtrack(dfsStack.pop(), dirSet);
		}
		
		return null;
		
	}

} // end of class RecursiveBacktrackerGenerator
