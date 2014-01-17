package controller;

import gui.Client;
import gui.components.Explorer;
import gui.components.Transaktionen;
import gui.components.Auswertung;
import gui.components.MainMenuBar;
import gui.components.Bestellpositionen;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.Date;

import javax.sql.rowset.CachedRowSet;
import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import main.Main;
import model.Configuration;
import model.DB;
import model.OutputTableModel;
import utils.NotExistInDatabaseException;

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
		addAuswertungenListeneres(this.client.getAuswertung());
		addMenuListeners(this.client.getMenu());
	}

	void addMenuListeners(Component component){
		Iterator<Entry<String, Component>> it = ((MainMenuBar) component).getComponentMap().entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, Component> pairs = (Map.Entry<String, Component>)it.next();
			if (pairs.getValue() instanceof JMenuItem){
				this.client.getMenu().addActionListeners(pairs.getValue(), new ActionEventListener());
			}
		}
	}

	void addTransaktionenListeners(Component component){
		Iterator<Entry<String, Component>> it = ((Transaktionen) component).getComponentMap().entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, Component> pairs = (Map.Entry<String, Component>)it.next();
			if (pairs.getValue() instanceof JButton){
				this.client.getTransaktionen().addActionListeners(pairs.getValue(), new ActionEventListener());
			}
			else if (pairs.getValue() instanceof JComboBox){
				this.client.getTransaktionen().addItemListeners(pairs.getValue(), new ItemEventListener());
			}
		}
	}

	void addAuswertungenListeneres(Component component){
		Iterator<Entry<String, Component>> it = ((Auswertung) component).getComponentMap().entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, Component> pairs = (Map.Entry<String, Component>)it.next();
			if (pairs.getValue() instanceof JButton){
				this.client.getAuswertung().addActionListeners(pairs.getValue(), new ActionEventListener());
			}
		}
	}

	void addExplorerListeners(Component component){
		Iterator<Entry<String, Component>> it = ((Explorer) component).getComponentMap().entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, Component> pairs = (Map.Entry<String, Component>)it.next();
			if (pairs.getValue() instanceof JTree){
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

			//-----------------  KUNDENPFLEGE - NEUEN KUNDEN ANLEGEN - AUSFUEHREN BUTTON
			if ( ae.getActionCommand() == COMPONENT_BUTTON_KUNDENPFLEGE_NEU_AUSFUEHREN ) {
				String kID = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KID)).getText();
				if ( !isValidKID(kID) )
					return;

				try {
					if ( db.checkIfElementExists(TABLE_KUNDE, "kid", kID) ) {
						JOptionPane.showMessageDialog(client, "<html>Der Kunde mit der Kunden-ID " + kID + " ist bereits vorhanden.</html>");
						return;
					}
				} catch (HeadlessException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				if ( Integer.parseInt(kID) == db.getBufferedKundenID() )
					db.needNextKundenID(true); // DB darf wieder naechsten KID liefern.

				String kName = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_NAME)).getText();
				String kAdresse = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_ADRESSE)).getText();
				String kTelNr = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_TEL)).getText();
				String kBranche = ((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE)).getSelectedItem().toString();
				String kNation = ((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_NATION)).getSelectedItem().toString();

				// Check, if the textfields are empty and have to be filled.
				String[] kundenDaten = { kName, kAdresse, kTelNr, kBranche, kNation };
				for ( String datum : kundenDaten ) {
					if ( datum.replaceAll("\\s+", "").isEmpty() ) {
						JOptionPane.showMessageDialog(client, KUNDENPFLEGE_MESSAGE_FILL_ALL_FIELDS);
						return;
					}
				}

				// Trying to inset new customer.
				try {
					db.insertKunde(kID, kName, kAdresse, kTelNr, kBranche, kNation);
					JOptionPane.showMessageDialog(client, "<html>Neuer Kunde mit Kunden-ID " + kID + " wurde erstellt. </html>");
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(client, e.getClass().getName() + " : " + e.getMessage());
					e.printStackTrace();
				}
				this.clearInputComponentsOfKundeNeuPflege();
			}

			//----------------- BESTELLVERWALTUNG - NEUE BESTELLUNG - POS HINZUFUEGEN BUTTON
			if (ae.getActionCommand() == "addNeu"){
				String pos = (String) ((JTextField) client.getComponentByName("inpNeu")).getText();
				if (!pos.contains(";")){
					JOptionPane.showMessageDialog(client, "<html>Bitte achten Sie auf die korrekte Trennung der einzelnen Felder durch ein ';'-Zeichen. </html>");
					return;
				}

				if (!pos.contains(";")){
					JOptionPane.showMessageDialog(client, "<html>Bitte achten Sie auf die korrekte Trennung der einzelnen Felder durch ein ';'-Zeichen. </html>");
					return;
				}

				pos.split("\\;");
				int elementCount = pos.split("\\;").length;
				if (elementCount <= 3 ){
					String posPid = pos.split("\\;")[0];
					String posMenge = pos.split("\\;")[1];
					try {
						Double totalPrice = db.calcTotalPrice(posPid, Integer.parseInt(posMenge));
						JOptionPane.showMessageDialog(client, "<html>Gesamtpreis der Position: "+totalPrice.toString() +" €</html>");
					} catch (NotExistInDatabaseException e) {
						client.showException(e);
					} catch (SQLException e1){
						client.showException(e1);
					} catch (NumberFormatException e2){
						client.showException(e2);
					}
				} else if (elementCount > 3) {
					JOptionPane.showMessageDialog(client, "<html>Bitte geben Sie nur Produkt-ID, Menge und den Positionstext an und achten Sie darauf, dass Sie ';' nur zum Trennen der Werte verwenden.</html>");
					return;
				} else {
					JOptionPane.showMessageDialog(client, "<html>Bitte geben Sie zumindest die Produkt-ID und Menge an. </html>");
					return;
				}
			}

			//----------------- BESTELLVERWALTUNG - BESTELLUNG AENDERN - POS HINZUFUEGEN BUTTON
			if (ae.getActionCommand() == "addEdit"){
				client.getTransaktionen().getPosEdit().invalidate();
				String pos = (String) ((JTextField) client.getComponentByName("inpEdit")).getText();
				if (!pos.contains(";")){
					JOptionPane.showMessageDialog(client, "<html>Bitte achten Sie auf die korrekte Trennung der einzelnen Felder durch ein ';'-Zeichen. </html>");
					return;
				}

				if (!pos.contains(";")){
					JOptionPane.showMessageDialog(client, "<html>Bitte achten Sie auf die korrekte Trennung der einzelnen Felder durch ein ';'-Zeichen. </html>");
					return;
				}

				pos.split("\\;");
				int elementCount = pos.split("\\;").length;

				Double totalPrice = null;
				if (elementCount <= 3 ){
					String posPid = pos.split("\\;")[0];
					String posMenge = pos.split("\\;")[1];
					try {
						totalPrice = db.calcTotalPrice(posPid, Integer.parseInt(posMenge));
						JOptionPane.showMessageDialog(client, "<html>Gesamtpreis der Position: "+totalPrice.toString() +" €</html>");
					} catch (NotExistInDatabaseException e) {
						client.showException(e);
					} catch (SQLException e1){
						client.showException(e1);
					} catch (NumberFormatException e2){
						client.showException(e2);
					}
				} else if (elementCount > 3) {
					JOptionPane.showMessageDialog(client, "<html>Bitte geben Sie nur Produkt-ID, Menge und den Positionstext an und achten Sie darauf, dass Sie ';' nur zum Trennen der Werte verwenden.</html>");
					return;
				} else {
					JOptionPane.showMessageDialog(client, "<html>Bitte geben Sie zumindest die Produkt-ID und Menge an. </html>");
					return;
				}

				List<String> tooltips = client.getTransaktionen().getPosEdit().getToolTips();
				tooltips.add("Gesamtpreis der Position: "+ totalPrice.toString() +" €");
				client.getTransaktionen().getPosEdit().addToolTips(tooltips);
				client.getTransaktionen().getPosEdit().addListener();
				client.revalidate();
				client.repaint();
			}

			//----------------- BESTELLVERWALTUNG - NEUE BESTELLUNG - POS ENTFERNEN BUTTON
			if (ae.getActionCommand() == "delNEU"){
				client.getTransaktionen().getPosEdit().invalidate();

				List<String> tooltips = client.getTransaktionen().getPosNeu().getToolTips();
				tooltips.remove(client.getTransaktionen().getPosNeu().getList().getSelectedIndex());
				client.getTransaktionen().getPosEdit().addToolTips(tooltips);
				client.getTransaktionen().getPosEdit().addListener();
				client.revalidate();
				client.repaint();
			}

			//----------------- BESTELLVERWALTUNG - BESTELLUNG AENDERN - POS ENTFERNEN BUTTON
			if (ae.getActionCommand() == "delEdit"){
				client.getTransaktionen().getPosEdit().invalidate();

				List<String> tooltips = client.getTransaktionen().getPosEdit().getToolTips();
				tooltips.remove(client.getTransaktionen().getPosEdit().getList().getSelectedIndex());
				client.getTransaktionen().getPosEdit().addToolTips(tooltips);
				client.getTransaktionen().getPosEdit().addListener();
				client.revalidate();
				client.repaint();
			}

			//----------------- BESTELLVERWALTUNG - NEUE BESTELLUNG - SPEICHERN BUTTON
			if (ae.getActionCommand() == COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_SPEICHERN){

				if (client.getTransaktionen().getPosNeu().getListModel().getSize() == 0){
					JOptionPane.showMessageDialog(client, "<html>Bitte geben Sie mind. eine Bestellposition an.</html>");
					return;
				}

				// Bestellkopf
				String bstid = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTID)).getText();
				String bstKid = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_KID)).getText();
				String anleger = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_ANLEGER)).getText();
				String bsttermin = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTERMIN)).getText();
				if (bsttermin.length() > 0) {
					if(!isValidDate(bsttermin)){ // checkt ob der bestelltermin den vorgaben entspricht
						return;
					}
				}
				String bsttext = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTEXT)).getText();

				try {
					if ( db.checkIfElementExists(TABLE_BESTELLUNG, "bstid", bstid) ) {
						JOptionPane.showMessageDialog(client, "<html>Es wurde bereits eine Bestellung mit der ID " + bstid + " angelegt.</html>");
						return;
					}
				} catch (HeadlessException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				// Bestellpositionen
				int bstposCount = client.getTransaktionen().getPosNeu().getListModel().getSize();
				String[][] bstpos = new String[bstposCount][3];
				List<String> tooltips = new ArrayList<String>();
				for (int i = 0; i < bstposCount; i++){
					String pos = (String) (client.getTransaktionen().getPosNeu().getListModel().getElementAt(i));
					if (!pos.contains(";")){
						JOptionPane.showMessageDialog(client, "<html>Fehler bei Postion "+(i+1)+": Bitte achten Sie auf die korrekte Trennung der einzelnen Felder durch ein ';'-Zeichen. </html>");
						return;
					}
					pos.split("\\;");

					int elementCount = pos.split("\\;").length;
					if (elementCount <= 3 ){
						for (int j = 0; j < elementCount; j++){
							bstpos[i][j] = pos.split("\\;")[j];
						}
						try {
							Double totalPrice = db.calcTotalPrice(""+bstpos[i][0], Integer.parseInt(bstpos[i][1]) );
							tooltips.add("Gesamtpreis der Position: "+totalPrice.toString() +" €");
						} catch (NotExistInDatabaseException e) {
							client.showException(e);
						} catch (SQLException e1){
							client.showException(e1);
						} catch (NumberFormatException e2){
							client.showException(e2);
						}
					} else if (elementCount > 3) {
						JOptionPane.showMessageDialog(client, "<html>Fehler bei Postion "+(i+1)+": Bitte geben Sie nur Produkt-ID, Menge und den Positionstext an und achten Sie darauf, dass Sie ';' nur zum Trennen der Werte verwenden.</html>");
						return;
					} else {
						JOptionPane.showMessageDialog(client, "<html>Fehler bei Position "+(i+1)+": Bitte geben Sie zumindest die Produkt-ID und Menge an. </html>");
						return;
					}
				}

				client.getTransaktionen().getPosNeu().addToolTips(tooltips);
				client.getTransaktionen().getPosNeu().addListener();

				if ( Integer.parseInt(bstid) == db.getBufferedBestellungsID() )
					db.needNextBestellungsID(true); // DB darf wieder naechsten bstID liefern.


				// Check, if the textfields are empty and have to be filled.
				String[] bestellDaten = { bstid, bstKid, anleger, bsttermin };
				for ( String datum : bestellDaten ) {
					if ( datum.replaceAll("\\s+", "").isEmpty() ) {
						JOptionPane.showMessageDialog(client, BESTELLVERWALTUNG_MESSAGE_FILL_ALL_FIELDS);
						return;
					}
				}
				// Trying to insert new order.
				try {
					db.bestellungSpeichern(bstid, bsttext, anleger, "OFFEN", db.dateFormat(bsttermin), bstKid, bstpos);
				} catch (SQLException e) {
					client.showException(e);
					return;
				} catch (NotExistInDatabaseException e) {
					client.showException(e);
					return;
				} catch (ParseException e) {
					client.showException(e);
				}
				JOptionPane.showMessageDialog(client, "<html>Neue Bestellung mit der Bestellungs-ID " + bstid + " wurde erstellt. </html>");

				//clearInputComponentsOfBestellverwaltungNeu();
				Vector<Vector<Object>> bestellKopf = null;
				try {
					bestellKopf = db.selectFromTable(TABLE_BESTELLUNG, "bstid = " + bstid);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				Iterator<Vector<Object>> itKopf = bestellKopf.iterator();
				client.invalidate();
				while (itKopf.hasNext()){
					Vector<Object> v = itKopf.next();
					((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_ANLAGEDATUM)).setText(""+db.dateFormat((Timestamp) v.get(3)));
					((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_AENDERUNGSDATUM)).setText(""+ db.dateFormat((Timestamp) v.get(4)));
					((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_STATUS)).setText(""+v.get(5));
				}
				((JButton) client.getComponentByName(COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_BESTAETIGEN)).setEnabled(true);
			}



			//----------------- BESTELLVERWALTUNG - NEUE BESTELLUNG - BESTAETIGEN BUTTON
			if (ae.getActionCommand() == COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_BESTAETIGEN){

				if (client.getTransaktionen().getPosNeu().getListModel().getSize() == 0){
					JOptionPane.showMessageDialog(client, "<html>Bitte geben Sie mind. eine Bestellposition an.</html>");
					return;
				}

				// Bestellkopf
				String bstid = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTID)).getText();
				String bstKid = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_KID)).getText();
				String anleger = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_ANLEGER)).getText();
				String bsttermin = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTERMIN)).getText();
				if (bsttermin.length() > 0) {
					if(!isValidDate(bsttermin)){ // checkt ob der bestelltermin den vorgaben entspricht
						return;
					}
				}
				String bsttext = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTEXT)).getText();

