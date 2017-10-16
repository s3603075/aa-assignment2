package mazeSolver;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import maze.Cell;
import maze.Maze;
import maze.Wall;

/**
 * Maze Solver that uses two separate DFS trees to find a solution.
 * 
 * @author Danny Ho			s3603075
 * @author Caleb Turner		s3604744
 *
 */

public class BiDirectionalRecursiveBacktrackerSolver implements MazeSolver {

	// Stacks to store the visited nodes for each DFS branch.
	private Stack<Cell> entranceStack = new Stack<Cell>();
	private Stack<Cell> exitStack = new Stack<Cell>();
	
	// The number of visited nodes.
	private int steps = 0;
	
	// The final path from entrance to exit for validation.
	private ArrayList<Cell> path = null;
	
	// Pointer to the Maze, on Yongli's advice to make it visible for other functions
	private Maze map = null;
	
	
	/**
	 * Algorithm for solving a maze using 2 DFS trees.
	 * 
	 * ******************************************************************************************
	 * 
	 * ALGORITHM BiDirectionalDFS(maze)
	 * Uses 2 separate DFS trees to solve a maze, one from the entrance, the other from the exit.
	 * INPUT: Maze maze.
	 * OUTPUT: None.
	 * 
	 *	1: entranceStack.push(entrance)
	 *	2: draw(entrance)
	 *	3: exitStack.push(exit)
	 *	3: draw(exit)
	 *	4: findNewPath(entranceStack)
	 *	5: combinePaths();
	 *
	 * ******************************************************************************************
	 *
	 * @param maze Input maze.
	 * 
	 */
	@Override
	public void solveMaze(Maze maze) {
		
		// Assigning a pointer so other functions can see the maze easily.
		map = maze;
		
		// Adding the base cells.
		entranceStack.push(maze.entrance);
		
		// Marking the cell, increasing the step counter, drawing the cell.
		// solveVisited variable is contained in Cell.java.
		maze.entrance.solveVisited = true;
		steps++;
		maze.drawFtPrt(maze.entrance);
		
		
		exitStack.push(maze.exit);
		maze.exit.solveVisited = true;
		steps++;
		maze.drawFtPrt(maze.exit);
		
		// Start recursively calling DFS's.
		findNewPath(entranceStack);
		
		// Combine the entrance path and exit path into 1 solution path.
		combinePath();

	} // end of solveMaze()
	
	
	/**
	 * Algorithm that recursively calls DFS, alternating for each stack.
	 * 
	 * ******************************************************************************************
	 * 
	 * ALGORITHM findNewPath(stack)
	 * INPUT: Stack<Cell> stack.
	 * OUTPUT: None.
	 * 
	 * 1: shouldCall = false
	 * 2: if path's have not collided
	 * 3:	if there are neighbours to forwards to
	 * 3:		draw the newest Cell
	 * 4:		shouldCall = true
	 * 5:	else
	 * 6:		if we can backtrack without emptying the stack
	 * 7:			should Call = true
	 * 8:		end if
	 * 9:	end if
	 * 10:end if
	 * 11:if shouldCall == true
	 * 12:	if stack == exitStack
	 * 13:		findNewPath(entranceStack)
	 * 14:	else
	 * 15:		findNewPath(exitStack)
	 * 16:	end if
	 * 17:end if
	 * 
	 * ******************************************************************************************
	 * 
	 * @param stack
	 */
	private void findNewPath(Stack<Cell> stack){
		
		// Determines whether we call findNewPath() again.
		Boolean call = false;
		
		// If we've haven't found a collision...
		if(checkCollision() == false){
			
			// Try to add a neighbour, mark it as solveVisited, increment the search count
			if(addNeighbourToStack(stack) == true){
				stack.peek().solveVisited = true;
				steps++;
				map.drawFtPrt(stack.peek());
				call = true;
			
			// If there were no neighbours, try to backtrack the search.
			}else{
				// If we can successfully backtrack, we can search for neighbours again.
				if(backTrack(stack) == true){
					call = true;
				}
			}
		}
			
		if(call == true){
			// Alternate between which stack gets called.
			if(stack == exitStack){
				findNewPath(entranceStack);
			}else{
				findNewPath(exitStack);
			}
		}
	}
	
	
	/**
	 * Algorithm that finds the next Cell in the DFS and adds it to the stack.
	 * 
	 * ******************************************************************************************
	 *
	 * ALGORITHM addNeighbourToStack(stack)
	 * Finds the next Cell in the DFS and adds it to the stack.
	 * INPUT: Stack<Cell> stack
	 * OUTPUT: Boolean
	 * 
	 *	1: cell = stack.peek()
	 *	2: validNeighbours = CellArray[]
	 *	3: for each neighbour of cell
	 *	4:		if cell is an unvisited node without a wall in the way
	 *	5:			validNeighbours.add(neighbour)
	 *	6:		end if
	 *	7: end for
	 *	8: if there is a tunnel at the cell
	 *	9:		validNeighbours.add(tunnelCell)
	 *	10: end if
	 *	11: if validNeighbours is empty
	 *	12:		return false
	 *	13: end if
	 *	14: newCell = randomCell(validNeighbours)
	 *	15: return true
	 *
	 * ******************************************************************************************
	 *
	 * @param stack The stack that we try to add the next Cell on to.
	 * @return Boolean Boolean for whether or not there was a valid neighbour Cell to move to.
	 */
	private Boolean addNeighbourToStack(Stack<Cell> stack){
		
		Cell cell = stack.peek();
		ArrayList<Cell> neighbours = new ArrayList<Cell>();
		
		// For each potential neighbour...
		for(int i=0; i<cell.neigh.length; i++){
			// If it is valid...
			if(cell.neigh[i] != null && cell.neigh[i].solveVisited == false && cell.wall[i].present == false){
				// Put in the pool for random selection.
				neighbours.add(cell.neigh[i]);	
			}
		}
		
		// If there was a tunnel cell, add it into the pool of valid neighbours.
		if(cell.tunnelTo != null){
			neighbours.add(cell.tunnelTo);
		}
		
		// If there were no valid neighbours, return false.
		if(neighbours.size() == 0){
			return false;
		}

		// Push a random valid neighbour onto the stack.
		int rand = ThreadLocalRandom.current().nextInt(0, neighbours.size());
		stack.push(neighbours.get(rand));
		
		return true;
	}
	
	
	/**
	 * Algorithm that determines whether or not the 2 DFS trees have collided.
	 *
	 * ******************************************************************************************
	 *
	 * ALGORITHM checkCollision()
	 * INPUT: None.
	 * OUTPUT: None.
	 * 
	 * 1: entranceTop = entranceStack.peek()
	 * 2: exitTop = exitStack.peek()
	 * 3: for each neighbour of entranceTop
	 * 4:	// We have found the path.
	 * 5: 	if neighbour is in exitStack
	 * 6:		while exitStack.peek() != neighbour
	 * 7:			exitStack.pop()
	 * 8:		end while
	 * 9:		return true
	 * 10:	end if
	 * 11: end for
	 * 12: if entranceTop has a tunnel
	 * 13:	if the tunnel ends in the exitStack
	 * 14:		while exitStack.peek() != neighbour
	 * 15:			exitStack.pop()
	 * 16:		end while
	 * 17:		return true
	 * 18:	end if
	 * 19: end if
	 * 20: for each neighbour of exitTop
	 * 21:	// We have found the path.
	 * 22:	if neighbour is in entranceStack
	 * 23:		while entranceStack.peek() != neighbour
	 * 24:			entranceStack.pop()
	 * 25:		end while
	 * 26:		return true
	 * 27:	end if
	 * 28: end for
	 * 29: if entranceTop has a tunnel
	 * 30: 	if the tunnel ends in the entranceStack
	 * 31: 		while entranceStack.peek() != neighbour
	 * 32:			entranceStack.pop()
	 * 33:		end while
	 * 34:		return true
	 * 35:	end if
	 * 36: end if
	 *
	 * ******************************************************************************************
	 *
	 * @return Boolean 
	 */
	// Checks if the 2 stack fronts have collided.
	// If they have, then we have a path ready.
	private Boolean checkCollision(){
		
		Cell entranceTop = entranceStack.peek();
		Cell exitTop = exitStack.peek();
		
		// Check if there is a neighbour that is in the other stack.
		for(int i=0; i<entranceTop.neigh.length; i++){
			if(entranceTop.neigh[i] != null){
				// If there is...
				if(exitStack.contains(entranceTop.neigh[i]) == true){
					
					// Pop exit stack until it stops at that cell.
					while(exitStack.peek() != entranceTop.neigh[i]){
						exitStack.pop();
					}
					
					// Return success.
					return true;
				}
			}
		}
		
		// If the entrance front has a tunnel that ends in the other stack...
		if(entranceTop.tunnelTo != null){
			if(exitStack.contains(entranceTop.tunnelTo) == true){
				
				// Pop exit stack until it stops at that cell.
				while(exitStack.peek() != entranceTop.tunnelTo){
					exitStack.pop();
				}
				
				// Return success.
				return true;
			}
		}
		
		// Same process for the other stack.
		for(int i=0; i<exitTop.neigh.length; i++){
			if(exitTop.neigh[i] != null){
				if(entranceStack.contains(exitTop.neigh[i]) == true){
					while(entranceStack.peek() != exitTop.neigh[i]){
						entranceStack.pop();
					}
					return true;
				}
			}
		}
		if(exitTop.tunnelTo != null){
			if(entranceStack.contains(exitTop.tunnelTo) == true){
				while(entranceStack.peek() != exitTop.tunnelTo){
					entranceStack.pop();
				}
				return true;
			}
		}
		
		// If there were no collisions, return failure.
		return false;
	}
	

