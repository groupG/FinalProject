import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class DataTree implements TreeSelectionListener {

	private JTree tree;
	private DefaultMutableTreeNode root;
	private DefaultTableModel default_table;

	private JLabel selectedNode;
	private JTable table;
	private JSplitPane splitPane;
	private JScrollPane treescroll, jtablescroll;
	private JPanel treePanel, dataPanel;

	private Controller controller;
	private CachedRowSet row_set;
	private OutputTable output_table;

	public DataTree(Controller controller, String owner) {
		this.controller = controller;

		this.treePanel = buildTreePanel(owner);

		//populateTable("SELECT * FROM ALTDATEN.KUNDEN WHERE ROWNUM <= 20");


		this.jtablescroll = new JScrollPane(this.table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);




		this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.treePanel,
				this.jtablescroll);
		this.splitPane.setOneTouchExpandable(true);
		this.splitPane.setDividerLocation(200);
		this.treescroll.setMinimumSize(new Dimension(100, 50));
		this.jtablescroll.setMinimumSize(new Dimension(100, 400));
		this.splitPane.setPreferredSize(new Dimension(400, 400));
		this.tree.addTreeSelectionListener(this);
	}

	public JPanel buildTreePanel(String owner) {
		List<String> tables = this.controller.getTables(owner);

		this.root = new DefaultMutableTreeNode(owner);
		DefaultMutableTreeNode[] tableNodes = new DefaultMutableTreeNode[tables.size()];

		int i = 0;
		for (String table : tables) {
			tableNodes[i] = new DefaultMutableTreeNode(table);
			List<String> columns = this.controller.getColumns(owner, table);
			for (String column : columns) {
				tableNodes[i].add(new DefaultMutableTreeNode(column));
			}
			this.root.add(tableNodes[i]);
			i++;
		}

		this.tree = new JTree(this.root);
		this.tree.setRootVisible(false);

		this.treescroll = new JScrollPane(this.tree);
		this.selectedNode = new JLabel();
		this.treescroll.add(this.selectedNode);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(this.treescroll, BorderLayout.CENTER);
		panel.add(this.selectedNode, BorderLayout.SOUTH);

		return panel;
	}

	public JPanel buildDataPanel() {
return null;
	}

	public void populateTable(String query) {
		this.row_set = this.controller.getContentsOfOutputTable(query);
		try {
			this.output_table = new OutputTable(this.row_set);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.table = new JTable();
		this.table.setModel(this.output_table);
	}

	public JSplitPane getSplitPane() {
		return this.splitPane;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.tree
				.getLastSelectedPathComponent();
		this.selectedNode.setText(selectedNode.getUserObject().toString());
		populateTable("SELECT * FROM ALTDATEN."
				+ selectedNode.getUserObject().toString()
				+ " WHERE ROWNUM <= 20");
		this.table.clearSelection();
		this.jtablescroll.repaint();
		this.jtablescroll.revalidate();

	}
}
