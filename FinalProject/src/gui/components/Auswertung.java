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

public class Auswertung extends JPanel implements Configuration {

	private static final long serialVersionUID = 2973871514227057444L;
	protected HashMap<String, Component> componentMap;

	public Auswertung() {
		super(new GridBagLayout());
		addComponent(this, createAnalysisPanel(), new Insets(0, 5, 0, 5), 0, 0);
		this.componentMap = new HashMap<String, Component>();
		createComponentMap(this);
	}

	public void addActionListeners(Component component, ActionListener ae){
		((AbstractButton) component).addActionListener(ae);
	}

	public Component createAnalysisPanel()
	{
		JTabbedPane tabbedPane = new JTabbedPane();
		// TODO: Tooltips, Shortcuts, PreferredSize dynamisch?

		// Tab1 - Produktanalyse
		JPanel panel_tab_1 = new GridBagTemplate(7, ANALYSE_TITLE_PRODUKTANALYSE, COMPONENT_PANEL_PRODUKTANALYSE, false);
		tabbedPane.addTab("Produktanalyse", panel_tab_1);

		// Tab2 - Senkung der Lieferkosten
		JPanel panel_tab_2 = new GridBagTemplate(8, ANALYSE_TITLE_LIEFERKOSTENSENKUNG, COMPONENT_PANEL_LIEFERKOSTENSENKUNG, false);
		tabbedPane.addTab("Lieferkostensenkung", panel_tab_2);

		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		return tabbedPane;
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

	public HashMap<String, Component> getComponentMap(){
	    return this.componentMap;
    }
}

