package gui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Configuration;

public class GridBagTemplate extends JPanel implements Configuration {

	private static final long serialVersionUID = 7966600021735934643L;
	protected HashMap<String, Component> componentMap;
	private int type;
	private String title;
	private String name;
	private Bestellpositionen pos;

	public GridBagTemplate(int type, String title, String name, boolean isScrollPane) {
		super(new GridBagLayout());
		this.type = type;
		this.title = title;
		this.name = name;

		this.setBorder(BorderFactory.createTitledBorder(this.title));
		this.setName(this.name);

		if (isScrollPane){
			JScrollPane scrollPane = new JScrollPane(createPanel(new JPanel(new GridBagLayout()),type),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setMinimumSize(new Dimension(500,500));


			addComponent(this,scrollPane, new Insets(0,0,0,0),0,0);
		}
		else {
			addComponent(this,createPanel(new JPanel(new GridBagLayout()),type), new Insets(0,0,0,0),0,0);
		}
		this.componentMap = new HashMap<String, Component>();
		createComponentMap(this);
	}

	public GridBagTemplate(int type, String title, String name, boolean isScrollPane, Bestellpositionen pos) {
		super(new GridBagLayout());
		this.type = type;
		this.title = title;
		this.name = name;
		this.pos = pos;
		this.setBorder(BorderFactory.createTitledBorder(this.title));
		this.setName(this.name);

		if (isScrollPane){
			JScrollPane scrollPane = new JScrollPane(createPanel(new JPanel(new GridBagLayout()),type),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setMinimumSize(new Dimension(500,500));


			addComponent(this,scrollPane, new Insets(0,0,0,0),0,0);
		}
		else {
			addComponent(this,createPanel(new JPanel(new GridBagLayout()),type), new Insets(0,0,0,0),0,0);
		}
		this.componentMap = new HashMap<String, Component>();
		createComponentMap(this);
	}

	public JPanel createPanel(JPanel container, int type) {

		switch (type) {
		case 0: // Template: Kunden anlegen
		{
			// KID, number, 10
			JLabel label_kid = new JLabel(KUNDENPFLEGE_LABEL_KID);
			JTextField _kid = new JTextField(10);
			_kid.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KID);
			addComponent(container, label_kid, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(container, _kid, new Insets(0, 5, 0, 5), 1, 0);

			// Name, string, 25
			JLabel label_name = new JLabel(KUNDENPFLEGE_LABEL_NAME);
			JTextField _name = new JTextField(10);
			_name.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_NAME);
			addComponent(container, label_name, new Insets(0, 5, 0, 5), 0, 1);
			addComponent(container, _name, new Insets(0, 5, 0, 5), 1, 1);

			// Adresse, char, 40
			JLabel label_adresse = new JLabel(KUNDENPFLEGE_LABEL_ADRESSE);
			JTextField _adresse = new JTextField(10);
			_adresse.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_ADRESSE);
			addComponent(container, label_adresse, new Insets(0, 5, 0, 5), 0, 2);
			addComponent(container, _adresse, new Insets(0, 5, 0, 5), 1, 2);

			// Tel, char, 15
			JLabel label_tel = new JLabel(KUNDENPFLEGE_LABEL_TEL);
			JTextField _tel = new JTextField(10);
			_tel.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_TEL);
			addComponent(container, label_tel, new Insets(0, 5, 0, 5), 0, 3);
			addComponent(container, _tel, new Insets(0, 5, 0, 5), 1, 3);

			// Konto, number, 12
			JLabel label_konto = new JLabel(KUNDENPFLEGE_LABEL_KONTO);
			NumberFormat format_konto = NumberFormat.getNumberInstance();
			format_konto.setMinimumFractionDigits(2);
			JFormattedTextField _konto = new JFormattedTextField(format_konto);
			_konto.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KONTO);
			_konto.setValue(new Double(0.00));
			_konto.setColumns(10);
			_konto.setEditable(false);
			addComponent(container, label_konto, new Insets(0, 5, 0, 5), 0, 4);
			addComponent(container, _konto, new Insets(0, 5, 0, 5), 1, 4);

			// Branche, char, 10
			JLabel label_branche = new JLabel(KUNDENPFLEGE_LABEL_BRANCHE);
			String[] branche_strings = { "AUTOMOBILE", "BUILDING", "FURNITURE",
					"HOUSEHOLD", "MACHINERY" };
			JComboBox<String> _branche = new JComboBox<String>(branche_strings);
			_branche.setName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE);
			_branche.setSelectedIndex(0);
			_branche.setEditable(true);
			addComponent(container, label_branche, new Insets(0, 5, 0, 5), 0, 5);
			addComponent(container, _branche, new Insets(0, 5, 0, 5), 1, 5);

