package model;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

/**
 * Klasse, welche den Verbindungsaufbau zur Oracle-Datenbank managed.
 *
 * @author borecki, dang
 */
public class DB implements Configuration {

	private Connection connection;
	private String url;
	private String[] credentials;
	private String query;

	/**
	 * Diese Variable dient als Falg und gibt an, ob der n&auml;chste KundenID (KID) berechnet werden soll.
	 * Ist sie <b>true</b>, wird der n&auml;chster KID gerechnet.
	 */
	private boolean needNextKID = true;

	/**
	 * In dieser Variable wird die aktuell vorgeschlagene, noch freie KundenID (KID) zwischengespeichert.
	 */
	private int kid_buffered = -1;

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
	 * &Uuml;berpr&uuml;ft, ob das gesuchte <i>element</i> in der Table <i>table</i> existiert.
	 *
	 * @param table : Table, in der nach dem Element <i>element</i> gesucht wird.
	 * @param element : Gesuchtes Element.
	 * @param value : Wert des gesuchten Elements.
	 * @return <i>true</i>, falls das gesuchte Element in der Table <i>table</i> existiert,
	 * 		   <i>false</i>, falls das gesuchte Element nicht in der Table <i>table</i> vorkommt.
	 * 
	 * @throws SQLException
	 */
	public boolean checkIfElementExists(String table, String element, String value) throws SQLException {
		Statement statement = null;
		String query = "SELECT * FROM " + TABLE_OWNER + "." + table + " " +
		               "WHERE " + element + " = " + value;
		boolean result = false; // false : Das gesuchte Element existiert nicht.
		try {
			statement = this.connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			result = rs.next(); // wenn true : Das gesuchte Element existiert in DB.
			rs.close(); // Close ResultSet object if it's not used more.
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return result;
	}

	public void showTables() throws SQLException{
		String query = "Select table_name from all_tables where owner='ALTDATEN'";
		Statement stmt = this.connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()){
			System.out.println(rs.getString(1));
			query = "Select column_name from all_tab_columns where owner = 'ALTDATEN' and table_name='" +rs.getString(1)+ "'";
			Statement stamt = this.connection.createStatement();
			ResultSet res = stamt.executeQuery(query);
			while(res.next()){
				System.out.println("--"+res.getString(1));
			}
		}
	}

	public List<String> getTables(String owner) throws SQLException {
		String query = "Select table_name from all_tables where owner='"+owner+"'";
		System.out.println(query);
		Statement statement = this.connection.createStatement();
		ResultSet result = statement.executeQuery(query);
		List<String> tables = new ArrayList<String>();
		while(result.next()){
			tables.add(result.getString(1));
		}
		return tables;
	}

	public List<String> getColumns(String owner, String table) throws SQLException {
		query = "Select column_name from all_tab_columns where owner = '"+owner+"' and table_name='" +table+ "'";
		Statement statement = this.connection.createStatement();
		ResultSet result = statement.executeQuery(query);
		List<String> columns = new ArrayList<String>();
		while(result.next()){
			columns.add(result.getString(1));
		}
		return columns;
	}

