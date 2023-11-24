package ass4;

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

	final String helpInfo = """
            Some information about the game:
            Criteria for a valid move:
            The move is not occupied by any mark.
            The move is made in the player's turn.
            The move is made within the 3 x 3 board.
            The game would continue and switch among the opposite player until it reaches either one of the following conditions:
            -Player 1 wins.
            -Player 2 wins.
            -Draw.
            """;

	public Controller(View view) {
		this.view = view;
	}

	public void newTurn() {
		view.getInfoLabel().setText("Please submit your name.");
		view.getBoardPanel().setEnabled(false);
		view.getSubmitButton().setEnabled(true);
		view.getInputField().setEnabled(true);

		view.getBoardPanel().clearBoard();
	}

	public void disconnect() {
		out.println("disconnect");
	}

	public void start() {
		try {
			this.socket = new Socket("127.0.0.1", 12396);
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
				view.getInfoLabel().setText("Hi, "+ name + " .Waiting for another player to join...");
				view.getSubmitButton().setEnabled(false);
				view.getInputField().setEnabled(false);
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

		newTurn();
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
				while (in.hasNextLine()) {
					message = in.nextLine().split(" ");
					out.flush();

                    // match message[0]
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
							view.showMessage("You win!");
							out.println("newGame");
						} else {
							view.getInfoLabel().setText("You lose!");
							view.showMessage("You lose!");
							out.println("newGame");
                        }
                        view.getBoardPanel().setEnabled(false);
						newTurn();
                    } else if (message[0].equals("tie")) {
                        view.getInfoLabel().setText("Tie!");
						view.showMessage("Tie!");
						out.println("newGame");
						view.getBoardPanel().setEnabled(false);
						newTurn();
                    } else if (message[0].equals("otherDisconnect")) {
						view.getInfoLabel().setText("The other player has disconnected.");
						view.showMessage("The other player has disconnected.");
						out.println("newGame");
						view.getBoardPanel().setEnabled(false);
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
