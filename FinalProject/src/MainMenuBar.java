import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class MainMenuBar {

	public MainMenuBar() {}
	
	public JMenuBar createMenuBar() {		
		// Create the main menu bar.
		JMenuBar menuBar = new JMenuBar();

		// Create menu 'Options'. 
		JMenu menu_options = new JMenu("Options");
		// Create the items of menu 'Options'.
		JMenuItem menuItem_logIn = new JMenuItem("Log in");
		JMenuItem menuItem_exit = new JMenuItem("Exit");
		
		menu_options.add(menuItem_logIn);
		menu_options.add(menuItem_exit);
		
		// Add actionListeners to the menu items.
		menuItem_logIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Für LoginView
				LoginView loginView = new LoginView();
			}
		});
		
		menuItem_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO's: Wir muessen noch das Connection-Objekt terminieren
				System.exit(0);				
			}
			
		});
		
		// Create menu 'About'.
		JMenu menu_about = new JMenu("About");
		
		// Add all created menues to menu bar.		
		menuBar.add(menu_options);
		menuBar.add(menu_about);
		
		// create an opaque box for menubar
		menuBar.add(Box.createRigidArea(new Dimension(100,30)));
		
		return menuBar;
	}
}
