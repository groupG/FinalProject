package gui.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.rowset.CachedRowSet;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import model.DB;
import model.OutputTableModel;

import controller.MainController;

public class DBOutput extends JPanel {

	private static final long serialVersionUID = -57138235195624646L;
	private JTable table;
	private JScrollPane scrollPane;

	private MainController controller;
	private CachedRowSet rowset;
	private OutputTableModel tableModel;

	private DB db;
	protected HashMap<String, Component> componentMap;

	public DBOutput(DB db, String query) throws SQLException {
		super(new BorderLayout());

		this.db = db;

		populateTable(query);

		this.table = new JTable(this.tableModel);
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		this.table.setFillsViewportHeight(true);
		this.table.setAutoCreateRowSorter(true);
		this.scrollPane = new JScrollPane(this.table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.add(scrollPane, BorderLayout.CENTER);
		this.componentMap = new HashMap<String, Component>();
		createComponentMap(this);
	}

	public void populateTable(String query) throws SQLException {
		if (query == null){
			query = new String("");
		}
		this.rowset = this.db.getContentsOfOutputTable(query);
		this.tableModel = new OutputTableModel(this.rowset);
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

	/* Getter und Setter */
	public MainController getController() {
		return this.controller;
	}

	public void setController(MainController controller) {
		this.controller = controller;
	}

	public CachedRowSet getRowset() {
		return this.rowset;
	}

	public void setRowset(String query) {
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
}