	public CachedRowSet getContentsOfOutputTable(String query){
		CachedRowSet row_set = null;

		try {
			row_set = new CachedRowSetImpl();
			row_set.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
			row_set.setConcurrency(ResultSet.CONCUR_UPDATABLE);
			row_set.setUsername(getCredentials()[0]);
			row_set.setPassword(getCredentials()[1]);
			row_set.setUrl(getUrl());
			row_set.setCommand(query);
			row_set.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return row_set;
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

	/**
	 * Diese Methode liefert den temporär gespeicherten Wert von Kunden-ID zur&uuml;ck.
	 * @return kid_buffered : Den zwischengespeicherten Wert von Kunden-ID.
	 */
	public int getBufferedKundenID() {
		return this.kid_buffered;
	}

	/**
	 * Diese Methode setzt die Variable <i>needNextKID</i> auf einen boolean Wert.
	 *
	 * @param b : true gibt an, dass der n&auml;chste KID von DB berechnet werden soll. Bei false wird kein neuer KID berechnet.
	 */
	public void needNextKundenID( boolean b ) {
		this.needNextKID = b;
	}

	/**
	 * This method suggests a KID for a new customer.
	 * Diese Methode schlägt eine KID fuer neuen Kunden.
	 *
	 * @return kid : Vorgeschlagener KID f&uuml;r den neuen Kunden.
	 *
	 */
	public int getKundenID() {
		if ( needNextKID ) {
			Statement stmtKID = null;
			String sql_query = "SELECT " + TABLE_OWNER + "." + SEQUENCE_KUNDE_KID + ".NEXTVAL FROM DUAL";
			try {
				stmtKID = connection.createStatement();
				ResultSet rs = stmtKID.executeQuery(sql_query);
				//rs.beforeFirst();
				rs.next();
				this.kid_buffered = rs.getInt(1);
				rs.close();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
			finally {
				if ( stmtKID != null ) {
					try {
						stmtKID.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				this.needNextKID = false; // ab hier werden voruebergehend keine weitere KIDs vorgeschlagen.
			}
		}
		return kid_buffered;
	}

	/**
	 * Diese Methode erstellt einen neuen Kunden mit Namen, Adresse, Telefonnummer, Nation, Branche.
	 * Der Kontostand des neuen Kunden wird mit 0.00 initialisiert.
	 *
	 * This method creates/inserts a new customer with own information like name, address, phone number, counter, branch.
	 * The initial account balance is 0.00.
	 *
	 * KUNDE : [KID], Name, Adresse, Telefonnummer, Konto, Branche, NID
	 *
	 * @param kID : KID (ID des Kunden).
	 * @param kName : Name des Kunden.
	 * @param kAdresse : Adresse des Kunden.
	 * @param kTelefon : Telefonnummer des Kunden.
	 * @param kBranche : Branche, in dem der Kunde t&auml;tig ist.
	 * @param kNation : Name des Landes, in dem der Kunde lebt.
	 * @throws SQLException
	 */
	public void insertKunde(int kID, String kName, String kAdresse, String kTelNr, String kBranche, String kNation) throws SQLException {

		// Set the AutoCommit mode off. Each separate statement or action won't be considered a unit transaction more.
		connection.setAutoCommit(false);
		// Default transaction isolation level of oracle is READ COMMITED.

		PreparedStatement stmt_InsertKunde = null;
		String query_InsertKunde = "INSERT INTO " + TABLE_OWNER + "." + TABLE_KUNDE + " " +
				   				   "VALUES (?,?,?,?,0.00,?,?)"; // KID, Name, Adresse, Telefonnr, Konto, Branche, Nation

		// Common statement for retrieving data from the database.
		Statement stmt_Retrieve = null;
		String sql_query;

		try {
			// Create a new Statement object to retrieve data in database.
			stmt_Retrieve = connection.createStatement();

			// Create a ResultSet object to hold row sets obtained from a query.
			ResultSet rs = null;

			// Determine the NID for a given nation name.
			// Bestimme die zugehoerige NID fuer den vorgegebenen Nationnamen kNation.
			sql_query = "SELECT nid FROM " + TABLE_OWNER + ".NATION " +
						"WHERE name = '" + kNation + "'";
			rs = stmt_Retrieve.executeQuery(sql_query);
			rs.next();
			int kNID = rs.getInt(1); // NID

			// Close the rs cursor, since no use more.
			rs.close();

			// Fuege den neuen Kunden mit allen seinen Daten in DB ein.
			stmt_InsertKunde = connection.prepareStatement(query_InsertKunde);
			stmt_InsertKunde.setInt(1, kID); // KID
			stmt_InsertKunde.setString(2, kName); // Name
			stmt_InsertKunde.setString(3, kAdresse); // Adresse
			stmt_InsertKunde.setString(4, kTelNr); // Telefonnummer
			stmt_InsertKunde.setString(5, kBranche); // Branche
			stmt_InsertKunde.setInt(6, kNID); // NID
			stmt_InsertKunde.executeUpdate();

			connection.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			if ( stmt_Retrieve != null ) {
				stmt_Retrieve.close();
			}
			if ( stmt_InsertKunde != null ) {
				stmt_InsertKunde.close();
			}
			connection.setAutoCommit(true);
		}
	}

	/**
	 * Diese Methode &auml;ndert die Daten eines Kunden.
	 * This method alters the data of a customer.
	 *
	 * KUNDE : [KID], Name, Adresse, Telefonnummer, Konto, Branche, NID
	 *
	 * @param kID : KID (ID des Kunden).
	 * @param kName : Name des Kunden.
	 * @param kAdresse : Adresse des Kunden.
	 * @param kTelefon : Telefonnummer des Kunden.
	 * @param kKonto : Kontostand eines des Kunden.
	 * @param kBranche : Branche, in dem der Kunde t&auml;tig ist.
	 * @param kNation : Name des Landes, in dem der Kunde lebt.
	 * @throws SQLException
	 */
	public void updateKunde(String kID, String kName, String kAdresse, String kTelNr, float kKonto, String kBranche, String kNation) throws SQLException {

		// Set the AutoCommit mode off. Each separate statement or action won't be considered a unit transaction more.
		connection.setAutoCommit(false);
		// Default transaction isolation level of oracle is READ COMMITED.

		PreparedStatement p_stmt = null;
		String query_UpdateKunde = "UPDATE " + TABLE_OWNER + "." + TABLE_KUNDE  + " " +
				   				   "SET kid = ?, " +
				   				   "    name = ?, " +
				   				   "    adresse = ?, " +
				   				   "    telefonnummer = ?, " +
				   				   "    konto = ?, " +
				   				   "    branche = ?, " +
				   				   "    nid = ? " +
				   				   "WHERE kid = " + kID;

		// Common statement for retrieving data from the database.
		Statement stmt = null;
		String sql_query;

		try {
			// Create a new Statement object to retrieve data in database.
			stmt = connection.createStatement();

			// Create a ResultSet object to hold row sets obtained from a query.
			ResultSet rs = null;

			// Determine the NID for a given nation name.
			// Bestimme die zugehoerige NID fuer den vorgegebenen Nationnamen kNation.
			sql_query = "SELECT nid FROM " + TABLE_OWNER + ".NATION " +
						"WHERE name = '" + kNation + "'";
			rs = stmt.executeQuery(sql_query);
			rs.next();
			int kNID = rs.getInt(1); // NID

			// Close the rs cursor, since no use more.
			rs.close();

			// Fuege den neuen Kunden mit allen seinen Daten in DB ein.
			p_stmt = connection.prepareStatement(query_UpdateKunde);
			p_stmt.setString(1, kName); // Name
			p_stmt.setString(2, kAdresse); // Adresse
			p_stmt.setString(3, kTelNr); // Telefonnummer
			p_stmt.setFloat(4, kKonto); // Kontostand
			p_stmt.setString(5, kBranche); // Branche
			p_stmt.setInt(6, kNID); // NID
			p_stmt.executeUpdate();

			connection.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			if ( stmt != null ) {
				stmt.close();
			}
			if ( p_stmt != null ) {
				p_stmt.close();
			}
			connection.setAutoCommit(true);
		}
	}
}