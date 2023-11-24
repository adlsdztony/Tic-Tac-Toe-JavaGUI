package ass4;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket serverSocket;
	private TicTacToe game;
	private int playerState = 0;
	int currentPlayer = 0;
	

	// The set of all the print writers for all the clients, used for broadcast.
	private Set<PrintWriter> writers = new HashSet<>();

	public static void main(String[] args) {
		System.out.println("Server is Running...");
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				System.out.println("Server Stopped.");
			}
		}));

		try (var listener = new ServerSocket(12396)) {
			Server myServer = new Server(listener);
			myServer.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		this.game = new TicTacToe();
	}

	public void start() {
		var pool = Executors.newFixedThreadPool(200);
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				if (playerState < 3) {
					if (playerState == 0) {
						pool.execute(new Handler(socket, 'x'));
						System.out.println("Connected to client X");
						playerState++;
					} else if (playerState == 1) {
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

	public class Handler implements Runnable {
		private Socket socket;
		private Scanner input;
		private PrintWriter output;
		private char player;

		public Handler(Socket socket, char player) {
			this.socket = socket;
			this.player = player;
		}

		public boolean newTurn() {
			game.newGame();
			while (input.hasNextLine()) {
				String[] message = input.nextLine().split(" ");

				System.out.println("Server Received: " + message[0]);

				// if (playerState < 3) {
				// 	output.println("otherDisconnect");
				// 	return false;
				// }

				if (message[0].equals("newGame")) {
					currentPlayer = 0;
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
							break;
						} else if (game.isBoardFull()) {
							for (PrintWriter writer : writers) {
								writer.println("tie");
							}
							break;
						}
						game.changePlayer();
						// // notify other player
						// for (PrintWriter writer : writers) {
						// writer.println("playerSwitch " + game.getCurrentPlayer());
						// }
					} else if (message[0].equals("disconnect")) {
						return true;
					} else {
						output.println("invalid");
					}
				}
				
			}
			return false;
		}

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
					System.out.println("Server Received: " + message[0]);
					
					if (message[0].equals("start")) {
						System.out.println("Server Sent: start " + player);
						currentPlayer += 1;
						while (currentPlayer < 2) {
							Thread.sleep(100);
						}
						output.println("start " + player);

						if(newTurn()){
							break;
						};
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
				for (PrintWriter writer : writers) {
					writer.println("otherDisconnect");
				}
			}
		}
	}

}
