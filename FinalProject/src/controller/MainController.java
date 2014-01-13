package controller;

import gui.Client;
import gui.components.Explorer;
import gui.components.DBOutput;
import gui.components.GridBagTemplate;
import gui.components.Transaktionen;
import gui.components.Auswertung;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.sql.rowset.CachedRowSet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import model.Configuration;
import model.DB;
import model.OutputTableModel;

/**
 *
 * @author borecki, dang
 *
 */
public class MainController implements Configuration{
	private DB db;
	private Client client;
	protected HashMap<String, Component> componentMap;

	public MainController(DB db, Client client) {
		this.db = db;
		this.client = client;
		this.componentMap = new HashMap<String, Component>();
		addTransaktionenListeners(this.client.getTransaktionen());
		addExplorerListeners(this.client.getExplorer());
	}

	void addTransaktionenListeners(Component component){
		Iterator<Entry<String, Component>> it = ((Transaktionen) component).getComponentMap().entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, Component> pairs = (Map.Entry<String, Component>)it.next();
//			System.out.println(pairs.getKey());
			if (pairs.getValue() instanceof JButton){
//				System.out.println("Button registriert: " + pairs.getKey());
				this.client.getTransaktionen().addActionListeners(pairs.getValue(), new ActionEventListener());
			}
			else if (pairs.getValue() instanceof JComboBox){
//				System.out.println("ComboBox registriert: " + pairs.getKey());
				this.client.getTransaktionen().addItemListeners(pairs.getValue(), new ItemEventListener());
			}
		}
	}

	void addExplorerListeners(Component component){
		Iterator<Entry<String, Component>> it = ((Explorer) component).getComponentMap().entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, Component> pairs = (Map.Entry<String, Component>)it.next();
			System.out.println(pairs.getKey());
			if (pairs.getValue() instanceof JTree){
				System.out.println("Tree registriert: " + pairs.getKey());
				this.client.getExplorer().addTreeSelectionListeners(pairs.getValue(), new TreeEventListener());
			}
		}
	}

	public List<String> getTables(String owner){
		List<String> tables = new ArrayList<String>();
		try {
			tables =  this.db.getTables(owner);
		} catch (SQLException e) {
			this.client.showException(e);
		}
		return tables;
	}

	public List<String> getColumns(String owner, String table){
		List<String> columns = new ArrayList<String>();
		try {
			columns =  this.db.getColumns(owner, table);
		} catch (SQLException e) {
			this.client.showException(e);
		}
		return columns;
	}

	public CachedRowSet getContentsOfOutputTable(String query){
		return this.db.getContentsOfOutputTable(query);
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

	class ActionEventListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
//			System.out.println("ActionEvent: " + ae);
//			if (ae.getActionCommand() == COMPONENT_BUTTON_KUNDENPFLEGE_NEU_AUSFUEHREN){
//				System.out.println(client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_NAME));
//				System.out.println(((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_NAME)).getText());
//				System.out.println(((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_ADRESSE)).getText());
//				System.out.println(((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_TEL)).getText());
//				System.out.println(((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KONTO)).getText());
//				System.out.println(((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_NATION)).getSelectedItem().toString());
//				System.out.println(((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE)).getSelectedItem().toString());
//				System.out.println(((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KID)).getText());
//			}

			// KUNDENPFLEGE - NEUEN KUNDEN ANLEGEN - AUSFUEHREN BUTTON
			if ( ae.getActionCommand() == COMPONENT_BUTTON_KUNDENPFLEGE_NEU_AUSFUEHREN ) {
				String inputKID = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KID)).getText();
				if ( !isValidKID(inputKID) )
					return;

				int kID = Integer.parseInt(inputKID);
				if ( kID == db.getBufferedKundenID() )
					db.needNextKundenID(true); // DB darf wieder naechsten KID liefern.

				String kName = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_NAME)).getText();
				String kAdresse = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_ADRESSE)).getText();
				String kTelNr = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_TEL)).getText();
				String kBranche = ((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE)).getSelectedItem().toString();
				String kNation = ((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_NATION)).getSelectedItem().toString();

				try {
					db.insertKunde(kID, kName, kAdresse, kTelNr, kBranche, kNation);
					JOptionPane.showMessageDialog(client, "<html>Neuer Kunde mit Kunden-ID " + kID + " wurde erstellt.</html>");
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(client, e.getClass().getName() + " : " + e.getMessage());
					e.printStackTrace();
				}
			}

			// KUNDENPFLEGE - KUDNEN AENDERN - SUCHEN BUTTON
			if ( ae.getActionCommand() == COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_SUCHEN ) {
				String inputKID = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KID)).getText();
				// Check if the given KID is valid.
				if ( !isValidKID(inputKID) )
					return;
				// Check if the given KID really exists in the database.
				try {
					if ( db.checkIfElementExists("KUNDE2", "kid", inputKID) ) {
						System.out.println("YESSSSSSSSSSSSSSSSSSSSSSSSSSSS");
					}
					else {
						System.out.println("NOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (ae.getActionCommand().equals(COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_BSTPOSHINZUFUEGEN)){
				client.showDialog(new GridBagTemplate(9,null,null));
			}
		}

		private boolean isValidKID(String inputKID) {
			boolean result = Pattern.matches("\\d*", inputKID);  // nur positive nummerische Werte.
			if ( !result ) {
				JOptionPane.showMessageDialog(client, "<html>Ung&uuml;ltige Eingabe. Kunden-KID darf nur positive numerische Werte haben, z.B. 2, 6, 89, 432 ...</html>");
			}
			return result;
		}
	}

	class ItemEventListener implements ItemListener{
	    @Override
	    public void itemStateChanged(ItemEvent ie) {

	    	System.out.println(COMPONENT_COMBO_KUNDENPFLEGE_ACTIONS);
	    	System.out.println(ie.getSource());
	    	if (ie.getStateChange() == ItemEvent.SELECTED && ((Component) ie.getSource()).getName().equals(COMPONENT_COMBO_KUNDENPFLEGE_ACTIONS)){

	    		CardLayout cl = (CardLayout) ((Container) client.getTransaktionen().getComponentByName(COMPONENT_PANEL_KUNDENPFLEGE)).getLayout();
	    		cl.show(((Container) client.getTransaktionen().getComponentByName(COMPONENT_PANEL_KUNDENPFLEGE)), (String) ie.getItem());
	    		System.out.println((int) ((JComboBox<?>) ie.getSource()).getSelectedIndex());

	    		if ((int) ((JComboBox<?>) ie.getSource()).getSelectedIndex() == 1)
	    		{
	    			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KID)).setText(""+db.getKundenID());
	    			client.repaint();

	    		}
	    		System.out.println("ItemEvent: " + ie);
	    	}
	    	else if (ie.getStateChange() == ItemEvent.SELECTED && ((Component) ie.getSource()).getName().equals(COMPONENT_COMBO_PRODUKTVERWALTUNG_ACTIONS)){
	    		CardLayout cl = (CardLayout) ((Container) client.getTransaktionen().getComponentByName(COMPONENT_PANEL_PRODUKTVERWALTUNG)).getLayout();
	    		cl.show(((Container) client.getTransaktionen().getComponentByName(COMPONENT_PANEL_PRODUKTVERWALTUNG)), (String) ie.getItem());
	    		System.out.println((int) ((JComboBox<?>) ie.getSource()).getSelectedIndex());

	    		if ((int) ((JComboBox<?>) ie.getSource()).getSelectedIndex() == 1)
	    		{
	    			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_NAME)).setText("blubb");
	    			client.repaint();
	    		}
	    	}
	    	else if (ie.getStateChange() == ItemEvent.SELECTED && ((Component) ie.getSource()).getName().equals(COMPONENT_COMBO_BESTELLVERWALTUNG_ACTIONS)){
	    		CardLayout cl = (CardLayout) ((Container) client.getTransaktionen().getComponentByName(COMPONENT_PANEL_BESTELLVERWALTUNG)).getLayout();
	    		cl.show(((Container) client.getTransaktionen().getComponentByName(COMPONENT_PANEL_BESTELLVERWALTUNG)), (String) ie.getItem());
	    		System.out.println((int) ((JComboBox<?>) ie.getSource()).getSelectedIndex());

	    		if ((int) ((JComboBox<?>) ie.getSource()).getSelectedIndex() == 1)
	    		{
	    			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_NAME)).setText("blubb");
	    			client.repaint();
	    		}
	    	}

	    }
	}

	class TreeEventListener implements TreeSelectionListener{

		@Override
		public void valueChanged(TreeSelectionEvent te) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) client.getExplorer().getTree().getLastSelectedPathComponent();
			System.out.println(selectedNode.getUserObject().toString());
			if (selectedNode.getChildCount() > 0){
				System.out.println("if: " + selectedNode.getUserObject().toString());
				OutputTableModel tableModel = null;
				try {
				    tableModel = (OutputTableModel) client.getDBOutput().populateTable("SELECT * FROM " +TABLE_OWNER+ "." + selectedNode.getUserObject().toString() + " WHERE ROWNUM <= 10000");
				} catch (SQLException e) {
					client.showException(e);
				}
				client.getDBOutput().removeScrollPane();
				client.getDBOutput().addTableModel(tableModel);
				client.getDBOutput().addTableToPane();
				client.revalidate();
				client.repaint();
			}
		}

	}
}


