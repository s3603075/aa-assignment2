package mazeGenerator;
import java.util*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import maze.Cell;
import maze.Maze;


/**
 * Maze Generator that uses a modified version of Prim's algorithm
 *  to generate a maze from a map.
 * 
 * @author Danny Ho			s3603075
 * @author Caleb Turner		s3604744
 *
 */

public class ModifiedPrimsGenerator implements MazeGenerator {

	// Array for the Z set.
	ArrayList<Cell> setZ = new ArrayList<Cell>();
	// Array for the Frontier set.
	ArrayList<Cell> setF = new ArrayList<Cell>();
	
	// Array for the valid set of directions (normal mazes).
	Integer dirSetN[] = {Maze.EAST,Maze.NORTH,Maze.WEST,Maze.SOUTH};
		
	// Array for the valid set of directions (hex mazes).
	Integer dirSetX[] = {Maze.EAST, Maze.NORTHEAST,Maze.NORTHWEST,Maze.WEST,Maze.SOUTHWEST, Maze.SOUTHEAST};
	
	/**
	 * Prim's Algorithm transformation of perfect Maze maze from Maze maze.
	 * 
	 * *********************************************************************
	 *  
	 * ALGORITHM Prim (maze)
	 * Uses Prim's algorithm to generate a perfect maze.
	 * INPUT: Maze maze.
	 * OUTPUT: None.
	 *  
	 *  // Find a random cell as the starting point.
	 *  1: startCell = randomCell(maze)
	 *  2: setZ.add(startCell)
	 *  3: for each neighbour of startCell
	 *  	setF.add(neighbour)
	 *  4: end for each
	 *  5: while maze isn't perfect
	 *  6:	carvePath()
	 *  7: end while
	 *  
	 *  ********************************************************************
	 *  
	 *  @param maze Input maze.
	 *  
	 */
	@Override
	public void generateMaze(Maze maze) {
		
		// Picking a random starting cell.
		Cell startCell = null;
		
		// Must check if starting cell is null, because in some Hex maze configurations
		//  there may be a null cell in the maze.
		while(startCell == null){
			int randC = ThreadLocalRandom.current().nextInt(0, maze.sizeC);
			int randR = ThreadLocalRandom.current().nextInt(0, maze.sizeR);
			
			startCell = maze.map[randR][randC];
		}
		
		// Add starting cell to set Z.
		setZ.add(startCell);
		
		// Add neighbours of starting cell to set F.
		// Finds the maze type to determine possible neighbours.
		if(maze.type == Maze.NORMAL){
			for(int i=0; i<dirSetN.length; i++){
				if(startCell.neigh[dirSetN[i]] != null){
					setF.add(startCell.neigh[dirSetN[i]]);
				}
			}

		// This maze generator only operates on Normal or Hex mazes, 
		//	so Hex is the only other option.
		}else{
			for(int i=0; i<dirSetX.length; i++){
				if(startCell.neigh[dirSetX[i]] != null){
					setF.add(startCell.neigh[dirSetX[i]]);
				}
			}
		}
		
		// Carves paths until set Z contains every node.
		Boolean complete = checkDone(maze);		
		while(complete == false){
			if(maze.type == Maze.NORMAL){
				carvePath(dirSetN);
			}else{
				carvePath(dirSetX);
			}
			
			complete = checkDone(maze);
		}

	} // end of generateMaze()

	
	/**
	 * Method that determines whether or not set Z contains every cell in maze.
	 * 
	 * ************************************************************************
	 * 
	 * ALGORITHM Check(maze)
	 * Determines if set Z contains every cell in the maze.
	 * INPUT: A maze that we compare against set Z.
	 * OUTPUT: Boolean value for if set Z contains every cell.
	 * 
	 * 1: if maze == NORMAL
	 * 2:	return if setZ.size == maze.numberOfCells
	 * 3: else
	 * 4:	return if setZ.size == maze.numberOfHexCells
	 * 5: end if
	 * 
	 * **************************************************************************
	 *
	 * @param maze
	 * @return Boolean if the set Z contains every cell.
	 */
	private Boolean checkDone(Maze maze){

		/*
		 * Logically, set Z only has new cells added to it if they are not already in set Z.
		 * Therefore if set Z contains as many cells as the maze, it contains every cell.
		 * 
		 * Removes comparison time between each element in the maze and each element in set Z.
		 * Reduces time complexity from O(n^2) to O(1), where n = number of cells.
		 */
		
		if(maze.type == Maze.NORMAL){
			return (setZ.size() == maze.sizeC * maze.sizeR);
			
		}else{
			return (setZ.size() == maze.sizeC * (maze.sizeR + 1/2));
		}

	}
	
	
	/**
	 * Method that carves a path between random cells.
	 * 
	 * ******************************************************************************
	 * 
	 * ALGORITHM CarvePath(dirSet[])
	 * Carves a path between 2 random cells in the maze.
	 * INPUT: A set of valid directions for neighbours dirSet[].
	 * OUTPUT: None.
	 * 
	 * 1: c = randomCell(setF)
	 * 2: setF.remove(c)
	 * 3: for each neighbour of c
	 * 4:	if neighbour is in set Z
	 * 5:			validNeighbours.add(neighbour)
	 * 6:	end if
	 * 7: end for
	 * 8: b = randomCell(validNeighbours)
	 * 9: cWall = wall between c and b
	 * 10: remove(cWall)
	 * 11: setZ.add(c)
	 * 12: for each neighbour of c
	 * 13: 	if setZ and setF do not contain neighbour
	 * 14:		setF.add(neighbour)
	 * 15:	end if
	 * 16: end for 
	 * 
	 * ******************************************************************************
	 * 
	 * @param dirSet The valid set of directions based on Maze type.
	 * 
	 */
	private void carvePath(Integer[] dirSet){

		// Pick a random Cell c from set F.
		int randIndex = ThreadLocalRandom.current().nextInt(0, setF.size());
		Cell c = setF.get(randIndex);

		// Remove it from set F.
		setF.remove(c);

		// Create array to hold valid neighbours for b to choose from.
		ArrayList<Cell> validNeighbours = new ArrayList<Cell>();

		// For each neighbour of c.
		for(int i=0; i<dirSet.length; i++){
			// If it isn't null and is in set Z.
			if(c.neigh[dirSet[i]] != null){
				if(setZ.contains(c.neigh[dirSet[i]]) == true){
					// Add to the valid set of neighbours that b could be.
					validNeighbours.add(c.neigh[dirSet[i]]);
				}	
			}
		}

		// Set b to a random, valid neighbour.
		randIndex = ThreadLocalRandom.current().nextInt(0, validNeighbours.size());
		Cell b = validNeighbours.get(randIndex);
		
		// Finding the wall between b and c place in the maze.
		Integer cWall = Arrays.asList(c.neigh).indexOf(b);
		
		// Remove wall.
		c.wall[cWall].present = false;

		// Add cell c to the set Z.
		setZ.add(c);
		
		// Add the neighbours of cell c to F
		for(int i=0; i<dirSet.length; i++){
			if(c.neigh[dirSet[i]] != null){
				// Neighbours must not be in either set to generate a perfect maze.
				if(setF.contains(c.neigh[dirSet[i]]) == false){
					if(setZ.contains(c.neigh[dirSet[i]]) == false){
						setF.add(c.neigh[dirSet[i]]);		
					}
				}	
			}
		}
	}
	
} // end of class ModifiedPrimsGenerator