//				try {
//					if ( db.checkIfElementExists(TABLE_BESTELLUNG, "bstid", bstid) ) {
//						JOptionPane.showMessageDialog(client, "<html>Es wurde bereits eine Bestellung mit der ID " + bstid + " angelegt.</html>");
//						return;
//					}
//				} catch (HeadlessException e1) {
//					e1.printStackTrace();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}

				// Bestellpositionen
				int bstposCount = client.getTransaktionen().getPosNeu().getListModel().getSize();
				String[][] bstpos = new String[bstposCount][3];
				List<String> tooltips = new ArrayList<String>();
				for (int i = 0; i < bstposCount; i++){
					String pos = (String) (client.getTransaktionen().getPosNeu().getListModel().getElementAt(i));
					if (!pos.contains(";")){
						JOptionPane.showMessageDialog(client, "<html>Fehler bei Postion "+(i+1)+": Bitte achten Sie auf die korrekte Trennung der einzelnen Felder durch ein ';'-Zeichen. </html>");
						return;
					}
					pos.split("\\;");

					int elementCount = pos.split("\\;").length;
					if (elementCount <= 3 ){
						for (int j = 0; j < elementCount; j++){
							bstpos[i][j] = pos.split("\\;")[j];
						}
						try {
							Double totalPrice = db.calcTotalPrice(""+bstpos[i][0], Integer.parseInt(bstpos[i][1]) );
							tooltips.add("Gesamtpreis der Position: "+totalPrice.toString() +" €");
						} catch (NotExistInDatabaseException e) {
							client.showException(e);
						} catch (SQLException e1){
							client.showException(e1);
						} catch (NumberFormatException e2){
							client.showException(e2);
						}
					} else if (elementCount > 3) {
						JOptionPane.showMessageDialog(client, "<html>Fehler bei Postion "+(i+1)+": Bitte geben Sie nur Produkt-ID, Menge und den Positionstext an und achten Sie darauf, dass Sie ';' nur zum Trennen der Werte verwenden.</html>");
						return;
					} else {
						JOptionPane.showMessageDialog(client, "<html>Fehler bei Position "+(i+1)+": Bitte geben Sie zumindest die Produkt-ID und Menge an. </html>");
						return;
					}
				}

				client.getTransaktionen().getPosNeu().addToolTips(tooltips);
				client.getTransaktionen().getPosNeu().addListener();

				if ( Integer.parseInt(bstid) == db.getBufferedBestellungsID() )
					db.needNextBestellungsID(true); // DB darf wieder naechsten bstID liefern.


				// Check, if the textfields are empty and have to be filled.
				String[] bestellDaten = { bstid, bstKid, anleger, bsttermin };
				for ( String datum : bestellDaten ) {
					if ( datum.replaceAll("\\s+", "").isEmpty() ) {
						JOptionPane.showMessageDialog(client, BESTELLVERWALTUNG_MESSAGE_FILL_ALL_FIELDS);
						return;
					}
				}
				// Trying to insert new order.
				boolean status = false;
				try {
					status = db.bestellungBestaetigen(bstid, bsttext, anleger, db.dateFormat(bsttermin), bstKid, bstpos);
				} catch (SQLException e) {
					client.showException(e);
					return;
				} catch (NotExistInDatabaseException e) {
					client.showException(e);
					return;
				} catch (ParseException e) {
					client.showException(e);
					return;
				}

				if (status){
					JOptionPane.showMessageDialog(client, "<html>Die Bestellung mit der ID " + bstid + " wurde bestaetigt. </html>");
					clearInputComponentsOfBestellverwaltungNeu();
				} else {
					int inputPrompt = JOptionPane.showConfirmDialog(client, "<html>Wir k&ouml;nnen den von Ihnen gew&uuml;nschten Lieferungstermin leider nicht best&auml;tigen. <br><br> Wollen Sie Ihren Liefertermin nach hinten verschieben? Falls nicht wird Ihre Bestellung verworfen.</html>", "Bestellbestätigung", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
					client.repaint();
					if (inputPrompt == 1){
						try {
							db.bestellungLoeschen(bstid);
						} catch (NotExistInDatabaseException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
						this.clearInputComponentsOfBestellverwaltungNeu();
					}
				}
			}

			//----------------- BESTELLVERWALTUNG - BESTELLUNG EDITIEREN - SUCHEN BUTTON
			if (ae.getActionCommand() == COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_SUCHEN){
				String bstID = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTID)).getText();

				// Check if the given BSTID is valid.
				if ( !isValidBstID(bstID) )
					return;
				// Check if the given BSTID really exists in the database.
				try {
					if ( !db.checkIfElementExists(TABLE_BESTELLUNG, "bstid", bstID) ) {
						JOptionPane.showMessageDialog(client, "<html>Es ist keine Bestellung mit der ID " + bstID + " in der Datenbank vorhanden.</html>");
						this.setInputComponentsOfBestellverwaltungEditEnabled(false);
						return;
					}

					// Bestellkopf ausfuellen
					Vector<Vector<Object>> bestellKopf = db.selectFromTable(TABLE_BESTELLUNG, "bstid = " + bstID);
					Iterator<Vector<Object>> itKopf = bestellKopf.iterator();

					client.invalidate();
					while (itKopf.hasNext()){
						Vector<Object> v = itKopf.next();
						((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTEXT)).setText((String) ((v.get(7) == null) ? "-" : (v.get(1))));
						((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER)).setText(""+v.get(2));
						((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_ANLAGEDATUM)).setText(""+db.dateFormat((Timestamp) v.get(3)));
						((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_AENDERUNGSDATUM)).setText(""+ db.dateFormat((Timestamp) v.get(4)));
						((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_STATUS)).setText(""+v.get(5));
						((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTERMIN)).setText(""+db.dateFormat((Timestamp) v.get(6)));
						((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_ERLEDIGTTERMIN)).setText((String) ((v.get(7) == null) ? "-" : db.dateFormat((Timestamp)(v.get(7)))));
						((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_KID)).setText(""+ v.get(8));
					}



					Vector<Vector<Object>> values = db.selectFromTable(TABLE_BESTELLPOSITION, "bstid = " + bstID);
					List<String> tooltips = new ArrayList<String>();
					DefaultListModel<String> listModel = new DefaultListModel<String>();
					Iterator<Vector<Object>> it = values.iterator();
					while (it.hasNext()){
						Vector<Object> v = it.next();
						String itPos = v.get(2)+";"+v.get(3)+((v.get(5) != null)?";"+v.get(5):"");
						try {
							Double totalPrice = db.calcTotalPrice(""+v.get(2), ((BigDecimal) v.get(3)).intValue() );
							tooltips.add("Gesamtpreis der Position: "+totalPrice.toString() +" €");
						} catch (NotExistInDatabaseException e) {
							client.showException(e);
						}
						listModel.addElement(itPos);
					}
					client.getTransaktionen().getPosEdit().removeList();
					client.getTransaktionen().getPosEdit().setModel(listModel);
					client.getTransaktionen().getPosEdit().addListToPane("listEdit", values.size()-1);
					client.getTransaktionen().getPosEdit().addModel();
					client.getTransaktionen().getPosEdit().addToolTips(tooltips);
					client.getTransaktionen().getPosEdit().addListener();
					client.revalidate();
					client.repaint();

					if (((String) bestellKopf.firstElement().get(5)).trim().equalsIgnoreCase("ERLEDIGT")){
						this.setInputComponentsOfBestellverwaltungEditEditable(false);
						this.setInputComponentsOfBestellverwaltungEditEnabled(false);
						((JButton) client.getComponentByName(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_SPEICHERN)).setEnabled(false);
						((JButton) client.getComponentByName(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_BESTAETIGEN)).setEnabled(false);
					} else {
						this.setInputComponentsOfBestellverwaltungEditEditable(true);
						this.setInputComponentsOfBestellverwaltungEditEnabled(true);
						((JButton) client.getComponentByName(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_SPEICHERN)).setEnabled(true);
						((JButton) client.getComponentByName(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_BESTAETIGEN)).setEnabled(true);
						((Bestellpositionen) client.getTransaktionen().getPosEdit()).getComponentByName("delEdit").setEnabled(false);
					}
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(client, e.getClass().getName() + " : " + e.getMessage());
					client.showException(e);
				}
			}

			//----------------- BESTELLVERWALTUNG - BESTELLUNG EDITIEREN - SPEICHERN BUTTON

			if (ae.getActionCommand() == COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_SPEICHERN){

				if (client.getTransaktionen().getPosEdit().getListModel().getSize() == 0){
					JOptionPane.showMessageDialog(client, "<html>Bitte geben Sie mind. eine Bestellposition an.</html>");
					return;
				}

				// Bestellkopf
				String bstid = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTID)).getText();
				String bstKid = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_KID)).getText();
				String anleger = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER)).getText();
				String bsttermin = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTERMIN)).getText();
				if (bsttermin.length() > 0) {
					if(!isValidDate(bsttermin)){ // checkt ob der bestelltermin den vorgaben entspricht
						return;
					}
				}
				String bsttext = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTEXT)).getText();

