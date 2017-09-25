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
		//TODO adjust for different maze type
		
		//Pick a random starting cell
		int randC = ThreadLocalRandom.current().nextInt(0, maze.sizeC);
		int randR = ThreadLocalRandom.current().nextInt(0, maze.sizeR);
		
		//Starting cell
		Cell startCell = maze.map[randC][randR];
		
		/**IN PROGRESS - DFS for normal**/
		switch(maze.type)	{
			case Maze.NORMAL:
				getUnvisited(startCell, dirSetN);
			case Maze.HEX:
				getUnvisited(startCell, dirSetX);
		}
		
	} // end of generateMaze()
	
	public Cell getUnvisited(Cell c, Integer[] dirSet)	{
		
		Cell neighCell;
		
		//Set cell to visited, add to stack
		c.visited = true;
		dfsStack.push(c);
		
		Collections.shuffle(Arrays.asList(dirSet));
		
		for(int i = 0; i < dirSet.length; i++)	{
			//Find if neighbours are visited
			neighCell = c.neigh[dirSet[i]]; 
			if(neighCell != null) {
				if(neighCell.visited == false)	{
					//Disable wall
					c.wall[dirSet[i]].present = false;
					//Return current cell to function
					return getUnvisited(neighCell, dirSet);
				}
			}
		}
		
		if(dfsStack.peek() != null) {
			return backtrack(dfsStack.pop(), dirSet);
		}
		
		return null;
		
	}
	
	public Cell backtrack(Cell c, Integer[] dirSet) {
		
		Cell neighCell;

		for(int i = 0; i < dirSet.length; i++)	{
			//Find if neighbours are visited
			neighCell = c.neigh[dirSet[i]];
			if(neighCell != null) {
				if(neighCell.visited == false)	{
					return getUnvisited(neighCell, dirSet);
				}
			}
		}
		
		if(dfsStack.peek() != null) {
			return backtrack(dfsStack.pop(), dirSet);
		}
		
		return null;
		
	}

} // end of class RecursiveBacktrackerGenerator
