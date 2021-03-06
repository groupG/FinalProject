package gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.rowset.CachedRowSet;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import model.Configuration;
import model.DB;
import model.OutputTableModel;

/**
 * Output-Klasse zum Anzeigen von Daten aus der Datenbank.
 * @author borecki
 *
 */
public class DBOutput extends JPanel implements Configuration {

	private static final long serialVersionUID = -57138235195624646L;
	private JTable table;
	private JScrollPane scrollPane;

	private CachedRowSet rowset;
	private OutputTableModel tableModel;

	private DB db;
	protected HashMap<String, Component> componentMap;
	private String filterTable;

	/**
	 * Neues DBOutput-Objekt zum Anzeigen von Daten aus der Datenbank.
	 * @param db
	 * @param query
	 * @throws SQLException
	 */
	public DBOutput(DB db, String query) throws SQLException {
		super(new GridBagLayout());
		this.db = db;
		this.tableModel = (OutputTableModel) populateTable(query);
		addTableModel(this.tableModel);
		addTableToPane();
		addFilterToPane();
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
	 * Fuellt die Tabelle mit den Daten, die die gegeben Query zurueckliefert.
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public TableModel populateTable(String query) throws SQLException {
		if (query == null) {
			query = new String("");
		}
		if (this.rowset != null)
			this.rowset.release();
		this.rowset = this.db.getContentsOfOutputTable(query);
		this.rowset.acceptChanges();
		return new OutputTableModel(this.rowset);
	}

	/**
	 * Erstellt eine neue Tabelle und initialisiert sie mit dem gegebenen TableModel.
	 * @param model
	 */
	public void addTableModel(TableModel model) {
		this.table = new JTable(model);
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		this.table.setRowSelectionAllowed(false);
		this.table.setColumnSelectionAllowed(false);
		this.table.setFillsViewportHeight(true);
		this.table.setCellSelectionEnabled(true);
		this.table.setAutoCreateRowSorter(true);
		this.table.getTableHeader().setBackground(Color.YELLOW);
	}

	/**
	 * Fuegt das Suchen/Filter-Panel zum Hauptpanel hinzu.
	 */
	public void addFilterToPane(){
		JLabel filter = new JLabel("Filter");
		JTextField filterText = new JTextField(70);
		filterText.setName("textfield_dboutput_filter");
		JButton filterButton = new JButton("Suchen");
		filterButton.setName("button_dboutput_filter");
		filterButton.setActionCommand("button_dboutput_filter");
		JPanel filterPanel = new JPanel();
		filterPanel.setName("panel_dboutput_filter");
		filterPanel.add(filter);
		filterPanel.add(filterText);
		filterPanel.add(filterButton);

		filterPanel.setMinimumSize(new Dimension(500,50));
		addComponent(this, filterPanel, new Insets(0, 5, 0, 5), 0, 1, 2, GridBagConstraints.REMAINDER, GridBagConstraints.HORIZONTAL, GridBagConstraints.LAST_LINE_START);
	}

	/**
	 * Fuegt die Scrollpane zum Hauptpanel hinzu.
	 */
	public void addTableToPane() {
		this.scrollPane = new JScrollPane(this.table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setName(COMPONENT_SCROLLPANE_DBOUTPUT);
		this.scrollPane.setMinimumSize(new Dimension(500,330));
		addComponent(this, this.scrollPane, new Insets(0, 5, 0, 5), 0, 0, 1, GridBagConstraints.RELATIVE, GridBagConstraints.BOTH, GridBagConstraints.FIRST_LINE_START);
	}

	/**
	 * Entfernt die Scrollpane vom Hauptpanel.
	 */
	public void removeScrollPane() {
		this.remove(this.scrollPane);
	}

	/**
	 * Erstellt eine Map mit allen Componenten des Containers.
	 * @param component
	 */
	public void createComponentMap(Component component) {
		this.componentMap.put(component.getName(), component);
		if (component instanceof Container) {
			if (((Container) component).getComponentCount() > 0) {
				for (Component child : ((Container) component).getComponents()) {
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
	 */
	public void addComponent(JPanel panel, Component c, Insets insets, int x,
			int y) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
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

	/* ############################*/
	/* ##### Getter & Setter  #####*/
	/* ############################*/

	public HashMap<String, Component> getComponentMap() {
		return this.componentMap;
	}

	public CachedRowSet getRowset() {
		return this.rowset;
	}

	public void setRowset(String query) throws SQLException{
		this.rowset = this.db.getContentsOfOutputTable(query);
	}

	public OutputTableModel getTableModel() {
		return this.tableModel;
	}

	public void setTableModel(CachedRowSet rowset) throws SQLException {
		if (rowset == null) {
			this.tableModel = new OutputTableModel(getRowset());
		} else {
			this.tableModel = new OutputTableModel(rowset);
		}
	}

	public JTable getTable() {
		return this.table;
	}

	public void setTable(OutputTableModel model) {
		if (model == null) {
			this.table = new JTable();
		} else {
			this.table = new JTable(model);
		}
	}

	public void setFilterTable(String table){
		this.filterTable = table;
	}

	public String getFilterTable(){
		return this.filterTable;
	}
}