			// Nation, char, 25
			// TODO: JComboBox mit Werten aus DB
			JLabel label_nation = new JLabel(KUNDENPFLEGE_LABEL_NATION);
			String[] nation_strings = { "Jemen", "Belize", "Neuseeland" };
			JComboBox<String> _nation = new JComboBox<String>(nation_strings);
			_nation.setName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_NATION);
			_nation.setSelectedIndex(0);
			_nation.setEditable(true);
			addComponent(container, label_nation, new Insets(0, 5, 0, 5), 0, 6);
			addComponent(container, _nation, new Insets(0, 5, 0, 5), 1, 6);

			// Ausfuehren
			JButton button_exec = new JButton(KUNDENPFLEGE_BUTTON_EXECUTE);
			button_exec.setName(COMPONENT_BUTTON_KUNDENPFLEGE_NEU_AUSFUEHREN);
			button_exec.setActionCommand(COMPONENT_BUTTON_KUNDENPFLEGE_NEU_AUSFUEHREN);
			addComponent(container, button_exec, new Insets(0,5,0,5), 2, 6);
		}
			return container;
		case 1: // Template: Kunden aendern
		{
			// KID, number, 10
			JLabel label_kid = new JLabel(KUNDENPFLEGE_LABEL_KID);
			JTextField _kid = new JTextField(10);
			_kid.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KID);
			addComponent(container, label_kid, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(container, _kid, new Insets(0, 5, 0, 5), 1, 0);

			// Name, string, 25
			JLabel label_name = new JLabel(KUNDENPFLEGE_LABEL_NAME);
			JTextField _name = new JTextField(10);
			_name.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_NAME);
			_name.setEditable(false);
			_name.setEnabled(false);
			addComponent(container, label_name, new Insets(0, 5, 0, 5), 0, 1);
			addComponent(container, _name, new Insets(0, 5, 0, 5), 1, 1);

			// Adresse, char, 40
			JLabel label_adresse = new JLabel(KUNDENPFLEGE_LABEL_ADRESSE);
			JTextField _adresse = new JTextField(10);
			_adresse.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_ADRESSE);
			_adresse.setEditable(false);
			_adresse.setEnabled(false);
			addComponent(container, label_adresse, new Insets(0, 5, 0, 5), 0, 2);
			addComponent(container, _adresse, new Insets(0, 5, 0, 5), 1, 2);

			// Tel, char, 15
			JLabel label_tel = new JLabel(KUNDENPFLEGE_LABEL_TEL);
			JTextField _tel = new JTextField(10);
			_tel.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_TEL);
			_tel.setEditable(false);
			_tel.setEnabled(false);
			addComponent(container, label_tel, new Insets(0, 5, 0, 5), 0, 3);
			addComponent(container, _tel, new Insets(0, 5, 0, 5), 1, 3);

			// Konto, number, 12
			JLabel label_konto = new JLabel(KUNDENPFLEGE_LABEL_KONTO);
			NumberFormat format_konto = NumberFormat.getNumberInstance();
			format_konto.setMinimumFractionDigits(2);
			JFormattedTextField _konto = new JFormattedTextField(format_konto);
			_konto.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KONTO);
			_konto.setValue(new Double(0.00));
			_konto.setColumns(10);
			_konto.setEditable(true);
			_konto.setEnabled(false);
			addComponent(container, label_konto, new Insets(0, 5, 0, 5), 0, 4);
			addComponent(container, _konto, new Insets(0, 5, 0, 5), 1, 4);

			// Branche, char, 10
			JLabel label_branche = new JLabel(KUNDENPFLEGE_LABEL_BRANCHE);
			String[] branche_strings = { "AUTOMOBILE", "BUILDING", "FURNITURE",
					"HOUSEHOLD", "MACHINERY" };
			JComboBox<String> _branche = new JComboBox<String>(branche_strings);
			_branche.setName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_BRANCHE);
			_branche.setSelectedIndex(0);
			_branche.setEditable(false);
			_branche.setEnabled(false);
			addComponent(container, label_branche, new Insets(0, 5, 0, 5), 0, 5);
			addComponent(container, _branche, new Insets(0, 5, 0, 5), 1, 5);

			// Nation, char, 25
			// TODO: JComboBox mit Werten aus DB
			JLabel label_nation = new JLabel(KUNDENPFLEGE_LABEL_NATION);
			String[] nation_strings = { "Jemen", "Belize", "Neuseeland" };
			JComboBox<String> _nation = new JComboBox<String>(nation_strings);
			_nation.setName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_NATION);
			_nation.setSelectedIndex(0);
			_nation.setEditable(false);
			_nation.setEnabled(false);
			addComponent(container, label_nation, new Insets(0, 5, 0, 5), 0, 6);
			addComponent(container, _nation, new Insets(0, 5, 0, 5), 1, 6);

			// Ausfuehren
			JButton button_exec = new JButton(KUNDENPFLEGE_BUTTON_EXECUTE);
			button_exec.setName(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN);
			button_exec.setActionCommand(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN);
			button_exec.setEnabled(false);
			addComponent(container, button_exec, new Insets(0, 5, 0, 5), 2, 6);

			// Suchen
			JButton button_search = new JButton(KUNDENPFLEGE_BUTTON_SUCHE);
			button_search.setName(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_SUCHEN);
			button_search.setActionCommand(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_SUCHEN);
			addComponent(container, button_search, new Insets(0, 5, 0, 5), 2, 0);

			// Fertig
			JButton button_exec_fertig = new JButton(KUNDENPFLEGE_BUTTON_EXECUTE_FERTIG);
			button_exec_fertig.setName(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN_FERTIG);
			button_exec_fertig.setActionCommand(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN_FERTIG);
			button_exec_fertig.setVisible(false);
			addComponent(container, button_exec_fertig, new Insets(0, 5, 0, 5), 2, 5);
		}
			return container;
		case 2: // Template: Zulieferung einbuchen
		{
			// ZID, number, 10
			JLabel label_zlid = new JLabel(PRODUKTVERWALTUNG_LABEL_ZLID);
			JTextField _zlid = new JTextField(10);
			_zlid.setName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_NEU_ZLID);
			addComponent(container, label_zlid, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(container, _zlid, new Insets(0, 5, 0, 5), 1, 0);

			// Suchen
			JButton button_search = new JButton(PRODUKTVERWALTUNG_BUTTON_SUCHE);
			button_search.setName(COMPONENT_BUTTON_PRODUKTVERWALTUNG_NEU_SUCHEN);
			button_search.setActionCommand(COMPONENT_BUTTON_PRODUKTVERWALTUNG_NEU_SUCHEN);
			addComponent(container, button_search, new Insets(0, 5, 0, 5), 2, 0);
			button_search.setVisible(false); // Soni

			// Einbuchen
			JButton button_exec = new JButton(PRODUKTVERWALTUNG_BUTTON_EINBUCHEN);
			button_exec.setName(COMPONENT_BUTTON_PRODUKTVERWALTUNG_NEU_EINBUCHEN);
			button_exec.setActionCommand(COMPONENT_BUTTON_PRODUKTVERWALTUNG_NEU_EINBUCHEN);
			addComponent(container, button_exec, new Insets(0, 5, 0, 5), 2, 0);
		}
			return container;
		case 3: // Template: Bestand umbuchen
		{
			// Ursprungslager, number, 10
			JLabel label_srcLager = new JLabel(PRODUKTVERWALTUNG_LABEL_SRCLAGER);
			JTextField _srcLager = new JTextField(10);
			_srcLager.setName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_SRCLAGER);
			addComponent(container, label_srcLager, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(container, _srcLager, new Insets(0, 5, 0, 5), 1, 0);

			// Ziellager, number, 10
			JLabel label_destLager = new JLabel(PRODUKTVERWALTUNG_LABEL_DESTLAGER);
			JTextField _destLager = new JTextField(10);
			_destLager.setName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_DESTLAGER);
			addComponent(container, label_destLager, new Insets(0, 5, 0, 5), 0, 1);
			addComponent(container, _destLager, new Insets(0, 5, 0, 5), 1, 1);

			// Produkt, number, 10
			JLabel label_pid = new JLabel(PRODUKTVERWALTUNG_LABEL_PRODUKT);
			JTextField _pid = new JTextField(10);
			_pid.setName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_PRODUKT);
			addComponent(container, label_pid, new Insets(0, 5, 0, 5), 0, 2);
			addComponent(container, _pid, new Insets(0, 5, 0, 5), 1, 2);

			// Menge, number, 10
			JLabel label_menge = new JLabel(PRODUKTVERWALTUNG_LABEL_MENGE);
			JTextField _menge = new JTextField(10);
			_menge.setName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_MENGE);
			addComponent(container, label_menge, new Insets(0, 5, 0, 5), 0, 3);
			addComponent(container, _menge, new Insets(0, 5, 0, 5), 1, 3);

			// Umbuchen
			JButton button_exec = new JButton(PRODUKTVERWALTUNG_BUTTON_UMBUCHEN);
			button_exec.setName(COMPONENT_BUTTON_PRODUKTVERWALTUNG_EDIT_UMBUCHEN);
			button_exec.setActionCommand(COMPONENT_BUTTON_PRODUKTVERWALTUNG_EDIT_UMBUCHEN);
			addComponent(container, button_exec, new Insets(0, 5, 0, 5), 2, 3);
		}
			return container;
		case 4: // Template, Bestellung Neu
		{
			// BSTID, number, 10
			JLabel label_bstid = new JLabel(BESTELLVERWALTUNG_LABEL_BSTID);
			JTextField _bstid = new JTextField(10);
			_bstid.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTID);
