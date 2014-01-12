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
	 * This method suggests a KID for a new customer.
	 * Diese Methode schlägt eine KID fuer neuen Kunden.
	 *
	 * @return kid : Vorgeschlagene KID f&uuml; den neuen Kunden.
	 */
	public int getKundenID() {
		int kid = -1;
		Statement stmtKID = null;
		String sql_query = "SELECT SEQ_KUNDE_KID FROM DUAL";
		try {
			stmtKID = connection.createStatement();
			ResultSet rs = stmtKID.executeQuery(sql_query);
			rs.first();
			kid = rs.getInt(1);
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		return kid;
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
	 * @param kBranche : Branche, in dem der Kunde t��tig ist.
	 * @param kNation : Name des Landes, in dem der Kunde lebt.
	 * @throws SQLException
	 */
	public void insertKunde(int kID, String kName, String kAdresse, String kTelNr, String kBranche, String kNation) throws SQLException {
		/*
		 * INSERT INTO KUNDE
		 * VALUES (kID, kName, kAdresse, kTelNr, 0.00, kBranche, NID)
		 */
		PreparedStatement stmt_InsertKunde = null;
		String query_InsertKunde = "INSERT INTO KUNDE " +
				   "VALUES (?,?,?,?,0.00,?,?)"; // KID, Name, Adresse, Telefonnr, Konto, Branche, Nation

		// Common statement for retrieving data from the database.
		Statement stmt_Retrieve = null;
		String sql_query;

		try {
			// Set the AutoCommit mode off. Each separate statement or action won't be considered a unit transaction more.
			connection.setAutoCommit(false);
			// Default transaction isolation level of oracle is READ COMMITED.

			// Create a new Statement object to retrieve data in database.
			stmt_Retrieve = connection.createStatement();

			// Create a ResultSet object to hold row sets obtained from a query.
			ResultSet rs = null;

			// Determine the NID for a given nation name.
			// Bestimme die zugehoerige NID fuer den vorgegebenen Nationnamen kNation.
			sql_query = "SELECT nid FROM NATION " +
						"WHERE name = '" + kNation + "'";
			rs = stmt_Retrieve.executeQuery(sql_query);
			rs.first(); // move the cursor to the first row of ResultSet rs.
			int kNID = rs.getInt("NID"); // NID (d.h. ID der Nation,in der der Kunde lebt).

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
		} catch ( SQLException e ) {
			e.printStackTrace();
			// TODO's:
			// Variante 1: SEQUENCE-Objekt schl��gt automatisch eine neue KID vor , wiederholt die Methode und erstellt einen Kunden.
			// Variante 2: DIe Methode wird mit dem Auswerfen der Exception abgebrochen. Der User f��hrt die Methode durch das Klicken auf Button nochmals aus.
		} finally {
			if ( stmt_Retrieve != null ) {
				stmt_Retrieve.close();
			}
			if ( stmt_InsertKunde != null ) {
				stmt_InsertKunde.close();
			}
		}
	}
}