import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;


public class DataTree {

	private JTree tree;
	private DefaultMutableTreeNode root;
	private DefaultTableModel default_table;

	private JTable table;
	private JSplitPane splitPane;
	private JScrollPane treescroll, jtablescroll;

	private Controller controller;

	public DataTree(Controller controller){
		this.controller = controller;
		List<String> tables = this.controller.getTables("ALTDATEN");

		/*Iterator<String> iterator = tables.iterator();
		while(iterator.hasNext()){
			System.out.println(iterator.next());
		}*/
		this.root = new DefaultMutableTreeNode("Datenbaum");
		DefaultMutableTreeNode[] tableNodes = new DefaultMutableTreeNode[tables.size()];

		int i = 0;
		for(String table : tables){
			tableNodes[i] = new DefaultMutableTreeNode(table);
			List<String> columns = this.controller.getColumns("ALTDATEN", table);
			for(String column : columns){
				tableNodes[i].add(new DefaultMutableTreeNode(column));
			}
			this.root.add(tableNodes[i]);
			i++;
		}



		this.tree = new JTree(this.root);
		this.tree.setRootVisible(false);
		this.table = new JTable();
		this.treescroll = new JScrollPane(this.tree);
		this.jtablescroll = new JScrollPane(this.table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.treescroll, this.jtablescroll);
		this.splitPane.setOneTouchExpandable(true);
		this.splitPane.setDividerLocation(200);
		this.treescroll.setMinimumSize(new Dimension(100,50));
		this.jtablescroll.setMinimumSize(new Dimension(100,50));
		this.splitPane.setPreferredSize(new Dimension(400,200));
		//this.tree.addTreeSelectionListener(this);


		//DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
		//if (node == null) return;
		//String nodeInfo = (String) node.getUserObject();
		//System.out.println(nodeInfo);
	}

	public JSplitPane getSplitPane(){
		return this.splitPane;
	}
}
