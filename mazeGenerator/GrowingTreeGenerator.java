package mazeGenerator;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import maze.Maze;
import maze.Cell;

/**
 * Uses Growing Tree to generate a maze from a map.
 * 
 * @author Danny Ho			s3603075
 * @author Caleb Turner		s3604744
 */

public class GrowingTreeGenerator implements MazeGenerator {
	
	//Random pick ratio threshold
	double threshold = 0.1;
	
	//Array for set of directions (normal and tunnel)
	Integer dirSetN[] = {Maze.EAST,Maze.NORTH,Maze.WEST,Maze.SOUTH};
		
	//Array for set of directions (hex)
	Integer dirSetX[] = {Maze.EAST, Maze.NORTHEAST,Maze.NORTHWEST,Maze.WEST,Maze.SOUTHWEST, Maze.SOUTHEAST};
	
	//ArrayList for selecting cell
	ArrayList<Cell> setZ = new ArrayList<Cell>();
	
	/**
	 * Growing tree algorithm transformation to perfect maze
	 * 
	 * *********************************************************************
	 *  
	 * ALGORITHM Growing Tree (maze)
	 * Use Prim's algorithm to generate a perfect maze.
	 * INPUT: Maze maze.
	 * OUTPUT: None.
	 *  
	 *  // Find a random cell as the starting point.
	 *  1: Generate random inputs for random starting cell
	 *  2: switch for maze type
	 *  3:		case for normal/hex maze
	 *  4:			add random starting cell to setZ
	 *  5:			while setZ is not empty
	 *  6:				perform strategy for set N/X for directions
	 *  7:			end while
	 *  
	 *  ********************************************************************
	 *  
	 *  @param maze Input maze.
	 *  
	 */
	
	@Override
	public void generateMaze(Maze maze) {
		
		Cell startCell;
		
		int randC = ThreadLocalRandom.current().nextInt(0, maze.sizeC);
		int randR = ThreadLocalRandom.current().nextInt(0, maze.sizeR);
		
		switch(maze.type)	{
			case Maze.NORMAL:
				//Starting cell for normal
				startCell = maze.map[randR][randC];
				//Add initial value to set
				setZ.add(startCell);
				//Iterate through strategy until array is empty
				while(!setZ.isEmpty())	{
					strategy(dirSetN);
				}
				break;
			case Maze.HEX:
				//Starting cell for hex
				startCell= maze.map[randR][randC + (randR + 1) / 2];
				setZ.add(startCell);
				while(!setZ.isEmpty())	{
					strategy(dirSetX);
				}
				break;
			}
		
	}
	
	/**
	 * Select a strategy to perform - either pick from first or random
	 * 
	 * *********************************************************************
	 *  
	 * ALGORITHM Growing Tree (maze)
	 * Use Prim's algorithm to generate a perfect maze.
	 * INPUT: Integer[] dirSet
	 * OUTPUT: None.
	 *  
	 *  1: Generate random number
	 *  2: if number is <= to threshold
	 *  3:		get a random element from setZ
	 *  4: else
	 *  5: shuffle directions in array
	 *  6: for elements in direction set
	 *  7:		pick a valid neighbouring cell
	 *  8:		if valid
	 *  9:			set Cell c, neigh to visited
	 *  10:			disable wall in direction
	 *  11:			add to setZ
	 *  12:			return
	 *  13:		endif
	 *  14: endfor
	 *  15: if no valid neighbours
	 *  16: 	remove from setZ
	 *  17: endif
	 *  
	 *  ********************************************************************
	 *  
	 *  @param dirSet[] dirSet for directional set.
	 *  
	 */
	
	public void strategy(Integer[] dirSet)	{
		Cell c;
		Cell neighCell;
		
		double chance = ThreadLocalRandom.current().nextDouble();
		
		if(chance <= threshold)	{
			int randIndex = ThreadLocalRandom.current().nextInt(0, setZ.size());
			c = setZ.get(randIndex);
			
		} else	{
			c = setZ.get(setZ.size() - 1);
		}
		
		Collections.shuffle(Arrays.asList(dirSet));
		
		for(int i = 0; i < dirSet.length; i++)	{
			neighCell = c.neigh[dirSet[i]];
			if(neighCell != null) {
				if(neighCell.visited == false)	{
					c.visited = true;
					neighCell.visited = true;
					c.wall[dirSet[i]].present = false;
					setZ.add(neighCell);
					return;
				}
			}
		}
		
		setZ.remove(c);
		return;
	}

}
