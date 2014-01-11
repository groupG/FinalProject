package gui.components;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import model.DB;

import controller.MainController;


/**
 *
 * @author borecki
 *
 */
public class Explorer extends JPanel/*implements TreeSelectionListener */{

	private static final long serialVersionUID = 4203281741809192212L;
	private JTree tree;
	private DefaultMutableTreeNode root;
	private JScrollPane scrollPane;
	private JLabel selectedNode;

	private MainController controller;
	private DB db;

	public Explorer(DB db, String owner) throws SQLException {
		super(new BorderLayout());
		this.db = db;

		buildExplorer(owner);

		this.scrollPane = new JScrollPane(this.tree);
		this.selectedNode = new JLabel();
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(selectedNode, BorderLayout.SOUTH);
	}

	public void buildExplorer(String owner) throws SQLException {

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
		//this.tree.addTreeSelectionListener(this);
	}

	public void addTreeSelectionListener(TreeSelectionListener e) {
		this.tree.addTreeSelectionListener(e);
	}

	/* Getter und Setter */
	public MainController getController() {
		return this.controller;
	}

	public void setController(MainController controller) {
		this.controller = controller;
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

