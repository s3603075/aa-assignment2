package mazeGenerator;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import maze.Cell;
import maze.Maze;

public class RecursiveBacktrackerGenerator implements MazeGenerator {
	
	//Array for set of directions (normal and tunnel)
	Integer dirSetN[] = {Maze.EAST,Maze.NORTH,Maze.WEST,Maze.SOUTH};
	
	//Array for set of directions (hex)
	Integer dirSetX[] = {Maze.EAST, Maze.NORTHEAST,Maze.NORTHWEST,Maze.WEST,Maze.SOUTHWEST, Maze.SOUTHEAST};
	
	//Stack for backtracking
	Deque<Cell> dfsStack = new ArrayDeque<Cell>();
	
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
	
	public Cell getUnvisited(Cell c, Integer[] dirSet)	{
		
		Cell neighCell;
		
		//Check if cell has a tunnel. If so, add to stack and teleport to other cell.
		if(c.tunnelTo != null && c.tunnelTo.visited == false)	{
			c.visited = true;
			c.tunnelTo.visited = true;
			dfsStack.push(c);
			return getUnvisited(c.tunnelTo, dirSet);
		}
		
		//Randomise set of directions
		Collections.shuffle(Arrays.asList(dirSet));
		
		for(int i = 0; i < dirSet.length; i++)	{
			//Find if neighbours are visited
			neighCell = c.neigh[dirSet[i]];
			if(neighCell != null) {
				if(neighCell.visited == false)	{
					//Mark cells as visited
					c.visited = true;
					neighCell.visited = true;
					//Push to stack
					dfsStack.push(c);
					//Disable wall
					c.wall[dirSet[i]].present = false;
					//Return neighbour cell to function
					return getUnvisited(neighCell, dirSet);
				}
			}
		}
		
		//Backtrack if no unvisited neighbours
		if(!dfsStack.isEmpty()) {
			return backtrack(dfsStack.pop(), dirSet);
		}
		
		return null;
		
	}
	
	public Cell backtrack(Cell c, Integer[] dirSet) {
		
		Cell neighCell;
		
		//If there is a tunnel, just backtrack to the previous item
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
