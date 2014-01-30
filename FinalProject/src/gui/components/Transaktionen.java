package gui.components;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import model.Configuration;

/**
 * Transaktionen-Klasse. Enthaelt alle Transaktionen, die der Client dem Kunden anbietet.
 * Kundenpflege zum Anlegen und Aendern von Kunden,
 * Produktverwaltung zum Ein- und Umbuchen von Bestaenden in die Lager und
 * Bestellverwaltung zum Anlegen, Aendern und Ausliefern von Bestellungen.
 * @author borecki
 *
 */
public class Transaktionen extends JPanel implements Configuration {

	private static final long serialVersionUID = 5054965523548199842L;
	private Bestellpositionen posNeu;
	private Bestellpositionen posEdit;
	protected HashMap<String, Component> componentMap;

	/**
	 * Neues Transaktionen-Panel.
	 */
	public Transaktionen() {
		super(new GridBagLayout());
		this.posNeu = new Bestellpositionen("bestellPosListNeu", "listNeu", "addNeu", "delNeu", "inpNeu");
		this.posEdit = new Bestellpositionen("bestellPosListEdit", "listEdit", "addEdit", "delEdit", "inpEdit");
		addComponent(this, createTransactionsPanel(), new Insets(0, 5, 0, 5), 0, 0);
		this.componentMap = new HashMap<String, Component>();
		createComponentMap(this);
	}

	/**
	 * Registriert einen ActionListener.
	 * @param component
	 * @param ae
	 */
	public void addActionListeners(Component component, ActionListener ae){
		((AbstractButton) component).addActionListener(ae);
	}

	/**
	 * Registriert einen ItemListener.
	 * @param component
	 * @param ie
	 */
	public void addItemListeners(Component component, ItemListener ie){
		((JComboBox<?>) component).addItemListener(ie);
	}

	/**
	 * Erzeugt alle 3 Transaktionen-Panels und ordnet sie in einer TabbedPane an.
	 * Jede TabbedPane selbst enthaelt ein CardLayout, in dem die einzelnen Transaktionen angelegt und aufrufbar sind.
	 * @return
	 */
	public Component createTransactionsPanel() {
		JTabbedPane tabbedPane = new JTabbedPane();

		// Tab1 - Kundenpflege
		JPanel panel_tab_1 = createCardKunden();
		tabbedPane.addTab("Kundenpflege", panel_tab_1);

		// Tab2 - Produktverwaltung
		JComponent panel_tab_2 = createCardProdukte();
		tabbedPane.addTab("Produktverwaltung", panel_tab_2);

		// Tab3 - Bestellverwaltung
		JComponent panel_tab_3 = createCardBestellungen();
		tabbedPane.addTab("Bestellverwaltung", panel_tab_3);

		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setName(COMPONENT_TABBEDPANE_TRANSAKTIONEN);

		return tabbedPane;
	}

	/**
	 * Erzeugt das Card-Panel fuer die Kundenpflege.
	 * @return
	 */
	public JPanel createCardKunden(){
		JPanel actions = new JPanel();
		actions.setBorder(BorderFactory.createTitledBorder(KUNDENPFLEGE_CARD_TITLE));
		actions.setName(COMPONENT_PANEL_KUNDENPFLEGE_ACTIONS);
		JComboBox<String> combo = new JComboBox<String>(KUNDENPFLEGE_COMBO_STRINGS);
		combo.setName(COMPONENT_COMBO_KUNDENPFLEGE_ACTIONS);
		combo.setEditable(false);
		actions.add(combo);

		JPanel cards = new JPanel(new CardLayout());
		cards.setName(COMPONENT_PANEL_KUNDENPFLEGE);
		cards.add(new JLabel(KUNDENPFLEGE_LABEL_INFOTEXT), KUNDENPFLEGE_COMBO_STRINGS[0]);
		cards.add(new GridBagTemplate(0, KUNDENPFLEGE_TITLE_KUNDE_NEU, COMPONENT_PANEL_KUNDENPFLEGE_NEU, false), KUNDENPFLEGE_COMBO_STRINGS[1]);
		cards.add(new GridBagTemplate(1, KUNDENPFLEGE_TITLE_KUNDE_EDIT, COMPONENT_PANEL_KUNDENPFLEGE_EDIT, false), KUNDENPFLEGE_COMBO_STRINGS[2]);

		JPanel panel = new JPanel(new GridBagLayout());
		addComponent(panel, actions, new Insets(0, 5, 0, 5), 0, 0);
		addComponent(panel, cards, new Insets(0, 5, 0, 5), 0, 1);

		return panel;
	}

