package gui;

import gui.components.Auswertung;
import gui.components.Bestellpositionen;
import gui.components.DBOutput;
import gui.components.Explorer;
import gui.components.MainMenuBar;
import gui.components.Transaktionen;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import model.Configuration;
import model.DB;

public class Client extends JFrame implements Configuration {

	private static final long serialVersionUID = 6212400999089087362L;
	protected HashMap<String, Component> componentMap;

	private DB db;
	private Explorer explorer;
	private DBOutput dboutput;
	private Transaktionen transaktionen;
	private Auswertung auswertung;
	private MainMenuBar menu;

	public Client(DB db, int width, int height) {

		setTitle(TITLE);
		setSize(new Dimension(width, height));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(true);
		this.menu = new MainMenuBar(COMPONENT_MENU);
		setJMenuBar(this.menu);

		this.db = db;
		this.getContentPane().add(initComponents(width, height));
		this.componentMap = new HashMap<String, Component>();
		createComponentMap(this.getContentPane());
		pack();
		setVisible(true);
		toFront();
		setLocationRelativeTo(null);
	}

	public Component initComponents(int width, int height){
		try {
			this.explorer = new Explorer(this.db, TABLE_OWNER);
			this.dboutput = new DBOutput(this.db,"Select table_name from all_tables where owner = '"+TABLE_OWNER+"'");
			this.transaktionen = new Transaktionen();
			this.auswertung = new Auswertung();
		} catch (SQLException e) {
			showException(e);
		}
		// top SplitPane
		this.explorer.setPreferredSize(new Dimension((int) Math.round(width * 0.2), (int) Math.round(height * 0.4)));
		this.dboutput.setPreferredSize(new Dimension((int) Math.round(width * 0.8), (int) Math.round(height * 0.4)));
		JSplitPane topSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.explorer, this.dboutput);
		topSplitPane.setSize(new Dimension(width, (int) Math.round(height * 0.3)));
		topSplitPane.setOneTouchExpandable(true);
		topSplitPane.setDividerLocation((int) Math.round(width * 0.2));

		// bottom SplitPane
		this.transaktionen.setPreferredSize(new Dimension((int) Math.round(width * 0.5), (int) Math.round(height * 0.7)));
		this.auswertung.setPreferredSize(new Dimension((int) Math.round(width * 0.5), (int) Math.round(height * 0.7)));
		JSplitPane bottomSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.transaktionen, this.auswertung);
		bottomSplitPane.setPreferredSize(new Dimension(width, (int) Math.round(height * 0.8)));
		bottomSplitPane.setOneTouchExpandable(true);
		bottomSplitPane.setDividerLocation((int) Math.round(width * 0.6));

		// main SplitPane
		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topSplitPane, bottomSplitPane);
		mainSplitPane.setPreferredSize(new Dimension(width, height));
		mainSplitPane.setOneTouchExpandable(true);
		mainSplitPane.setDividerLocation((int)Math.round(height * 0.42));

		return mainSplitPane;
	}

	public void showException(Exception e) {
		JOptionPane.showMessageDialog(this.getContentPane(), new String[] {
				e.getClass().getName() + ": ", e.getMessage() });
	}

	public void showDialog(Object obj){
		JOptionPane.showMessageDialog(this.getContentPane(), obj);
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

	public Explorer getExplorer() {
		return this.explorer;
	}

	public void setExplorer(Explorer explorer) {
		this.explorer = explorer;
	}

	public DBOutput getDBOutput() {
		return this.dboutput;
	}

	public void setDBOutput(DBOutput dboutput) {
		this.dboutput = dboutput;
	}

	public Transaktionen getTransaktionen(){
		return this.transaktionen;
	}

	public void setTransaktionen(Transaktionen transaktionen){
		this.transaktionen = transaktionen;
	}

	public Auswertung getAuswertung(){
		return this.auswertung;
	}

	public void setAuswertung(Auswertung auswertung){
		this.auswertung = auswertung;
	}

	public MainMenuBar getMenu(){
		return this.menu;
	}
}
