import java.util.ArrayList;

public class Point {
	private ArrayList<Point> neighbors;
	private int currentState;
	private int nextState;
	private int numStates = 6;
	
	public Point() {
		currentState = 0;
		nextState = 0;
		neighbors = new ArrayList<Point>();
	}

	public void clicked() {
		currentState=(++currentState)%numStates;	
	}
	
	public int getState() {
		return currentState;
	}

	public void setState(int s) {
		currentState = s;
	}

	public void calculateNewState() {
		//TODO: insert logic which updates according to currentState and number of active neighbors

//		// 23/3 - game of life
//		int[] oneToOne = {2, 3};
//		int[] zeroToOne = {3};

//		// 2345/45678 - cities
//		int[] oneToOne = {2, 3, 4, 5};
//		int[] zeroToOne = {4, 5, 6, 7, 8};

		// 45678/3 - coral
		int[] oneToOne = {4, 5, 6, 7, 8};
		int[] zeroToOne = {3};

		int aliveNei = aliveNeighbors();
		switch (currentState)
		{
			case 0 -> {
				if (isInArray(aliveNei, zeroToOne))
					nextState = 1;
				else
					nextState = 0;
			}

			case 1 -> {
				if (isInArray(aliveNei, oneToOne))
					nextState = 1;
				else
					nextState = 0;
			}

			default -> throw new IllegalStateException("Unexpected state value: " + currentState);
		};
	}

	public void changeState() {
		currentState = nextState;
	}
	
	public void addNeighbor(Point nei) {
		neighbors.add(nei);
	}

	//TODO: write method counting all active neighbors of THIS point
	private int aliveNeighbors()
	{
		int res = 0;
		for(var nei: neighbors)
			if (nei.currentState == 1)
				++res;

		return res;
	}

	private boolean isInArray(int element, int[] array)
	{
		for (int j : array)
			if (j == element)
				return true;
		return false;
	}
}
