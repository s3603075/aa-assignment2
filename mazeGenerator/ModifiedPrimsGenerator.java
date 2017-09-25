package mazeGenerator;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import maze.Cell;
import maze.Maze;

public class ModifiedPrimsGenerator implements MazeGenerator {
	
	Integer dirSetN[] = {Maze.EAST,Maze.NORTH,Maze.WEST,Maze.SOUTH};
	ArrayList<Cell> setZ = new ArrayList<Cell>();
	ArrayList<Cell> setF = new ArrayList<Cell>();
	
	/*1. Pick a random starting cell and add it to set Z (initially Z is empty, after addition it contains
	just the starting cell). Put all neighbouring cells of starting cell into the frontier set F.
	
	2. Randomly select a cell c from the frontier set and remove it from F. Randomly select a cell b
	that is in Z and adjecent to the cell c. Carve a path between c and b.
	
	3. Add cell c to the set Z. Add the neighbours of cell c to the frontier set F.
	
	4. Repeat step 2 until Z includes every cell in the maze. At the end of the process, we would have
	generated a perfect maze.*/

	@Override
	public void generateMaze(Maze maze) {
		int randC = ThreadLocalRandom.current().nextInt(0, maze.sizeC);
		int randR = ThreadLocalRandom.current().nextInt(0, maze.sizeR);
		
		Cell startCell = maze.map[randR][randC];
		
		setZ.add(startCell);
		
		Cell neighCell;
		
		for(int i = 0; i < dirSetN.length; i++)	{
			neighCell = startCell.neigh[dirSetN[i]];
			if(neighCell != null) {
				setF.add(neighCell);
			}
		}
		
		int randIndex = ThreadLocalRandom.current().nextInt(0, setF.size());
		
		Cell c = setF.get(randIndex);
		
		setF.remove(randIndex);
		
		
		

	} // end of generateMaze()

} // end of class ModifiedPrimsGenerator
