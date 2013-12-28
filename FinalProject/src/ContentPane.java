import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.sun.rowset.CachedRowSetImpl;

/**
 * Main-View. Ermoeglicht es dem Nutzer, Unterkunfts-ID's einzugeben und liefert
 * die entsprechenden Resultate zurueck.
 *
 * @author borecki
 */
public class ContentPane extends JFrame implements ConfigImpl {

	private static final long serialVersionUID = 5054965523548199842L;

	// GUI-Elemente
	private Container contentPane;
	private OutputTable output_table;
	private JTable table;
	private JButton buttonExecute;
	private JButton buttonLogout;
	private JLabel labelFeedback;
	private JLabel label_U_ID;
	private JTextField text_U_ID;
	private Border blackline, redline, greenline, yellowline;
	private TitledBorder title;
	private GridBagConstraints layout_constraints;
	private JScrollPane scrollPane;

	// Logik
	private DB db;
	private CachedRowSet row_set;

	/**
	 * Konstruktor fuer die Main-View.
	 *
	 * @param db
	 * @throws SQLException
	 */
	public ContentPane(DB db) throws SQLException {
		super(TITLE);

		this.db = db;
		buildGui();
	}

	public void buildGui() {

		// Schliesst die Anwendung und trennt die Datenbank-Verdindung
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				close();
				System.exit(0);
			}
		});

		//this.label_U_ID = new JLabel(LABEL_U_ID);
		this.label_U_ID = new JLabel("UID: ");
		this.labelFeedback = new JLabel(LABEL_MAIN_FEEDBACK_INFO);
		this.text_U_ID = new JTextField(10);
		this.buttonLogout = new JButton(LABEL_LOGOUT);
		this.buttonExecute = new JButton(LABEL_EXECUTE);

		this.contentPane = getContentPane();
		this.contentPane
				.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.contentPane.setLayout(new GridBagLayout());
		this.layout_constraints = new GridBagConstraints();

		// Label fuer Unterkunfts-ID
		this.layout_constraints.fill = GridBagConstraints.HORIZONTAL;
		this.layout_constraints.anchor = GridBagConstraints.LINE_START;
		this.layout_constraints.ipady = 1;
		this.layout_constraints.weightx = 0.25;
		this.layout_constraints.weighty = 0;
		this.layout_constraints.gridx = 0;
		this.layout_constraints.gridy = 0;
		this.layout_constraints.gridwidth = 1;
		this.layout_constraints.insets = new Insets(10, 5, 5, 20);
		this.contentPane.add(this.label_U_ID, this.layout_constraints);

		// Textfeld fuer Unterkunfts-ID
		this.layout_constraints.fill = GridBagConstraints.HORIZONTAL;
		this.layout_constraints.anchor = GridBagConstraints.LINE_END;
		this.layout_constraints.weightx = 0.75;
		this.layout_constraints.weighty = 0;
		this.layout_constraints.gridx = 1;
		this.layout_constraints.gridy = 0;
		this.layout_constraints.gridwidth = 1;
		this.layout_constraints.insets = new Insets(10, 5, 5, 20);
		this.contentPane.add(this.text_U_ID, this.layout_constraints);

		// Label fuer Feedback an den Nutzer
		this.blackline = BorderFactory.createLineBorder(Color.black);
		this.redline = BorderFactory.createLineBorder(Color.red);
		this.greenline = BorderFactory.createLineBorder(Color.green);
		this.yellowline = BorderFactory.createLineBorder(Color.yellow);
		this.title = BorderFactory.createTitledBorder(this.blackline,
				BORDER_TITLE_INFO);
		this.title.setTitleJustification(TitledBorder.CENTER);
		this.labelFeedback.setBorder(this.title);
		this.layout_constraints.weighty = 2;
		this.layout_constraints.gridx = 0;
		this.layout_constraints.gridy = 1;
		this.layout_constraints.gridwidth = 2;
		this.layout_constraints.gridheight = 2;
		this.layout_constraints.insets = new Insets(10, 5, 5, 20);
		this.contentPane.add(this.labelFeedback, this.layout_constraints);

		// Button zum Ausfuehren der Query
		this.layout_constraints.fill = GridBagConstraints.HORIZONTAL;
		this.layout_constraints.anchor = GridBagConstraints.LINE_END;
		this.layout_constraints.weighty = 1;
		this.layout_constraints.gridx = 2;
		this.layout_constraints.gridy = 0;
		this.layout_constraints.gridwidth = 1;
		this.layout_constraints.gridheight = 2;
		this.layout_constraints.insets = new Insets(10, 5, 5, 20);
		this.contentPane.add(this.buttonExecute, this.layout_constraints);
		// ActionListener fuer den Execute-Button
		this.buttonExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// check, ob die ID existiert
				boolean recordExists = false;
				try {
					recordExists = db.checkIfElementexists("UNTERKUNFT",
							"u_id", getInput());
				} catch (SQLException e2) {
					showSQLException(e2);
				}
				try {
					// falls ja --> positives Feedback + Daten werden
					// geholt/angezeigt
					if (recordExists) {
						feedback(1);
						populateTable(false);
						updateTable();
					}
					// falls nein --> negatives Feedback
					else {
						feedback(2);
					}
				} catch (SQLException e1) {
					showSQLException(e1);
				}
			}
		});

		// Button zum Logout
		this.layout_constraints.fill = GridBagConstraints.HORIZONTAL;
		this.layout_constraints.anchor = GridBagConstraints.LINE_START;
		this.layout_constraints.weighty = 2;
		this.layout_constraints.gridx = 2;
		this.layout_constraints.gridy = 1;
		this.layout_constraints.gridwidth = 1;
		this.layout_constraints.gridheight = 2;
		this.layout_constraints.insets = new Insets(10, 5, 5, 20);
		this.contentPane.add(this.buttonLogout, this.layout_constraints);
		this.buttonLogout.addActionListener(new ActionListener() {
			// aktuelle View wird beendet und eine neue Login-View wird erzeugt
			public void actionPerformed(ActionEvent e) {
				close();
				dispose();
				LoginView login = new LoginView();
				login.pack();
				login.setVisible(true);
				login.setLocationRelativeTo(null);
			}
		});

		// Logik und Elemente zum Befuellen der Tabelle
		try {
			populateTable(true); // initial wird eine leere Dummy-Tabelle
									// angezeigt
		} catch (SQLException e2) {
			showSQLException(e2);
		}
		this.scrollPane = new JScrollPane(this.table);
		this.layout_constraints.fill = GridBagConstraints.BOTH;
		this.layout_constraints.anchor = GridBagConstraints.CENTER;
		this.layout_constraints.gridx = 0;
		this.layout_constraints.gridy = 4;
		this.layout_constraints.gridwidth = 3;
		this.layout_constraints.insets = new Insets(10, 5, 5, 20);
		this.contentPane.add(this.scrollPane, this.layout_constraints);
	}

	/**
	 * Zeigt Feedback-Meldungen in einem TextLabel an.
	 *
	 * @param type
	 */
	public void feedback(int type) {
		switch (type) {
		case 0: // Hinweis
			this.title.setTitle(BORDER_TITLE_INFO);
			this.title.setBorder(yellowline);
			this.labelFeedback.setText(LABEL_MAIN_FEEDBACK_INFO);
			this.contentPane.revalidate();
			this.contentPane.repaint();
			break;
		case 1: // Erfolg
			this.title.setTitle(BORDER_TITLE_SUCCESS);
			this.title.setBorder(greenline);
			this.labelFeedback.setText(LABEL_MAIN_FEEDBACK_SUCCESS.replace(
					"###ID###", getInput()));
			this.contentPane.revalidate();
			this.contentPane.repaint();
			break;
		case 2: // Warnung
			this.title.setTitle(BORDER_TITLE_WARNING);
			this.title.setBorder(redline);
			this.labelFeedback.setText(LABEL_MAIN_FEEDBACK_WARNING);
			this.contentPane.revalidate();
			this.contentPane.repaint();
		}
	}

	/**
	 * Befuellt die Tabelle in der View. Bei der Initialisierung der GUI mit
	 * einer leeren Dummy-Tabelle, danach mit dem Ergebnis der Query.
	 *
	 * @param init
	 * @throws SQLException
	 */

	public void populateTable(boolean init) throws SQLException {

		if (init) {
			// Dummy-Tabelle
			String[] colHeadings = { "B_ID", "Name", "Personen", "von", "bis" };
			int numRows = 15;
			DefaultTableModel model = new DefaultTableModel(numRows,
					colHeadings.length);
			model.setColumnIdentifiers(colHeadings);
			this.table = new JTable(model);
		} else {
			// Query zum Abfragen der gewuenschten Daten
			this.db.setQuery(QUERY + getInput());
			// CachedRowSet wird geholt
			this.row_set = getContentsOfOutputTable();
			// neues TableModel mit dem neuen RowSet
			this.output_table = new OutputTable(this.row_set);
			this.table = new JTable();
			this.table.setModel(output_table);
		}
	}

	/**
	 * Gibt SQLExceptions als Dialog aus.
	 *
	 * @param e
	 */
	private void showSQLException(SQLException e) {
		JOptionPane.showMessageDialog(ContentPane.this, new String[] {
				e.getClass().getName() + ": ", e.getMessage() });
	}

	/**
	 * Entfernt die JScrollPane aus dem Container und fuegt sie erneut an
	 * letzter Stelle hinzu. Zeigt die neuen Datensatze an, nachdem der Nutzer
	 * den Execute-Button geklickt hat.
	 */
	public void updateTable() {
		this.scrollPane = null;
		this.scrollPane = new JScrollPane(this.table);
		this.layout_constraints.fill = GridBagConstraints.BOTH;
		this.layout_constraints.anchor = GridBagConstraints.CENTER;
		this.layout_constraints.gridx = 0;
		this.layout_constraints.gridy = 4;
		this.layout_constraints.gridwidth = 3;
		this.layout_constraints.insets = new Insets(10, 5, 5, 20);

		int comp_count = this.contentPane.getComponentCount();

		// entfernt die letzte Komponente (JScrollPane)
		this.contentPane.remove(comp_count - 1);
		// fuegt die neue JScrollPane wieder an letzter STelle hinzu
		this.contentPane.add(this.scrollPane, this.layout_constraints);
		// ordnet die Elemente des Containers neu an
		this.contentPane.revalidate();
		this.contentPane.repaint();
	}

	/**
	 * Schliesst die bestehende Datenbankverbindung.
	 */
	public void close() {
		try {
			if (this.db.getConnection() != null) {
				try {
					this.db.getConnection().close();
				} catch (SQLException e) {
					showSQLException(e);
				}
			}
		} catch (NullPointerException e) {
		}
	}

	/**
	 * Holt den Wert aus dem Unterkunfts-ID-Textfeld.
	 *
	 * @return
	 */
	public String getInput() {
		return this.text_U_ID.getText();
	}

	/**
	 * Erstellt ein neues CachedRowSet mit hilfe der bestehenden
	 * Datenbankverbindung. CachedRowSet lassen sich verarbeiten, selbst wenn
	 * das Statement, mit welchem man sie geholt hat, geschlossen wurde.
	 *
	 * @return
	 * @throws SQLException
	 */
	public CachedRowSet getContentsOfOutputTable() throws SQLException {
		CachedRowSet row_set = null;

		try {
			row_set = new CachedRowSetImpl();
			row_set.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
			row_set.setConcurrency(ResultSet.CONCUR_UPDATABLE);
			row_set.setUsername(this.db.getCredentials()[0]);
			row_set.setPassword(this.db.getCredentials()[1]);
			row_set.setUrl(this.db.getUrl());
			row_set.setCommand(this.db.getQuery());
			row_set.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return row_set;
	}

	public void addComponent(Container cont, GridBagLayout gbl, Component c, Insets insets, int x, int y, int width, int height, double weightx, double weighty){

	}
}