//				try {
//					if ( db.checkIfElementExists(TABLE_BESTELLUNG, "bstid", bstid) ) {
//						JOptionPane.showMessageDialog(client, "<html>Es wurde bereits eine Bestellung mit der ID " + bstid + " angelegt.</html>");
//						return;
//					}
//				} catch (HeadlessException e1) {
//					e1.printStackTrace();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}

				// Bestellpositionen
				int bstposCount = client.getTransaktionen().getPosEdit().getListModel().getSize();
				String[][] bstpos = new String[bstposCount][3];
				List<String> tooltips = new ArrayList<String>();
				for (int i = 0; i < bstposCount; i++){
					String pos = (String) (client.getTransaktionen().getPosEdit().getListModel().getElementAt(i));
					if (!pos.contains(";")){
						JOptionPane.showMessageDialog(client, "<html>Fehler bei Postion "+(i+1)+": Bitte achten Sie auf die korrekte Trennung der einzelnen Felder durch ein ';'-Zeichen. </html>");
						return;
					}
					pos.split("\\;");

					int elementCount = pos.split("\\;").length;
					if (elementCount <= 3 ){
						for (int j = 0; j < elementCount; j++){
							bstpos[i][j] = pos.split("\\;")[j];
						}
						try {
							Double totalPrice = db.calcTotalPrice(""+bstpos[i][0], Integer.parseInt(bstpos[i][1]) );
							tooltips.add("Gesamtpreis der Position: "+totalPrice.toString() +" €");
						} catch (NotExistInDatabaseException e) {
							client.showException(e);
						} catch (SQLException e1){
							client.showException(e1);
						} catch (NumberFormatException e2){
							client.showException(e2);
						}
					} else if (elementCount > 3) {
						JOptionPane.showMessageDialog(client, "<html>Fehler bei Postion "+(i+1)+": Bitte geben Sie nur Produkt-ID, Menge und den Positionstext an und achten Sie darauf, dass Sie ';' nur zum Trennen der Werte verwenden.</html>");
						return;
					} else {
						JOptionPane.showMessageDialog(client, "<html>Fehler bei Position "+(i+1)+": Bitte geben Sie zumindest die Produkt-ID und Menge an. </html>");
						return;
					}
				}

				client.getTransaktionen().getPosEdit().addToolTips(tooltips);
				client.getTransaktionen().getPosEdit().addListener();

				if ( Integer.parseInt(bstid) == db.getBufferedBestellungsID() )
					db.needNextBestellungsID(true); // DB darf wieder naechsten bstID liefern.


				// Check, if the textfields are empty and have to be filled.
				String[] bestellDaten = { bstid, bstKid, anleger, bsttermin };
				for ( String datum : bestellDaten ) {
					if ( datum.replaceAll("\\s+", "").isEmpty() ) {
						JOptionPane.showMessageDialog(client, BESTELLVERWALTUNG_MESSAGE_FILL_ALL_FIELDS);
						return;
					}
				}
				// Trying to insert new order.
				try {
					db.bestellungAendern(bstid, bsttext, anleger, db.dateFormat(bsttermin), bstKid, bstpos);
				} catch (SQLException e) {
					client.showException(e);
					return;
				} catch (NotExistInDatabaseException e) {
					client.showException(e);
					return;
				} catch (ParseException e) {
					client.showException(e);
				}
				JOptionPane.showMessageDialog(client, "<html>Die Bestellung mit der ID " + bstid + " wurde ge&auml;ndert. </html>");
				clearInputComponentsOfBestellverwaltungEdit();

			}

			//----------------- BESTELLVERWALTUNG - BESTELLUNG EDITIEREN - BESTAETIGEN BUTTON
			if (ae.getActionCommand() == COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_SPEICHERN){

				if (client.getTransaktionen().getPosEdit().getListModel().getSize() == 0){
					JOptionPane.showMessageDialog(client, "<html>Bitte geben Sie mind. eine Bestellposition an.</html>");
					return;
				}

				// Bestellkopf
				String bstid = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTID)).getText();
				String bstKid = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_KID)).getText();
				String anleger = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER)).getText();
				String bsttermin = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTERMIN)).getText();
				if (bsttermin.length() > 0) {
					if(!isValidDate(bsttermin)){ // checkt ob der bestelltermin den vorgaben entspricht
						return;
					}
				}
				String bsttext = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTEXT)).getText();

