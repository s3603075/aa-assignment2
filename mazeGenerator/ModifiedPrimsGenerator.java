package mazeGenerator;
import java.util*;

import maze.Maze;

public class ModifiedPrimsGenerator implements MazeGenerator {

	@Override
	public void generateMaze(Maze maze) {
		// TODO Auto-generated method stub
		
		// Pick random cell in map
		Random rand;
		
		int randomR = rand.nextInt(maze.sizeR);
		int randomC = rand.nextInt(maze.sizeC);
		
		Cell rootCell = maze[randomR][randomC];
		
		
		
		

		
		// Sets
		ArrayList<Cell> frontier = new ArrayList<Cell>();
		
		ArrayList<Cell> Z = new ArrayList<Cell>();
		
		
		// Add root to Z
		Z.add(rootCell);
		
		
		// Add neighbours to frontier set F
		
		// Check if neighbours in each direction are in the map
		
		// Use my methods
		
		if(maze instanceof NormalMaze){
			addNeighboursNormalMaze(rootCell, maze, frontier);
		}else if(maze instanceof HexMaze){
			addNeighboursHexMaze(rootCell, maze, frontier);
		}
		
		
		
		// Randomly pick cell (C) from F and remove it from F
		
		
		Cell C = frontier.get(rand.nextInt(frontier.size));
		
		frontier.remove(frontier.indexOf(C));
		
		
		// Randomly pick cell (B) that is in Z and adjacent to C
		
		// Cell.neighbours[]
		// Pick random
		
		// For each cell in neighbours
		
		// check if neighbour of  new cell
		// true == add to potential set
		// randomly pick an item from potential set
			// generate random number from 0 to size-1
			// get cell at that point
		// determine it's connection to new cell
		// break wall between them
			// set wall[i] = null

		
		// Repeat until every cell in Z is in the maze
		
		
		
		
		
	} // end of generateMaze()

	
	// These 4 cover all 8 possibilities
	private void breakNorthWall(Cell northCell, Cell southCell){
		northCell.wall[SOUTH] = null;
		southCell.wall[NORTH] = null;
	}
	
	private void breakNorthEastWall(Cell neCell, Cell seCell){
		neCell.wall[SOUTHWEST] = null;
		seCell.wall[NORTHEAST] = null;
	}
	
	private void breakEastWall(Cell eastCell, Cell westCell){
		eastCell.wall[WEST] = null;
		westCell.wall[EAST] = null;
	}

	private void breakSouthEastWall(Cell seCell, Cell neCell){
		seCell.wall[NORTHWEST] = null;
		neCell.wall[SOUTHEAST] = null;
	}
	
	
	private void addNeighboursNormalMaze(Cell currentCell, Maze maze, ArrayList<Cell> frontier){

		// if the potential neighbour is in the maze
		if(maze.isIn(currentCell.r + deltaR[NORTH], currentCell.c + deltaC[NORTH]) == true){
			// and is not already in the frontier
			if(frontier.contains(maze[currentCell.r + deltaR[NORTH]][currentCell.c + deltaC[NORTH]])
					== false){
				frontier.add(maze[currentCell.r + deltaR[NORTH]][rootCell.c + deltaC[NORTH]]);
			}
		}
		
		if(maze.isIn(currentCell.r + deltaR[EAST], currentCell.c + deltaC[EAST]) == true){
			if(frontier.contains(maze[currentCell.r + deltaR[EAST]][currentCell.c + deltaC[EAST]])
					== false){
				frontier.add(maze[currentCell.r + deltaR[EAST]][rootCell.c + deltaC[EAST]]);
			}
		}
		
		if(maze.isIn(currentCell.r + deltaR[WEST], currentCell.c + deltaC[WEST]) == true){
			if(frontier.contains(maze[currentCell.r + deltaR[WEST]][currentCell.c + deltaC[WEST]])
					== false){
				frontier.add(maze[currentCell.r + deltaR[WEST]][rootCell.c + deltaC[WEST]]);
			}
		}
		
		if(maze.isIn(currentCell.r + deltaR[SOUTH], currentCell.c + deltaC[SOUTH]) == true){
			if(frontier.contains(maze[currentCell.r + deltaR[SOUTH]][currentCell.c + deltaC[SOUTH]])
					== false){
				frontier.add(maze[currentCell.r + deltaR[SOUTH]][rootCell.c + deltaC[SOUTH]]);
			}
		}

	}
	
	private void addNeighboursHexMaze(Cell currentCell, Maze maze, ArrayList<Cell> frontier){
		// if the potential neighbour is in the maze
		if(maze.isIn(currentCell.r + deltaR[EAST], currentCell.c + deltaC[EAST]) == true){
			// and is not already in the frontier
			if(frontier.contains(maze[currentCell.r + deltaR[EAST]][currentCell.c + deltaC[EAST]])
					== false){
				frontier.add(maze[currentCell.r + deltaR[EAST]][rootCell.c + deltaC[EAST]]);
			}
		}
		
		if(maze.isIn(currentCell.r + deltaR[WEST], currentCell.c + deltaC[WEST]) == true){
			if(frontier.contains(maze[currentCell.r + deltaR[WEST]][currentCell.c + deltaC[WEST]])
					== false){
				
				frontier.add(maze[currentCell.r + deltaR[WEST]][rootCell.c + deltaC[WEST]]);
			}
		}
		
		if(maze.isIn(currentCell.r + deltaR[NORTHEAST], currentCell.c + deltaC[NORTHEAST]) == true){
			if(frontier.contains(maze[currentCell.r + deltaR[NORTHEAST]][currentCell.c + deltaC[NORTHEAST]])
					== false){
				
				frontier.add(maze[currentCell.r + deltaR[NORTHEAST]][rootCell.c + deltaC[NORTHEAST]]);
			}
		}
		
		if(maze.isIn(currentCell.r + deltaR[SOUTHEAST], currentCell.c + deltaC[SOUTHEAST]) == true){
			if(frontier.contains(maze[currentCell.r + deltaR[SOUTHEAST]][currentCell.c + deltaC[SOUTHEAST]])
					== false){
				frontier.add(maze[currentCell.r + deltaR[SOUTHEAST]][rootCell.c + deltaC[SOUTHEAST]]);
			}
		}
		
		if(maze.isIn(currentCell.r + deltaR[NORTHWEST], currentCell.c + deltaC[NORTHWEST]) == true){
			if(frontier.contains(maze[currentCell.r + deltaR[NORTHWEST]][currentCell.c + deltaC[NORTHWEST]])
					== false){
				frontier.add(maze[currentCell.r + deltaR[NORTHWEST]][rootCell.c + deltaC[NORTHWEST]]);
			}
		}
		
		if(maze.isIn(currentCell.r + deltaR[SOUTHWEST], currentCell.c + deltaC[SOUTHWEST]) == true){
			if(frontier.contains(maze[currentCell.r + deltaR[SOUTHWEST]][currentCell.c + deltaC[SOUTHWEST]])
					== false){
				frontier.add(maze[currentCell.r + deltaR[SOUTHWEST]][rootCell.c + deltaC[SOUTHWEST]]);
			}
		}

		
	}
	
	
} // end of class ModifiedPrimsGenerator
