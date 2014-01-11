package controller;

import gui.Client;
import gui.components.Transactions;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.rowset.CachedRowSet;
import javax.swing.JButton;
import javax.swing.JComboBox;

import model.Configuration;
import model.DB;

/**
 *
 * @author borecki
 *
 */
public class MainController implements Configuration{
	private DB db;
	private Client client;

	public MainController(DB db, Client client) {
		this.db = db;
		this.client = client;
		addListeners(this.client.getTransactions());
	}

	void addListeners(Component component){
		Iterator<Entry<String, Component>> it = ((Transactions) component).getComponentMap().entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, Component> pairs = (Map.Entry<String, Component>)it.next();
			System.out.println(pairs.getKey());
			if (pairs.getValue() instanceof JButton){
				System.out.println("Button registriert: " + pairs.getKey());
				((JButton) pairs.getValue()).addActionListener(new ActionEventListener());
			}
			else if (pairs.getValue() instanceof JComboBox){
				System.out.println("ComboBox registriert: " + pairs.getKey());
				((JComboBox<?>) pairs.getValue()).addItemListener(new ItemEventListener());
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

	class ActionEventListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			System.out.println("ActionEvent: " + ae);
			if (ae.getActionCommand() == COMPONENT_BUTTON_KUNDENPFLEGE_NEU){

			}
		}
	}

	class ItemEventListener implements ItemListener{
	    @Override
	    public void itemStateChanged(ItemEvent ie) {
	    	  CardLayout cl = (CardLayout) ((Container) client.getTransactions().getComponentByName(COMPONENT_PANEL_KUNDENPFLEGE)).getLayout();
	    	  cl.show(((Container) client.getTransactions().getComponentByName(COMPONENT_PANEL_KUNDENPFLEGE)), (String) ie.getItem());
	          //client.showException(new Exception((String) ie.getItem()));
	          System.out.println("ItemEvent: " + ie);
	    }
	}
}


