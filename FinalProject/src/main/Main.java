package main;

import gui.Client;
import gui.Login;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import model.Configuration;
import model.DB;
import controller.MainController;

/**
 * Klasse zum Starten des Clients. Initialisert einen LoginDialog, ueber welchen
 * man zur Hauptanwendung gelangen kann.
 *
 * @author borecki
 *
 */
public class Main implements Configuration {
	public static void main(String[] args) throws Exception {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					for (LookAndFeelInfo info : UIManager
							.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
					createAndBuildGUI();
//					createAndBuildLoginGui();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void createAndBuildGUI() throws Exception {
		DB db = new DB(DRIVER, CONNECTION_THIN_LOCALHOST + "dbprakt", "projekt_2013_g",
				"dbprakt");
		Client client = new Client(db,CLIENT_WIDTH, CLIENT_HEIGHT);
		MainController controller = new MainController(db, client);
		controller.initListeners();
	}

	public static void createAndBuildLoginGui() {
		Login login = new Login();
		login.setResizable(false);
		login.pack();
		login.setVisible(true);
		login.setLocationRelativeTo(null);
	}
}
