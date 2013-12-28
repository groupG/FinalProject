import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainView extends JPanel implements ConfigImpl {

	private static final long serialVersionUID = 5054965523548199842L;
	private JPanel panel;
	private JTextArea _debug;

	public MainView() {
		super(new GridLayout(1, 1));
		add(buildGui());
	}

	public Component buildGui() {
		JTabbedPane tabbedPane = new JTabbedPane();

		// Tab1
		JComponent panel_tab_1 = buildTab1();
		tabbedPane.addTab("Kunden/Lieferanten", panel_tab_1);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		panel_tab_1.setPreferredSize(new Dimension(800, 200));

		// Tab2
		JComponent panel_tab_2 = buildTab2();
		tabbedPane.addTab("Produkte/Lieferungen", panel_tab_2);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		panel_tab_2.setPreferredSize(new Dimension(800, 200));

		// Tab2
		JComponent panel_tab_3 = makeTextPanel("Bestelltransaktionen");
		tabbedPane.addTab("Bestellungen", panel_tab_3);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
		panel_tab_3.setPreferredSize(new Dimension(800, 200));

		// Tab2
		JComponent panel_tab_4 = makeTextPanel("Produktanalyse");
		tabbedPane.addTab("Analyse", panel_tab_4);
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
		panel_tab_4.setPreferredSize(new Dimension(800, 200));

		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		return tabbedPane;
	}

	// Pflegetransaktionen
	public JPanel buildTab1() {

		/* Gui-Elemente */
		panel = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		panel.setLayout(gbl);

		JLabel label_pflege = new JLabel(LABEL_PFLEGETRANSAKTION);
		addComponent(panel, gbl, label_pflege, new Insets(0,5,0,5), 0, 0);

		// KID, number, 10
		/*
		 * CREATE SEQUENCE kid_seq
		 *   MINVALUE 1
		 *   START WITH ??
		 *   INCREMENT BY 1
		 *   NOCACHE;
		 * */
		JLabel label_kid = new JLabel(LABEL_KID);
		JTextField _kid = new JTextField(10);
		addComponent(panel, gbl, label_kid, new Insets(0,5,0,5), 0, 1);
		addComponent(panel, gbl, _kid, new Insets(0,5,0,5), 1, 1);

		// Name, string, 25
		JLabel label_name = new JLabel(LABEL_NAME);
		JTextField _name = new JTextField(10);
		addComponent(panel, gbl, label_name, new Insets(0,5,0,5), 0, 2);
		addComponent(panel, gbl, _name, new Insets(0,5,0,5), 1, 2);

		// Adresse, char, 40
		JLabel label_adresse = new JLabel(LABEL_ADRESSE);
		JTextField _adresse = new JTextField(10);
		addComponent(panel, gbl, label_adresse, new Insets(0,5,0,5), 0, 3);
		addComponent(panel, gbl, _adresse, new Insets(0,5,0,5), 1, 3);

		// Tel, char, 15
		JLabel label_tel = new JLabel(LABEL_TEL);
		JTextField _tel = new JTextField(10);
		addComponent(panel, gbl, label_tel, new Insets(0,5,0,5), 0, 4);
		addComponent(panel, gbl, _tel, new Insets(0,5,0,5), 1, 4);

		// Nation, char, 25
		// TODO: JComboBox mit Werten aus DB
		JLabel label_nation = new JLabel(LABEL_NATION);
		String[] nation_strings = {"Nation 1", "Nation 2", "Nation 3"};
		JComboBox<String> _nation = new JComboBox<String>(nation_strings);
		_nation.setSelectedIndex(0);
		_nation.setEditable(true);
		addComponent(panel, gbl, label_nation, new Insets(0,5,0,5), 0, 5);
		addComponent(panel, gbl, _nation, new Insets(0,5,0,5), 1, 5);

		// Konto, number, 12
		JLabel label_konto = new JLabel(LABEL_KONTO);
		NumberFormat format_konto = NumberFormat.getNumberInstance();
		format_konto.setMinimumFractionDigits(2);
		JFormattedTextField _konto = new JFormattedTextField(format_konto);
		_konto.setValue(new Double(0.00));
		_konto.setColumns(10);
		_konto.setEditable(false);
		addComponent(panel, gbl, label_konto, new Insets(0,5,0,5), 0, 6);
		addComponent(panel, gbl, _konto, new Insets(0,5,0,5), 1, 6);

		// Branche, char, 10
		JLabel label_branche = new JLabel(LABEL_BRANCHE);
		String[] branche_strings = {"AUTOMOBILE", "BUILDING", "FURNITURE", "HOUSEHOLD", "MACHINERY"};
		JComboBox<String> _branche = new JComboBox<String>(branche_strings);
		_branche.setSelectedIndex(0);
		_branche.setEditable(true);
		addComponent(panel, gbl, label_branche, new Insets(0,5,0,5), 0, 7);
		addComponent(panel, gbl, _branche, new Insets(0,5,0,5), 1, 7);

		// Debug-Output
		JLabel label_debug = new JLabel("Debug: ");
		_debug = new JTextArea(100, 10);
		_debug.setAutoscrolls(true);

		List<Component> complist = getAllInputs(panel);
		String debug_string = "Anzahl der Elemente: " + complist.size();
		_debug.setText(debug_string);
		addComponent(panel, gbl, label_debug, new Insets(0,5,0,5), 2, 0);
		addComponent(panel, gbl, _debug, new Insets(0,5,0,5), 2, 1, 1, 6, 1.0, 1.0);

		// Ausfuehren
		JButton button_exec = new JButton(LABEL_EXECUTE);
		button_exec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Component> complist = getAllInputs(panel);
				String debug_string = getDebugText(complist);
				_debug.setText(debug_string);
			}
		});
		addComponent(panel, gbl, button_exec, new Insets(0,5,0,5), 2, 7);

		return panel;
	}

	public JPanel buildTab2() {

		/* Gui-Elemente */
		JPanel panel = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		panel.setLayout(gbl);

		// ZID
		JLabel label_zid = new JLabel(LABEL_ZID);
		NumberFormat format_zid = NumberFormat.getNumberInstance();
		JTextField _zid = new JFormattedTextField(format_zid);
		_zid.setColumns(10);
		addComponent(panel, gbl, label_zid, new Insets(0,5,0,5), 0, 0);
		addComponent(panel, gbl, _zid, new Insets(0,5,0,5), 1, 0);

		// Zulieferungsposition
		JLabel label_posnr = new JLabel(LABEL_POSNR);
		NumberFormat format_posnr = NumberFormat.getNumberInstance();
		JTextField _posnr = new JFormattedTextField(format_posnr);
		_posnr.setColumns(10);
		_posnr.setEditable(false);
		addComponent(panel, gbl, label_posnr, new Insets(0,5,0,5), 0, 1);
		addComponent(panel, gbl, _posnr, new Insets(0,5,0,5), 1, 1);

		// PID
		JLabel label_pid = new JLabel(LABEL_PID);
		NumberFormat format_pid = NumberFormat.getNumberInstance();
		JTextField _pid = new JFormattedTextField(format_pid);
		_pid.setColumns(10);
		_pid.setEditable(false);
		addComponent(panel, gbl, label_pid, new Insets(0,5,0,5), 0, 2);
		addComponent(panel, gbl, _pid, new Insets(0,5,0,5), 1, 2);

		// Anzahl
		JLabel label_anzahl = new JLabel(LABEL_ANZAHL);
		NumberFormat format_anzahl = NumberFormat.getIntegerInstance();
		JTextField _anzahl = new JFormattedTextField(format_anzahl);
		_anzahl.setColumns(10);
		_anzahl.setEditable(false);
		addComponent(panel, gbl, label_anzahl, new Insets(0,5,0,5), 0, 3);
		addComponent(panel, gbl, _anzahl, new Insets(0,5,0,5), 1, 3);

		// Status
		JLabel label_status = new JLabel(LABEL_STATUS);
		JTextField _status = new JTextField(10);
		_status.setEditable(false);
		addComponent(panel, gbl, label_status, new Insets(0,5,0,5), 0, 4);
		addComponent(panel, gbl, _status, new Insets(0,5,0,5), 1, 4);

		// Ausfuehren
		JButton button_exec = new JButton(LABEL_EXECUTE);
		addComponent(panel, gbl, button_exec, new Insets(0,5,0,5), 2, 5);

		return panel;
	}

	public JFrame buildTab3() {
		return null;
	}

	public JFrame buildTab4() {
		return null;
	}

	public JFrame buildTab5() {
		return null;
	}

	public List<Component> getAllInputs(Container container){
		Component[] components = container.getComponents();
		List<Component> component_list = new ArrayList<Component>();
		for (Component comp : components){
			component_list.add(comp);
			if (comp instanceof Container){
				component_list.addAll(getAllInputs((Container) comp));
			}
		}
		return component_list;
	}

	public String getDebugText(List<Component> complist){
		String output = "";
		for (int i=0; i<complist.size();i++){
			if(complist.get(i) instanceof JTextField){
				output += "Element: " + ((JTextField) complist.get(i)).getText() + "\n";
			}
		}
		return output;
	}

	public void addComponent(JPanel panel, GridBagLayout gbl,
			Component c, Insets insets, int x, int y, int width, int height,
			double weightx, double weighty) {
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

	public void addComponent(JPanel panel, GridBagLayout layout,
			Component c, Insets insets, int x, int y) {
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

	protected JComponent makeTextPanel(String text) {
		JPanel tab_header = new JPanel(false);
		JLabel fillText = new JLabel(text);
		fillText.setHorizontalAlignment(JLabel.CENTER);
		tab_header.setLayout(new GridLayout(1, 1));
		tab_header.add(fillText);
		return tab_header;
	}

	protected ImageIcon makeImageIcon(String path) {
		java.net.URL imgUrl = MainView.class.getResource(path);
		if (imgUrl != null) {
			return new ImageIcon(imgUrl);
		} else {
			System.err.println("Datei konnte nicht gefudnen werden. Pfad: "
					+ path);
			return null;
		}
	}
}
