package mazeSolver;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import maze.Cell;
import maze.Maze;

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
	private int stackTurn = 0;
	private Boolean solved = false;
	
	@Override
	public void solveMaze(Maze maze) {
		
		// Adding the base nodes
		maze.entrance.visited = true;
		steps++;
		maze.drawFtPrt(maze.entrance);
		
		maze.exit.visited = true;
		steps++;
		maze.drawFtPrt(maze.exit);
		
		entranceStack.add(maze.entrance);
		exitStack.add(maze.exit);
		
		solved = findNewPath(entranceStack, maze);

	} // end of solveMaze()

	
	private Boolean findNewPath(Stack<Cell> stack, Maze maze){
		// If we've found a path, propagate the true back up the calls
		if(checkCollision() == true){
			return true;
		}
		
		if(stack.peek().tunnelTo != null){
			stack.peek().visited = true;
			steps++;
			maze.drawFtPrt(stack.peek());
			stack.push(stack.peek().tunnelTo);
			
		}else{
		
			// Otherwise, try to add a neighbour, mark it as visited, increment the search count
			if(addNeighbourToStack(stack) == true){
				stack.peek().visited = true;
				steps++;
				maze.drawFtPrt(stack.peek());
	
			// If there are no new neighbours, try to backtrack
			}else{
				if(backTrack(stack) == false){
					// If we can't backtrack, something went wrong and there is no solution
					return false;
				}
			}
		}
		
		// Alternate between which stack gets called.
		if(stackTurn == 0){
			stackTurn = 1;
			return findNewPath(entranceStack, maze);
		}else{
			stackTurn = 0;
			return findNewPath(exitStack, maze);
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
		
		// If there were no valid neighbours, return false.
		if(neighbours.size() == 0){
			return false;
		}

		// Push a random valid neighbour onto the stack
		int rand = ThreadLocalRandom.current().nextInt(0, neighbours.size());
		entranceStack.push(neighbours.get(rand));
		
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

	@Override
	public boolean isSolved() {
		
		// Need find path first
		return solved;
	} // end if isSolved()


	@Override
	public int cellsExplored() {
		return steps;
	} // end of cellsExplored()

} // end of class BiDirectionalRecursiveBackTrackerSolver
