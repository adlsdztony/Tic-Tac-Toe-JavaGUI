package ass4;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
		String ip;
		if (args.length == 0) {
			ip = "localhost";
		} else {
			ip = args[0];
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