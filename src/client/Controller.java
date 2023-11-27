package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Controller {

	private View view;
	private MouseListener boardPanelListener;
	private ActionListener submitButtonListener;

	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	private char player;
	private String ip;

	final String helpInfo = "Some information about the game:\nCriteria for a valid move:\nThe move is not occupied by any mark.\nThe move is made in the player's turn.\nThe move is made within the 3 x 3 board.\nThe game would continue and switch among the opposite player until it reaches either one of the following conditions:\n-Player 1 wins.\n-Player 2 wins.\n-Draw.";



	public Controller(View view, String ip) {
		this.view = view;
		this.ip = ip;
	}

	public void newTurn() {
		// view.getInfoLabel().setText("Please submit your name.");
		// view.getBoardPanel().setEnabled(false);
		// view.getSubmitButton().setEnabled(true);
		// view.getInputField().setEnabled(true);
		view.getBoardPanel().clearBoard();
		out.println("start");
		view.getFrame().repaint();
	}

	public void disconnect() {
		out.println("disconnect");
	}

	public void start() {
		view.getSubmitButton().setEnabled(false);
		view.getInputField().setEnabled(false);
		try {
			this.socket = new Socket(ip, 12396);
			this.in = new Scanner(socket.getInputStream());
			this.out = new PrintWriter(socket.getOutputStream(), true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		boardPanelListener = new MouseListener() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int x = e.getX() / 100;
				int y = e.getY() / 100;
				out.println("move " + y + " " + x);
				System.out.println("move " + y + " " + x);
			}

			public void mousePressed(java.awt.event.MouseEvent e) {
			}

			public void mouseReleased(java.awt.event.MouseEvent e) {
			}

			public void mouseEntered(java.awt.event.MouseEvent e) {
			}

			public void mouseExited(java.awt.event.MouseEvent e) {
			}
		};
		view.getBoardPanel().addMouseListener(boardPanelListener);

		submitButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				String name = view.getInputField().getText();
				view.getInfoLabel().setText("Hi, " + name + " .Waiting for another player to join...");
				view.getSubmitButton().setEnabled(false);
				view.getInputField().setEnabled(false);
				view.getFrame().setTitle("Tic Tac Toe-" + name);
				out.println("start");
			}
		};
		view.getSubmitButton().addActionListener(submitButtonListener);

		JMenuBar menuBar = new JMenuBar();

		JMenu menuControl = new JMenu("Control");
		JMenuItem menuItemExit = new JMenuItem("Exit");
		menuItemExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disconnect();
				System.exit(0);
			}
		});
		menuControl.add(menuItemExit);

		JMenu menuHelp = new JMenu("Help");
		JMenuItem menuItemInstruction = new JMenuItem("Instruction");
		menuItemInstruction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(view.getFrame(), helpInfo);
			}
		});
		menuHelp.add(menuItemInstruction);

		menuBar.add(menuControl);
		menuBar.add(menuHelp);
		view.getFrame().setJMenuBar(menuBar);

		// newTurn();
		view.getBoardPanel().setEnabled(false);
		view.getBoardPanel().clearBoard();
		// Creates a new Thread for reading server messages
		Thread handler = new ClinetHandler(socket);
		handler.start();
	}

	class ClinetHandler extends Thread {
		private Socket socket;

		public ClinetHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				readFromServer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void readFromServer() throws Exception {
			try {
				view.getInfoLabel().setText("connecting to server...");
				String[] message;
				message = in.nextLine().split(" ");
				if (message[0].equals("connected")) {
					view.getInfoLabel().setText("Connected to server.");
				} else if (message[0].equals("full")) {
					view.getInfoLabel().setText("Server is full.");
					return;
				} else {
					view.getInfoLabel().setText("Failed to connect to server.");
					return;
				}
				view.getSubmitButton().setEnabled(true);
				view.getInputField().setEnabled(true);
				while (in.hasNextLine()) {
					message = in.nextLine().split(" ");
					out.flush();

					if (message[0].equals("start")) {
						view.getSubmitButton().setEnabled(false);
						view.getInputField().setEnabled(false);
						view.getInfoLabel().setText("Game started. You are player " + message[1] + ".");
						player = message[1].charAt(0);
						view.getBoardPanel().setEnabled(true);
						view.getFrame().repaint();
					} else if (message[0].equals("move")) {
						int x = Integer.parseInt(message[1]);
						int y = Integer.parseInt(message[2]);
						view.getBoardPanel().setBoard(x, y, message[3].charAt(0));
						if (message[3].charAt(0) == player) {
							view.getInfoLabel().setText("Opponent's turn. Waiting for the other player to move.");
						} else {
							view.getInfoLabel().setText("Your turn.");
						}
						view.getBoardPanel().repaint();
					} else if (message[0].equals("invalid")) {
						view.getInfoLabel().setText("Invalid move. Try again.");
					} else if (message[0].equals("wait")) {
						view.getInfoLabel().setText("Not your turn. Please wait for the other player to move.");
					} else if (message[0].equals("win")) {
						if (message[1].charAt(0) == player) {
							view.getInfoLabel().setText("You win!");
							int opt = JOptionPane.showConfirmDialog(view.getFrame(),
									"You win! Do you want to play again?", "Game over", JOptionPane.YES_NO_OPTION);
							if (opt == JOptionPane.YES_OPTION) {
								out.println("newGame");
							} else {
								out.println("disconnect");
								System.exit(0);
							}
						} else {
							view.getInfoLabel().setText("You lose!");
							int opt = JOptionPane.showConfirmDialog(view.getFrame(),
									"You lose! Do you want to play again?", "Game over", JOptionPane.YES_NO_OPTION);
							if (opt == JOptionPane.YES_OPTION) {
								out.println("newGame");
							} else {
								out.println("disconnect");
								System.exit(0);
							}
						}
						newTurn();
					} else if (message[0].equals("tie")) {
						view.getInfoLabel().setText("Tie!");
						int opt = JOptionPane.showConfirmDialog(view.getFrame(),
								"Tie! Do you want to play again?", "Game over", JOptionPane.YES_NO_OPTION);
						if (opt == JOptionPane.YES_OPTION) {
							out.println("newGame");
						} else {
							out.println("disconnect");
							System.exit(0);
						}
						newTurn();
					} else if (message[0].equals("otherDisconnect")) {
						view.getInfoLabel().setText("The other player has disconnected.");
						int opt = JOptionPane.showConfirmDialog(view.getFrame(),
								"The other player has disconnected. Do you want to leave?", "Game over",
								JOptionPane.YES_NO_OPTION);
						if (opt == JOptionPane.YES_OPTION) {
							out.println("disconnect");
							System.exit(0);
						} else {
							out.println("newGame");
						}
						newTurn();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				socket.close();
			}
		}
	}

}
