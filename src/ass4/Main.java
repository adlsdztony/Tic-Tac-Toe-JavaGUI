package ass4;

import java.net.ServerSocket;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		String ip;
		String mode;
		if (args.length == 0) {
			ip = "localhost";
			mode = "client";
		} else if (args.length == 1) {
			if (args[0].equals("server")) {
				ip = "localhost";
				mode = "server";
			} else {
				ip = args[0];
				mode = "client";
			}
		} else {
			ip = args[1];
			mode = args[0];
		}
		if (mode.equals("client")) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					View view = new View();
					Controller controller = new Controller(view, ip);
					controller.start();
				}
			});
		} else if (mode.equals("server")) {
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
	}
}