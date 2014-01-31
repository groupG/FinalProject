package main;

import gui.Login;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import model.Configuration;

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
					createAndBuildLoginGui();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void createAndBuildLoginGui() {
		Login login = new Login();
		login.setResizable(false);
		login.pack();
		login.setVisible(true);
		login.setLocationRelativeTo(null);
	}
}