//				try {
//					if ( db.checkIfElementExists(TABLE_BESTELLUNG, "bstid", bstid) ) {
//						JOptionPane.showMessageDialog(client, "<html>Es wurde bereits eine Bestellung mit der ID " + bstid + " angelegt.</html>");
//						return;
//					}
//				} catch (HeadlessException e1) {
//					e1.printStackTrace();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}

				// Bestellpositionen
				int bstposCount = client.getTransaktionen().getPosEdit().getListModel().getSize();
				String[][] bstpos = new String[bstposCount][3];
				List<String> tooltips = new ArrayList<String>();
				for (int i = 0; i < bstposCount; i++){
					String pos = (String) (client.getTransaktionen().getPosEdit().getListModel().getElementAt(i));
					if (!pos.contains(";")){
						JOptionPane.showMessageDialog(client, "<html>Fehler bei Postion "+(i+1)+": Bitte achten Sie auf die korrekte Trennung der einzelnen Felder durch ein ';'-Zeichen. </html>");
						return;
					}
					pos.split("\\;");

					int elementCount = pos.split("\\;").length;
					if (elementCount <= 3 ){
						for (int j = 0; j < elementCount; j++){
							bstpos[i][j] = pos.split("\\;")[j];
						}
						try {
							Double totalPrice = db.calcTotalPrice(""+bstpos[i][0], Integer.parseInt(bstpos[i][1]) );
							tooltips.add("Gesamtpreis der Position: "+totalPrice.toString() +" €");
						} catch (NotExistInDatabaseException e) {
							client.showException(e);
						} catch (SQLException e1){
							client.showException(e1);
						} catch (NumberFormatException e2){
							client.showException(e2);
						}
					} else if (elementCount > 3) {
						JOptionPane.showMessageDialog(client, "<html>Fehler bei Postion "+(i+1)+": Bitte geben Sie nur Produkt-ID, Menge und den Positionstext an und achten Sie darauf, dass Sie ';' nur zum Trennen der Werte verwenden.</html>");
						return;
					} else {
						JOptionPane.showMessageDialog(client, "<html>Fehler bei Position "+(i+1)+": Bitte geben Sie zumindest die Produkt-ID und Menge an. </html>");
						return;
					}
				}

				client.getTransaktionen().getPosEdit().addToolTips(tooltips);
				client.getTransaktionen().getPosEdit().addListener();

				if ( Integer.parseInt(bstid) == db.getBufferedBestellungsID() )
					db.needNextBestellungsID(true); // DB darf wieder naechsten bstID liefern.


				// Check, if the textfields are empty and have to be filled.
				String[] bestellDaten = { bstid, bstKid, anleger, bsttermin };
				for ( String datum : bestellDaten ) {
					if ( datum.replaceAll("\\s+", "").isEmpty() ) {
						JOptionPane.showMessageDialog(client, BESTELLVERWALTUNG_MESSAGE_FILL_ALL_FIELDS);
						return;
					}
				}
				// Trying to insert new order.
				try {
					db.bestellungAendern(bstid, bsttext, anleger, db.dateFormat(bsttermin), bstKid, bstpos);
				} catch (SQLException e) {
					client.showException(e);
					return;
				} catch (NotExistInDatabaseException e) {
					client.showException(e);
					return;
				} catch (ParseException e) {
					client.showException(e);
				}
				JOptionPane.showMessageDialog(client, "<html>Die Bestellung mit der ID " + bstid + " wurde ge&auml;ndert. </html>");
				this.clearInputComponentsOfBestellverwaltungEdit();

			}

			//----------------- BESTELLVERWALTUNG - BESTELLUNG EDITIEREN - BESTAETIGEN BUTTON
			if (ae.getActionCommand() == COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_BESTAETIGEN){

				if (client.getTransaktionen().getPosEdit().getListModel().getSize() == 0){
					JOptionPane.showMessageDialog(client, "<html>Bitte geben Sie mind. eine Bestellposition an.</html>");
					return;
				}

				// Bestellkopf
				String bstid = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTID)).getText();
				String bstKid = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_KID)).getText();
				String anleger = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER)).getText();
				String bsttermin = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTERMIN)).getText();
				if (bsttermin.length() > 0) {
					if(!isValidDate(bsttermin)){ // checkt ob der bestelltermin den vorgaben entspricht
						return;
					}
				}
				String bsttext = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTEXT)).getText();

