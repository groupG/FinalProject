import java.awt.Color;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * Login-View. Ermoeglicht es dem Nutzer, sich bei der Datenbank anzumelden und
 * zur Hauptanwendung zu gelangen.
 *
 * @author borecki
 *
 */
public class LoginView extends JFrame implements ConfigImpl {

	private static final long serialVersionUID = 5418722630206964965L;

	// GUI-Elemente
	private Container loginPane;
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
	public LoginView() {
		super(TITLE);

		// Schliesst die Anwendung und trennt die Datenbank-Verdindung
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
		this.loginPane
				.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.loginPane.setLayout(new GridBagLayout());
		GridBagConstraints layout_constraints = new GridBagConstraints();

		// Label fuer Benutzername
		layout_constraints.fill = GridBagConstraints.HORIZONTAL;
		layout_constraints.anchor = GridBagConstraints.LINE_START;
		layout_constraints.ipady = 1;
		layout_constraints.gridx = 0;
		layout_constraints.gridy = 0;
		layout_constraints.gridwidth = 1;
		layout_constraints.insets = new Insets(10, 5, 5, 20);
		this.loginPane.add(this.labelUserName, layout_constraints);

		// Label fuer Passwort
		layout_constraints.fill = GridBagConstraints.HORIZONTAL;
		layout_constraints.anchor = GridBagConstraints.CENTER;
		layout_constraints.ipady = 1;
		layout_constraints.gridx = 0;
		layout_constraints.gridy = 1;
		layout_constraints.gridwidth = 1;
		this.loginPane.add(this.labelPassword, layout_constraints);

		// Label fuer Feedback an den Nutzer
		this.blackline = BorderFactory.createLineBorder(Color.black);
		this.redline = BorderFactory.createLineBorder(Color.red);
		this.greenline = BorderFactory.createLineBorder(Color.green);
		this.yellowline = BorderFactory.createLineBorder(Color.yellow);
		this.title = BorderFactory.createTitledBorder(this.blackline, BORDER_TITLE_INFO);
		this.title.setTitleJustification(TitledBorder.CENTER);
		this.labelFeedback.setBorder(this.title);
		layout_constraints.fill = GridBagConstraints.HORIZONTAL;
		layout_constraints.anchor = GridBagConstraints.LINE_START;
		layout_constraints.ipady = 1;
		layout_constraints.gridx = 0;
		layout_constraints.gridy = 2;
		layout_constraints.gridwidth = 2;
		this.loginPane.add(this.labelFeedback, layout_constraints);

		// Text fuer Benutzername
		this.textUserName.setColumns(15);
		layout_constraints.fill = GridBagConstraints.HORIZONTAL;
		layout_constraints.anchor = GridBagConstraints.CENTER;
		layout_constraints.ipady = 1;
		layout_constraints.gridx = 1;
		layout_constraints.gridy = 0;
		layout_constraints.gridwidth = 1;
		layout_constraints.insets = new Insets(10, 5, 5, 20);
		this.loginPane.add(this.textUserName, layout_constraints);

		// Text fuer Passwort
		this.textPassword.setColumns(15);
		layout_constraints.fill = GridBagConstraints.HORIZONTAL;
		layout_constraints.anchor = GridBagConstraints.CENTER;
		layout_constraints.ipady = 1;
		layout_constraints.gridx = 1;
		layout_constraints.gridy = 1;
		layout_constraints.gridwidth = 1;
		this.loginPane.add(this.textPassword, layout_constraints);

		// Button zum Leeren der Textfelder
		layout_constraints.fill = GridBagConstraints.HORIZONTAL;
		layout_constraints.anchor = GridBagConstraints.LINE_END;
		layout_constraints.ipady = 1;
		layout_constraints.gridx = 2;
		layout_constraints.gridy = 0;
		layout_constraints.insets = new Insets(10, 5, 5, 20);
		this.loginPane.add(this.buttonClear, layout_constraints);
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
					db = new DB(DRIVER, CONNECTION_THIN, user, pwd);
				} catch (SQLException e1) {
					feedback(2);
					reset();
					showSQLException(e1);
				} catch (Exception e1) {
				} finally {
					if (db != null) {
						feedback(1);
						dispose();
						ContentPane contentPane = null;
						try {
							contentPane = new ContentPane(db);
						} catch (SQLException e1) {
							showSQLException(e1);
						}
						contentPane.pack();
						contentPane.setVisible(true);
						contentPane.setLocationRelativeTo(null);
					}
				}
			}
		});
		layout_constraints.fill = GridBagConstraints.HORIZONTAL;
		layout_constraints.anchor = GridBagConstraints.LINE_END;
		layout_constraints.ipady = 1;
		layout_constraints.gridx = 2;
		layout_constraints.gridy = 1;
		layout_constraints.gridwidth = 1;
		this.loginPane.add(this.buttonLogin, layout_constraints);
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
			this.labelFeedback
					.setText(LABEL_LOGIN_FEEDBACK_INFO);
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
			this.labelFeedback
					.setText(LABEL_LOGIN_FEEDBACK_WARNING);
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
}
