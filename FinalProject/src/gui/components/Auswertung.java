package gui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import model.Configuration;

/**
 * Analyse-Klasse. Enthaelt alle Auswertungen, die der Client dem Kunden zur Ausfuehrung anbietet.
 * Produktanalyse zum Auswerten von Produktentypen und Groessen und
 * Lieferkostenanalyse zum Auswerten der Lieferkosten fuer die Bestaende eines gegeben Produkts.
 * @author borecki
 *
 */
public class Auswertung extends JPanel implements Configuration {

	private static final long serialVersionUID = 2973871514227057444L;
	protected HashMap<String, Component> componentMap;

	/**
	 * Neues Auswertungen-Panel.
	 */
	public Auswertung() {
		super(new GridBagLayout());
		addComponent(this, createAnalysisPanel(), new Insets(0, 5, 0, 5), 0, 0);
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
	 * Erzeugt beide Analyse-Panels und ordnet sie in einer TabbedPane an.
	 * @return
	 */
	public Component createAnalysisPanel()
	{
		JTabbedPane tabbedPane = new JTabbedPane();

		// Tab1 - Produktanalyse
		JPanel panel_tab_1 = new GridBagTemplate(7, ANALYSE_TITLE_PRODUKTANALYSE, COMPONENT_PANEL_PRODUKTANALYSE, false);
		tabbedPane.addTab("Produktanalyse", panel_tab_1);

		// Tab2 - Senkung der Lieferkosten
		JPanel panel_tab_2 = new GridBagTemplate(8, ANALYSE_TITLE_LIEFERKOSTENSENKUNG, COMPONENT_PANEL_LIEFERKOSTENSENKUNG, false);
		tabbedPane.addTab("Lieferkostensenkung", panel_tab_2);

		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		return tabbedPane;
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
	 * Fuegt eine Komponente dem GridBagLayout hinzu.
	 * @param panel
	 * @param c
	 * @param insets
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param weightx
	 * @param weighty
	 */
	public void addComponent(JPanel panel, Component c,
			Insets insets, int x, int y, int width, int height, double weightx,
			double weighty) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
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

	/* ############################*/
	/* ##### Getter & Setter  #####*/
	/* ############################*/

	public HashMap<String, Component> getComponentMap(){
	    return this.componentMap;
    }
}