//			addComponent(container, label_bstid, new Insets(0, 5, 0, 5), 0, 0, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
//			addComponent(container, _bstid, new Insets(0, 5, 0, 5), 1, 0, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
			addComponent(container, label_bstid, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(container, _bstid, new Insets(0, 5, 0, 5), 1, 0);

			// KID, number, 10
			JLabel label_kid = new JLabel(BESTELLVERWALTUNG_LABEL_KID);
			JTextField _kid = new JTextField(10);
			_kid.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_KID);
			addComponent(container, label_kid, new Insets(0, 5, 0, 5), 0, 1);
			addComponent(container, _kid, new Insets(0, 5, 0, 5), 1, 1);

			// Anleger, string, 12
			JLabel label_anleger = new JLabel(BESTELLVERWALTUNG_LABEL_ANLEGER);
			JTextField _anleger = new JTextField(10);
			_anleger.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_ANLEGER);
//			addComponent(container, label_anleger, new Insets(0, 5, 0, 5), 0, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
//			addComponent(container, _anleger, new Insets(0, 5, 0, 5), 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
			addComponent(container, label_anleger, new Insets(0, 5, 0, 5), 0, 2);
			addComponent(container, _anleger, new Insets(0, 5, 0, 5), 1, 2);

			// Bestelltermin, date
			JLabel label_bsttermin = new JLabel(BESTELLVERWALTUNG_LABEL_BSTTERMIN);
			JTextField _bsttermin = new JTextField(10);
			_bsttermin.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTERMIN);
