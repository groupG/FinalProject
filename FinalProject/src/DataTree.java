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

	private Controller controller;
	private CachedRowSet row_set;
	private OutputTable output_table;

	public DataTree(Controller controller) {
		this.controller = controller;
		List<String> tables = this.controller.getTables("ALTDATEN");

		/*
		 * Iterator<String> iterator = tables.iterator();
		 * while(iterator.hasNext()){ System.out.println(iterator.next()); }
		 */
		this.root = new DefaultMutableTreeNode("ALTDATEN");
		DefaultMutableTreeNode[] tableNodes = new DefaultMutableTreeNode[tables.size()];

		int i = 0;
		for (String table : tables) {
			tableNodes[i] = new DefaultMutableTreeNode(table);
			List<String> columns = this.controller
					.getColumns("ALTDATEN", table);
			for (String column : columns) {
				tableNodes[i].add(new DefaultMutableTreeNode(column));
			}
			this.root.add(tableNodes[i]);
			i++;
		}

		this.tree = new JTree(this.root);
		this.tree.setRootVisible(false);
		this.table = new JTable();
		this.treescroll = new JScrollPane(this.tree);
		this.selectedNode = new JLabel();
		this.treescroll.add(this.selectedNode);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(this.treescroll, BorderLayout.CENTER);
		panel.add(this.selectedNode, BorderLayout.SOUTH);
		this.jtablescroll = new JScrollPane(this.table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				panel, this.jtablescroll);
		this.splitPane.setOneTouchExpandable(true);
		this.splitPane.setDividerLocation(200);
		this.treescroll.setMinimumSize(new Dimension(100, 50));
		this.jtablescroll.setMinimumSize(new Dimension(100, 400));
		this.splitPane.setPreferredSize(new Dimension(400, 400));
		this.tree.addTreeSelectionListener(this);
	}

	public void populateTable(String query){
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
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
		this.selectedNode.setText(selectedNode.getUserObject().toString());
	}
}
