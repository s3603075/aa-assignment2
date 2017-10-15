package mazeGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import maze.Cell;
import maze.Maze;

public class ModifiedPrimsGenerator implements MazeGenerator {

	ArrayList<Cell> setZ = new ArrayList<Cell>();
	ArrayList<Cell> setF = new ArrayList<Cell>();
	
	//Array for set of directions (normal and tunnel)
	Integer dirSetN[] = {Maze.EAST,Maze.NORTH,Maze.WEST,Maze.SOUTH};
		
	//Array for set of directions (hex)
	Integer dirSetX[] = {Maze.EAST, Maze.NORTHEAST,Maze.NORTHWEST,Maze.WEST,Maze.SOUTHWEST, Maze.SOUTHEAST};

	
	/*1. Pick a random starting cell and add it to set Z (initially Z is empty, after addition it contains
	just the starting cell). Put all neighbouring cells of starting cell into the frontier set F.
	
	2. Randomly select a cell c from the frontier set and remove it from F. Randomly select a cell b
	that is in Z and adjacent to the cell c. Carve a path between c and b.
	
	3. Add cell c to the set Z. Add the neighbours of cell c to the frontier set F.
	
	4. Repeat step 2 until Z includes every cell in the maze. At the end of the process, we would have
	generated a perfect maze.*/

	@Override
	public void generateMaze(Maze maze) {
		
		// Picking a random starting cell.
		// There is an issue with Hex mazes where you can start at a null cell.
		// This loop prevents that by waiting until a random cell is valid.
		// Only problem is that it might make the running time infinitely long if we are unlucky.
		// Still worth the risk.
		Cell startCell = null;
		while(startCell == null){
			// Picking a random starting cell.
			int randC = ThreadLocalRandom.current().nextInt(0, maze.sizeC);
			int randR = ThreadLocalRandom.current().nextInt(0, maze.sizeR);
			
			startCell = maze.map[randR][randC];
		}
		
		// Add starting cell to set Z.
		setZ.add(startCell);
		
		// Finding initial neighbours
		if(maze.type == Maze.NORMAL){
			for(int i=0; i<dirSetN.length; i++){
				if(startCell.neigh[dirSetN[i]] != null){
					setF.add(startCell.neigh[dirSetN[i]]);
				}
			}

		}else{
			for(int i=0; i<dirSetX.length; i++){
				if(startCell.neigh[dirSetX[i]] != null){
					setF.add(startCell.neigh[dirSetX[i]]);
				}
			}
		}
		
		
		// Keep adding paths / breaking walls until Z contains all cells
		Boolean complete = checkDone(maze);		
		while(complete == false){
			if(maze.type == Maze.NORMAL){
				addPath(dirSetN);
			}else{
				addPath(dirSetX);
			}

			complete = checkDone(maze);
		}

	} // end of generateMaze()
	
	
	// If Z is the same size as the number of Cells in the Map,
	// they should be the same
	private Boolean checkDone(Maze maze){

		if(maze.type == Maze.NORMAL){
			return (setZ.size() == maze.sizeC * maze.sizeR);
			
		}else{
			return (setZ.size() == maze.sizeC * (maze.sizeR + 1/2));
		}

	}
	
	private void addPath(Integer[] dirSet){

		// Pick random cell c from F
		int randIndex = ThreadLocalRandom.current().nextInt(0, setF.size());
		Cell c = setF.get(randIndex);

		// Remove it from F.
		setF.remove(c);

		// Pick random cell b from Z (must be a neighbour of c).
		// Finding the neighbours that are also in Z
		ArrayList<Cell> validNeighbours = new ArrayList<Cell>();

		
		// Checking c's neighbours, then if setZ contains them.
		// Typically faster than setZ->c neighbour.
		for(int i=0; i<dirSet.length; i++){
			if(c.neigh[dirSet[i]] != null){
				if(setZ.contains(c.neigh[dirSet[i]]) == true){
					validNeighbours.add(c.neigh[dirSet[i]]);
				}	
			}
		}

		// Picking random valid neighbour.
		randIndex = ThreadLocalRandom.current().nextInt(0, validNeighbours.size());
		Cell b = validNeighbours.get(randIndex);
		
		// Finding the wall between b and c place in the maze.
		Integer cWall = Arrays.asList(c.neigh).indexOf(b);
		
		// Remove wall
		c.wall[cWall].present = false;

		// Add cell c to the set Z.
		setZ.add(c);
		
		// Add the neighbours of cell c to F

		// This little fucker caused most of the problems
		for(int i=0; i<dirSet.length; i++){
			if(c.neigh[dirSet[i]] != null){
				if(setF.contains(c.neigh[dirSet[i]]) == false){
					if(setZ.contains(c.neigh[dirSet[i]]) == false){
						setF.add(c.neigh[dirSet[i]]);		
					}
				}	
			}
		}
	}

	
} // end of class ModifiedPrimsGenerator