//			addComponent(container, label_bsttermin, new Insets(0, 5, 0, 5), 0, 2, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
//			addComponent(container, _bsttermin, new Insets(0, 5, 0, 5), 1, 2, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
			addComponent(container, label_bsttermin, new Insets(0, 5, 0, 5), 0, 3);
			addComponent(container, _bsttermin, new Insets(0, 5, 0, 5), 1, 3);

			// Bestelltext, string, 256
			JLabel label_bsttext = new JLabel(BESTELLVERWALTUNG_LABEL_BSTTEXT);
			JTextField _bsttext = new JTextField(10);
			_bsttext.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTEXT);
//			addComponent(container, label_bsttext, new Insets(0, 5, 0, 5), 0, 3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
//			addComponent(container, _bsttext, new Insets(0, 5, 0, 5), 1, 3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
			addComponent(container, label_bsttext, new Insets(0, 5, 0, 5), 0, 4);
			addComponent(container, _bsttext, new Insets(0, 5, 0, 5), 1, 4);

			// Bestellpositionen, panel
			addComponent(container, this.pos, new Insets(0,0,0,0), 0, 5, 2, 1, GridBagConstraints.BOTH, GridBagConstraints.FIRST_LINE_START);

			// Status, char, 20
			JLabel label_status = new JLabel(BESTELLVERWALTUNG_LABEL_STATUS);
			JLabel _status = new JLabel("-");
			_status.setName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_STATUS);
			// TODO Farbe nach status vergeben
			addComponent(container, label_status, new Insets(0, 5, 0, 5), 2, 0);
			addComponent(container, _status, new Insets(0, 5, 0, 5), 3, 0);

			// Anlagedatum, date
			JLabel label_anlagedatum = new JLabel(BESTELLVERWALTUNG_LABEL_ANLAGEDATUM);
			JLabel _anlagedatum = new JLabel("-");
			_anlagedatum.setName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_ANLAGEDATUM);
			addComponent(container, label_anlagedatum, new Insets(0, 5, 0, 5), 2, 1);
			addComponent(container, _anlagedatum, new Insets(0, 5, 0, 5), 3, 1);

			// Aenderungsdatum, date
			JLabel label_aenderungsdatum = new JLabel(BESTELLVERWALTUNG_LABEL_AENDERUNGSDATUM);
			JLabel _aenderungsdatum = new JLabel("-");
			_aenderungsdatum.setName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_AENDERUNGSDATUM);
			addComponent(container, label_aenderungsdatum, new Insets(0, 5, 0, 5), 2, 2);
			addComponent(container, _aenderungsdatum, new Insets(0, 5, 0, 5), 3, 2);

			// Erledigttermin, date
			JLabel label_erledigttermin = new JLabel(BESTELLVERWALTUNG_LABEL_ERLEDIGTTERMIN);
			JLabel _erledigttermin = new JLabel("-");
			_erledigttermin.setName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_ERLEDIGTTERMIN);
			addComponent(container, label_erledigttermin, new Insets(0, 5, 0, 5), 2, 3);
			addComponent(container, _erledigttermin, new Insets(0, 5, 0, 5), 3, 3);

			JButton button_abbrechen = new JButton("Abbrechen");