//				try {
//					if ( db.checkIfElementExists(TABLE_BESTELLUNG, "bstid", bstid) ) {
//						JOptionPane.showMessageDialog(client, "<html>Es wurde bereits eine Bestellung mit der ID " + bstid + " angelegt.</html>");
//						return;
//					}
//				} catch (HeadlessException e1) {
//					e1.printStackTrace();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}

				// Bestellpositionen
				int bstposCount = client.getTransaktionen().getPosEdit().getListModel().getSize();
				String[][] bstpos = new String[bstposCount][3];
				List<String> tooltips = new ArrayList<String>();
				for (int i = 0; i < bstposCount; i++){
					String pos = (String) (client.getTransaktionen().getPosEdit().getListModel().getElementAt(i));
					if (!pos.contains(";")){
						JOptionPane.showMessageDialog(client, "<html>Fehler bei Postion "+(i+1)+": Bitte achten Sie auf die korrekte Trennung der einzelnen Felder durch ein ';'-Zeichen. </html>");
						return;
					}
					pos.split("\\;");

					int elementCount = pos.split("\\;").length;
					if (elementCount <= 3 ){
						for (int j = 0; j < elementCount; j++){
							bstpos[i][j] = pos.split("\\;")[j];
						}
						try {
							Double totalPrice = db.calcTotalPrice(""+bstpos[i][0], Integer.parseInt(bstpos[i][1]) );
							tooltips.add("Gesamtpreis der Position: "+totalPrice.toString() +" €");
						} catch (NotExistInDatabaseException e) {
							client.showException(e);
						} catch (SQLException e1){
							client.showException(e1);
						} catch (NumberFormatException e2){
							client.showException(e2);
						}
					} else if (elementCount > 3) {
						JOptionPane.showMessageDialog(client, "<html>Fehler bei Postion "+(i+1)+": Bitte geben Sie nur Produkt-ID, Menge und den Positionstext an und achten Sie darauf, dass Sie ';' nur zum Trennen der Werte verwenden.</html>");
						return;
					} else {
						JOptionPane.showMessageDialog(client, "<html>Fehler bei Position "+(i+1)+": Bitte geben Sie zumindest die Produkt-ID und Menge an. </html>");
						return;
					}
				}

				client.getTransaktionen().getPosEdit().addToolTips(tooltips);
				client.getTransaktionen().getPosEdit().addListener();

				if ( Integer.parseInt(bstid) == db.getBufferedBestellungsID() )
					db.needNextBestellungsID(true); // DB darf wieder naechsten bstID liefern.


				// Check, if the textfields are empty and have to be filled.
				String[] bestellDaten = { bstid, bstKid, anleger, bsttermin };
				for ( String datum : bestellDaten ) {
					if ( datum.replaceAll("\\s+", "").isEmpty() ) {
						JOptionPane.showMessageDialog(client, BESTELLVERWALTUNG_MESSAGE_FILL_ALL_FIELDS);
						return;
					}
				}
				// Trying to insert new order.
				boolean status = false;
				try {
					status = db.bestellungBestaetigen(bstid, bsttext, anleger, db.dateFormat(bsttermin), bstKid, bstpos);
				} catch (SQLException e) {
					client.showException(e);
					return;
				} catch (NotExistInDatabaseException e) {
					client.showException(e);
					return;
				} catch (ParseException e) {
					client.showException(e);
					return;
				}

				if (status){
					JOptionPane.showMessageDialog(client, "<html>Die Bestellung mit der ID " + bstid + " wurde bestaetigt. </html>");
					clearInputComponentsOfBestellverwaltungEdit();
				} else {
					int inputPrompt = JOptionPane.showConfirmDialog(client, "<html>Wir k&ouml;nnen den von Ihnen gew&uuml;nschten Lieferungstermin leider nicht best&auml;tigen. <br><br> Wollen Sie Ihren Liefertermin nach hinten verschieben? Falls nicht wird Ihre Bestellung verworfen.</html>", "Bestellbestätigung", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
					client.repaint();
					if (inputPrompt == 1){
						try {
							db.bestellungLoeschen(bstid);
						} catch (NotExistInDatabaseException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
						this.clearInputComponentsOfBestellverwaltungEdit();
					}
				}
			}

			//----------------- KUNDENPFLEGE - KUDNEN AENDERN - SUCHEN BUTTON
			if ( ae.getActionCommand() == COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_SUCHEN ) {
				((JButton) client.getComponentByName(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN_FERTIG)).setVisible(false);
				String kID = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KID)).getText();
				// Check if the given KID is valid.
				if ( !isValidKID(kID) )
					return;
				// Check if the given KID really exists in the database.
				try {
					if ( !db.checkIfElementExists(TABLE_KUNDE, "kid", kID) ) {
						JOptionPane.showMessageDialog(client, "<html>Der Kunde mit KID " + kID + " ist nicht in der Datenbank vorhanden.</html>");
						return;
					}


					Vector<Object> values = db.selectFromTable(TABLE_KUNDE, "kid = " + kID).firstElement();

					((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_NAME)).setText((String) values.get(1));
					((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_ADRESSE)).setText((String) values.get(2));
					((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_TEL)).setText((String) values.get(3));
					((JFormattedTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KONTO)).setText("" +  (BigDecimal) values.get(4));
					((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_BRANCHE)).setSelectedItem((String) values.get(5));
					((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_NATION)).setSelectedItem(mapToName(((BigDecimal) values.get(6)).intValue()));


					this.setInputComponentsOfKudenpflegeEditEditable(true);
					this.setInputComponentsOfKudenpflegeEditEnabled(true);
					((JButton) client.getComponentByName(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN)).setEnabled(true);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(client, e.getClass().getName() + " : " + e.getMessage());
					e.printStackTrace();
				}
			}

			//----------------- BESTELLVERWALTUNG - BESTELLUNG AUSLIEFERN - AUSLIEFERN BUTTON
			if (ae.getActionCommand() == COMPONENT_BUTTON_BESTELLVERWALTUNG_GO_AUSLIEFERN) {
				String bstid = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_GO_BSTID)).getText();
				// Check if the given KID is valid.
				if ( !isValidBstID(bstid) )
					return;

				// Check if the given KID really exists in the database.
				try {
					if ( !db.checkIfElementExists(TABLE_BESTELLUNG, "bstid", bstid ) ) {
						JOptionPane.showMessageDialog(client, "<html>Die Bestellung " + bstid + " ist nicht in der Datenbank vorhanden.</html>");
						return;
					}

					boolean success = db.bestellungAusliefern(bstid);
					String msg = "";
					if ( !success ) {
						msg = "<html>Die Bestellung mit der ID " + bstid + " kann nicht beliefert werden.</html>";
					} else {
						msg = "<html>Die Bestellung mit der ID " + bstid + " wurde erfolgreich beliefert.</html>";
					}
					JOptionPane.showMessageDialog(client, msg);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(client, e.getClass().getName() + " : " + e.getMessage());
					e.printStackTrace();
				} catch (NotExistInDatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			//----------------- KUNDENPFLEGE - KUDNEN AENDERN - EDIT BUTTON
			if ( ae.getActionCommand() == COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN ) {
				String kID = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KID)).getText();
				try {
					// Check if the table is locked and used by another user first.
					boolean isLocked = db.lockRows(TABLE_KUNDE, "kid = " + kID);
					if ( isLocked ) {
						((JButton) client.getComponentByName(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN_FERTIG)).setVisible(true);
						((JButton) client.getComponentByName(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN)).setEnabled(false);
						return;
					}
					JOptionPane.showMessageDialog(client, "<html>Der Kunde mit KID " + kID + " wird derzeit von einem anderen Benutzer bearbeitet." +
														  "<br />Bitte versuchen Sie in einem späteren Zeitpunkt nochmals.</html>");
					this.clearInputComponentsOfKundeEditPflege();
					this.setInputComponentsOfKudenpflegeEditEnabled(true);
					((JButton) client.getComponentByName(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN)).setEnabled(false);

				} catch (SQLException e) {
					JOptionPane.showMessageDialog(client, e.getClass().getName() + " : " + e.getMessage());
					e.printStackTrace();
				}
			}

			//----------------- KUNDENPFLEGE - KUDNEN AENDERN - FERTIG BUTTON
			if ( ae.getActionCommand() == COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN_FERTIG ) {
				String kID = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KID)).getText();
				String kName = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_NAME)).getText();
				String kAdresse = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_ADRESSE)).getText();
				String kTelNr = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_TEL)).getText();
				double kKonto = ((Number) ((JFormattedTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KONTO)).getValue()).doubleValue();
				String kBranche = ((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_BRANCHE)).getSelectedItem().toString();
				String kNation = ((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_NATION)).getSelectedItem().toString();

				String[] kundenDaten = { kName, kAdresse, kTelNr, kBranche, kNation };
				for ( String datum : kundenDaten ) {
					if ( datum.replaceAll("\\s+", "").isEmpty() ) {
						JOptionPane.showMessageDialog(client, KUNDENPFLEGE_MESSAGE_FILL_ALL_FIELDS);
						return;
					}
				}

				try {
					db.updateKunde(kID, kName, kAdresse, kTelNr, kKonto, kBranche, kNation);
					JOptionPane.showMessageDialog(client, "<html>Der Kunde mit Kunden-ID " + kID + " wurde aktualisiert. </html>");
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(client, e.getClass().getName() + " : " + e.getMessage());
					e.printStackTrace();
				}
				finally {
					((JButton) client.getComponentByName(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN_FERTIG)).setVisible(false);
					((JButton) client.getComponentByName(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN)).setVisible(true);
					this.setInputComponentsOfKudenpflegeEditEditable(false);
					this.setInputComponentsOfKudenpflegeEditEnabled(false);
				}
			}

			//----------------- PRODUKTVERWALTUNG - ZULIEFERUNG EINBUCHEN
			if ( ae.getActionCommand() == COMPONENT_BUTTON_PRODUKTVERWALTUNG_NEU_EINBUCHEN ) {
				String zlid = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_NEU_ZLID)).getText();

				// Check if the textfield for zlid is empty.
				if ( zlid.isEmpty() ) {
					JOptionPane.showMessageDialog(client, "Bitte geben Sie eine Zulieferungs-ID an.");
					return;
				}

				// Check if the given KID is valid.
				if ( !Pattern.matches("\\d*", zlid) ) { // nur positive nummerische Werte.
					JOptionPane.showMessageDialog(client, PRODUKTVERWALTUNG_MESSAGE_INVALID_ZLID);
					return;
				}

				// Neue Zulieferung einbuchen.
				boolean success = true;
				try {
					success = db.zulieferungEinbuchen(zlid);
				} catch ( NotExistInDatabaseException  ne ) {
					JOptionPane.showMessageDialog(client, "<html>Die Zulieferung mit der ID " + zlid + " ist nicht in der Datenbank vorhanden.</html>");
					ne.printStackTrace();
					return;
				} catch ( SQLException e ) {
					e.printStackTrace();
					return;
				}

				String msg = "";
				if ( !success ) {
					msg = "<html>Die Zulieferung mit der ID " + zlid + " kann nicht mehr eingebucht werden. Sie wurde bereits erledigt.</html>";
				} else {
					msg = "<html>Die Zuliefeung mit der ID " + zlid + " wurde erfolgreich eingebucht.</html>";
				}
				JOptionPane.showMessageDialog(client, msg);
			}

			//----------------- PRODUKTVERWALTUNG - BESTAND UMBUCHEN
			if ( ae.getActionCommand() == COMPONENT_BUTTON_PRODUKTVERWALTUNG_EDIT_UMBUCHEN ) {
				String srcLager = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_SRCLAGER)).getText();
				String destLager = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_DESTLAGER)).getText();
				String pid  = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_PRODUKT)).getText();
				String menge = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_MENGE)).getText();

				// Check the input strings on valid numeric values, or if they are empty.
				String[] inputs = { srcLager, destLager, pid, menge };
				for ( String s : inputs ) {
					if ( !Pattern.matches("\\d*", s ) ) {
						JOptionPane.showMessageDialog(client, "Die Eingabe darf nur positive numerische Werte enthalten, z.B. 1, 5, 98, 2098...");
						return;
					}
					if ( s.isEmpty() ) { // Check empty string.
						JOptionPane.showMessageDialog(client, "<html>Bitte f&uuml;llen Sie alle Felder aus.</html>");
						return;
					}
				}

				if ( srcLager.equals(destLager) ) {
					JOptionPane.showMessageDialog(client, "Es kann nichts umgebucht werden. Ursprungs- und Ziellager sind gleich.");
					return;
				}

				// Bestaende umbuchen.
				int success = -999;
				try {
					success = db.bestandUmbuchen(srcLager, destLager, pid, Integer.parseInt(menge));
				} catch ( NotExistInDatabaseException ne ) {
					JOptionPane.showMessageDialog(client, "<html>Ursprungslager + " + srcLager + " oder Produkt " + pid + " ist nicht in der Datenbank vorhanden.</html>");
					// ne.printStackTrace();
					return;
				} catch ( SQLException e ) {
					e.printStackTrace();
					return;
				}

				String msg = "";
				if ( success != -999 ) {
					msg = "<html>Bestand reicht nicht aus! Nur " + success + " St&uuml;ck vorr&auml;tig.</html>";
				} else {
					msg = "<html>Die Best&auml;nde wurden erfolgreich vom Lager " + srcLager + " auf Ziellager " + destLager + " umgebucht.</html>";
				}
				JOptionPane.showMessageDialog(client, msg);
			}

			//----------------- PRODUKTANALYSE
			if ( ae.getActionCommand() == COMPONENT_BUTTON_PRODUKTANALYSE_AUSFUEHREN ) {
				String typ = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_PRODUKTANALYSE_TYP)).getText();
				String groesse = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_PRODUKTANALYSE_GROESSE)).getText();

				try {
					if ( !db.checkIfElementExists(TABLE_PRODUKT, "typ", "'"+typ+"'") ) {
						JOptionPane.showMessageDialog(client, "<html>Es existiert kein Produkt vom Typ '" + typ + "'.</html>");
						return;
					}
				} catch (HeadlessException e) {
					client.showException(e);
				} catch (SQLException e) {
					client.showException(e);
				}
				client.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));


				try {
					db.callProcedureProduktanalyse(typ, Integer.parseInt(groesse));
					OutputTableModel tableModel = null;
					try {
					    tableModel = (OutputTableModel) client.getDBOutput().populateTable("SELECT * FROM " +TABLE_OWNER+ "." + "ANALYSEHELPRES");
					} catch (SQLException e) {
						client.showException(e);
					}
					client.getDBOutput().removeScrollPane();
					client.getDBOutput().addTableModel(tableModel);
					client.getDBOutput().addTableToPane();
					client.revalidate();
					client.repaint();
					db.dropProduktTabelle();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotExistInDatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				client.setCursor(Cursor.getDefaultCursor());
			}

			//----------------- LIEFERKOSTENSENKUNG
			if ( ae.getActionCommand() == COMPONENT_BUTTON_LIEFERKOSTEN_AUSFUEHREN ) {

				String produkt = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_LIEFERKOSTEN_PRODUKT)).getText();
				try {
					if ( !db.checkIfElementExists(TABLE_PRODUKT, "name", "'"+produkt+"'") ) {
						JOptionPane.showMessageDialog(client, "<html>Es existiert kein Produkt mit dem Namen '" + produkt + "'.</html>");
						return;
					}
				} catch (HeadlessException e) {
					client.showException(e);
				} catch (SQLException e) {
					client.showException(e);
				}
				client.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				try {
					db.callProcedureLieferkostenAnalyse(produkt);
					OutputTableModel tableModel = null;
					try {
					    tableModel = (OutputTableModel) client.getDBOutput().populateTable("SELECT * FROM " +TABLE_OWNER+ "." + "ANALYSE2HELPRES");
					} catch (SQLException e) {
						client.showException(e);
					}
					client.getDBOutput().removeScrollPane();
					client.getDBOutput().addTableModel(tableModel);
					client.getDBOutput().addTableToPane();
					client.revalidate();
					client.repaint();
					db.dropLieferkostenTabelle();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotExistInDatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				client.setCursor(Cursor.getDefaultCursor());
			}

			if (ae.getActionCommand() == COMPONENT_ITEM_MENU_LOGOUT){
				int inputPrompt = JOptionPane.showConfirmDialog(client, "Wollen Sie sich wirklich abmelden?", "Haufkof Client - Abmelden", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
				client.repaint();
				if (inputPrompt == 0){
					client.dispose();
					try {
						db._DB();
					} catch (SQLException e) {
						client.showException(e);
					}
					Main.createAndBuildLoginGui();
				}
			}

			if (ae.getActionCommand() == COMPONENT_ITEM_MENU_EXIT){
				int inputPrompt = JOptionPane.showConfirmDialog(client, "Wollen Sie die Anwendung wirklich beenden?", "Haufkof Client - Beenden", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
				client.repaint();
				if (inputPrompt == 0){
					System.exit(0);
				}
			}

			if (ae.getActionCommand() == COMPONENT_ITEM_MENU_INFO){
				JOptionPane.showMessageDialog(client, "<html>Haufkof-Client, Betaversion </html>", "Haufkof Client - Info", JOptionPane.INFORMATION_MESSAGE, null);
				client.repaint();
			}
		}

		//-------------------- Private auxiliary methods for KUNDENPFLEGE
		/**
		 * Diese Methode &uuml;berpr&uuml;ft, ob der Input sich nur aus numerischen Werte (Ziffern) besteht.
		 * @param input
		 * @return <i>true</i>, falls der Input nur numerische Werte hat. Sonst <i>false</i>.
		 */
		private boolean isValidKID(String input) {
			boolean result = Pattern.matches("\\d*", input);  // nur positive nummerische Werte.
			if ( !result ) {
				JOptionPane.showMessageDialog(client, KUNDENPFLEGE_MESSAGE_INVALID_KID);
			}
			return result;
		}

		/**
		 * Diese Methode &uuml;berpr&uuml;ft, ob der Input sich nur aus numerischen Werte (Ziffern) besteht.
		 * @param input
		 * @return <i>true</i>, falls der Input nur numerische Werte hat. Sonst <i>false</i>.
		 */
		private boolean isValidBstID(String input) {
			boolean result = Pattern.matches("\\d*", input);  // nur positive nummerische Werte.
			if ( !result ) {
				JOptionPane.showMessageDialog(client, BESTELLVERWALTUNG_MESSAGE_INVALID_BSTID);
			}
			return result;
		}



		/**
		 * Diese Methode bildet die NID einer Nation auf deren Namen ab. StandardmA6uml;&szlig; gibt es nur 3 Nationen:
		 *
		 * 		NID  |  NAME
		 *   --------+---------
		 *      12   |  Belize
		 *      73   |  Jemen
		 *     104   |  Neuseeland
		 *
		 * @param nid : ID einer der obigen 3 Nationen.
		 * @return Namen der Nation mit der NID <i>nid</i>.
		 */
		private String mapToName(int nid) {
			String name = ( nid == 12 ) ? "Belize" : ( ( nid == 73 ) ? "Jemen" : "Neuseeland" );
			return name;
		}

		private void setInputComponentsOfKudenpflegeEditEditable(boolean b) {
			client.invalidate();
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_NAME)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_ADRESSE)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_TEL)).setEditable(b);
			((JFormattedTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KONTO)).setEditable(b);
			((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_BRANCHE)).setEditable(b);
			((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_NATION)).setEditable(b);
			client.revalidate();
			client.repaint();
		}

		private void setInputComponentsOfKudenpflegeEditEnabled(boolean b) {
			client.invalidate();
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_NAME)).setEnabled(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_ADRESSE)).setEnabled(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_TEL)).setEnabled(b);
			((JFormattedTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KONTO)).setEnabled(b);
			((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_BRANCHE)).setEnabled(b);
			((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_NATION)).setEnabled(b);
			client.revalidate();
			client.repaint();
		}

		private void clearInputComponentsOfKundeEditPflege() {
			client.invalidate();
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_NAME)).setText("");
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_ADRESSE)).setText("");
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_TEL)).setText("");
			((JFormattedTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KONTO)).setText("");
			((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_BRANCHE)).setSelectedItem("");
			((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_NATION)).setSelectedItem("");
			client.revalidate();
			client.repaint();
		}

		private void setInputComponentsOfKudenpflegeNeuEditable(boolean b) {
			client.invalidate();
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_NAME)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_ADRESSE)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_TEL)).setEditable(b);
			((JFormattedTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KONTO)).setEditable(b);
			((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE)).setEditable(b);
			((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_NATION)).setEditable(b);
			client.revalidate();
			client.repaint();
		}

		private void setInputComponentsOfKudenpflegeNeuEnabled(boolean b) {
			client.invalidate();
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_NAME)).setEnabled(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_ADRESSE)).setEnabled(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_TEL)).setEnabled(b);
			((JFormattedTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KONTO)).setEnabled(b);
			((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE)).setEnabled(b);
			((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_NATION)).setEnabled(b);
			client.revalidate();
			client.repaint();
		}

		private void clearInputComponentsOfKundeNeuPflege() {
			client.invalidate();
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_NAME)).setText("");
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_ADRESSE)).setText("");
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_TEL)).setText("");
		//	((JFormattedTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KONTO)).setText("");
			((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE)).setSelectedIndex(0);
			((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_NATION)).setSelectedIndex(0);
			client.revalidate();
			client.repaint();
		}

		private void setInputComponentsOfBestellverwaltungEditEditable(boolean b) {
			client.invalidate();
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTEXT)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER)).setEditable(b);
//			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_ANLAGEDATUM)).setEditable(b);
//			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_AENDERUNGSDATUM)).setEditable(b);
//			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_STATUS)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTERMIN)).setEditable(b);
//			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_ERLEDIGTTERMIN)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_KID)).setEditable(b);
			((JTextField) ((Bestellpositionen) client.getTransaktionen().getPosEdit()).getComponentByName("inpEdit")).setEditable(b);
			client.revalidate();
			client.repaint();
		}

		private void setInputComponentsOfBestellverwaltungEditEnabled(boolean b) {
			client.invalidate();
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTEXT)).setEnabled(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER)).setEnabled(b);
//			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_ANLAGEDATUM)).setEditable(b);
//			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_AENDERUNGSDATUM)).setEditable(b);
//			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_STATUS)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTERMIN)).setEnabled(b);
//			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_ERLEDIGTTERMIN)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_KID)).setEnabled(b);
			((JTextField) ((Bestellpositionen) client.getTransaktionen().getPosEdit()).getComponentByName("inpEdit")).setEnabled(b);
			((JButton) ((Bestellpositionen) client.getTransaktionen().getPosEdit()).getComponentByName("delEdit")).setEnabled(b);
