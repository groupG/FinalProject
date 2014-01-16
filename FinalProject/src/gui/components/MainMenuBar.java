package gui.components;

import gui.Login;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import model.Configuration;


public class MainMenuBar extends JMenuBar implements Configuration{

	private static final long serialVersionUID = -6476913133727030324L;
	protected HashMap<String, Component> componentMap;

	public MainMenuBar(String name) {
		this.componentMap = new HashMap<String, Component>();

		// Create menu 'Options'.
		JMenu menu_options = new JMenu(MENU_OPTIONS);
		// Create the items of menu 'Options'.
		JMenuItem menuItem_logOut = new JMenuItem(ITEM_LOGOUT);
		menuItem_logOut.setName(COMPONENT_ITEM_MENU_LOGOUT);
		JMenuItem menuItem_exit = new JMenuItem(ITEM_EXIT);
		menuItem_logOut.setName(COMPONENT_ITEM_MENU_EXIT);

		menu_options.add(menuItem_logOut);
		menu_options.add(menuItem_exit);

		// Add actionListeners to the menu items.
		menuItem_logOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Fuer LoginView
				Login loginView = new Login();
				loginView.pack();
				loginView.setLocationRelativeTo(null);
				loginView.setVisible(true);
			}
		});

		// Create menu 'Hilfe'.
		JMenu menu_about = new JMenu(MENU_ABOUT);
		JMenuItem menuItem_hilfe = new JMenuItem(ITEM_INFO);
		menuItem_hilfe.setName(COMPONENT_ITEM_MENU_INFO);
		menu_about.add(menuItem_hilfe);


		// set name
		this.setName(name);

		// Add all created menues to menu bar.
		this.add(menu_options);
		this.add(menu_about);

		// create an opaque box for menubar
		this.add(Box.createRigidArea(new Dimension(100,30)));

		// adds all components to a map
		createComponentMap(this);
	}

	public void addActionListeners(Component component, ActionListener ae){
		((AbstractButton) component).addActionListener(ae);
	}

	public void createComponentMap(Component component)
	{
		this.componentMap.put(component.getName(), component);
		if (component instanceof Container)
		{
			if (((Container) component).getComponentCount() > 0){
				for (Component child : ((Container) component).getComponents()){
					createComponentMap(child);
				}
			}
		}
	}

	public Component getComponentByName(String name) {
		if (this.componentMap.containsKey(name)) {
			return (Component) this.componentMap.get(name);
		} else
			return null;
	}

	public HashMap<String, Component> getComponentMap(){
	    return this.componentMap;
    }
}