//			addComponent(container, button_abbrechen, new Insets(0, 5, 0, 5), 1, 5);

			// Speichern
			JButton button_speichern = new JButton(BESTELLVERWALTUNG_BUTTON_SPEICHERN);
			button_speichern.setName(COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_SPEICHERN);
			button_speichern.setActionCommand(COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_SPEICHERN);
			addComponent(container, button_speichern, new Insets(0, 5, 0, 5), 2, 4);

			// Bestaetigen
			JButton button_bestaetigen = new JButton(BESTELLVERWALTUNG_BUTTON_BESTAETIGEN);
			button_bestaetigen.setName(COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_BESTAETIGEN);
			button_bestaetigen.setActionCommand(COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_BESTAETIGEN);
			addComponent(container, button_bestaetigen, new Insets(0, 5, 0, 5), 3, 4);
		}
			return container;
		case 5: // Template, Bestellung EDIT
		{
			// BSTID, number, 10
			JLabel label_bstid = new JLabel(BESTELLVERWALTUNG_LABEL_BSTID);
			JTextField _bstid = new JTextField(10);
			_bstid.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTID);
			addComponent(container, label_bstid, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(container, _bstid, new Insets(0, 5, 0, 5), 1, 0);

			// KID, number, 10
			JLabel label_kid = new JLabel(BESTELLVERWALTUNG_LABEL_KID);
			JTextField _kid = new JTextField(10);
			_kid.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_KID);
			_kid.setEditable(false);
			_kid.setEnabled(false);
			addComponent(container, label_kid, new Insets(0, 5, 0, 5), 0, 1);
			addComponent(container, _kid, new Insets(0, 5, 0, 5), 1, 1);

			// Anleger, string, 12
			JLabel label_anleger = new JLabel(BESTELLVERWALTUNG_LABEL_ANLEGER);
			JTextField _anleger = new JTextField(10);
			_anleger.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER);
			_anleger.setEditable(false);
			_anleger.setEnabled(false);
			addComponent(container, label_anleger, new Insets(0, 5, 0, 5), 0, 2);
			addComponent(container, _anleger, new Insets(0, 5, 0, 5), 1, 2);

			// Bestelltermin, date
			JLabel label_bsttermin = new JLabel(BESTELLVERWALTUNG_LABEL_BSTTERMIN);
			JTextField _bsttermin = new JTextField(10);
			_bsttermin.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTERMIN);
			_bsttermin.setEditable(false);
			_bsttermin.setEnabled(false);
			addComponent(container, label_bsttermin, new Insets(0, 5, 0, 5), 0, 3);
			addComponent(container, _bsttermin, new Insets(0, 5, 0, 5), 1, 3);

			// Bestelltext, string, 256
			JLabel label_bsttext = new JLabel(BESTELLVERWALTUNG_LABEL_BSTTEXT);
			JTextField _bsttext = new JTextField(10);
			_bsttext.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTEXT);
			_bsttext.setEditable(false);
			_bsttext.setEnabled(false);
			addComponent(container, label_bsttext, new Insets(0, 5, 0, 5), 0, 4);
			addComponent(container, _bsttext, new Insets(0, 5, 0, 5), 1, 4);

			// Bestellpositionen, panel
			addComponent(container, this.pos, new Insets(0,0,0,0), 0, 5, 2, 1, GridBagConstraints.BOTH, GridBagConstraints.FIRST_LINE_START);

			// Status, char, 20
			JLabel label_status = new JLabel(BESTELLVERWALTUNG_LABEL_STATUS);
			JLabel _status = new JLabel("OFFEN");
			_status.setName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_STATUS);
			addComponent(container, label_status, new Insets(0, 5, 0, 5), 2, 1);
			addComponent(container, _status, new Insets(0, 5, 0, 5), 3, 1);

			// Anlagedatum, date
			JLabel label_anlagedatum = new JLabel(BESTELLVERWALTUNG_LABEL_ANLAGEDATUM);
			JLabel _anlagedatum = new JLabel("31.12.2014");
			_anlagedatum.setName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_ANLAGEDATUM);
			addComponent(container, label_anlagedatum, new Insets(0, 5, 0, 5), 2, 2);
			addComponent(container, _anlagedatum, new Insets(0, 5, 0, 5), 3, 2);

			// Aenderungsdatum, date
			JLabel label_aenderungsdatum = new JLabel(BESTELLVERWALTUNG_LABEL_AENDERUNGSDATUM);
			JLabel _aenderungsdatum = new JLabel("31.12.2014");
			_aenderungsdatum.setName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_AENDERUNGSDATUM);
			addComponent(container, label_aenderungsdatum, new Insets(0, 5, 0, 5), 2, 3);
			addComponent(container, _aenderungsdatum, new Insets(0, 5, 0, 5), 3, 3);

			// Erledigttermin, date
			JLabel label_erledigttermin = new JLabel(BESTELLVERWALTUNG_LABEL_ERLEDIGTTERMIN);
			JLabel _erledigttermin = new JLabel("31.12.2014");
			_erledigttermin.setName(COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_ERLEDIGTTERMIN);
			addComponent(container, label_erledigttermin, new Insets(0, 5, 0, 5), 2, 4);
			addComponent(container, _erledigttermin, new Insets(0, 5, 0, 5), 3, 4);

			// Suchen
			JButton button_search = new JButton(BESTELLVERWALTUNG_BUTTON_SUCHE);
			button_search.setName(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_SUCHEN);
			button_search.setActionCommand(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_SUCHEN);
			addComponent(container, button_search, new Insets(0, 5, 0, 5), 2, 0);

			// Speichern
			JButton button_speichern = new JButton(BESTELLVERWALTUNG_BUTTON_SPEICHERN);
			button_speichern.setName(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_SPEICHERN);
			button_speichern.setActionCommand(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_SPEICHERN);
			button_speichern.setEnabled(false);
			addComponent(container, button_speichern, new Insets(0, 5, 0, 5), 2, 5, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH);

			// Bestaetigen
			JButton button_bestaetigen = new JButton(BESTELLVERWALTUNG_BUTTON_BESTAETIGEN);
			button_bestaetigen.setName(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_BESTAETIGEN);
			button_bestaetigen.setActionCommand(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_BESTAETIGEN);
			button_bestaetigen.setEnabled(false);
			addComponent(container, button_bestaetigen, new Insets(0, 5, 0, 5), 3, 5, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH);

			// Fertig
			JButton button_exec_fertig = new JButton(BESTELLVERWALTUNG_BUTTON_EXECUTE_FERTIG);
			button_exec_fertig.setName(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_AENDERN_FERTIG);
			button_exec_fertig.setActionCommand(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_AENDERN_FERTIG);
			button_exec_fertig.setVisible(false);
			addComponent(container, button_exec_fertig, new Insets(0, 5, 0, 5), 2, 6, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH);
		}
			return container;
		case 6: // Template, Bestellung GO
		{
			// BSTID, number, 10
			JLabel label_bstid = new JLabel(BESTELLVERWALTUNG_LABEL_BSTID);
			JTextField _bstid = new JTextField(10);
			_bstid.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_GO_BSTID);
			addComponent(container, label_bstid, new Insets(0, 5, 0, 5), 0, 0, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH);
			addComponent(container, _bstid, new Insets(0, 5, 0, 5), 1, 0, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH);

			// Ausliefern
			JButton button_ausliefern = new JButton(BESTELLVERWALTUNG_BUTTON_AUSLIEFERN);
			button_ausliefern.setName(COMPONENT_BUTTON_BESTELLVERWALTUNG_GO_AUSLIEFERN);
			button_ausliefern.setActionCommand(COMPONENT_BUTTON_BESTELLVERWALTUNG_GO_AUSLIEFERN);
			addComponent(container, button_ausliefern, new Insets(0, 5, 0, 5), 2, 0, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH);


		}
			return container;
		case 7: // Template: Produktanalyse
		{
			// Typ, string, 25
			JLabel label_typ = new JLabel(ANALYSE_LABEL_TYP);
			JTextField _typ = new JTextField(10);
			_typ.setName(COMPONENT_TEXTFIELD_PRODUKTANALYSE_TYP);
			addComponent(container, label_typ, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(container, _typ, new Insets(0, 5, 0, 5), 1, 0);

			// Groesse, number, 10
			JLabel label_groesse = new JLabel(ANALYSE_LABEL_GROESSE);
			JTextField _groesse = new JTextField(10);
			_groesse.setName(COMPONENT_TEXTFIELD_PRODUKTANALYSE_GROESSE);
			addComponent(container, label_groesse, new Insets(0, 5, 0, 5), 0, 1);
			addComponent(container, _groesse, new Insets(0, 5, 0, 5), 1, 1);

			// Ausfueren
			JButton button_exec = new JButton(ANALYSE_BUTTON_AUSFUEHREN);
			button_exec.setName(COMPONENT_BUTTON_PRODUKTANALYSE_AUSFUEHREN);
			button_exec.setActionCommand(COMPONENT_BUTTON_PRODUKTANALYSE_AUSFUEHREN);
			addComponent(container, button_exec, new Insets(0, 5, 0, 5), 2, 1);
		}
			return container;
		case 8: // Template: Senkung der Lieferkosten
		{
			// PID, number, 10
			JLabel label_pid = new JLabel(ANALYSE_LABEL_PRODUKT);
			JTextField _pid = new JTextField(10);
			_pid.setName(COMPONENT_TEXTFIELD_LIEFERKOSTEN_PRODUKT);
			addComponent(container, label_pid, new Insets(0, 5, 0, 5), 0, 0, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
			addComponent(container, _pid, new Insets(0, 5, 0, 5), 1, 0, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

			// Ausfueren
			JButton button_exec = new JButton(ANALYSE_BUTTON_AUSFUEHREN);
			button_exec.setName(COMPONENT_BUTTON_LIEFERKOSTEN_AUSFUEHREN);
			button_exec.setActionCommand(COMPONENT_BUTTON_LIEFERKOSTEN_AUSFUEHREN);
			addComponent(container, button_exec, new Insets(0, 5, 0, 5), 2, 0, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
		}
			return container;
		case 9:
		{
			// POSNR, number, 10
			JLabel label_posnr = new JLabel(BESTELLVERWALTUNG_LABEL_POSNR);
			JLabel _posnr = new JLabel();
			_posnr.setName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_BSTPOS_POSNR);
			addComponent(container, label_posnr, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(container, _posnr, new Insets(0, 5, 0, 5), 1, 0);

			// BSTID, number, 10
			JLabel label_bstid = new JLabel(BESTELLVERWALTUNG_LABEL_BSTID);
			JTextField _bstid = new JTextField(10);
			_bstid.setName(COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_BSTPOS_BSTID);
			addComponent(container, label_bstid, new Insets(0, 5, 0, 5), 0, 1);
			addComponent(container, _bstid, new Insets(0, 5, 0, 5), 1, 1);

			// Produkt, number, 10
			JLabel label_pid = new JLabel(BESTELLVERWALTUNG_LABEL_PRODUKT);
			JTextField _pid = new JTextField(10);
			_pid.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTPOS_PID);
			addComponent(container, label_pid, new Insets(0, 5, 0, 5), 0, 2);
			addComponent(container, _pid, new Insets(0, 5, 0, 5), 1, 2);

			// Menge, number, 10
			JLabel label_menge = new JLabel(BESTELLVERWALTUNG_LABEL_MENGE);
			JTextField _menge = new JTextField(10);
			_menge.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTPOS_MENGE);
			addComponent(container, label_menge, new Insets(0, 5, 0, 5), 0, 3);
			addComponent(container, _menge, new Insets(0, 5, 0, 5), 1, 3);

			// Preis, number, 10
			JLabel label_preis = new JLabel(BESTELLVERWALTUNG_LABEL_PREIS);
			JTextField _preis = new JTextField(10);
			_preis.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTPOS_PREIS);
			addComponent(container,label_preis, new Insets(0, 5, 0, 5), 0, 4);
			addComponent(container, _preis, new Insets(0, 5, 0, 5), 1, 4);

			// Positionstext, number, 10
			JLabel label_postext = new JLabel(BESTELLVERWALTUNG_LABEL_POSTEXT);
			JTextArea _postext = new JTextArea(10, 10);
			_postext.setName(COMPONENT_TEXTAREA_BESTELLVERWALTUNG_NEU_BSTPOS_POSTEXT);
			addComponent(container, label_postext, new Insets(0, 5, 0, 5), 0, 5);
			addComponent(container, _postext, new Insets(0, 5, 0, 5), 1, 5);
		}
		return container;
		default:
			return container;
		}
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

	public void addComponent(JPanel panel, Component c, Insets insets, int x, int y, int width, int height, int fill, int anchor) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = anchor;
		constraints.fill = fill;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.insets = insets;
		panel.add(c, constraints);
	}

	public void addComponent(JPanel panel, Component c,
			Insets insets, int x, int y) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.insets = insets;
		panel.add(c, constraints);
	}

	public HashMap<String, Component> getComponentMap(){
	    return this.componentMap;
    }

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
