import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Klasse zum Starten der Anwendung. Initialisert einen LoginDialog, ueber
 * welchen man zur Hauptanwendung gelangen kann.
 *
 * @author borecki
 *
 */
public class Main implements ConfigImpl {
	public static void main(String[] args) throws Exception {
		/*LoginView login = new LoginView();
		login.pack();
		login.setVisible(true);
		login.setLocationRelativeTo(null);*/

		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				//UIManager.put("swing.boldMetal", Boolean.FALSE);
				buildAndDisplayGUI();
			}
		});
	}

	public static void buildAndDisplayGUI(){
		JFrame gui = new JFrame(TITLE);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.add(new MainView(), BorderLayout.CENTER);
		gui.setSize(800,600);
		gui.pack();
		gui.setVisible(true);
		gui.setLocationRelativeTo(null);
	}
}