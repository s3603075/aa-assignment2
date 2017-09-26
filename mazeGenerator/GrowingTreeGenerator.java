package mazeGenerator;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import maze.Maze;
import maze.Cell;

public class GrowingTreeGenerator implements MazeGenerator {
	// Growing tree maze generator. As it is very general, here we implement as "usually pick the most recent cell, but occasionally pick a random cell"
	
	/*1. Pick a random starting cell and add it to set Z (initially Z is empty, after addition it contains
		just the starting cell).
	  2. Using a particular strategy (see below) select a cell b from Z. If cell b has unvisited neighbouring
		cells, randomly select a neighbour, carve a path to it, and add the selected neighbour to set Z.
		If b has no unvisited neighbours, remove it from Z.
	  3. Repeat step 2 until Z is empty.
	  
	  Depending on what strategy is used to select a cell from V , we obtain different behaviour. If we
	  select the newest cell added to V , then this is the same as recursive backtracker. If we randomly select
	  a cell in V , then this is similar to Prim’s generation approach. Other strategies can be a mixture of
	  both (have a try!).
	*/
	
	double threshold = 0.1;
	
	//Array for set of directions (normal and tunnel)
	Integer dirSetN[] = {Maze.EAST,Maze.NORTH,Maze.WEST,Maze.SOUTH};
		
	//Array for set of directions (hex)
	Integer dirSetX[] = {Maze.EAST, Maze.NORTHEAST,Maze.NORTHWEST,Maze.WEST,Maze.SOUTHWEST, Maze.SOUTHEAST};
	
	//ArrayList for selecting cell
	ArrayList<Cell> setZ = new ArrayList<Cell>();
	
	@Override
	public void generateMaze(Maze maze) {
		
		Cell startCell;
		
		int randC = ThreadLocalRandom.current().nextInt(0, maze.sizeC);
		int randR = ThreadLocalRandom.current().nextInt(0, maze.sizeR);
		
		switch(maze.type)	{
			case Maze.NORMAL:
				//Starting cell for normal
				startCell = maze.map[randR][randC];
				setZ.add(startCell);
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
