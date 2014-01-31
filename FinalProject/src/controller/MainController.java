package controller;

import gui.Client;
import gui.components.Auswertung;
import gui.components.Bestellpositionen;
import gui.components.DBOutput;
import gui.components.Explorer;
import gui.components.MainMenuBar;
import gui.components.Transaktionen;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import main.Main;
import model.Configuration;
import model.DB;
import model.OutputTableModel;
import utils.NotExistInDatabaseException;

/**
 * Controller-Klasse. Faengt alle Events ab, die in der GUI ausgeloest werden und verarbeitet diese. Greift auf die DB-Klasse zu und aendert die Anzeige der GUI.
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
	}

	/**
	 * Intitialisiert alle EventListener.
	 */
	public void initListeners(){
		addTransaktionenListeners(this.client.getTransaktionen());
		addOutputListeners(this.client.getDBOutput());
		addExplorerListeners(this.client.getExplorer());
		addAuswertungenListeneres(this.client.getAuswertung());
		addMenuListeners(this.client.getMenu());
	}
	/**
	 * Registriert Listener fuer das Menue.
	 * @param component
	 */
	void addMenuListeners(Component component){
		Iterator<Entry<String, Component>> it = ((MainMenuBar) component).getComponentMap().entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, Component> pairs = (Map.Entry<String, Component>)it.next();
			if (pairs.getValue() instanceof JMenuItem){
				this.client.getMenu().addActionListeners(pairs.getValue(), new ActionEventListener());
			}
		}
	}

	/**
	 * Registriert Listener fuer das Transaktionen-Panel.
	 * @param component
	 */
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

	/**
	 * Registriert Listener fuer das Auswertungen-Panel.
	 * @param component
	 */
	void addAuswertungenListeneres(Component component){
		Iterator<Entry<String, Component>> it = ((Auswertung) component).getComponentMap().entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, Component> pairs = (Map.Entry<String, Component>)it.next();
			if (pairs.getValue() instanceof JButton){
				this.client.getAuswertung().addActionListeners(pairs.getValue(), new ActionEventListener());
			}
		}
	}

	/**
	 * Registriert Listener fuer den DB-Explorer.
	 * @param component
	 */
	void addExplorerListeners(Component component){
		Iterator<Entry<String, Component>> it = ((Explorer) component).getComponentMap().entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, Component> pairs = (Map.Entry<String, Component>)it.next();
			if (pairs.getValue() instanceof JTree){
				this.client.getExplorer().addTreeSelectionListeners(pairs.getValue(), new TreeEventListener());
			}
		}
	}

	/**
	 * Registriert Listener fuer den DB-Output.
	 * @param component
	 */
	void addOutputListeners(Component component){
		Iterator<Entry<String, Component>> it = ((DBOutput) component).getComponentMap().entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, Component> pairs = (Map.Entry<String, Component>)it.next();
			if (pairs.getValue() instanceof JButton){
				this.client.getDBOutput().addActionListeners(pairs.getValue(), new ActionEventListener());
			}
		}
	}

	/**
	 * Erstellt eine Map mit allen Componenten des Containers.
	 * @param component
	 */
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

	/**
	 * Gibt die Componente zurueck, die den Namen name hat.
	 * @param name
	 * @return
	 */
	public Component getComponentByName(String name) {
		if (this.componentMap.containsKey(name)) {
			return (Component) this.componentMap.get(name);
		} else
			return null;
	}

	/**
	 * Eigene Implemntierung einer ActionListner-Klasse, welche alle Actionevent abfaengt und verarbeitet.
	 * @author borecki, dang
	 *
	 */
	class ActionEventListener implements ActionListener {

		@SuppressWarnings("unchecked")
		@Override
		public void actionPerformed(ActionEvent ae) {

			/* ##########################*/
			/* ###### Kundenpflege  #####*/
			/* ##########################*/

			//-----------------  KUNDENPFLEGE - NEUEN KUNDEN ANLEGEN - AUSFUEHREN BUTTON
			String msg_invalid_customerName = "<html>Der Kundenname darf nur Buchstaben enthalten, z.B. Konrad Zuse, Alibaba usw.<br />"
					+ "Wortkombinationen wie '3ZF8', 'X11', 'S0loX' und Sonderzeichen sind unterbunden.</html>";
			if ( ae.getActionCommand() == COMPONENT_BUTTON_KUNDENPFLEGE_NEU_AUSFUEHREN ) {
				String kID = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KID)).getText();
				if ( !isValidKID(kID) )
					return;

				try {
					if ( db.checkIfElementExists(TABLE_KUNDE, "kid", kID) ) {
						JOptionPane.showMessageDialog(client, "<html>Der Kunde mit der Kunden-ID " + kID + " ist bereits vorhanden.");
						((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KID)).setText("" + db.getKundenID());
						this.clearInputComponentsOfKundeNeuPflege();
						return;
					}
				} catch (HeadlessException e) {
					client.showException(e);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				if ( Integer.parseInt(kID) == db.getBufferedKundenID() )
					db.needNextKundenID(true); // DB darf wieder naechsten KID liefern.

				String kName = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_NAME)).getText();
				if (kName.length() > 0) {
					if(!isValidName(kName, msg_invalid_customerName)){
						return;
					}
					if(!checkStringLength(kName,25)){
						return;
					}
				}
				String kAdresse = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_ADRESSE)).getText();
				if (kAdresse.length() > 0){
					if(!checkStringLength(kAdresse,40)){
						return;
					}
				}
				String kTelNr = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_TEL)).getText();
				if (kTelNr.length() > 0){
					if(!checkStringLength(kTelNr,15)){
						return;
					}
				}
				String kBranche = ((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE)).getSelectedItem().toString();
				if (kBranche.length() > 0) {
					if(!isValidBranche(kBranche)){
						return;
					}
					if(!checkStringLength(kBranche,30)){
						return;
					}
				}
				String kNation = ((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_NATION)).getSelectedItem().toString();

				// Check, if the textfields are empty and have to be filled.
				String[] kundenDaten = { kName, kAdresse, kTelNr, kBranche, kNation };
				for ( String datum : kundenDaten ) {
					if ( datum.replaceAll("\\s+", "").isEmpty() ) {
						JOptionPane.showMessageDialog(client, KUNDENPFLEGE_MESSAGE_FILL_ALL_FIELDS);
						return;
					}
				}

				// Trying to insert new customer.
				try {
					db.insertKunde(kID, kName, kAdresse, kTelNr, kBranche, kNation);
					JOptionPane.showMessageDialog(client, "<html>Neuer Kunde mit Kunden-ID " + kID + " wurde erstellt. </html>");
					((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KID)).setText("" + db.getKundenID());
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(client, e.getClass().getName() + " : " + e.getMessage());
					client.showException(e);
				}
				this.clearInputComponentsOfKundeNeuPflege();

    			// JComboBox 'Branche' aktualisieren, d.h. die Liste um neue (eingegebenen) Branchen erweitern.
    			final DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<String>();
    			Vector<Vector<Object>> values = null;
    			try {
    				values = db.selectFromTable(TABLE_KUNDE, new String[] {"Branche"}, "");
    			} catch ( SQLException ex ) {
    				ex.printStackTrace();
    			}
    			for ( int i = 0; i < values.size(); i++ ) {
    				comboModel.addElement( values.get(i).get(0).toString().trim() );
    				// Mit der unteren Zeile ist eleganter, die JComboBox-Liste zu erweitern. Aber da muessen wir die Struktur in GridBagTemplate aendern.
    				// Und das wollten wir nicht. Daher die obere Zeile. Cheers.
    				// ((JComboBox<String>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE)).addItem(values.get(i).get(0).toString());
    			}
    			((JComboBox<String>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE)).setModel(comboModel);


				client.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				OutputTableModel tableModel = null;
				String table = TABLE_KUNDE;
				try {
				    tableModel = (OutputTableModel) client.getDBOutput().populateTable("SELECT * FROM " +TABLE_OWNER+ "." + table + " WHERE ROWNUM <= 100 ORDER BY 1 DESC");
				} catch (SQLException e) {
					client.showException(e);
				}
				client.getDBOutput().setFilterTable(table);
				client.getDBOutput().removeScrollPane();
				client.getDBOutput().addTableModel(tableModel);
				client.getDBOutput().addTableToPane();
				client.revalidate();
				client.repaint();
				client.setCursor(Cursor.getDefaultCursor());
