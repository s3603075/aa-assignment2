package mazeGenerator;

import java.util.Collections;
import java.util.Deque;
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
		getUnvisited(startCell);
		
	} // end of generateMaze()
	
	public Cell getUnvisited(Cell c)	{
		
		Cell neighCell = null;
		
		Collections.shuffle(Arrays.asList(dirSetN));
		
		for(int i = 0; i < dirSetN.length; i++)	{
			//Find if neighbours are visited
			neighCell = c.neigh[dirSetN[i]]; 
			if(neighCell != null) {
				if(neighCell.visited == false)	{
					//Set prev cell to visited, add to stack
					c.visited = true;
					dfsStack.push(c);
					//Disable wall of start and current
					c.wall[dirSetN[i]].present = false;
					neighCell.wall[Maze.oppoDir[dirSetN[i]]].present = false;
					//Return current cell to function
					return getUnvisited(neighCell);
				}
			}
		}
		
		if(dfsStack.peek() != null) {
			return backtrack(dfsStack.pop());
		}
		
		return null;
		
	}
	
	public Cell backtrack(Cell c) {
		
		Cell neighCell = null;

		for(int i = 0; i < dirSetN.length; i++)	{
			//Find if neighbours are visited
			neighCell = c.neigh[dirSetN[i]];
			if(neighCell != null) {
				if(neighCell.visited == false)	{
					return getUnvisited(neighCell);
				}
			}
		}
		
		if(dfsStack.peek() != null) {
			return backtrack(dfsStack.pop());
		}
		
		return null;
		
		
	}

} // end of class RecursiveBacktrackerGenerator
