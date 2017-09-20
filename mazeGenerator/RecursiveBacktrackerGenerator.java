package mazeGenerator;

import java.util.Collections;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import maze.Cell;
import maze.Maze;

public class RecursiveBacktrackerGenerator implements MazeGenerator {
	
	//Array for set of directions (normal and tunnel)
	Integer dirSetN[] = {Maze.EAST,Maze.NORTH,Maze.WEST,Maze.SOUTH};
	
	//Array for set of directions (hex)
	Integer dirSetX[] = {Maze.EAST, Maze.NORTHEAST,Maze.NORTHWEST,Maze.WEST,Maze.SOUTHWEST, Maze.SOUTHEAST};

	@Override
	public void generateMaze(Maze maze) {
		//TODO adjust for different maze type
		
		//Pick a random starting cell
		int randC = ThreadLocalRandom.current().nextInt(0, maze.sizeC);
		int randR = ThreadLocalRandom.current().nextInt(0, maze.sizeR);
		
		//Starting cell
		Cell startCell = maze.map[randC][randR];
		
		/**IN PROGRESS - DFS for normal**/
		//- Add to stack
		//- Backtrack
		
		getUnvisited(startCell);
		
		/**END**/
		
	} // end of generateMaze()
	
	public Cell getUnvisited(Cell c)	{
		Cell curCell;
		
		Collections.shuffle(Arrays.asList(dirSetN));
		
		for(int i = 0; i < dirSetN.length; i++)	{
			//Find if neighbours are visited
			if(c.neigh[dirSetN[i]].visited == false)	{
				//Assign to current cell
				curCell = c.neigh[dirSetN[i]];
				//Set prev cell to visited
				c.visited = true;
				//Disable wall of start and current
				c.wall[dirSetN[i]].present = false;
				curCell.wall[Maze.oppoDir[dirSetN[i]]].present = false;
				//Return current cell to function
				return getUnvisited(curCell);
			} else if (i == dirSetN.length - 1) {
				System.out.println("Backtrack");
				return null;
			}
		}
		
		return null;
	}

} // end of class RecursiveBacktrackerGenerator