	/**
	 * Erzeugt das Card-Panel fuer die Produktverwaltung.
	 * @return
	 */
	public JPanel createCardProdukte(){
		JPanel actions = new JPanel();
		actions.setBorder(BorderFactory.createTitledBorder(PRODUKTVERWALTUNG_CARD_TITLE));
		actions.setName(COMPONENT_PANEL_PRODUKTVERWALTUNG_ACTIONS);
		JComboBox<String> combo = new JComboBox<String>(PRODUKTVERWALTUNG_COMBO_STRINGS);
		combo.setName(COMPONENT_COMBO_PRODUKTVERWALTUNG_ACTIONS);
		combo.setEditable(false);
		actions.add(combo);

		JPanel cards = new JPanel(new CardLayout());
		cards.setName(COMPONENT_PANEL_PRODUKTVERWALTUNG);
		cards.add(new JLabel(PRODUKTVERWALTUNG_LABEL_INFOTEXT), PRODUKTVERWALTUNG_COMBO_STRINGS[0]);
		cards.add(new GridBagTemplate(2, PRODUKTVERWALTUNG_TITLE_ZULIEFERUNG_NEU, COMPONENT_PANEL_PRODUKTVERWALTUNG_ZULIEFERUNG_NEU, false), PRODUKTVERWALTUNG_COMBO_STRINGS[1]);
		cards.add(new GridBagTemplate(3, PRODUKTVERWALTUNG_TITLE_BESTAND_EDIT, COMPONENT_PANEL_PRODUKTVERWALTUNG_BESTAND_EDIT, false), PRODUKTVERWALTUNG_COMBO_STRINGS[2]);

		JPanel panel = new JPanel(new GridBagLayout());
		addComponent(panel, actions, new Insets(0, 5, 0, 5), 0, 0);
		addComponent(panel, cards, new Insets(0, 5, 0, 5), 0, 1);

		return panel;
	}

	/**
	 * Erzeugt das Card-Panel fuer die Bestellverwaltung.
	 * @return
	 */
	public JPanel createCardBestellungen(){
		JPanel actions = new JPanel();
		actions.setBorder(BorderFactory.createTitledBorder(BESTELLVERWALTUNG_CARD_TITLE));
		actions.setName(COMPONENT_PANEL_BESTELLVERWALUNG_ACTIONS);
		JComboBox<String> combo = new JComboBox<String>(BESTELLVERWALTUNG_COMBO_STRINGS);
		combo.setName(COMPONENT_COMBO_BESTELLVERWALTUNG_ACTIONS);
		combo.setEditable(false);
		actions.add(combo);

		JPanel cards = new JPanel(new CardLayout());
		cards.setName(COMPONENT_PANEL_BESTELLVERWALTUNG);
		cards.setSize(new Dimension(640,630));

		JPanel card1 = new GridBagTemplate(4, BESTELLVERWALTUNG_TITLE_BESTELLUNG_NEU, COMPONENT_PANEL_BESTELLVERWALTUNG_NEU, false, this.posNeu);
		card1.setMinimumSize(new Dimension(400,400));
		card1.setPreferredSize(new Dimension(640,630));
		JPanel card2 = new GridBagTemplate(5, BESTELLVERWALTUNG_TITLE_BESTELLUNG_EDIT, COMPONENT_PANEL_BESTELLVERWALTUNG_EDIT, false, this.posEdit);
		card2.setMinimumSize(new Dimension(400,400));
		card2.setPreferredSize(new Dimension(640,630));
		JPanel card3 = new GridBagTemplate(6, BESTELLVERWALTUNG_TITLE_BESTELLUNG_GO, COMPONENT_PANEL_BESTELLVERWALTUNG_GO, false);
		card3.setMinimumSize(new Dimension(400,400));
		card3.setPreferredSize(new Dimension(640,630));

		cards.add(new JLabel(BESTELLVERWALTUNG_LABEL_INFOTEXT), BESTELLVERWALTUNG_COMBO_STRINGS[0]);
		cards.add(card1, BESTELLVERWALTUNG_COMBO_STRINGS[1]);
		cards.add(card2, BESTELLVERWALTUNG_COMBO_STRINGS[2]);
		cards.add(card3, BESTELLVERWALTUNG_COMBO_STRINGS[3]);

		JPanel panel = new JPanel(new GridBagLayout());
		addComponent(panel, actions, new Insets(0, 5, 0, 5), 0, 0, GridBagConstraints.REMAINDER, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH);
		addComponent(panel, cards, new Insets(0, 5, 0, 5), 0, 1, 1, GridBagConstraints.REMAINDER, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

		return panel;
	}

	/**
	 * Fuegt eine Komponente dem GridBagLayout hinzu.
	 * @param panel
	 * @param c
	 * @param insets
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param fill
	 * @param anchor
	 */
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

	/**
	 * Fuegt eine Komponente dem GridBagLayout hinzu.
	 * @param panel
	 * @param c
	 * @param insets
	 * @param x
	 * @param y
	 */
	public void addComponent(JPanel panel, Component c,
			Insets insets, int x, int y) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.insets = insets;
		panel.add(c, constraints);
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

	/* ############################*/
	/* ##### Getter & Setter  #####*/
	/* ############################*/

	public HashMap<String, Component> getComponentMap(){
	    return this.componentMap;
    }

	public Bestellpositionen getPosNeu(){
		return this.posNeu;
	}

	public Bestellpositionen getPosEdit(){
		return this.posEdit;
	}
}
