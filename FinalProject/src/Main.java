import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

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
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
						if ("Nimbus".equals(info.getName())){
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
					buildAndDisplayGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void buildAndDisplayGUI() throws Exception{
		JFrame gui = new JFrame(TITLE);
		Controller controller = new Controller(DRIVER, CONNECTION_THIN+"dbprakt", "", "");
		//controller.showTables();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setJMenuBar(new MainMenuBar().createMenuBar());
		gui.add(new DataTree(controller, "ALTDATEN").getSplitPane(), BorderLayout.CENTER);
		gui.add(new MainView(controller), BorderLayout.SOUTH);
		gui.setSize(new Dimension(1024,600));
		gui.pack();
		gui.setVisible(true);
		gui.setLocationRelativeTo(null);
	}
}