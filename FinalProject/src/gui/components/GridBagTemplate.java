package gui.components;

import java.awt.Component;
import java.awt.Container;
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
import javax.swing.JTextField;

import model.Configuration;

public class GridBagTemplate extends JPanel implements Configuration {

	private static final long serialVersionUID = 7966600021735934643L;
	protected HashMap<String, Component> componentMap;
	private int type;
	private String title;
	private String name;

	public GridBagTemplate(int type, String title, String name) {
		this.type = type;
		this.title = title;
		this.name = name;

		GridBagLayout gbl = new GridBagLayout();
		this.setLayout(gbl);
		this.setBorder(BorderFactory.createTitledBorder(this.title));
		this.setName(this.name);

		createPanel(this.type, gbl);
		this.componentMap = new HashMap<String, Component>();
		createComponentMap(this);
	}

	public void createPanel(int type, GridBagLayout gbl) {

		switch (type) {
		case 0: // Template: Kunden anlegen
		{
			// KID, number, 10
			JLabel label_kid = new JLabel(KUNDENPFLEGE_LABEL_KID);
			JTextField _kid = new JTextField(10);
			_kid.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KID);
			addComponent(this, gbl, label_kid, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(this, gbl, _kid, new Insets(0, 5, 0, 5), 1, 0);

			// Name, string, 25
			JLabel label_name = new JLabel(KUNDENPFLEGE_LABEL_NAME);
			JTextField _name = new JTextField(10);
			_name.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_NAME);
			addComponent(this, gbl, label_name, new Insets(0, 5, 0, 5), 0, 1);
			addComponent(this, gbl, _name, new Insets(0, 5, 0, 5), 1, 1);

			// Adresse, char, 40
			JLabel label_adresse = new JLabel(KUNDENPFLEGE_LABEL_ADRESSE);
			JTextField _adresse = new JTextField(10);
			_adresse.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_ADRESSE);
			addComponent(this, gbl, label_adresse, new Insets(0, 5, 0, 5), 0, 2);
			addComponent(this, gbl, _adresse, new Insets(0, 5, 0, 5), 1, 2);

			// Tel, char, 15
			JLabel label_tel = new JLabel(KUNDENPFLEGE_LABEL_TEL);
			JTextField _tel = new JTextField(10);
			_tel.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_TEL);
			addComponent(this, gbl, label_tel, new Insets(0, 5, 0, 5), 0, 3);
			addComponent(this, gbl, _tel, new Insets(0, 5, 0, 5), 1, 3);

