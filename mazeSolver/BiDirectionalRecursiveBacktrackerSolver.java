package mazeSolver;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import maze.Cell;
import maze.Maze;
import maze.Wall;

/**
 * Implements the BiDirectional recursive backtracking maze solving algorithm.
 * 
 * Basically, run 2 simultaneous DFS branches from the entrance and exit.
 * If they collide, then we have a path.
 */
public class BiDirectionalRecursiveBacktrackerSolver implements MazeSolver {

	private Stack<Cell> entranceStack = new Stack<Cell>();
	private Stack<Cell> exitStack = new Stack<Cell>();
	private int steps = 0;
	private int stackTurn = 1;
	
	private ArrayList<Cell> path = null;
	private Maze map = null;
	
	@Override
	public void solveMaze(Maze maze) {
		
		map = maze;
		
		// Adding the base nodes
		entranceStack.push(maze.entrance);
		maze.entrance.visited = true;
		steps++;
		maze.drawFtPrt(maze.entrance);
		
		exitStack.push(maze.exit);
		maze.exit.visited = true;
		steps++;
		maze.drawFtPrt(maze.exit);
		
		findNewPath(entranceStack);
		combinePath();

	} // end of solveMaze()
	
	
	private void findNewPath(Stack<Cell> stack){
		
		// Determines whether we call findNewPath again.
		Boolean call = false;
		
		// If we've haven't found a collision...
		if(checkCollision() == false){
			
			// Try to add a neighbour, mark it as visited, increment the search count
			if(addNeighbourToStack(stack) == true){
				stack.peek().visited = true;
				steps++;
				map.drawFtPrt(stack.peek());
				call = true;
			
			// If there were no neighbours, try to backtrack
			}else{
				// If we can successfully backtrack, call again
				if(backTrack(stack) == true){
					call = true;
				}
			}
		}
			
		if(call == true){
			// Alternate between which stack gets called.
			if(stackTurn == 0){
				stackTurn = 1;
				findNewPath(entranceStack);
			}else{
				stackTurn = 0;
				findNewPath(exitStack);
			}
		}
		
	}
	
	
	private Boolean addNeighbourToStack(Stack<Cell> stack){
		
		Cell cell = stack.peek();
		ArrayList<Cell> neighbours = new ArrayList<Cell>();
		
		// For each potential neighbour...
		for(int i=0; i<cell.neigh.length; i++){
			// If it is valid...
			if(cell.neigh[i] != null && cell.neigh[i].visited == false && cell.wall[i].present == false){
				// Put in pool for random selection
				neighbours.add(cell.neigh[i]);	
			}
		}
		
		if(cell.tunnelTo != null){
			neighbours.add(cell.tunnelTo);
		}
		
		// If there were no valid neighbours, return false.
		if(neighbours.size() == 0){
			return false;
		}

		// Push a random valid neighbour onto the stack
		int rand = ThreadLocalRandom.current().nextInt(0, neighbours.size());
		stack.push(neighbours.get(rand));
		
		return true;
	}
	
	
	// Checks if the 2 stack fronts have collided.
	// If they have, then we have a path ready.
	private Boolean checkCollision(){
		
		Cell entranceTop = entranceStack.peek();
		Cell exitTop = exitStack.peek();
		
		// If 1 of the fronts is a neighbour of the other...
		if(Arrays.asList(entranceTop.neigh).contains(exitTop) == true){
			
			// And there is no wall between them...
			int index = Arrays.asList(entranceTop.neigh).indexOf(exitTop);
			if(entranceTop.wall[index].present == false){
				
				// We've found the end.
				return true;
			}
		}

		// No collision.
		return false;
	}
	

	// Backtracks a stack by a given amount.
	private Boolean backTrack(Stack<Cell> stack){
		if(stack.empty() == false){
			stack.pop();
			return true;
		}
		
		// If we were going to pop the last element in the stack, then there was no path 
		return false;
	}

	private void combinePath(){
		path = new ArrayList<Cell>(entranceStack);

		@SuppressWarnings("unchecked")
		Stack<Cell> tempExit = (Stack<Cell>) exitStack.clone();
		
		while(tempExit.size() > 0){
			path.add(tempExit.peek());
			tempExit.pop();
		}

	}
	
	
	
	@Override
	public boolean isSolved() {
		// If the path is empty, then we didn't solve the maze.
		if(path.isEmpty() == true){
			return false;
		}

		// If the 1st part of the path is the entrance...
		if(path.get(0) == map.entrance){
					
			Cell current;
			Cell next;
			Wall wall;
			
			// Loop through each element in the path,
			for(int i=0; i<path.size() - 1; i++){
				
				current = path.get(i);
				next = path.get(i+1);
						
				// Check if we can reach the next item in the path
				if(Arrays.asList(current.neigh).contains(next) || current.tunnelTo == next){
					
					// If the next item wasn't a tunnel, we need to check if there was a wall there
					if(current.tunnelTo != next){
						wall = current.wall[Arrays.asList(current.neigh).indexOf(next)];
						if(wall.present == true){
							return false;
						}
					}
					
				}else{
					return false;
				}
			}
			
			// Check if the last element in the path is the exit
			if(path.get(path.size() - 1) == map.exit){
				// If it passed all of these checks, then the maze was solved.
				return true;
			}
					
		}		
				
		return false;
	} // end if isSolved()
   

	@Override
	public int cellsExplored() {		
		return steps;
	} // end of cellsExplored()

} // end of class BiDirectionalRecursiveBackTrackerSolver
