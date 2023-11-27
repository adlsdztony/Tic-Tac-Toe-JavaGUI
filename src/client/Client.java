package client;

import javax.swing.SwingUtilities;

/**
 * Client class
 * main function of the client
 * 
 * @author Zhou Zilong
 * @version 2023-11-27
 */
public class Client {

	/**
	 * main function
	 * take the ip address as the argument
	 * 
	 * @param args the ip address of the server
	 */
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