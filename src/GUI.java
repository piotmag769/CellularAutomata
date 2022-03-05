import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class containing GUI: board + buttons
 */
public class GUI extends JPanel implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private Timer timer;
	private Board board;
	private JComboBox<String> simulationList;
	private JButton start;
	private JButton clear;
	private JSlider pred;
	private JFrame frame;
	private int iterNum = 0;
	private final int maxDelay = 500;
	private final int initDelay = 100;
	private boolean running = false;

	public GUI(JFrame jf) {
		frame = jf;
		timer = new Timer(initDelay, this);
		timer.stop();
	}

	/**
	 * @param container to which GUI and board is added
	 */
	public void initialize(Container container) {
		container.setLayout(new BorderLayout());
		container.setSize(new Dimension(1024, 768));

		JPanel buttonPanel = new JPanel();

		simulationList = new JComboBox<>(new String[]{"Game of Life", "Cities", "Coral", "Rain"});
		simulationList.setActionCommand("simulation changed");
		simulationList.addActionListener(this);

		start = new JButton("Start");
		start.setActionCommand("Start");
		start.setToolTipText("Starts clock");
		start.addActionListener(this);

		clear = new JButton("Clear");
		clear.setActionCommand("clear");
		clear.setToolTipText("Clears the board");
		clear.addActionListener(this);

		pred = new JSlider();
		pred.setMinimum(0);
		pred.setMaximum(maxDelay);
		pred.setToolTipText("Time speed");
		pred.addChangeListener(this);
		pred.setValue(maxDelay - timer.getDelay());

		buttonPanel.add(simulationList);
		buttonPanel.add(start);
		buttonPanel.add(clear);
		buttonPanel.add(pred);

		board = new Board(1024, 768 - buttonPanel.getHeight());
		container.add(board, BorderLayout.CENTER);
		container.add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * handles clicking on each button
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(timer)) {
			iterNum++;
			frame.setTitle(simulationList.getSelectedItem() + " (" + Integer.toString(iterNum) + " iteration)");
			board.iteration();
		} else {
			String command = e.getActionCommand();
			switch (command) {
				case "Start" -> {
					if (!running) {
						timer.start();
						start.setText("Pause");
					} else {
						timer.stop();
						start.setText("Start");
					}
					running = !running;
					clear.setEnabled(true);
				}
				case "clear" -> {
					iterNum = 0;
					timer.stop();
					running = false;
					start.setEnabled(true);
					board.clear();
					frame.setTitle("Cellular Automata Toolbox");
					start.setText("Start");
				}
				case "simulation changed" -> {
					// reset simulation
					iterNum = 0;
					timer.stop();
					running = false;
					start.setEnabled(true);
					board.clear();
					frame.setTitle("Cellular Automata Toolbox");
					start.setText("Start");
					String s = (String) simulationList.getSelectedItem();
					int simulationNumber = switch (s) {
						case "Game of Life" -> 1;
						case "Cities" -> 2;
						case "Coral" -> 3;
						case "Rain" -> 4;
						default -> throw new IllegalStateException("Unexpected demo box value: " + s);
					};
					// set simulation numbers and assign neighbors once again
					this.board.setSimulationNumber(simulationNumber);
					Point.setSimulationNumber(simulationNumber);
					board.assignAllNeighbors();
				}
			}
		}
	}

	/**
	 * slider to control simulation speed
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		timer.setDelay(maxDelay - pred.getValue());
	}
}
