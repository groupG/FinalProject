package gui;

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
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import controller.MainController;

import model.Configuration;
import model.DB;

/**
 * Login-View. Ermoeglicht es dem Nutzer, sich bei der Datenbank anzumelden und
 * zur Hauptanwendung zu gelangen.
 *
 * @author borecki
 *
 */
public class Login extends JFrame implements Configuration {

	private static final long serialVersionUID = 5418722630206964965L;

	// GUI-Elemente
	private Container loginPane;
	private JPanel loginPanel;
	private JLabel labelUserName;
	private JLabel labelPassword;
	private JLabel labelFeedback;
	private JTextField textUserName;
	private JPasswordField textPassword;
	private JButton buttonLogin;
	private JButton buttonClear;
	private Border blackline, redline, greenline, yellowline;
	private TitledBorder title;

	// Logik
	private DB db;

	/**
	 * Konstruktor. Erzeugt eine neue Login-View.
	 */
	public Login() {
		super(TITLE);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				close();
				System.exit(0);
			}
		});

		this.labelUserName = new JLabel(LABEL_USERNAME);
		this.labelPassword = new JLabel(LABEL_PASSWORD);
		this.labelFeedback = new JLabel(LABEL_LOGIN_FEEDBACK_INFO);
		this.textUserName = new JTextField(15);
		this.textPassword = new JPasswordField(15);
		this.buttonLogin = new JButton(LABEL_LOGIN);
		this.buttonClear = new JButton(LABEL_CLEAR);

		this.loginPane = getContentPane();
		this.loginPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.loginPanel = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		this.loginPanel.setLayout(gbl);

		// Label fuer Benutzername
		addComponent(this.loginPanel, gbl, labelUserName, new Insets(0,5,0,5), 0, 0);

		// Label fuer Passwort
		addComponent(this.loginPanel, gbl, labelPassword, new Insets(0,5,0,5), 0, 1);

		// Label fuer Feedback an den Nutzer
		this.blackline = BorderFactory.createLineBorder(Color.black);
		this.redline = BorderFactory.createLineBorder(Color.red);
		this.greenline = BorderFactory.createLineBorder(Color.green);
		this.yellowline = BorderFactory.createLineBorder(Color.yellow);
		this.title = BorderFactory.createTitledBorder(this.blackline,
				BORDER_TITLE_INFO);
		this.title.setTitleJustification(TitledBorder.CENTER);
		this.labelFeedback.setBorder(this.title);
		addComponent(this.loginPanel, gbl, labelFeedback, new Insets(0,5,0,5), 0, 2,0,2,1.0,1.0);

		// Text fuer Benutzername
		this.textUserName.setColumns(15);
		addComponent(this.loginPanel, gbl, textUserName, new Insets(10, 5, 5, 20), 1, 0);

		// Text fuer Passwort
		this.textPassword.setColumns(15);
		addComponent(this.loginPanel, gbl, textPassword, new Insets(10, 5, 5, 20), 1, 1);

		// Button zum Leeren der Textfelder
		addComponent(this.loginPanel, gbl, buttonClear, new Insets(10, 5, 5, 20), 2, 0);
		this.buttonClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});

		// Button zum Einloggen
		this.buttonLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user = getUserName();
				String pwd = String.valueOf(getPassword());

				try {

					db = new DB(DRIVER, CONNECTION_THIN + "dbprakt", user, pwd);

				} catch (SQLException e1) {
					feedback(2);
					reset();
					showSQLException(e1);
				} catch (Exception e1) {
				} finally {
					if (db != null) {
						feedback(1);
						dispose();
						Client client = new Client(db,CLIENT_WIDTH,CLIENT_HEIGHT);
						MainController controller = new MainController(db, client);
					}
				}
			}
		});
		addComponent(this.loginPanel, gbl, buttonLogin, new Insets(10, 5, 5, 20), 2, 1);
		this.loginPane.add(this.loginPanel);
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
			this.labelFeedback.setText(LABEL_LOGIN_FEEDBACK_INFO);
			this.loginPane.revalidate();
			this.loginPane.repaint();
			break;
		case 1: // Erfolg
			this.title.setTitle(BORDER_TITLE_SUCCESS);
			this.title.setBorder(greenline);
			this.labelFeedback.setText(LABEL_LOGIN_FEEDBACK_SUCCESS);
			this.loginPane.revalidate();
			this.loginPane.repaint();
			break;
		case 2: // Warnung
			this.title.setTitle(BORDER_TITLE_WARNING);
			this.title.setBorder(redline);
			this.labelFeedback.setText(LABEL_LOGIN_FEEDBACK_WARNING);
			this.loginPane.revalidate();
			this.loginPane.repaint();
		}
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
	 * Leert beide Eingabefelder.
	 */
	public void reset() {
		this.textPassword.setText("");
		this.textUserName.setText("");
	}

	/**
	 * Holt den Inhalt des Benutzernamenfeldes.
	 *
	 * @return
	 */
	public String getUserName() {
		return this.textUserName.getText();
	}

	/**
	 * Holt den Inhalt des Passwordfeldes.
	 *
	 * @return
	 */
	public char[] getPassword() {
		return this.textPassword.getPassword();
	}

	/**
	 * Gibt SQLExceptions als Dialog aus.
	 *
	 * @param e
	 */
	private void showSQLException(SQLException e) {
		JOptionPane.showMessageDialog(this, new String[] {
				e.getClass().getName() + ": ", e.getMessage() });
	}

	public void addComponent(JPanel panel, GridBagLayout gbl,
			Component c, Insets insets, int x, int y, int width, int height,
			double weightx, double weighty) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.insets = insets;
		panel.add(c, constraints);
	}

	public void addComponent(JPanel panel, GridBagLayout layout,
			Component c, Insets insets, int x, int y) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.insets = insets;
		panel.add(c, constraints);
	}
}