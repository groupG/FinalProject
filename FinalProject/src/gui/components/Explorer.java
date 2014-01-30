package gui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import model.Configuration;
import model.DB;


/**
 * Explorer-Klasse, die einen Einblick in den Tablespace gewaehrt und alle Relationen in einer baumartigen Struktur anzeigt.
 * @author borecki
 *
 */
public class Explorer extends JPanel implements Configuration {

	private static final long serialVersionUID = 4203281741809192212L;
	private JTree tree;
	private DefaultMutableTreeNode root;
	private JScrollPane scrollPane;

	private DB db;
	protected HashMap<String, Component> componentMap;

	/**
	 * Neues Explorer-Objekt.
	 * @param db
	 * @param owner
	 * @throws SQLException
	 */
	public Explorer(DB db, String owner) throws SQLException {
		super(new GridBagLayout());
		this.db = db;
		addComponent(this, createExplorerPanel(owner), new Insets(0, 5, 0, 5), 0, 0);
		this.componentMap = new HashMap<String, Component>();
		createComponentMap(this);
	}

	/**
	 * Registriert einen TreeSelectionListener.
	 * @param component
	 * @param te
	 */
	public void addTreeSelectionListeners(Component component, TreeSelectionListener te){
		((JTree) component).addTreeSelectionListener(te);
	}

	/**
	 * Erzeugt ein neues Explorer-Panel. Knoten repraesentieren Relationen, Blaetter Spalten.
	 * @param owner
	 * @return
	 * @throws SQLException
	 */
	public Component createExplorerPanel(String owner) throws SQLException {

		this.root = new DefaultMutableTreeNode(owner);
		List<String> tables = this.db.getTables(owner);

		DefaultMutableTreeNode[] tableNodes = new DefaultMutableTreeNode[tables.size()];

		int i = 0;
		for (String table : tables) {
			tableNodes[i] = new DefaultMutableTreeNode(table);
			List<String> columns = this.db.getColumns(owner, table);
			for (String column : columns) {
				tableNodes[i].add(new DefaultMutableTreeNode(column));
			}
			this.root.add(tableNodes[i]);
			i++;
		}

		this.tree = new JTree(this.root);
		this.tree.setRootVisible(false);
		this.tree.setName(COMPONENT_TREE_EXPLORER);
		this.scrollPane = new JScrollPane(this.tree);
		this.scrollPane.setName(COMPONENT_SCROLLPANE_EXPLORER);

		return scrollPane;
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
	 */
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

	/* ############################*/
	/* ##### Getter & Setter  #####*/
	/* ############################*/

	public HashMap<String, Component> getComponentMap(){
	    return this.componentMap;
    }

	public JTree getTree() {
		return this.tree;
	}

	public void setTree(DefaultMutableTreeNode rootNode) {
		if (rootNode == null) {
			this.tree = new JTree(this.root);
		} else {
			this.tree = new JTree(rootNode);
		}
	}

	public DefaultMutableTreeNode getRoot() {
		return this.root;
	}

	public void setRoot(String owner) {
		this.root = new DefaultMutableTreeNode(owner);
	}
}
