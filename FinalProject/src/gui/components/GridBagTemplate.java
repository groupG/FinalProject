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

			// Nation, char, 25
			// TODO: JComboBox mit Werten aus DB
			JLabel label_nation = new JLabel(KUNDENPFLEGE_LABEL_NATION);
			String[] nation_strings = { "Nation 1", "Nation 2", "Nation 3" };
			JComboBox<String> _nation = new JComboBox<String>(nation_strings);
			_nation.setName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_NATION);
			_nation.setSelectedIndex(0);
			_nation.setEditable(true);
			addComponent(this, gbl, label_nation, new Insets(0, 5, 0, 5), 0, 4);
			addComponent(this, gbl, _nation, new Insets(0, 5, 0, 5), 1, 4);

			// Konto, number, 12
			JLabel label_konto = new JLabel(KUNDENPFLEGE_LABEL_KONTO);
			NumberFormat format_konto = NumberFormat.getNumberInstance();
			format_konto.setMinimumFractionDigits(2);
			JFormattedTextField _konto = new JFormattedTextField(format_konto);
			_konto.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KONTO);
			_konto.setValue(new Double(0.00));
			_konto.setColumns(10);
			_konto.setEditable(false);
			addComponent(this, gbl, label_konto, new Insets(0, 5, 0, 5), 0, 5);
			addComponent(this, gbl, _konto, new Insets(0, 5, 0, 5), 1, 5);

			// Branche, char, 10
			JLabel label_branche = new JLabel(KUNDENPFLEGE_LABEL_BRANCHE);
			String[] branche_strings = { "AUTOMOBILE", "BUILDING", "FURNITURE",
					"HOUSEHOLD", "MACHINERY" };
			JComboBox<String> _branche = new JComboBox<String>(branche_strings);
			_branche.setName(COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE);
			_branche.setSelectedIndex(0);
			_branche.setEditable(true);
			addComponent(this, gbl, label_branche, new Insets(0, 5, 0, 5), 0, 6);
			addComponent(this, gbl, _branche, new Insets(0, 5, 0, 5), 1, 6);

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

			// Nation, char, 25
			// TODO: JComboBox mit Werten aus DB
			JLabel label_nation = new JLabel(KUNDENPFLEGE_LABEL_NATION);
			String[] nation_strings = { "Nation 1", "Nation 2", "Nation 3" };
			JComboBox<String> _nation = new JComboBox<String>(nation_strings);
			_nation.setName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_NATION);
			_nation.setSelectedIndex(0);
			_nation.setEditable(true);
			addComponent(this, gbl, label_nation, new Insets(0, 5, 0, 5), 0, 4);
			addComponent(this, gbl, _nation, new Insets(0, 5, 0, 5), 1, 4);

			// Konto, number, 12
			JLabel label_konto = new JLabel(KUNDENPFLEGE_LABEL_KONTO);
			NumberFormat format_konto = NumberFormat.getNumberInstance();
			format_konto.setMinimumFractionDigits(2);
			JFormattedTextField _konto = new JFormattedTextField(format_konto);
			_konto.setName(COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KONTO);
			_konto.setValue(new Double(0.00));
			_konto.setColumns(10);
			_konto.setEditable(false);
			addComponent(this, gbl, label_konto, new Insets(0, 5, 0, 5), 0, 5);
			addComponent(this, gbl, _konto, new Insets(0, 5, 0, 5), 1, 5);

			// Branche, char, 10
			JLabel label_branche = new JLabel(KUNDENPFLEGE_LABEL_BRANCHE);
			String[] branche_strings = { "AUTOMOBILE", "BUILDING", "FURNITURE",
					"HOUSEHOLD", "MACHINERY" };
			JComboBox<String> _branche = new JComboBox<String>(branche_strings);
			_branche.setName(COMPONENT_COMBO_KUNDENPFLEGE_EDIT_BRANCHE);
			_branche.setSelectedIndex(0);
			_branche.setEditable(true);
			addComponent(this, gbl, label_branche, new Insets(0, 5, 0, 5), 0, 6);
			addComponent(this, gbl, _branche, new Insets(0, 5, 0, 5), 1, 6);

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
			JLabel label_zlid = new JLabel(PRODUKTVERWALTUNG_LABEL_ZLID);
			JTextField _zlid = new JTextField(10);
			_zlid.setName(PRODUKTVERWALTUNG_LABEL_ZLID);
			addComponent(this, gbl, label_zlid, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(this, gbl, _zlid, new Insets(0, 5, 0, 5), 1, 0);

			// Suchen
			JButton button_search = new JButton(PRODUKTVERWALTUNG_BUTTON_SUCHE);
			button_search.setName(PRODUKTVERWALTUNG_BUTTON_SUCHE);
			addComponent(this, gbl, button_search, new Insets(0, 5, 0, 5), 2, 0);
		}
			break;
		case 3: // Template: Bestand umbuchen
		{
			// Ursprungslager, number, 10
			JLabel label_srcLager = new JLabel(PRODUKTVERWALTUNG_LABEL_SRCLAGER);
			JTextField _srcLager = new JTextField(10);
			_srcLager.setName(PRODUKTVERWALTUNG_LABEL_SRCLAGER);
			addComponent(this, gbl, label_srcLager, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(this, gbl, _srcLager, new Insets(0, 5, 0, 5), 0, 1);

			// Ziellager, number, 10
			JLabel label_destLager = new JLabel(
					PRODUKTVERWALTUNG_LABEL_DESTLAGER);
			JTextField _destLager = new JTextField(10);
			_srcLager.setName(PRODUKTVERWALTUNG_LABEL_DESTLAGER);
			addComponent(this, gbl, label_destLager, new Insets(0, 5, 0, 5), 0, 1);
			addComponent(this, gbl, _destLager, new Insets(0, 5, 0, 5), 1, 1);

			// Produkt, number, 10
			JLabel label_pid = new JLabel(
					PRODUKTVERWALTUNG_LABEL_DESTLAGER);
			JTextField _pid = new JTextField(10);
			_pid.setName(PRODUKTVERWALTUNG_LABEL_DESTLAGER);
			addComponent(this, gbl, label_pid, new Insets(0, 5, 0, 5), 0, 2);
			addComponent(this, gbl, _pid, new Insets(0, 5, 0, 5), 1, 2);

			// Menge, number, 10
			JLabel label_menge = new JLabel(
					PRODUKTVERWALTUNG_LABEL_DESTLAGER);
			JTextField _menge = new JTextField(10);
			_menge.setName(PRODUKTVERWALTUNG_LABEL_DESTLAGER);
			addComponent(this, gbl, label_menge, new Insets(0, 5, 0, 5), 0, 3);
			addComponent(this, gbl, _menge, new Insets(0, 5, 0, 5), 1, 3);
		}
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7: // Template: Produktanalyse
		{
			// PID, number, 10
			JLabel label_pid = new JLabel(ANALYSE_LABEL_PID);
			JTextField _pid = new JTextField(10);
			_pid.setName(COMPONENT_TEXTFIELD_ANALYSE_PID);
			addComponent(this, gbl, label_pid, new Insets(0, 5, 0, 5), 0, 0);
			addComponent(this, gbl, _pid, new Insets(0, 5, 0, 5), 1, 0);

			// Typ, string, 25
			JLabel label_typ = new JLabel(ANALYSE_LABEL_TYP);
			JTextField _typ = new JTextField(10);
			_typ.setName(COMPONENT_TEXTFIELD_ANALYSE_TYP);
			addComponent(this, gbl, label_typ, new Insets(0, 5, 0, 5), 0, 1);
			addComponent(this, gbl, _typ, new Insets(0, 5, 0, 5), 1, 1);

			// Groesse, number, 10
			JLabel label_groesse = new JLabel(ANALYSE_LABEL_GROESSE);
			JTextField _groesse = new JTextField(10);
			_groesse.setName(COMPONENT_TEXTFIELD_ANALYSE_GROESSE);
			addComponent(this, gbl, label_groesse, new Insets(0, 5, 0, 5), 0, 2);
			addComponent(this, gbl, _groesse, new Insets(0, 5, 0, 5), 1, 2);
		}
			break;
		case 8: // Template: Senkung der Lieferkosten
		{
			// PID, number, 10
			JLabel label_pid = new JLabel(ANALYSE_LABEL_PID);
			JTextField _pid = new JTextField(10);
			_pid.setName(COMPONENT_TEXTFIELD_ANALYSE_PID);
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