//			((JButton) ((Bestellpositionen) client.getTransaktionen().getPosEdit()).getComponentByName("addEdit")).setEnabled(b);
			client.revalidate();
			client.repaint();
		}

		private void setInputComponentsOfBestellverwaltungNeuEditable(boolean b) {
			client.invalidate();
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTEXT)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_ANLEGER)).setEditable(b);
//			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_ANLAGEDATUM)).setEditable(b);
//			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_AENDERUNGSDATUM)).setEditable(b);
//			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_STATUS)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTERMIN)).setEditable(b);
//			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_ERLEDIGTTERMIN)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_KID)).setEditable(b);
			((JTextField) ((Bestellpositionen) client.getTransaktionen().getPosEdit()).getComponentByName("inpNeu")).setEditable(b);
			client.revalidate();
			client.repaint();
		}

		private void setInputComponentsOfBestellverwaltungNeuEnabled(boolean b) {
			client.invalidate();
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTEXT)).setEnabled(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_ANLEGER)).setEnabled(b);
//			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_ANLAGEDATUM)).setEditable(b);
//			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_AENDERUNGSDATUM)).setEditable(b);
//			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_STATUS)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTERMIN)).setEnabled(b);
//			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_ERLEDIGTTERMIN)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_KID)).setEnabled(b);
			((JTextField) ((Bestellpositionen) client.getTransaktionen().getPosEdit()).getComponentByName("inpNeu")).setEnabled(b);
			client.revalidate();
			client.repaint();
		}

		private void clearInputComponentsOfBestellverwaltungNeu() {
			client.invalidate();
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTEXT)).setText("");
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_ANLEGER)).setText("");
			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_ANLAGEDATUM)).setText("-");
			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_AENDERUNGSDATUM)).setText("-");
			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_STATUS)).setText("-");
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTERMIN)).setText("");
			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_ERLEDIGTTERMIN)).setText("-");
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_KID)).setText("");
			((JTextField) ((Bestellpositionen) client.getTransaktionen().getPosNeu()).getComponentByName("inpNeu")).setText("");
			DefaultListModel listModel = new DefaultListModel();
			client.getTransaktionen().getPosNeu().removeList();
			client.getTransaktionen().getPosNeu().setModel(listModel);
			client.getTransaktionen().getPosNeu().addListToPane("listNeu",-1);
			client.getTransaktionen().getPosNeu().addModel();
			client.revalidate();
			client.repaint();
		}

		private void clearInputComponentsOfBestellverwaltungEdit() {
			client.invalidate();
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTEXT)).setText("");
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER)).setText("");
			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_ANLAGEDATUM)).setText("-");
			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_AENDERUNGSDATUM)).setText("-");
			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_STATUS)).setText("-");
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTERMIN)).setText("");
			((JLabel) client.getComponentByName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_ERLEDIGTTERMIN)).setText("-");
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTERMIN)).setText("");
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_KID)).setText("");
			((JTextField) ((Bestellpositionen) client.getTransaktionen().getPosEdit()).getComponentByName("inpEdit")).setText("");
			DefaultListModel listModel = new DefaultListModel();
			client.getTransaktionen().getPosEdit().removeList();
			client.getTransaktionen().getPosEdit().setModel(listModel);
			client.getTransaktionen().getPosEdit().addListToPane("listEdit",-1);
			client.getTransaktionen().getPosEdit().addModel();
			client.revalidate();
			client.repaint();
		}

		private boolean isValidBSTID(String inputBSTID){
			boolean result = Pattern.matches("\\d*", inputBSTID);
			if ( !result ) {
				JOptionPane.showMessageDialog(client, BESTELLVERWALTUNG_MESSAGE_INVALID_BSTID);
			}
			return result;
		}

		private boolean isValidDate(String inputDate){
			boolean result = Pattern.matches("\\d{2}\\.\\d{2}\\.\\d{2}$",inputDate);
			if ( !result ){
				JOptionPane.showMessageDialog(client, BESTELLVERWALTUNG_MESSAGE_INVALID_BSTTERMIN);
			}
			return result;
		}

		private void calcBstPosPreis(){

		}

		private void getProduktPreis(){

		}
	}

	class ItemEventListener implements ItemListener{
	    @Override
	    public void itemStateChanged(ItemEvent ie) {
	    	if (ie.getStateChange() == ItemEvent.SELECTED && ((Component) ie.getSource()).getName().equals(COMPONENT_COMBO_KUNDENPFLEGE_ACTIONS)){

	    		CardLayout cl = (CardLayout) ((Container) client.getTransaktionen().getComponentByName(COMPONENT_PANEL_KUNDENPFLEGE)).getLayout();
	    		cl.show(((Container) client.getTransaktionen().getComponentByName(COMPONENT_PANEL_KUNDENPFLEGE)), (String) ie.getItem());

	    		if ((int) ((JComboBox<?>) ie.getSource()).getSelectedIndex() == 1)
	    		{
	    			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KID)).setText(""+db.getKundenID());
	    			client.repaint();

	    		}
	    	}
	    	else if (ie.getStateChange() == ItemEvent.SELECTED && ((Component) ie.getSource()).getName().equals(COMPONENT_COMBO_PRODUKTVERWALTUNG_ACTIONS)){
	    		CardLayout cl = (CardLayout) ((Container) client.getTransaktionen().getComponentByName(COMPONENT_PANEL_PRODUKTVERWALTUNG)).getLayout();
	    		cl.show(((Container) client.getTransaktionen().getComponentByName(COMPONENT_PANEL_PRODUKTVERWALTUNG)), (String) ie.getItem());
	    	}
	    	else if (ie.getStateChange() == ItemEvent.SELECTED && ((Component) ie.getSource()).getName().equals(COMPONENT_COMBO_BESTELLVERWALTUNG_ACTIONS)){
	    		CardLayout cl = (CardLayout) ((Container) client.getTransaktionen().getComponentByName(COMPONENT_PANEL_BESTELLVERWALTUNG)).getLayout();
	    		cl.show(((Container) client.getTransaktionen().getComponentByName(COMPONENT_PANEL_BESTELLVERWALTUNG)), (String) ie.getItem());

	    		((Bestellpositionen) client.getTransaktionen().getPosEdit()).getComponentByName("inpEdit").setEnabled(false);

	    		//----------- BESTELLUNG VERWALTUNG - Bestellung anlegen
	    		if ( (int) ((JComboBox<String>) ie.getSource()).getSelectedIndex() == 1 ) {
	    			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTID)).setText("" + db.generateBSTID());
	    			((JButton) client.getComponentByName(COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_BESTAETIGEN)).setEnabled(false);
	    			client.repaint();
	    		}
	    	}

	    }
	}

	class TreeEventListener implements TreeSelectionListener{

		@Override
		public void valueChanged(TreeSelectionEvent te) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) client.getExplorer().getTree().getLastSelectedPathComponent();

			if (selectedNode.getChildCount() > 0){
				client.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
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
				client.setCursor(Cursor.getDefaultCursor());
			}
		}

	}
}
