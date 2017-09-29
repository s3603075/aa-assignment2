package mazeGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import maze.Cell;
import maze.Maze;

public class ModifiedPrimsGenerator implements MazeGenerator {

	ArrayList<Cell> setZ = new ArrayList<Cell>();
	ArrayList<Cell> setF = new ArrayList<Cell>();

	
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
		int randC = ThreadLocalRandom.current().nextInt(0, maze.sizeC);
		int randR = ThreadLocalRandom.current().nextInt(0, maze.sizeR);
		
		Cell startCell = maze.map[randR][randC];
		
		// Add starting cell to set Z.
		setZ.add(startCell);
		
		// Add neighbours of starting cell to F.
		for(int i=0; i<startCell.neigh.length; i++){
			if(startCell.neigh[i] != null){
				setF.add(startCell.neigh[i]);
			}
		}

		// Keep adding paths / breaking walls until Z contains all cells
		Boolean complete = checkIfComplete(maze);		
		while(complete == false){
			addPath();
			complete = checkIfComplete(maze);
		}

	} // end of generateMaze()
	
	
	// If Z is the same size as the number of Cells in the Map,
	// they should be the same
	private Boolean checkIfComplete(Maze maze){

		if(maze.type == Maze.NORMAL){
			if(setZ.size() != (maze.sizeC * maze.sizeR)){
				return false;
			}
		}else if(maze.type == Maze.HEX){
			if(setZ.size() != (maze.sizeC * (maze.sizeR + 1)/2)){
				return false;
			}
		}
		
		return true;
	}
	
	private void addPath(){
		// Pick random cell c from F, remove from F.
		int randIndex = ThreadLocalRandom.current().nextInt(0, setF.size());
		Cell c = setF.get(randIndex);
		setF.remove(randIndex);

		// Pick random cell b from Z (must be a neighbour of c).

		// Finding the neighbours that are also in Z
		ArrayList<Cell> validNeighbours = new ArrayList<Cell>();
		for(int i=0; i<c.neigh.length; i++){
			if(c.neigh[i] != null){
				if(setZ.contains(c.neigh[i]) == true){
					validNeighbours.add(c.neigh[i]);
				}
			}
		}

		// Picking random valid cell.
		randIndex = ThreadLocalRandom.current().nextInt(0, validNeighbours.size());
		Cell b = validNeighbours.get(randIndex);

		// Finding the wall between b and c place in the maze.
		Integer cWall = Arrays.asList(c.neigh).indexOf(b);
		
		// Remove wall
		c.wall[cWall].present = false;
		
		// Add cell c to the set Z.
		setZ.add(c);
		
		// Add the neighbours of cell c to F
		for(int i=0; i<c.neigh.length; i++){
			if(setF.contains(c.neigh[i]) == false){
				setF.add(c.neigh[i]);
			}
		}

	}


} // end of class ModifiedPrimsGenerator
