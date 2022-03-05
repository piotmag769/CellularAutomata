import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Point {
	private ArrayList<Point> neighbors;
	private int currentState;
	private int nextState;
	private int numStates = 6;
	private static int simulationNumber = 1; // 1 - GameOfLife, 2 - Cities, 3 - Coral, 4 - Rain

	private final Random random = new Random();

	public Point() {
		currentState = 0;
		nextState = 0;
		neighbors = new ArrayList<Point>();
	}

	public void clicked() {
		currentState = (++currentState) % numStates;
	}

	public int getState() {
		return currentState;
	}

	public void setState(int s) {
		currentState = s;
	}

	public void calculateNewState() {
		int[] oneToOne = {};
		int[] zeroToOne = {};

		switch (simulationNumber) {
			// 23/3 - game of life
			case 1:
				oneToOne = new int[]{2, 3};
				zeroToOne = new int[]{3};
				break;

			// 2345/45678 - cities
			case 2:
				oneToOne = new int[]{2, 3, 4, 5};
				zeroToOne = new int[]{4, 5, 6, 7, 8};
				break;

			// 45678/3 - coral
			case 3:
				oneToOne = new int[]{4, 5, 6, 7, 8};
				zeroToOne = new int[]{3};
				break;

			// rain
			case 4:
				if (currentState > 0)
					nextState = currentState - 1;
				else if (this.aliveNeighbors() == 1)
					nextState = 6;
				break;
		}

		if (simulationNumber != 4) {
			int aliveNei = aliveNeighbors();
			switch (currentState) {
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
			}
		}
	}

	public void changeState() {
		currentState = nextState;
	}

	public void addNeighbor(Point nei) {
		neighbors.add(nei);
	}

	private int aliveNeighbors() {
		int res = 0;
		for (var nei : neighbors)
			// >= 1 for case of rain
			if (nei.currentState >= 1)
				++res;

		return res;
	}

	private boolean isInArray(int element, int[] array) {
		for (int j : array)
			if (j == element)
				return true;
		return false;
	}

	public void drop() {
		// prob 5%
		if (random.nextInt(20) == 1)
			this.currentState = 6;
	}

	public void assignNeighbors(Point[][] points, int length, int height, int x, int y) {
		neighbors = new ArrayList<>();
		if (simulationNumber == 4) {
			if (y - 1 >= 0) {
//				System.out.println(x + " " + y + " " + x + " " + (y - 1) );
				this.addNeighbor(points[x][y - 1]);
			}
		}
		else
			for (int a = Math.max(0, x - 1); a <= Math.min(length - 1, x + 1); a++)
				for (int b = Math.max(0, y - 1); b <= Math.min(height - 1, y + 1); b++)
					if (a != x || b != y) {
//						System.out.println(x + " " + y + ": " + a + " " + b);
						this.addNeighbor(points[a][b]);
					}
	}

	public static void setSimulationNumber(int simulationNumber) {
		Point.simulationNumber = simulationNumber;
	}
}