//    			final DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<String>();
//    			Vector<Vector<Object>> values = null;
//    			try {
//    				values = db.selectFromTable(TABLE_KUNDE, new String[] {"Branche"}, "");
//    			} catch ( SQLException ex ) {
//    				ex.printStackTrace();
//    			}
//    			for ( int i = 0; i < values.size(); i++ ) {
//    				comboModel.addElement( values.get(i).get(0).toString().trim() );
//    				// Mit der unteren Zeile ist eleganter, die JComboBox-Liste zu erweitern. Aber da muessen wir die Struktur in GridBagTemplate aendern.
//    				// Und das wollten wir nicht. Daher die obere Zeile. Cheers.
//    				// ((JComboBox<String>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE)).addItem(values.get(i).get(0).toString());
//    			}
//    			((JComboBox<String>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE)).setModel(comboModel);
//    			client.repaint();
				ItemEventListener iel = new ItemEventListener();
				iel.refreshComboBoxBranche(0);
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
					System.out.println(((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_BRANCHE)).getItemCount());
					for (int i = 0; i < ((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_BRANCHE)).getItemCount(); i++){
						if (((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_BRANCHE)).getItemAt(i).equals(((String) values.get(5)).trim())){
							((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_BRANCHE)).setSelectedIndex(i);
						}
					}

					((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_NATION)).setSelectedItem(mapToName(((BigDecimal) values.get(6)).intValue()));

					this.setInputComponentsOfKudenpflegeEditEditable(true);
					this.setInputComponentsOfKudenpflegeEditEnabled(true);
					((JButton) client.getComponentByName(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN)).setEnabled(true);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(client, e.getClass().getName() + " : " + e.getMessage());
					client.showException(e);
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
					client.showException(e);
				}
			}

			//----------------- KUNDENPFLEGE - KUDNEN AENDERN - FERTIG BUTTON
			if ( ae.getActionCommand() == COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN_FERTIG ) {
				String kID = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KID)).getText();
				String kName = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_NAME)).getText();
				if (kName.length() > 0) {
					if(!isValidName(kName, msg_invalid_customerName)){
						return;
					}
					if(!checkStringLength(kName, 25)){
						return;
					}
				}
				String kAdresse = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_ADRESSE)).getText();
				if (kAdresse.length() > 0) {
					if(!checkStringLength(kAdresse, 40)){
						return;
					}
				}
				String kTelNr = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_TEL)).getText();
				if (kTelNr.length() > 0) {
					if(!checkStringLength(kTelNr, 15)){
						return;
					}
				}
				double kKonto = ((Number) ((JFormattedTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KONTO)).getValue()).doubleValue();
				String kBranche = ((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_BRANCHE)).getSelectedItem().toString();
				if (kBranche.length() > 0) {
					if(!isValidBranche(kBranche)){
						return;
					}
					if(!checkStringLength(kBranche, 30)){
						return;
					}
				}
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
					client.showException(e);
				}
				finally {
					((JButton) client.getComponentByName(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN_FERTIG)).setVisible(false);
					((JButton) client.getComponentByName(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN)).setVisible(true);
					this.setInputComponentsOfKudenpflegeEditEditable(false);
					this.setInputComponentsOfKudenpflegeEditEnabled(false);
				}

				client.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				OutputTableModel tableModel = null;
				String table = TABLE_KUNDE;
				try {
				    tableModel = (OutputTableModel) client.getDBOutput().populateTable("SELECT * FROM " +TABLE_OWNER+ "." + table + " WHERE ROWNUM <= 100 ORDER BY 1 DESC");
				} catch (SQLException e) {
					client.showException(e);
				}
				client.getDBOutput().setFilterTable(table);
				client.getDBOutput().removeScrollPane();
				client.getDBOutput().addTableModel(tableModel);
				client.getDBOutput().addTableToPane();
				client.revalidate();
				client.repaint();
				client.setCursor(Cursor.getDefaultCursor());
			}



			/* ##############################*/
			/* ##### Produktverwaltung  #####*/
			/* ##############################*/

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
					client.showException(ne);
					return;
				} catch ( SQLException e ) {
					client.showException(e);
					return;
				}

				String msg = "";
				if ( !success ) {
					msg = "<html>Die Zulieferung mit der ID " + zlid + " kann nicht mehr eingebucht werden. Sie wurde bereits erledigt.</html>";
				} else {
					msg = "<html>Die Zuliefeung mit der ID " + zlid + " wurde erfolgreich eingebucht.</html>";
				}
				JOptionPane.showMessageDialog(client, msg);

				client.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				OutputTableModel tableModel = null;
				String table = TABLE_ZULIEFERUNG;
				try {
				    tableModel = (OutputTableModel) client.getDBOutput().populateTable("SELECT * FROM " +TABLE_OWNER+ "." + table + " WHERE ROWNUM <= 100 ORDER BY 1 DESC");
				} catch (SQLException e) {
					client.showException(e);
				}
				client.getDBOutput().setFilterTable(table);
				client.getDBOutput().removeScrollPane();
				client.getDBOutput().addTableModel(tableModel);
				client.getDBOutput().addTableToPane();
				client.revalidate();
				client.repaint();
				client.setCursor(Cursor.getDefaultCursor());
			}

			//----------------- PRODUKTVERWALTUNG - BESTAND UMBUCHEN
			if ( ae.getActionCommand() == COMPONENT_BUTTON_PRODUKTVERWALTUNG_EDIT_UMBUCHEN ) {
				String srcLager = ((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_PRODUKTVERWALTUNG_EDIT_SRCLAGER)).getSelectedItem().toString();
				String destLager = ((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_PRODUKTVERWALTUNG_EDIT_DESTLAGER)).getSelectedItem().toString();
				String pid  = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_PRODUKT)).getText();
				String menge = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_MENGE)).getText();

				try {
					if ( !db.checkIfElementExists(TABLE_LAGER, "lagid", "'"+srcLager+"'") ) {
						JOptionPane.showMessageDialog(client, "<html>Es existiert kein Lager mit der ID '" + srcLager + "'.</html>");
						return;
					}
					if ( !db.checkIfElementExists(TABLE_LAGER, "lagid", "'"+destLager+"'") ) {
						JOptionPane.showMessageDialog(client, "<html>Es existiert kein Lager mit der ID '" + destLager + "'.</html>");
						return;
					}
					if ( !db.checkIfElementExists(TABLE_PRODUKT, "pid", "'"+pid+"'") ) {
						JOptionPane.showMessageDialog(client, "<html>Es existiert kein Produkt mit der ID '" + pid + "'.</html>");
						return;
					}
				} catch (HeadlessException e) {
					client.showException(e);
				} catch (SQLException e) {
					client.showException(e);
				}

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
					// nclient.showException(e);
					return;
				} catch ( SQLException e ) {
					client.showException(e);
					return;
				}

				String msg = "";
				if ( success != -999 ) {
					msg = "<html>Bestand reicht nicht aus! Nur " + success + " St&uuml;ck vorr&auml;tig.</html>";
				} else {
					msg = "<html>Die Best&auml;nde wurden erfolgreich vom Lager " + srcLager + " auf Ziellager " + destLager + " umgebucht.</html>";
				}
				JOptionPane.showMessageDialog(client, msg);

				client.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				OutputTableModel tableModel = null;
				String table = TABLE_LAGERT;
				try {
				    tableModel = (OutputTableModel) client.getDBOutput().populateTable("SELECT * FROM " +TABLE_OWNER+ "." + table + " WHERE pid = " + pid + " AND ROWNUM <= 100 ORDER BY 1 DESC");
				} catch (SQLException e) {
					client.showException(e);
				}
				client.getDBOutput().setFilterTable(table);
				client.getDBOutput().removeScrollPane();
				client.getDBOutput().addTableModel(tableModel);
				client.getDBOutput().addTableToPane();
				client.revalidate();
				client.repaint();
				client.setCursor(Cursor.getDefaultCursor());
			}

			/* ###############################*/
			/* ###### Bestellverwaltung  #####*/
			/* ###############################*/

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
				if (anleger.length() > 0) {
					if(!isValidInput(anleger)){
						return;
					}
					if(!checkStringLength(anleger, 12)){
						return;
					}
				}
				String bsttermin = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTERMIN)).getText();
				if (bsttermin.length() > 0) {
					if(!isValidDate(bsttermin)){ // checkt ob der bestelltermin den vorgaben entspricht
						return;
					}
				}
				String bsttext = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTEXT)).getText();
				if (bsttext.length() > 0) {
					if(!isValidInput(bsttext)){
						return;
					}
				}

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
							JOptionPane.showMessageDialog(client, "Kunde nicht existiert.");
							return;
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
				client.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				try {
					db.bestellungSpeichern(bstid, bsttext, anleger, "OFFEN", db.dateFormat(bsttermin), bstKid, bstpos);
					JOptionPane.showMessageDialog(client, "<html>Neue Bestellung mit der Bestellungs-ID " + bstid + " wurde erstellt. </html>");
				} catch (SQLException e) {
					client.showException(e);
					return;
				} catch (NotExistInDatabaseException e) {
					client.showException(e);
					return;
				} catch (ParseException e) {
					client.showException(e);
				}

				Vector<Vector<Object>> bestellKopf = null;
				try {
					bestellKopf = db.selectFromTable(TABLE_BESTELLUNG, "bstid = " + bstid);
				} catch (SQLException e) {
					client.showException(e);
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


				OutputTableModel tableModel = null;
				String table = TABLE_BESTELLUNG;
				try {
				    tableModel = (OutputTableModel) client.getDBOutput().populateTable("SELECT * FROM " +TABLE_OWNER+ "." + table + " WHERE ROWNUM <= 100 ORDER BY 1 DESC");
				} catch (SQLException e) {
					client.showException(e);
				}
				client.getDBOutput().setFilterTable(table);
				client.getDBOutput().removeScrollPane();
				client.getDBOutput().addTableModel(tableModel);
				client.getDBOutput().addTableToPane();
				client.revalidate();
				client.repaint();
				client.setCursor(Cursor.getDefaultCursor());
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
				if (anleger.length() > 0) {
					if(!isValidInput(anleger)){
						return;
					}
				}
				String bsttermin = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTERMIN)).getText();
				if (bsttermin.length() > 0) {
					if(!isValidDate(bsttermin)){ // checkt ob der bestelltermin den vorgaben entspricht
						return;
					}
				}
				String bsttext = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTEXT)).getText();
				if (bsttext.length() > 0) {
					if(!isValidInput(bsttext)){
						return;
					}
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
				client.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
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

					OutputTableModel tableModel = null;
					String table = TABLE_BESTELLUNG;
					try {
					    tableModel = (OutputTableModel) client.getDBOutput().populateTable("SELECT * FROM " +TABLE_OWNER+ "." + table + " WHERE ROWNUM <= 100 ORDER BY 1 DESC");
					} catch (SQLException e) {
						client.showException(e);
					}
					client.getDBOutput().setFilterTable(table);
					client.getDBOutput().removeScrollPane();
					client.getDBOutput().addTableModel(tableModel);
					client.getDBOutput().addTableToPane();
					client.revalidate();
					client.repaint();
				} else {
					int inputPrompt = JOptionPane.showConfirmDialog(client, "<html>Wir k&ouml;nnen den von Ihnen gew&uuml;nschten Lieferungstermin leider nicht best&auml;tigen. <br><br> Wollen Sie Ihren Liefertermin nach hinten verschieben? <b>Vorsicht:</b> Falls nicht wird Ihre Bestellung verworfen.</html>", "Bestellbestätigung", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
					client.repaint();
					if (inputPrompt == 1){
						try {
							db.bestellungLoeschen(bstid);
						} catch (NotExistInDatabaseException e) {
							client.showException(e);
						} catch (SQLException e) {
							client.showException(e);
						} catch (ParseException e) {
							client.showException(e);
						}
						this.clearInputComponentsOfBestellverwaltungNeu();
					}
				}
				client.setCursor(Cursor.getDefaultCursor());
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

			//----------------- BESTELLVERWALTUNG - BESTELLUNG AENDERN - SUCHEN BUTTON
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
						this.clearInputComponentsOfBestellverwaltungEdit();
						return;
					}

					// Bestellkopf ausfuellen
					Vector<Vector<Object>> bestellKopf = db.selectFromTable(TABLE_BESTELLUNG, "bstid = " + bstID);
					Iterator<Vector<Object>> itKopf = bestellKopf.iterator();

					client.invalidate();
					while (itKopf.hasNext()){
						Vector<Object> v = itKopf.next();
						((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTEXT)).setText((String) ((v.get(1) == null) ? "" : (v.get(1))));
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
						((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER)).setEditable(false);
						((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER)).setEnabled(false);
						((JButton) client.getComponentByName(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_SPEICHERN)).setEnabled(true);
						((JButton) client.getComponentByName(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_BESTAETIGEN)).setEnabled(true);
						((Bestellpositionen) client.getTransaktionen().getPosEdit()).getComponentByName("delEdit").setEnabled(false);
					}
				} catch (SQLException e) {
					client.showException(e);
				}
			}

			//----------------- BESTELLVERWALTUNG - BESTELLUNG AENDERN - SPEICHERN BUTTON
			if (ae.getActionCommand() == COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_SPEICHERN){

				String bstid = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTID)).getText();
				try {
					if ( !db.checkIfElementExists(TABLE_BESTELLUNG, "bstid", bstid) ) {
						JOptionPane.showMessageDialog(client, "<html>Die Bestell-ID " + bstid + " ist nicht in der Datenbank vorhanden. <br> Bitte wechseln Sie zu \"Bestellung anlegen\" falls Sie eine neue Bestellung erstellen wollen.</html>");
						return;
					}
				} catch (HeadlessException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				if (client.getTransaktionen().getPosEdit().getListModel().getSize() == 0){
					JOptionPane.showMessageDialog(client, "<html>Bitte geben Sie mind. eine Bestellposition an.</html>");
					return;
				}

				// Bestellkopf
//				String bstid = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTID)).getText();
				String bstKid = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_KID)).getText();
				String anleger = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER)).getText();
				if (anleger.length() > 0) {
					if(!isValidInput(anleger)){
						return;
					}
				}
				String bsttermin = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTERMIN)).getText();
				if (bsttermin.length() > 0) isValidDate(bsttermin); // checkt ob der bestelltermin den vorgaben entspricht
				String bsttext = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTEXT)).getText();
				if (bsttext.length() > 0) {
					if(!isValidInput(bsttext)){
						return;
					}
				}

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

				if ( !isValidDate(bsttermin) ) {
					return;
				}

				// Trying to insert new order.
				client.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				boolean success = false;
				try {
					success = db.bestellungAendern(bstid, bsttext, anleger, db.dateFormat(bsttermin), bstKid, bstpos);

					if (success == true){
						JOptionPane.showMessageDialog(client, "<html>Die Bestellung mit der ID " + bstid + " wurde ge&auml;ndert. </html>");
					} else {
						JOptionPane.showMessageDialog(client, "<html>Die Bestellung konnte nicht ge&auml;ndert werden, da der Bestelltermin nicht eingehalten werden kann.</html>");
					}

					OutputTableModel tableModel = null;
					String table = TABLE_BESTELLUNG;
					try {
					    tableModel = (OutputTableModel) client.getDBOutput().populateTable("SELECT * FROM " +TABLE_OWNER+ "." + table + " WHERE ROWNUM <= 100 ORDER BY 1 DESC");
					} catch (SQLException e) {
						client.showException(e);
					}
					client.getDBOutput().setFilterTable(table);
					client.getDBOutput().removeScrollPane();
					client.getDBOutput().addTableModel(tableModel);
					client.getDBOutput().addTableToPane();
					client.revalidate();
					client.repaint();
				} catch (SQLException e) {
					client.showException(e);
					return;
				} catch (NotExistInDatabaseException e) {
					client.showException(e);
					return;
				} catch (ParseException e) {
					client.showException(e);
				}
				client.setCursor(Cursor.getDefaultCursor());
			}

			//----------------- BESTELLVERWALTUNG - BESTELLUNG AENDERN - BESTAETIGEN BUTTON
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
				if (bsttext.length() > 0) {
					if(!isValidInput(bsttext)){
						return;
					}
				}

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
				client.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
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
					this.clearInputComponentsOfBestellverwaltungEdit();
					((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER)).setEditable(true);
					((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER)).setEnabled(true);

					OutputTableModel tableModel = null;
					String table = TABLE_BESTELLUNG;
					try {
					    tableModel = (OutputTableModel) client.getDBOutput().populateTable("SELECT * FROM " +TABLE_OWNER+ "." + table + " WHERE ROWNUM <= 100 ORDER BY 1 DESC");
					} catch (SQLException e) {
						client.showException(e);
					}
					client.getDBOutput().setFilterTable(table);
					client.getDBOutput().removeScrollPane();
					client.getDBOutput().addTableModel(tableModel);
					client.getDBOutput().addTableToPane();
					client.revalidate();
					client.repaint();

				} else {
					int inputPrompt = JOptionPane.showConfirmDialog(client, "<html>Wir k&ouml;nnen den von Ihnen gew&uuml;nschten Lieferungstermin leider nicht best&auml;tigen. <br><br> Wollen Sie Ihren Liefertermin nach hinten verschieben? <b>Vorsicht:</b> Falls nicht wird Ihre Bestellung verworfen!</html>", "Bestellbestätigung", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
					client.repaint();
					if (inputPrompt == 1){
						try {
							db.bestellungLoeschen(bstid);
						} catch (NotExistInDatabaseException e) {
							client.showException(e);
						} catch (SQLException e) {
							client.showException(e);
						} catch (ParseException e) {
							client.showException(e);
						}
						this.clearInputComponentsOfBestellverwaltungEdit();
					}
				}
				client.setCursor(Cursor.getDefaultCursor());
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
						JOptionPane.showMessageDialog(client, "<html>Die Bestell-ID " + bstid + " ist nicht in der Datenbank vorhanden. <br> Bitte wechseln Sie zu \"Bestellung anlegen\" falls Sie eine neue Bestellung erstellen wollen.</html>");
						return;
					}

					boolean success = db.bestellungAusliefern(bstid);
					String msg = "";
					if ( !success ) {
						msg = "<html>Die Bestellung mit der ID " + bstid + " kann nicht beliefert werden.</html>";
					} else {
						msg = "<html>Die Bestellung mit der ID " + bstid + " wurde erfolgreich beliefert.</html>";

						OutputTableModel tableModel = null;
						String table = TABLE_BESTELLUNG;
						try {
						    tableModel = (OutputTableModel) client.getDBOutput().populateTable("SELECT * FROM " +TABLE_OWNER+ "." + table + " WHERE ROWNUM <= 100 ORDER BY 1 DESC");
						} catch (SQLException e) {
							client.showException(e);
						}
						client.getDBOutput().setFilterTable(table);
						client.getDBOutput().removeScrollPane();
						client.getDBOutput().addTableModel(tableModel);
						client.getDBOutput().addTableToPane();
						client.revalidate();
						client.repaint();

					}
					JOptionPane.showMessageDialog(client, msg);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(client, e.getClass().getName() + " : " + e.getMessage());
					client.showException(e);
				} catch (NotExistInDatabaseException e) {
					client.showException(e);
				} catch (ParseException e) {
					client.showException(e);
				}
			}

			/* ###############################*/
			/* ######## Auswertungen  ########*/
			/* ###############################*/

			//----------------- PRODUKTANALYSE
			if ( ae.getActionCommand() == COMPONENT_BUTTON_PRODUKTANALYSE_AUSFUEHREN ) {
				String typ = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_PRODUKTANALYSE_TYP)).getText();
				if (typ.length() > 0) {
					if(!isValidInput(typ)){
						return;
					}
				}
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
					client.showException(e);
				} catch (NotExistInDatabaseException e) {
					client.showException(e);
				}
				client.setCursor(Cursor.getDefaultCursor());
			}

			//----------------- LIEFERKOSTENSENKUNG
			if ( ae.getActionCommand() == COMPONENT_BUTTON_LIEFERKOSTEN_AUSFUEHREN ) {

				String produkt = ((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_LIEFERKOSTEN_PRODUKT)).getText();
				if (produkt.length() > 0) {
					if(!isValidInput(produkt)){
						return;
					}
				}
				try {
					if ( !db.checkIfElementExists(TABLE_PRODUKT, "pid", "'"+produkt+"'") ) {
						JOptionPane.showMessageDialog(client, "<html>Es existiert kein Produkt mit der ID '" + produkt + "'.</html>");
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
					client.showException(e);
				} catch (NotExistInDatabaseException e) {
					client.showException(e);
				}
				client.setCursor(Cursor.getDefaultCursor());
			}

			/* ###############################*/
			/* ########### MenuBar  ##########*/
			/* ###############################*/

			if (ae.getActionCommand() == COMPONENT_ITEM_MENU_LOGOUT){
				int inputPrompt = JOptionPane.showConfirmDialog(client, "Wollen Sie sich wirklich abmelden?", "Haufkof Client - Abmelden", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
				client.repaint();
				if (inputPrompt == 0){
					client.close();
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
					client.close();
					System.exit(0);
				}
			}

			if (ae.getActionCommand() == COMPONENT_ITEM_MENU_INFO){
				JOptionPane.showMessageDialog(client, "<html>Haufkof-Client, Finale Version <br><br> by Son Dang, <br> Darius Borecki, <br> Viktoria Pleintiger </html>", "Haufkof Client - Info", JOptionPane.INFORMATION_MESSAGE, null);
				client.repaint();
			}

			/* ###############################*/
			/* ########## DBOutput  ##########*/
			/* ###############################*/

			if (ae.getActionCommand() == "button_dboutput_filter"){
				client.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				OutputTableModel tableModel = null;
				String table = client.getDBOutput().getFilterTable();
				String query = ((JTextField) client.getDBOutput().getComponentByName("textfield_dboutput_filter")).getText();
				try {
					if (query.length() == 0)
				        tableModel = (OutputTableModel) client.getDBOutput().populateTable("SELECT * FROM " +TABLE_OWNER+ "." + table + " WHERE ROWNUM <= 1000");
					else if (query.contains("select") && query.contains("from"))
						tableModel = (OutputTableModel) client.getDBOutput().populateTable(query);
					else
						tableModel = (OutputTableModel) client.getDBOutput().populateTable("SELECT * FROM " +TABLE_OWNER+ "." + table + " WHERE " + query);
				} catch (SQLException e) {
					client.showException(e);
				}
				client.getDBOutput().setFilterTable(table);
				client.getDBOutput().removeScrollPane();
				client.getDBOutput().addTableModel(tableModel);
				client.getDBOutput().addTableToPane();
				client.revalidate();
				client.repaint();
				client.setCursor(Cursor.getDefaultCursor());
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
		 *
		 * @param input
		 * @param msg
		 * @return
		 */
		public boolean isValidName(String input, String msg) {
			boolean result = Pattern.matches("(\\p{Alpha}+\\s*)+", input);
			if ( !result ) {
				JOptionPane.showMessageDialog(client, msg);
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

		@SuppressWarnings("unused")
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

		@SuppressWarnings("unused")
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
			((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE)).setSelectedIndex(0);
			((JComboBox<?>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_NATION)).setSelectedIndex(0);
			client.revalidate();
			client.repaint();
		}

		private void setInputComponentsOfBestellverwaltungEditEditable(boolean b) {
			client.invalidate();
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTEXT)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTERMIN)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_KID)).setEditable(b);
			((JTextField) ((Bestellpositionen) client.getTransaktionen().getPosEdit()).getComponentByName("inpEdit")).setEditable(b);
			client.revalidate();
			client.repaint();
		}

		private void setInputComponentsOfBestellverwaltungEditEnabled(boolean b) {
			client.invalidate();
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTEXT)).setEnabled(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER)).setEnabled(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTERMIN)).setEnabled(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_KID)).setEnabled(b);
			((JTextField) ((Bestellpositionen) client.getTransaktionen().getPosEdit()).getComponentByName("inpEdit")).setEnabled(b);
			((JButton) ((Bestellpositionen) client.getTransaktionen().getPosEdit()).getComponentByName("delEdit")).setEnabled(b);
			client.revalidate();
			client.repaint();
		}

		@SuppressWarnings("unused")
		private void setInputComponentsOfBestellverwaltungNeuEditable(boolean b) {
			client.invalidate();
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTEXT)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_ANLEGER)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTERMIN)).setEditable(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_KID)).setEditable(b);
			((JTextField) ((Bestellpositionen) client.getTransaktionen().getPosEdit()).getComponentByName("inpNeu")).setEditable(b);
			client.revalidate();
			client.repaint();
		}

		@SuppressWarnings("unused")
		private void setInputComponentsOfBestellverwaltungNeuEnabled(boolean b) {
			client.invalidate();
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTEXT)).setEnabled(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_ANLEGER)).setEnabled(b);
			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTERMIN)).setEnabled(b);
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
			DefaultListModel<String> listModel = new DefaultListModel<String>();
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
			DefaultListModel<String> listModel = new DefaultListModel<String>();
			client.getTransaktionen().getPosEdit().removeList();
			client.getTransaktionen().getPosEdit().setModel(listModel);
			client.getTransaktionen().getPosEdit().addListToPane("listEdit",-1);
			client.getTransaktionen().getPosEdit().addModel();
			client.revalidate();
			client.repaint();
		}

		private boolean isValidDate(String inputDate){
			boolean result = Pattern.matches("\\d{2}\\.\\d{2}\\.\\d{2}$",inputDate);
			if ( !result ){
				JOptionPane.showMessageDialog(client, BESTELLVERWALTUNG_MESSAGE_INVALID_BSTTERMIN);
			}
			return result;
		}

		private boolean isValidInput(String input){
			boolean result = Pattern.matches("(?=.*\\S)[a-zA-Z0-9\\s]*",input);
			if ( !result ){
				JOptionPane.showMessageDialog(client, "<html>Bitte verwenden Sie nur Ziffern und Buchstaben f&uuml;r die Eingabe.</html>");
			}
			return result;
		}

		private boolean isValidBranche(String input){
			boolean result = Pattern.matches("[A-Z]*",input);
			if ( !result ){
				JOptionPane.showMessageDialog(client, "<html>Bitte verwenden Sie nur Gro&szlig;buchstaben f&uuml;r die Eingabe der Branche.</html>");
			}
			return result;
		}

		private boolean checkStringLength(String input, int length){
			boolean result = (input.length() > length);
			if ( result ){
				JOptionPane.showMessageDialog(client, "<html>Der Input '" + input + "' &uuml;berschreitet die maxile L&auml;nge von " + length + " Zeichen.</html>");
			}
			return !result;
		}
	}

	/**
	 * Eigene Implemntierung einer ItemListner-Klasse, welche alle Itemevents abfaengt und verarbeitet.
	 * @author borecki
	 *
	 */
	class ItemEventListener implements ItemListener{
	    @SuppressWarnings("unchecked")
		@Override
	    public void itemStateChanged(ItemEvent ie) {
	    	if (ie.getStateChange() == ItemEvent.SELECTED && ((Component) ie.getSource()).getName().equals(COMPONENT_COMBO_KUNDENPFLEGE_ACTIONS)){

	    		CardLayout cl = (CardLayout) ((Container) client.getTransaktionen().getComponentByName(COMPONENT_PANEL_KUNDENPFLEGE)).getLayout();
	    		cl.show(((Container) client.getTransaktionen().getComponentByName(COMPONENT_PANEL_KUNDENPFLEGE)), (String) ie.getItem());

	    		// NEUEN KUNDEN ANLEGEN
	    		if ((int) ((JComboBox<?>) ie.getSource()).getSelectedIndex() == 1)
	    		{
	    			// Neue KundenID vorschlagen.
	    			((JTextField) client.getComponentByName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KID)).setText(""+db.getKundenID());
	    			// JComboBox 'Branche' aktualisieren, d.h. die Liste um neue (eingegebenen) Branchen erweitern.
	    			refreshComboBoxBranche(0); // 0 fuer den JComboBox im Reiter "Neuen Kunden anlegen".
	    		}

	    		// KUDEN AENDERN
	    		if ((int) ((JComboBox<?>) ie.getSource()).getSelectedIndex() == 2)
	    		{
	    			// JComboBox 'Branche' aktualisieren, d.h. die Liste um neue (eingegebenen) Branchen erweitern.
	    			refreshComboBoxBranche(1); // 1 fuer den JComboBox im Reiter "Kunden aendern".
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

	    /**
	     * Diese Methode aktualisiert und erweitert die JComboBox-Liste "Branche" um neue eingegebenen Items.
	     *
	     * @param type : 0 f&uuml;r den JComboBox im Reiter "Neuen Kunden anlegen", 1 f&uuml;r den JComboBox im Reiter "Kunden &auml;ndern".
	     */
	    @SuppressWarnings("unchecked")
		public void refreshComboBoxBranche(int type) {
			// JComboBox 'Branche' aktualisieren, d.h. die Liste um neue (eingegebenen) Branchen erweitern.
			final DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<String>();
			Vector<Vector<Object>> values = null;
			try {
				values = db.selectFromTable(TABLE_KUNDE, new String[] {"Branche"}, "");
			} catch ( SQLException ex ) {
				ex.printStackTrace();
			}
			for ( int i = 0; i < values.size(); i++ ) {
				comboModel.addElement(values.get(i).get(0).toString().trim());
				// Mit der unteren Zeile ist eleganter, die JComboBox-Liste zu erweitern. Aber da muessen wir die Struktur in GridBagTemplate aendern.
				// Und das wollten wir nicht. Daher die obere Zeile. Cheers.
				// ((JComboBox<String>) client.getComponentByName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE)).addItem(values.get(i).get(0).toString());
			}
			String comboBoxName = ( type == 0 ) ? COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE : COMPONENT_COMBO_KUNDENPFLEGE_EDIT_BRANCHE;
			((JComboBox<String>) client.getComponentByName(comboBoxName)).setModel(comboModel);
			client.repaint();
	    }
	}

	/**
	 * Eigene Implemntierung einer TreeEventListener-Klasse, welche alle TreeSelectionEvents abfaengt und verarbeitet.
	 * @author borecki
	 *
	 */
	class TreeEventListener implements TreeSelectionListener{
		@Override
		public void valueChanged(TreeSelectionEvent te) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) client.getExplorer().getTree().getLastSelectedPathComponent();

			if (selectedNode.getChildCount() > 0){
				client.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				OutputTableModel tableModel = null;
				String table = selectedNode.getUserObject().toString();
				try {
				    tableModel = (OutputTableModel) client.getDBOutput().populateTable("SELECT * FROM " +TABLE_OWNER+ "." + table + " WHERE ROWNUM <= 100 ORDER BY 1 DESC");
				} catch (SQLException e) {
					client.showException(e);
				}
				client.getDBOutput().setFilterTable(table);
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
