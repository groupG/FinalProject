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
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import model.Configuration;

public class Transaktionen extends JPanel implements Configuration {

	private static final long serialVersionUID = 5054965523548199842L;

	protected HashMap<String, Component> componentMap;

	public Transaktionen() {
		super(new GridBagLayout());
		addComponent(this, createTransactionsPanel(), new Insets(0, 5, 0, 5), 0, 0);
		this.componentMap = new HashMap<String, Component>();
		createComponentMap(this);
	}

	public void addActionListeners(Component component, ActionListener ae){
		((AbstractButton) component).addActionListener(ae);
	}

	public void addItemListeners(Component component, ItemListener ie){
		((JComboBox<?>) component).addItemListener(ie);
	}

	public Component createTransactionsPanel() {
		JTabbedPane tabbedPane = new JTabbedPane();
		// TODO: Tooltips, Shortcuts, PreferredSize dynamisch?

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

		JPanel card1 = new GridBagTemplate(4, BESTELLVERWALTUNG_TITLE_BESTELLUNG_NEU, COMPONENT_PANEL_BESTELLVERWALTUNG_NEU,true);
		card1.setMinimumSize(new Dimension(400,400));
		card1.setPreferredSize(new Dimension(640,630));
		JPanel card2 = new GridBagTemplate(5, BESTELLVERWALTUNG_TITLE_BESTELLUNG_EDIT, COMPONENT_PANEL_BESTELLVERWALTUNG_EDIT, true);
		card2.setMinimumSize(new Dimension(400,400));
		card2.setPreferredSize(new Dimension(640,630));
		JPanel card3 = new GridBagTemplate(6, BESTELLVERWALTUNG_TITLE_BESTELLUNG_GO, COMPONENT_PANEL_BESTELLVERWALTUNG_GO, true);
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
