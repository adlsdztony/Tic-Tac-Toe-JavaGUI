package server;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import game.TicTacToe;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A multithreaded Tic Tac Toe server. When a client connects the server
 * requests
 * a player type (X or O) and then connects them to a game with another player.
 * The game is played on a 3x3 board where players take turns placing their mark
 * (X or O) on the board. The first player to get 3 in a row wins.
 *
 * @author Zhou Zilong
 * @since 2023-11-27
 */
public class Server {
	private ServerSocket serverSocket;
	private TicTacToe game;
	private int playerState = 0;
	private int currentPlayer = 0;

	// The set of all the print writers for all the clients, used for broadcast.
	private Set<PrintWriter> writers = new HashSet<>();

	/**
	 * Runs the application. Pairs up clients that connect.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Server is Running...");
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				System.out.println("Server Stopped.");
			}
		}));

		try (ServerSocket listener = new ServerSocket(12396)) {
			Server myServer = new Server(listener);
			myServer.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Constructs a Server object.
	 * 
	 * @param serverSocket
	 */
	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		this.game = new TicTacToe();
	}

	/**
	 * Runs the server.
	 */
	public void start() {
		ExecutorService pool = Executors.newFixedThreadPool(200);
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				if (playerState < 3) {
					if (playerState == 0 || playerState == 2) {
						pool.execute(new Handler(socket, 'x'));
						System.out.println("Connected to client X");
						playerState++;
					} else {
						pool.execute(new Handler(socket, 'o'));
						System.out.println("Connected to client O");
						playerState += 2;
					}
				} else {
					PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
					output.println("full");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * A handler thread class. Handlers are spawned from the listening loop and
	 * are responsible for a dealing with a single client and broadcasting its
	 * messages.
	 */
	public class Handler implements Runnable {
		private Socket socket;
		private Scanner input;
		private PrintWriter output;
		private char player;

		/**
		 * Constructs a handler thread, squirreling away the socket. All the
		 * interesting work is done in the run method.
		 * 
		 * @param socket
		 * @param player
		 */
		public Handler(Socket socket, char player) {
			this.socket = socket;
			this.player = player;
		}

		/**
		 * Handles a new turn.
		 * 
		 * @return true if the client disconnects
		 */
		public boolean newTurn() {
			game.newGame();
			while (input.hasNextLine()) {
				String[] message = input.nextLine().split(" ");

				// System.out.println("Server Received: " + message[0]);

				if (message[0].equals("newGame")) {
					currentPlayer -= 1;
					break;
				} else if (message[0].equals("move")) {
					if (game.getCurrentPlayer() != player) {
						output.println("wait");
						continue;
					}
					int x = Integer.parseInt(message[1]);
					int y = Integer.parseInt(message[2]);
					char mark = player;
					if (game.makeMove(x, y)) {
						for (PrintWriter writer : writers) {
							writer.println("move " + x + " " + y + " " + mark);
						}
						if (game.checkForWin()) {
							for (PrintWriter writer : writers) {
								writer.println("win " + game.getCurrentPlayer());
							}
						} else if (game.isBoardFull()) {
							for (PrintWriter writer : writers) {
								writer.println("tie");
							}
						}
						game.changePlayer();
					} else {
						output.println("invalid");
					}
				} else if (message[0].equals("disconnect")) {
					return true;
				}

			}
			return false;
		}

		/**
		 * Services this thread's client by repeatedly requesting a screen name
		 * until a unique one has been submitted, then acknowledges the name and
		 * registers the output stream for the client in a global set, then
		 * repeatedly gets inputs and broadcasts them.
		 */
		@Override
		public void run() {
			System.out.println("Connected: " + socket);

			try {
				input = new Scanner(socket.getInputStream());
				output = new PrintWriter(socket.getOutputStream(), true);

				output.println("connected");

				// add this client to the broadcast list
				writers.add(output);

				while (input.hasNextLine()) {
					String[] message = input.nextLine().split(" ");
					// System.out.println("Server Received: " + message[0]);

					if (message[0].equals("start")) {
						// System.out.println("Server Sent: start " + player);
						currentPlayer += 1;
						while (currentPlayer < 2) {
							Thread.sleep(100);
						}
						output.println("start " + player);

						if (newTurn()) {
							break;
						}
						;
					} else if (message[0].equals("disconnect")) {
						break;
					}

				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				// client disconnected
				if (output != null) {
					writers.remove(output);
				}
				playerState -= player == 'x' ? 1 : 2;
				currentPlayer -= 1;
				for (PrintWriter writer : writers) {
					writer.println("otherDisconnect");
				}
				System.out.println("Disconnected: " + player);

			}
		}
	}

}
