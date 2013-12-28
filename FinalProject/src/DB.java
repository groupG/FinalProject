import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Klasse, welche den Verbindungsaufbau zur Oracle-Datenbank managed.
 *
 * @author borecki
 */
public class DB {

	private Connection connection;
	private String url;
	private String[] credentials;
	private String query;

	/**
	 * Konstruktor. Erzeugt eine neue Instanz der DB-Klasse. Es wird
	 * gleichzeitig eine Verbindung zur Datenbank aufgebaut.
	 *
	 * @param driver
	 * @param connection
	 * @param user
	 * @param password
	 * @throws Exception
	 */
	public DB(String driver, String connection, String user, String password)
			throws Exception {
		Class.forName(driver);
		setCredentials(new String[] { user, password });
		setUrl(connection);
		setConnection(getUrl(), getCredentials());
		setQuery("");
	}

	/**
	 * Schliesst die Verbindung zur Datenbank.
	 *
	 * @throws SQLException
	 */
	public void _DB() throws SQLException {
		this.connection.close();
	}

	/**
	 * Ueberprueft, ob das angegebene Element in der gesuchten Relation der
	 * Tabelle REISE_TABLES_2013 vorhanden ist.
	 *
	 * @param table
	 * @param element
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public boolean checkIfElementexists(String table, String element,
			String value) throws SQLException {
		Statement statement = null;
		setQuery("SELECT * FROM reise_tables_2013." + table + " WHERE "
				+ element + "=" + value);
		try {
			statement = this.connection.createStatement();
			ResultSet result = statement.executeQuery(getQuery());
			if (result.next()) {
				return true;
			}
		} catch (SQLException e) {

		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return false;
	}

	public boolean getNations() throws SQLException {

		return false;
	}

	public String[] getCredentials() {
		return this.credentials;
	}

	public void setCredentials(String[] credentials) {
		this.credentials = credentials;
	}

	public Connection getConnection() {
		return this.connection;
	}

	/**
	 * Baut eine Verbindung zur Datenbank ueber den DriverManger auf.
	 *
	 * @param connection
	 * @param credentials
	 * @return
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public boolean setConnection(String connection, String[] credentials)
			throws ConnectException, SQLException {

		this.connection = DriverManager.getConnection(connection,
				credentials[0], credentials[1]);
		return !this.connection.equals(null);
	}

	public String getQuery() {
		return this.query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
