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
	
	@Override
	public void solveMaze(Maze maze) {
		
		// Adding the base nodes
		maze.entrance.visited = true;
		maze.exit.visited = true;
		
		entranceStack.add(maze.entrance);
		exitStack.add(maze.exit);
		
		// While we have not reached a terminating condition...
		Boolean complete = false;
		
		// stackTurn alternates between the stacks.
		// Less repeated code.
		int stackTurn = 2;
		
		while(complete == false){
			if(stackTurn % 2 == 0){
				// If there are no new, valid neighbours to add to the stack...
				if(addNeighbourToStack(entranceStack) == false){
					// If we can't backtrack anymore...
					if(backTrack(entranceStack, 1) == false){
						// Then one of the stacks would be empty, and we can't have the 2 fronts link
						break;
					}
				}else{
					// Mark new node as visited, include in counter.
					entranceStack.peek().visited = true;
					steps++;
				}
				stackTurn = 1;
				
			}else{
				
				if(addNeighbourToStack(exitStack) == false){
					if(backTrack(exitStack, 1) == false){
						break;
					}
				}else{
					exitStack.peek().visited = true;
					steps++;
				}
				stackTurn = 2;
			}
			
			// Since we alternate between DFS steps for the entrance and exit stacks I have 2 checkCollisions()
			// If you know a good way to alternate
			if(checkCollision() == true){
				break;
			}

		}

	} // end of solveMaze()

	
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
	// Can probably change this to just backtrack once.
	private Boolean backTrack(Stack<Cell> stack, int x){
		if(stack.size() > 0){
			for(int i=0; i<x; i++){
				stack.pop();
			}
			return true;
		}
		
		// If we were going to pop the last element in the stack, then there was no path 
		return false;
	}

	@Override
	public boolean isSolved() {
		// TODO Auto-generated method stub
		return false;
	} // end if isSolved()


	@Override
	public int cellsExplored() {
		return steps;
	} // end of cellsExplored()

} // end of class BiDirectionalRecursiveBackTrackerSolver
