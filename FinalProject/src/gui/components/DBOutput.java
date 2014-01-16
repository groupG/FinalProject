package gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.rowset.CachedRowSet;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import model.Configuration;
import model.DB;
import model.OutputTableModel;

public class DBOutput extends JPanel implements Configuration{

	private static final long serialVersionUID = -57138235195624646L;
	private JTable table;
	private JScrollPane scrollPane;

	private CachedRowSet rowset;
	private OutputTableModel tableModel;

	private DB db;
	protected HashMap<String, Component> componentMap;

	public DBOutput(DB db, String query) throws SQLException {
		super(new GridBagLayout());

		this.db = db;

		this.tableModel = (OutputTableModel) populateTable(query);
		addTableModel(this.tableModel);
		addTableToPane();

		this.componentMap = new HashMap<String, Component>();
		createComponentMap(this);
	}

	public TableModel populateTable(String query) throws SQLException {
		if (query == null){
			query = new String("");
		}
		if (this.rowset != null) this.rowset.release();
		this.rowset = this.db.getContentsOfOutputTable(query);
		this.rowset.acceptChanges();
		return new OutputTableModel(this.rowset);
	}

	public void addTableModel(TableModel model){
		this.table = new JTable(model);
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		this.table.setFillsViewportHeight(true);
		this.table.setAutoCreateRowSorter(true);
		this.table.getTableHeader().setBackground(Color.YELLOW);
	}

	public void addTableToPane(){
		this.scrollPane = new JScrollPane(this.table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setName(COMPONENT_SCROLLPANE_DBOUTPUT);
		addComponent(this, this.scrollPane, new Insets(0, 5, 0, 5), 0, 0);
	}

	public void removeScrollPane(){
		this.remove(this.scrollPane);
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

	public void addComponent(JPanel panel, Component c,
			Insets insets, int x, int y) {
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