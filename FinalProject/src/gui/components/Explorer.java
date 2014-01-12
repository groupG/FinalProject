package gui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import model.Configuration;
import model.DB;


/**
 *
 * @author borecki
 *
 */
public class Explorer extends JPanel implements Configuration {

	private static final long serialVersionUID = 4203281741809192212L;
	private JTree tree;
	private DefaultMutableTreeNode root;
	private JScrollPane scrollPane;
	private JLabel selectedNode;

	private DB db;
	protected HashMap<String, Component> componentMap;

	public Explorer(DB db, String owner) throws SQLException {
		super(new GridBagLayout());
		this.db = db;

		this.selectedNode = new JLabel();
		addComponent(this, createExplorerPanel(owner), new Insets(0, 5, 0, 5), 0, 0);
//		addComponent(this, this.selectedNode, new Insets(0, 5, 0, 5), 0, 1);

		this.componentMap = new HashMap<String, Component>();
		createComponentMap(this);
	}

	public void addTreeSelectionListeners(Component component, TreeSelectionListener te){
		((JTree) component).addTreeSelectionListener(te);
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

	public HashMap<String, Component> getComponentMap(){
	    return this.componentMap;
    }

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

//	public void addTreeSelectionListener(TreeSelectionListener e) {
//		this.tree.addTreeSelectionListener(e);
//	}

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

