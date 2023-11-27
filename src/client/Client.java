package client;

import javax.swing.SwingUtilities;


public class Client {
	public static void main(String[] args) {
		String ip;
		if (args.length != 0) {
			ip = args[0];
		} else {
			ip = "localhost";
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				View view = new View();
				Controller controller = new Controller(view, ip);
				controller.start();
			}
		});
	}
}