			// Konto, number, 12
			JLabel label_konto = new JLabel(KUNDENPFLEGE_LABEL_KONTO);
			NumberFormat format_konto = NumberFormat.getNumberInstance();
			format_konto.setMinimumFractionDigits(2);
			JFormattedTextField _konto = new JFormattedTextField(format_konto);
			_konto.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KONTO);
			_konto.setValue(new Double(0.00));
			_konto.setColumns(10);
			_konto.setEditable(false);
			addComponent(this, gbl, label_konto, new Insets(0, 5, 0, 5), 0, 4);
			addComponent(this, gbl, _konto, new Insets(0, 5, 0, 5), 1, 4);

			// Branche, char, 10
			JLabel label_branche = new JLabel(KUNDENPFLEGE_LABEL_BRANCHE);
			String[] branche_strings = { "AUTOMOBILE", "BUILDING", "FURNITURE",
					"HOUSEHOLD", "MACHINERY" };
			JComboBox<String> _branche = new JComboBox<String>(branche_strings);
			_branche.setName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE);
			_branche.setSelectedIndex(0);
			_branche.setEditable(true);
			addComponent(this, gbl, label_branche, new Insets(0, 5, 0, 5), 0, 5);
			addComponent(this, gbl, _branche, new Insets(0, 5, 0, 5), 1, 5);

			// Nation, char, 25
			// TODO: JComboBox mit Werten aus DB
			JLabel label_nation = new JLabel(KUNDENPFLEGE_LABEL_NATION);
			String[] nation_strings = { "Nation 1", "Nation 2", "Nation 3" };
			JComboBox<String> _nation = new JComboBox<String>(nation_strings);
			_nation.setName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_NATION);
			_nation.setSelectedIndex(0);
			_nation.setEditable(true);
			addComponent(this, gbl, label_nation, new Insets(0, 5, 0, 5), 0, 6);
			addComponent(this, gbl, _nation, new Insets(0, 5, 0, 5), 1, 6);

			// Ausfuehren
			JButton button_exec = new JButton(KUNDENPFLEGE_BUTTON_EXECUTE);
			button_exec.setName(COMPONENT_BUTTON_KUNDENPFLEGE_NEU_AUSFUEHREN);
			button_exec.setActionCommand(COMPONENT_BUTTON_KUNDENPFLEGE_NEU_AUSFUEHREN);
			addComponent(this, gbl, button_exec, new Insets(0,5,0,5), 2, 6);
		}
			break;
		case 1: // Template: Kunden aendern
		{
			// KID, number, 10
			JLabel label_kid = new JLabel(KUNDENPFLEGE_LABEL_KID);
			JTextField _kid = new JTextField(10);
			_kid.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KID);
			addComponent(this, gbl, label_kid, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(this, gbl, _kid, new Insets(0, 5, 0, 5), 1, 0);

			// Name, string, 25
			JLabel label_name = new JLabel(KUNDENPFLEGE_LABEL_NAME);
			JTextField _name = new JTextField(10);
			_name.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_NAME);
			addComponent(this, gbl, label_name, new Insets(0, 5, 0, 5), 0, 1);
			addComponent(this, gbl, _name, new Insets(0, 5, 0, 5), 1, 1);

			// Adresse, char, 40
			JLabel label_adresse = new JLabel(KUNDENPFLEGE_LABEL_ADRESSE);
			JTextField _adresse = new JTextField(10);
			_adresse.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_ADRESSE);
			addComponent(this, gbl, label_adresse, new Insets(0, 5, 0, 5), 0, 2);
			addComponent(this, gbl, _adresse, new Insets(0, 5, 0, 5), 1, 2);

			// Tel, char, 15
			JLabel label_tel = new JLabel(KUNDENPFLEGE_LABEL_TEL);
			JTextField _tel = new JTextField(10);
			_tel.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_TEL);
			addComponent(this, gbl, label_tel, new Insets(0, 5, 0, 5), 0, 3);
			addComponent(this, gbl, _tel, new Insets(0, 5, 0, 5), 1, 3);

			// Konto, number, 12
			JLabel label_konto = new JLabel(KUNDENPFLEGE_LABEL_KONTO);
			NumberFormat format_konto = NumberFormat.getNumberInstance();
			format_konto.setMinimumFractionDigits(2);
			JFormattedTextField _konto = new JFormattedTextField(format_konto);
			_konto.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KONTO);
			_konto.setValue(new Double(0.00));
			_konto.setColumns(10);
			_konto.setEditable(false);
			addComponent(this, gbl, label_konto, new Insets(0, 5, 0, 5), 0, 4);
			addComponent(this, gbl, _konto, new Insets(0, 5, 0, 5), 1, 4);

			// Branche, char, 10
			JLabel label_branche = new JLabel(KUNDENPFLEGE_LABEL_BRANCHE);
			String[] branche_strings = { "AUTOMOBILE", "BUILDING", "FURNITURE",
					"HOUSEHOLD", "MACHINERY" };
			JComboBox<String> _branche = new JComboBox<String>(branche_strings);
			_branche.setName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_BRANCHE);
			_branche.setSelectedIndex(0);
			_branche.setEditable(true);
			addComponent(this, gbl, label_branche, new Insets(0, 5, 0, 5), 0, 5);
			addComponent(this, gbl, _branche, new Insets(0, 5, 0, 5), 1, 5);

			// Nation, char, 25
			// TODO: JComboBox mit Werten aus DB
			JLabel label_nation = new JLabel(KUNDENPFLEGE_LABEL_NATION);
			String[] nation_strings = { "Jemen", "Belize", "Neuseeland" };
			JComboBox<String> _nation = new JComboBox<String>(nation_strings);
			_nation.setName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_NATION);
			_nation.setSelectedIndex(0);
			_nation.setEditable(true);
			addComponent(this, gbl, label_nation, new Insets(0, 5, 0, 5), 0, 6);
			addComponent(this, gbl, _nation, new Insets(0, 5, 0, 5), 1, 6);

			// Ausfuehren
			JButton button_exec = new JButton(KUNDENPFLEGE_BUTTON_EXECUTE);
			button_exec.setName(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN);
			button_exec.setActionCommand(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN);
			addComponent(this, gbl, button_exec, new Insets(0, 5, 0, 5), 2, 6);

			// Suchen
			JButton button_search = new JButton(KUNDENPFLEGE_BUTTON_SUCHE);
			button_search.setName(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_SUCHEN);
			button_search.setActionCommand(COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_SUCHEN);
			addComponent(this, gbl, button_search, new Insets(0, 5, 0, 5), 2, 0);
		}
			break;
		case 2: // Template: Zulieferung einbuchen
		{
			// ZID, number, 10
			JLabel label_zlid = new JLabel(PRODUKTVERWALTUNG_LABEL_PRODUKT);
			JTextField _zlid = new JTextField(10);
			_zlid.setName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_NEU_ZLID);
			addComponent(this, gbl, label_zlid, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(this, gbl, _zlid, new Insets(0, 5, 0, 5), 1, 0);

			// Suchen
			JButton button_search = new JButton(PRODUKTVERWALTUNG_BUTTON_SUCHE);
			button_search.setName(COMPONENT_BUTTON_PRODUKTVERWALTUNG_NEU_SUCHEN);
			button_search.setActionCommand(COMPONENT_BUTTON_PRODUKTVERWALTUNG_NEU_SUCHEN);
			addComponent(this, gbl, button_search, new Insets(0, 5, 0, 5), 2, 0);

			// Einbuchen
			JButton button_exec = new JButton(PRODUKTVERWALTUNG_BUTTON_EINBUCHEN);
			button_exec.setName(COMPONENT_BUTTON_PRODUKTVERWALTUNG_NEU_EINBUCHEN);
			button_exec.setActionCommand(COMPONENT_BUTTON_PRODUKTVERWALTUNG_NEU_EINBUCHEN);
			addComponent(this, gbl, button_exec, new Insets(0, 5, 0, 5), 2, 1);
		}
			break;
		case 3: // Template: Bestand umbuchen
		{
			// Ursprungslager, number, 10
			JLabel label_srcLager = new JLabel(PRODUKTVERWALTUNG_LABEL_SRCLAGER);
			JTextField _srcLager = new JTextField(10);
			_srcLager.setName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_SRCLAGER);
			addComponent(this, gbl, label_srcLager, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(this, gbl, _srcLager, new Insets(0, 5, 0, 5), 1, 0);

			// Ziellager, number, 10
			JLabel label_destLager = new JLabel(PRODUKTVERWALTUNG_LABEL_DESTLAGER);
			JTextField _destLager = new JTextField(10);
			_destLager.setName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_DESTLAGER);
			addComponent(this, gbl, label_destLager, new Insets(0, 5, 0, 5), 0, 1);
			addComponent(this, gbl, _destLager, new Insets(0, 5, 0, 5), 1, 1);

			// Produkt, number, 10
			JLabel label_pid = new JLabel(PRODUKTVERWALTUNG_LABEL_PRODUKT);
			JTextField _pid = new JTextField(10);
			_pid.setName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_PRODUKT);
			addComponent(this, gbl, label_pid, new Insets(0, 5, 0, 5), 0, 2);
			addComponent(this, gbl, _pid, new Insets(0, 5, 0, 5), 1, 2);

			// Menge, number, 10
			JLabel label_menge = new JLabel(PRODUKTVERWALTUNG_LABEL_MENGE);
			JTextField _menge = new JTextField(10);
			_menge.setName(COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_MENGE);
			addComponent(this, gbl, label_menge, new Insets(0, 5, 0, 5), 0, 3);
			addComponent(this, gbl, _menge, new Insets(0, 5, 0, 5), 1, 3);

			// Umbuchen
			JButton button_exec = new JButton(PRODUKTVERWALTUNG_BUTTON_UMBUCHEN);
			button_exec.setName(COMPONENT_BUTTON_PRODUKTVERWALTUNG_EDIT_UMBUCHEN);
			button_exec.setActionCommand(COMPONENT_BUTTON_PRODUKTVERWALTUNG_EDIT_UMBUCHEN);
			addComponent(this, gbl, button_exec, new Insets(0, 5, 0, 5), 2, 3);
		}
			break;
		case 4:
		{
			// BSTID, number, 10
			JLabel label_bstid = new JLabel(BESTELLVERWALTUNG_LABEL_BSTID);
			JTextField _bstid = new JTextField(10);
			_bstid.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTID);
			addComponent(this, gbl, label_bstid, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(this, gbl, _bstid, new Insets(0, 5, 0, 5), 1, 0);

			// Bestelltext, string, 256
			JLabel label_bsttext = new JLabel(BESTELLVERWALTUNG_LABEL_BSTTEXT);
			JTextField _bsttext = new JTextField(10);
			_bsttext.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTEXT);
			addComponent(this, gbl, label_bsttext, new Insets(0, 5, 0, 5), 0, 1);
			addComponent(this, gbl, _bsttext, new Insets(0, 5, 0, 5), 1, 1);

			// Anleger, string, 12
			JLabel label_anleger = new JLabel(BESTELLVERWALTUNG_LABEL_ANLEGER);
			JTextField _anleger = new JTextField(10);
			_anleger.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_ANLEGER);
			addComponent(this, gbl, label_anleger, new Insets(0, 5, 0, 5), 0, 2);
			addComponent(this, gbl, _anleger, new Insets(0, 5, 0, 5), 1, 2);

			// Status, char, 20
			JLabel label_status = new JLabel(BESTELLVERWALTUNG_LABEL_STATUS);
			JTextField _status = new JTextField(10);
			_status.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_STATUS);
			addComponent(this, gbl, label_status, new Insets(0, 5, 0, 5), 0, 3);
			addComponent(this, gbl, _status, new Insets(0, 5, 0, 5), 1, 3);

			// Anlagedatum, date
			JLabel label_anlagedatum = new JLabel(BESTELLVERWALTUNG_LABEL_ANLAGEDATUM);
			JTextField _anlagedatum = new JTextField(10);
			_anlagedatum.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_ANLAGEDATUM);
			addComponent(this, gbl, label_anlagedatum, new Insets(0, 5, 0, 5), 0, 4);
			addComponent(this, gbl, _anlagedatum, new Insets(0, 5, 0, 5), 1, 4);

			// Aenderungsdatum, date
			JLabel label_aenderungsdatum = new JLabel(BESTELLVERWALTUNG_LABEL_AENDERUNGSDATUM);
			JTextField _aenderungsdatum = new JTextField(10);
			_aenderungsdatum.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_AENDERUNGSDATUM);
			addComponent(this, gbl, label_aenderungsdatum, new Insets(0, 5, 0, 5), 0, 5);
			addComponent(this, gbl, _aenderungsdatum, new Insets(0, 5, 0, 5), 1, 5);

			// Bestelltermin, date
			JLabel label_bsttermin = new JLabel(BESTELLVERWALTUNG_LABEL_BSTTERMIN);
			JTextField _bsttermin = new JTextField(10);
			_bsttermin.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTERMIN);
			addComponent(this, gbl, label_bsttermin, new Insets(0, 5, 0, 5), 0, 6);
			addComponent(this, gbl, _bsttermin, new Insets(0, 5, 0, 5), 1, 6);

			// Erledigttermin, date
			JLabel label_erledigttermin = new JLabel(BESTELLVERWALTUNG_LABEL_ERLEDIGTTERMIN);
			JTextField _erledigttermin = new JTextField(10);
			_erledigttermin.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_ERLEDIGTTERMIN);
			addComponent(this, gbl, label_erledigttermin, new Insets(0, 5, 0, 5), 0, 7);
			addComponent(this, gbl, _erledigttermin, new Insets(0, 5, 0, 5), 1, 7);

			// Speichern
			JButton button_speichern = new JButton(BESTELLVERWALTUNG_BUTTON_SPEICHERN);
			button_speichern.setName(COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_SPEICHERN);
			button_speichern.setActionCommand(COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_SPEICHERN);
			addComponent(this, gbl, button_speichern, new Insets(0, 5, 0, 5), 2, 6);

			// Bestaetigen
			JButton button_bestaetigen = new JButton(BESTELLVERWALTUNG_BUTTON_BESTAETIGEN);
			button_bestaetigen.setName(COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_BESTAETIGEN);
			button_bestaetigen.setActionCommand(COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_BESTAETIGEN);
			addComponent(this, gbl, button_bestaetigen, new Insets(0, 5, 0, 5), 2, 7);
		}
			break;
		case 5:
		{
			// BSTID, number, 10
			JLabel label_bstid = new JLabel(BESTELLVERWALTUNG_LABEL_BSTID);
			JTextField _bstid = new JTextField(10);
			_bstid.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTID);
			addComponent(this, gbl, label_bstid, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(this, gbl, _bstid, new Insets(0, 5, 0, 5), 1, 0);

			// Bestelltext, string, 256
			JLabel label_bsttext = new JLabel(BESTELLVERWALTUNG_LABEL_BSTTEXT);
			JTextField _bsttext = new JTextField(10);
			_bsttext.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTEXT);
			addComponent(this, gbl, label_bsttext, new Insets(0, 5, 0, 5), 0, 1);
			addComponent(this, gbl, _bsttext, new Insets(0, 5, 0, 5), 1, 1);

			// Anleger, string, 12
			JLabel label_anleger = new JLabel(BESTELLVERWALTUNG_LABEL_ANLEGER);
			JTextField _anleger = new JTextField(10);
			_anleger.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER);
			addComponent(this, gbl, label_anleger, new Insets(0, 5, 0, 5), 0, 2);
			addComponent(this, gbl, _anleger, new Insets(0, 5, 0, 5), 1, 2);

			// Status, char, 20
			JLabel label_status = new JLabel(BESTELLVERWALTUNG_LABEL_STATUS);
			JTextField _status = new JTextField(10);
			_status.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_STATUS);
			addComponent(this, gbl, label_status, new Insets(0, 5, 0, 5), 0, 3);
			addComponent(this, gbl, _status, new Insets(0, 5, 0, 5), 1, 3);

			// Anlagedatum, date
			JLabel label_anlagedatum = new JLabel(BESTELLVERWALTUNG_LABEL_ANLAGEDATUM);
			JTextField _anlagedatum = new JTextField(10);
			_anlagedatum.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLAGEDATUM);
			addComponent(this, gbl, label_anlagedatum, new Insets(0, 5, 0, 5), 0, 4);
			addComponent(this, gbl, _anlagedatum, new Insets(0, 5, 0, 5), 1, 4);

			// Aenderungsdatum, date
			JLabel label_aenderungsdatum = new JLabel(BESTELLVERWALTUNG_LABEL_AENDERUNGSDATUM);
			JTextField _aenderungsdatum = new JTextField(10);
			_aenderungsdatum.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_AENDERUNGSDATUM);
			addComponent(this, gbl, label_aenderungsdatum, new Insets(0, 5, 0, 5), 0, 5);
			addComponent(this, gbl, _aenderungsdatum, new Insets(0, 5, 0, 5), 1, 5);

			// Bestelltermin, date
			JLabel label_bsttermin = new JLabel(BESTELLVERWALTUNG_LABEL_BSTTERMIN);
			JTextField _bsttermin = new JTextField(10);
			_bsttermin.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTERMIN);
			addComponent(this, gbl, label_bsttermin, new Insets(0, 5, 0, 5), 0, 6);
			addComponent(this, gbl, _bsttermin, new Insets(0, 5, 0, 5), 1, 6);

			// Erledigttermin, date
			JLabel label_erledigttermin = new JLabel(BESTELLVERWALTUNG_LABEL_ERLEDIGTTERMIN);
			JTextField _erledigttermin = new JTextField(10);
			_erledigttermin.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ERLEDIGTTERMIN);
			addComponent(this, gbl, label_erledigttermin, new Insets(0, 5, 0, 5), 0, 7);
			addComponent(this, gbl, _erledigttermin, new Insets(0, 5, 0, 5), 1, 7);

			// Speichern
			JButton button_speichern = new JButton(BESTELLVERWALTUNG_BUTTON_SPEICHERN);
			button_speichern.setName(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_SPEICHERN);
			button_speichern.setActionCommand(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_SPEICHERN);
			addComponent(this, gbl, button_speichern, new Insets(0, 5, 0, 5), 2, 6);

			// Bestaetigen
			JButton button_bestaetigen = new JButton(BESTELLVERWALTUNG_BUTTON_BESTAETIGEN);
			button_bestaetigen.setName(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_BESTAETIGEN);
			button_bestaetigen.setActionCommand(COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_BESTAETIGEN);
			addComponent(this, gbl, button_bestaetigen, new Insets(0, 5, 0, 5), 2, 7);
		}
			break;
		case 6:
		{
			// BSTID, number, 10
			JLabel label_bstid = new JLabel(BESTELLVERWALTUNG_LABEL_BSTID);
			JTextField _bstid = new JTextField(10);
			_bstid.setName(COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_GO_BSTID);
			addComponent(this, gbl, label_bstid, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(this, gbl, _bstid, new Insets(0, 5, 0, 5), 1, 0);

			// Ausliefern
			JButton button_ausliefern = new JButton(BESTELLVERWALTUNG_BUTTON_AUSLIEFERN);
			button_ausliefern.setName(COMPONENT_BUTTON_BESTELLVERWALTUNG_GO_AUSLIEFERN);
			button_ausliefern.setActionCommand(COMPONENT_BUTTON_BESTELLVERWALTUNG_GO_AUSLIEFERN);
			addComponent(this, gbl, button_ausliefern, new Insets(0, 5, 0, 5), 2, 0);


		}
			break;
		case 7: // Template: Produktanalyse
		{
			// PID, number, 10
			JLabel label_pid = new JLabel(ANALYSE_LABEL_PID);
			JTextField _pid = new JTextField(10);
			_pid.setName(COMPONENT_TEXTFIELD_PRODUKTANALYSE_PID);
			addComponent(this, gbl, label_pid, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(this, gbl, _pid, new Insets(0, 5, 0, 5), 1, 0);

			// Typ, string, 25
			JLabel label_typ = new JLabel(ANALYSE_LABEL_TYP);
			JTextField _typ = new JTextField(10);
			_typ.setName(COMPONENT_TEXTFIELD_PRODUKTANALYSE_TYP);
			addComponent(this, gbl, label_typ, new Insets(0, 5, 0, 5), 0, 1);
			addComponent(this, gbl, _typ, new Insets(0, 5, 0, 5), 1, 1);

			// Groesse, number, 10
			JLabel label_groesse = new JLabel(ANALYSE_LABEL_GROESSE);
			JTextField _groesse = new JTextField(10);
			_groesse.setName(COMPONENT_TEXTFIELD_PRODUKTANALYSE_GROESSE);
			addComponent(this, gbl, label_groesse, new Insets(0, 5, 0, 5), 0, 2);
			addComponent(this, gbl, _groesse, new Insets(0, 5, 0, 5), 1, 2);
		}
			break;
		case 8: // Template: Senkung der Lieferkosten
		{
			// PID, number, 10
			JLabel label_pid = new JLabel(ANALYSE_LABEL_PID);
			JTextField _pid = new JTextField(10);
			_pid.setName(COMPONENT_TEXTFIELD_LIEFERKOSTEN_PID);
			addComponent(this, gbl, label_pid, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(this, gbl, _pid, new Insets(0, 5, 0, 5), 1, 0);
		}
			break;
		default:
			break;
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

	public void addComponent(JPanel panel, GridBagLayout gbl, Component c,
			Insets insets, int x, int y, int width, int height, double weightx,
			double weighty) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.insets = insets;
		panel.add(c, constraints);
	}

	public void addComponent(JPanel panel, GridBagLayout layout, Component c,
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