	/**
	 * Algorithm to backtrack a stack without emptying it.
	 * 
	 * ******************************************************************************************
	 *
	 * ALGORITHM backTrack(stack)
	 * Pops a stack without emptying the stack.
	 * INPUT: Stack<Cell> stack.
	 * OUTPUT: Boolean.
	 *
	 * 1: if there is more than 1 element in the stack
	 * 2: 	stack.pop()
	 * 3: 	return true
	 * 4: end if
	 * 5: return false
	 * 
	 * ******************************************************************************************
	 *
	 * @param stack The stack to backtrack.
	 * @return Boolean Boolean representing whether or not the stack was successfully backtracked.
	 */
	private Boolean backTrack(Stack<Cell> stack){
		if(stack.size() > 1){
			stack.pop();
			return true;
		}
		
		// If we were going to pop the last element in the stack, then there was no path. 
		return false;
	}

	
	/**
	 * Algorithm that combines the paths created by the DFS's.
	 * 
	 * ******************************************************************************************
	 *
	 * ALGORITHM combinePaths()
	 * Combines the paths generated by the entrance DFS and the exit DFS.
	 * INPUT: None.
	 * OUTPUT: None.
	 *
	 * 1: path = arrayCopy(entranceStack)
	 * 2: tempExit = stackCopy(exitStack)
	 * 3: while tempExit is not empty
	 * 4: 	path.add(tempExit.peek())
	 * 5:	tempExit.pop()
	 * 6: end while
	 *
	 * ******************************************************************************************
	 * 
	 */
	private void combinePath(){
		// Copying the entrance stack in the form of an array for easier validation.
		path = new ArrayList<Cell>(entranceStack);

		// Have to suppress the warning, but we can guarantee that the exitStack is a Stack<Cell>.
		@SuppressWarnings("unchecked")
		Stack<Cell> tempExit = (Stack<Cell>) exitStack.clone();
		
		// While there are elements in the exitStack copy...
		while(tempExit.size() > 0){
			// Move them to the path.
			path.add(tempExit.peek());
			tempExit.pop();
		}
	}
	
	
	/**
	 * 
	 * Algorithm that determines whether or not the maze was solved.
	 * 
	 * ******************************************************************************************
	 *
	 * ALGORITHM isSolved() 
	 * Determines if each Cell in the path is a neighbour of one another.
	 * INPUT: None.
	 * OUTPUT: Boolean.
	 * 
	 * 1: if path is empty
	 * 2: 	return false
	 * 3: end if
	 * 4: if the 1st cell in the path is the map's entrance
	 * 5:	for each cell in the path
	 * 6:		if the next cell in the path is a neighbour of the current cell or is it's tunnel
	 * 7:			if there is a wall blocking the neighbour
	 * 8:				return false
	 * 9:			end if
	 * 10:		else
	 * 11:			return false
	 * 12:		end if
	 * 13:	end for
	 * 14: if the last cell in the path is the exit
	 * 15:	return true
	 * 16: end if
	 * 17: return false
	 * 
	 * ******************************************************************************************
	 *
	 * @return Boolean If the path is a valid solution for the maze.
	 */
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
   

	/**
	 * Returns the number of steps taken in the DFS.
	 * 
	 * ******************************************************************************************
	 *
	 * ALGORITHM: cellsExplored()
	 * Returns the number of steps taken in the DFS.
	 * INPUT: None.
	 * OUTPUT: Integer.
	 * 
	 * 1: return steps
	 * 
	 * ******************************************************************************************
	 *
	 * @return Integer The number of steps taken in the DFS.
	 * 
	 */
	@Override
	public int cellsExplored() {		
		return steps;
	} // end of cellsExplored()

} // end of class BiDirectionalRecursiveBackTrackerSolver
