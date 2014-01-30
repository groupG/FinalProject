package model;

import java.net.ConnectException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

import utils.NotExistInDatabaseException;

/**
 * Klasse, welche den Verbindungsaufbau zur Oracle-Datenbank managed.
 *
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

	private boolean needNextBSTID = true;

	/**
	 * In dieser Variable wird die aktuell vorgeschlagene, noch freie KundenID (KID) zwischengespeichert.
	 */
	private int kid_buffered = -1;

	private int bstid_buffered = -1;

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
		alterDateFormat();
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
			query = "Select column_name from all_tab_columns where owner = 'ALTDATEN' and table_name='" +rs.getString(1)+ "'";
			Statement stamt = this.connection.createStatement();
			ResultSet res = stamt.executeQuery(query);
			while(res.next()){
			}
		}
	}

	public List<String> getTables(String owner) throws SQLException {
		String query = "Select table_name from all_tables where owner='"+owner+"'";
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

	public CachedRowSet getContentsOfOutputTable(String query) throws SQLException{
		CachedRowSet row_set = null;

//		try {
			row_set = new CachedRowSetImpl();
			row_set.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
			row_set.setConcurrency(ResultSet.CONCUR_UPDATABLE);
			row_set.setUsername(getCredentials()[0]);
			row_set.setPassword(getCredentials()[1]);
			row_set.setUrl(getUrl());
			row_set.setCommand(query);
			row_set.execute();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
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
	 * Diese Methode liefert den tempor��r gespeicherten Wert von Kunden-ID zur&uuml;ck.
	 * @return kid_buffered : Den zwischengespeicherten Wert von Kunden-ID.
	 */
	public int getBufferedKundenID() {
		return this.kid_buffered;
	}

	/**
	 * Diese Methode liefert den tempor��r gespeicherten Wert von Bestellungs-ID zur&uuml;ck.
	 * @return bstid_buffered : Den zwischengespeicherten Wert von Bestellungs-ID.
	 */
	public int getBufferedBestellungsID() {
		return this.bstid_buffered;
	}

	/**
	 * Diese Methode setzt die Variable <i>needNextKID</i> auf einen boolean Wert.
	 *
	 * @param b : true gibt an, dass der n&auml;chste KID von DB berechnet werden soll. Bei false wird kein neuer KID berechnet.
	 */
	public void needNextKundenID( boolean b ) {
		this.needNextKID = b;
	}

	public void needNextBestellungsID( boolean b ) {
		this.needNextBSTID = b;
	}
	/**
	 * This method suggests a KID for a new customer.
	 * Diese Methode schl&auml;gt eine KID fuer neuen Kunden.
	 *
	 * @return kid : Vorgeschlagener KID f&uuml;r den neuen Kunden.
	 *
	 */
//	public int getKundenID() {
//		if ( needNextKID ) {
//			Statement stmt = null;
//			String sql_query = "SELECT " + TABLE_OWNER + "." + SEQUENCE_KUNDE_KID + ".NEXTVAL FROM DUAL";
//			try {
//				stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//				ResultSet rs = stmt.executeQuery(sql_query);
//				//rs.beforeFirst();
//				rs.next();
//				this.kid_buffered = rs.getInt(1);
//				rs.close();
//			}
//			catch ( SQLException e ) {
//				e.printStackTrace();
//			}
//			finally {
//				if ( stmt != null ) {
//					try {
//						stmt.close();
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//				}
//				this.needNextKID = false; // ab hier werden voruebergehend keine weitere KIDs vorgeschlagen.
//			}
//		}
//		return kid_buffered;
//	}

	public int getKundenID() {
		if ( needNextKID ) {
			Statement stmt = null;
			String sql_query;
			try {
				stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = null;
				int tmp_kid = -1;

				do {
					sql_query = "SELECT " + TABLE_OWNER + "." + SEQUENCE_KUNDE_KID + ".NEXTVAL FROM DUAL";
					rs = stmt.executeQuery(sql_query);
					//rs.beforeFirst();
					rs.next();
					tmp_kid = rs.getInt(1);
					sql_query = "SELECT * FROM " + TABLE_OWNER + ".KUNDE " +
								"WHERE kid = " + tmp_kid;
					rs = stmt.executeQuery(sql_query);
				} while ( rs.next() );

				rs.close();
				this.kid_buffered = tmp_kid;
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
			finally {
				if ( stmt != null ) {
					try {
						stmt.close();
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
	public void insertKunde(String kID, String kName, String kAdresse, String kTelNr, String kBranche, String kNation) throws SQLException {

		// Set the AutoCommit mode off. Each separate statement or action won't be considered a unit transaction more.
		connection.setAutoCommit(false);
		// Default transaction isolation level of oracle is READ COMMITED.

		PreparedStatement stmt_InsertKunde = null;
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
			sql_query = "INSERT INTO " + TABLE_OWNER + "." + TABLE_KUNDE + " " +
	   				    "VALUES (?,?,?,?,0.00,?,?)"; // KID, Name, Adresse, Telefonnr, Konto, Branche, Nation

			stmt_InsertKunde = connection.prepareStatement(sql_query);
			stmt_InsertKunde.setInt(1, Integer.parseInt(kID)); // KID
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
	public void updateKunde(String kID, String kName, String kAdresse, String kTelNr, double kKonto, String kBranche, String kNation) throws SQLException {
		// Set the AutoCommit mode off. Each separate statement or action won't be considered a unit transaction more.
		//this.connection.setAutoCommit(false);
		//connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

		PreparedStatement p_stmt = null;
		String query_UpdateKunde = "UPDATE " + TABLE_OWNER + "." + TABLE_KUNDE  + " " +
				   				   "SET " +
				   				   "    name = ?, " +
				   				   "    adresse = ?, " +
				   				   "    telefonnummer = ?, " +
				   				   "    konto = ?, " +
				   				   "    branche = ?, " +
				   				   "    nid = ? " +
				   				   "WHERE kid = " + kID;

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
			p_stmt.setDouble(4, kKonto); // Kontostand
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

	/**
	 * Mit dieser Methode lockt eine Transaktion eine Zeile einer Tabelle. Nur auf alle anderen nicht geblockten Zeilen
	 * k&ouml;nnen andere (konkurrierende) Transaktionen weiter zugreifen.
	 *
	 * @param table : Tabelle. in der eine Zeile gelockt wird.
	 * @param condition : Bedingung in WHERE-Klausel.
	 * @return <i>true</i>, falls eine Transaktion als erstes auf die Zeile zugreift und locken kann.
	 * 		   <i>false</i>, falls die Zeile bereits von einer anderen Transaktion gelockt ist.
	 * @throws SQLException
	 */
	public boolean lockRows(String table, String condition) throws SQLException {
		this.connection.setAutoCommit(false);

		Statement stmt = null;
		String sql_query;

		boolean isLocked = false; // there is still no lock.
		try {
			stmt = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			sql_query = "SELECT * FROM " + TABLE_OWNER + "." + table + " " +
					    "WHERE " + condition + " FOR UPDATE NOWAIT";
			ResultSet rs = stmt.executeQuery(sql_query); // Lock the coherent row.
			// SQLException can be caused here, if no rs can be returned.
			isLocked = rs.first(); // true, i.e. the record (row) is being locked now. There was no lock set on it before.
		} catch ( SQLException e ) {
			//e.printStackTrace();
			// SQLException, because this record is already locked by a previous process
			// and cannot be accessed by the current process.
		}
		finally {
			if ( stmt != null ) {
				stmt.close();
			}
		}
		return isLocked;
	}

	/**
	 * Diese Methode liefert eine Menge von Zeilen als Ergebnis der SELECT-Query mit der Bedingung <i>condition</i>.
	 *
	 * @param table : Tabelle, in der gesucht wird.
	 * @param condition : Pr&auml;dikat oder Bedingung in WHERE-Klasuel.
	 * @return Menge von Zeilen, die Bedingung <i>condition</i> erf&uuml;llen.
	 * @throws SQLException
	 */
	public Vector<Vector<Object>> selectFromTable(String table, String condition) throws SQLException {
		Vector<Vector<Object>> values = new Vector<Vector<Object>>();
		// connecion.setAutoCommit(true);
		Statement stmt = null;
		String sql_query = "SELECT * FROM " + TABLE_OWNER + "." + table + " " +
				           "WHERE " + condition;
		try {
			stmt = this.connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql_query);
			while ( rs.next() ) {
				Vector<Object> v = new Vector<Object>();
				int i = 1;
				while ( i > 0 ) {
					try {
						v.add(rs.getObject(i));
					} catch ( SQLException e ) {
						break;
					}
					i++;
				}
				values.add(v);
			}
			rs.close();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			if ( stmt != null ) {
				stmt.close();
			}
		}
		return values;
	}

	/**
	 * Diese Methode bucht die Produktbest&auml;nde einer Zulieferung mit der gegebenen ID <i>zlid</i> auf das
	 * in der Zulieferung angegebene Lager ein.
	 *
	 * 	 Zulieferung : [ZLID], Liefertext, Liefertermin, Aenderungsdatum, Anleger, Anlagedatum, Status, Erledigt_Termin, LID, LAGID
	 *   Zulieferungsposition : [POSNR], Anzahl, Preis, Positionstext, [ZLID], PID
	 *   Lager : [LAGID], Adresse
	 *   Lagert : [LAGID], [PID], Anzahl
	 *
	 * @param zlid : Zulieferungs-ID
	 * @throws NotInDatabaseException
	 * @throws SQLException
	 */
	public boolean zulieferungEinbuchen(String zlid) throws NotExistInDatabaseException, SQLException {
		this.connection.setAutoCommit(false);

		Statement stmt = null;
		String sql_query;
		boolean success = true; // wenn eine Zulieferung erfolgreich eingebucht werden kann.

		try {
			stmt = this.connection.createStatement();
			sql_query = "SELECT status, lagid FROM ZULIEFERUNG WHERE zlid = " + zlid;
			ResultSet rs = stmt.executeQuery(sql_query);

			// Check if the given zlid is in the database, i.e. the ResultSet is empty.
			if ( !rs.next() ) {
				throw new NotExistInDatabaseException("ResultSet is empty. The method first() has returned false :-)");
			}

			// If the zlid is really in a databse, i.e. the ResultSet is not empty.
			//rs.first();
			String status = rs.getString("status");
			int lagid = rs.getInt("lagid"); // Determine the lagid of LAGER included in ZULIEFERUNG.

			// Muss hier denglisch verwenden. Mein Wortschatz ist begrenzt. ;-)
			// Check if the Zulieferung is still open (OFFEN) or already finished (ERLEDIGT).
			if ( status.trim().equals("ERLEDIGT") ) {
				return false;
			}

			// If the Zulieferung is still OPEN., ...
			// Set the STATUS of ZULIEFERUNG to ERLEDIGT and the ERLEDIGT_TERMIN to current date (SYSDATE).
			sql_query = "UPDATE " + TABLE_OWNER + ".ZULIEFERUNG " +
			            "SET STATUS = 'ERLEDIGT', ERLEDIGT_TERMIN = SYSDATE " +
					    "WHERE ZLID = " + zlid;
			stmt.executeUpdate(sql_query);

			// Now book the Zulieferungspositionen.
			sql_query = "SELECT anzahl, pid FROM ZULIEFERUNGSPOSITION " +
						"WHERE zlid = " + zlid;
			rs = stmt.executeQuery(sql_query);
			while ( rs.next() ) {
				int anzahl = rs.getInt("ANZAHL");
				int pid = rs.getInt("PID");
				sql_query = "SELECT anzahl FROM " + TABLE_OWNER + ".LAGERT " +
							"WHERE lagid = " + lagid + " AND pid = " + pid;
				ResultSet _rs_ = stmt.executeQuery(sql_query);
				// Update the number of products in warehouse.
				// Aktualisiere die Anzahl der Produkte im Lager.
				int aktuellerBestand = 0;
				int count = 0;
				while ( _rs_.next() ) {
					aktuellerBestand = _rs_.getInt("ANZAHL");
					count++;
				}
				// If the product (PID) does not exist in LAGERT, then INSERT it into LAGERT.
				if ( count == 0 ) {
					sql_query = "INSERT INTO " + TABLE_OWNER + " LAGERT " +
								"VALUES (" + lagid + ", " + pid + ", " + anzahl + ")";
				}
				else { // the product (PID) is existing in the LAGERT, then UPDATE.
					aktuellerBestand += anzahl;
					sql_query = "UPDATE LAGERT " +
								"SET anzahl = " + aktuellerBestand + " " +
								"WHERE lagid = " + lagid + " AND pid = " + pid;
				}
				_rs_.close();
			}

			rs.close();
			this.connection.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			//this.connection.rollback();
		} finally {
			if ( stmt != null ) {
				stmt.close();
			}
			this.connection.setAutoCommit(true);
		}

		return success;
	}

	/**
	 *
	 * @param srcLager
	 * @param destLager
	 * @param pid
	 * @param menge
	 * @return
	 * @throws SQLException
	 * @throws NotExistInDatabaseException
	 */
	public int bestandUmbuchen(String srcLager, String destLager, String pid, int menge) throws SQLException, NotExistInDatabaseException {
		this.connection.setAutoCommit(false);
		Savepoint savePoint1 = this.connection.setSavepoint();

		Statement stmt = null;
		String sql_query;

		int success = -999; // wenn eine Umbuchung erfolgreich abgeschlossen werden kann.

		try {

			stmt = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// Suche den aktuellen Produktbestand im Ursprungslager zuerst.
			sql_query = "SELECT anzahl FROM " + TABLE_OWNER + ".LAGERT " +
						"WHERE lagid = " + srcLager + " AND pid =  " + pid;
			ResultSet rs = stmt.executeQuery(sql_query);

			// Falls srcLger nicht in der Datenbank vorhanden ist, oder es gibts noch kein Produkt pid im Lager.
			if ( !rs.first() ) {
				throw new NotExistInDatabaseException("ResultSet is empty. The method first() has returned false.");
			}

			rs.first(); // move the rs cursor to the first element.
			int aktuellerBestand = 0; // wird verwendet, um die aktuelle Anzahl sowohl im Ursprungs- als auch Ziellager zu speichern.
			aktuellerBestand = rs.getInt("ANZAHL");

			// Ueberpruefe, ob der aktuelle Bestand im Ursprungslager ausreicht, d.h. aktuellerBestand >= menge.
			// Falls der Bestand nicht genuegt.
			if ( aktuellerBestand < menge ) {
				return aktuellerBestand; // Bestand reicht nicht aus. Nur x Stueck vorraetig.
			}

			// Falls der Bestand ausreicht, d.h. aktuellerBestand >= menge.
			aktuellerBestand -= menge;
			sql_query = "UPDATE " + TABLE_OWNER + ".LAGERT " +
						"SET anzahl = " + aktuellerBestand + " " +
						"WHERE lagid = " + srcLager + " AND pid = " + pid;
			stmt.executeUpdate(sql_query);

			// Jetzt buche die Menge von Ursprungslager nach Ziellager um.
			// Aber zuerst bestimme die akutelle Produktmenge im Ziellager.
			sql_query = "SELECT anzahl FROM " + TABLE_OWNER + ".LAGERT " +
						"WHERE lagid = " + destLager + " AND pid = " + pid;
			rs = stmt.executeQuery(sql_query);
			if ( rs.first() ) { // Falls rs nicht leer ist, d.h. Produkt mit PID ist auch im Ziellager vorhanden.
				// Dann UPDATE die Anzahl.
				aktuellerBestand = rs.getInt(1); // Anzahl im Ziellager.
				sql_query = "UPDATE " + TABLE_OWNER + ".LAGERT " +
							"SET anzahl = " + ( aktuellerBestand + menge ) + " " +
							"WHERE lagid = " + destLager + " AND pid = " + pid;
			}
			else { // Produkt (PID) existiert nicht im Ziellage.
				// Daher INSERT.
				sql_query = "INSERT INTO " + TABLE_OWNER + ".LAGERT " +
							"VALUES (" + destLager + ", " + pid + ", " + menge + ")";
			}
			stmt.executeUpdate(sql_query);
			this.connection.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			this.connection.rollback(savePoint1);
		} finally {
			if ( stmt != null ) {
				stmt.close();
			}
			this.connection.setAutoCommit(true);
		}

		return success;
	}

	public void callProcedureProduktanalyse(String typ, int groesse) throws SQLException, NotExistInDatabaseException{
		this.connection.setAutoCommit(false);
		Savepoint savePoint1 = this.connection.setSavepoint();

		CallableStatement cs = null;
		try {
			cs = this.connection.prepareCall("{call ANALYSE(?,?)}");
			cs.setString(1, typ);
			cs.setInt(2, groesse);
			cs.execute();
		} catch ( SQLException e ){
			e.printStackTrace();
			this.connection.rollback(savePoint1);
		} finally {
			if ( cs != null ) {
				try{
					cs.close();
				} catch (SQLException e){
					e.printStackTrace();
				}
			}
			this.connection.setAutoCommit(true);
		}
	}

	public void callProcedureLieferkostenAnalyse(String produkt) throws SQLException, NotExistInDatabaseException{
		this.connection.setAutoCommit(false);
		Savepoint savePoint1 = this.connection.setSavepoint();

		CallableStatement cs = null;
		try {
			cs = this.connection.prepareCall("{call ANALYSE2(?)}");
			cs.setString(1, produkt);
			cs.execute();
			this.connection.commit();
		} catch ( SQLException e ){
			e.printStackTrace();
			this.connection.rollback(savePoint1);
		} finally {
			if ( cs != null ) {
				try{
					cs.close();
				} catch (SQLException e){
					e.printStackTrace();
				}
			}
			this.connection.setAutoCommit(true);
		}
	}

	/**
	 * Diese Methode &uuml;berpr&uuml;ft, ob es zu einem vorgegebenen Liefertermin gen&uuml;gende Menge
	 * eines Produkttyps zum Verkauf gibt.
	 *
	 * @param pid
	 * @param menge
	 * @param liefertermin
	 * @return <i>true</i>, falls der Liefertermin haltbar ist. <i>false</i> sonst.
	 * @throws SQLException
	 * @throws NotExistInDatabaseException
	 */
	public boolean callProcedureCheckLiefertermin(int pid, int menge, String liefertermin)
			throws SQLException, NotExistInDatabaseException {
		this.connection.setAutoCommit(false);
		Savepoint savePoint1 = this.connection.setSavepoint();

		boolean keeping = false;
		CallableStatement cs = null;
		try {
			cs = this.connection.prepareCall("{call BESTAETIGUNGTEST(?,?,?,?)}");
			cs.setInt(1, pid);
			cs.setInt(2, menge);
			cs.setString(3, liefertermin);
			cs.registerOutParameter(4, java.sql.Types.INTEGER);
			cs.execute();
			int bestand = cs.getInt(4);
			keeping = ( bestand > 0 ) ? true : false;
		} catch ( SQLException e ){
			e.printStackTrace();
			this.connection.rollback(savePoint1);
		} finally {
			if ( cs != null ) {
				try{
					cs.close();
				} catch (SQLException e){
					e.printStackTrace();
				}
			}
			this.connection.setAutoCommit(true);
		}
		this.connection.commit();
		return keeping;
	}

	public void dropLieferkostenTabelle() throws SQLException, NotExistInDatabaseException{
		this.connection.setAutoCommit(false);
		Savepoint savePoint1 = this.connection.setSavepoint();

		CallableStatement cs = null;
		try {
			cs = this.connection.prepareCall("{call DROPANALYSE2HELPRES()}");
			cs.execute();
			this.connection.commit();
		} catch ( SQLException e ){
			e.printStackTrace();
			this.connection.rollback(savePoint1);
		} finally {
			if ( cs != null ) {
				try{
					cs.close();
				} catch (SQLException e){
					e.printStackTrace();
				}
			}
			this.connection.setAutoCommit(true);
		}
	}

	public void dropProduktTabelle() throws SQLException, NotExistInDatabaseException{
		this.connection.setAutoCommit(false);
		Savepoint savePoint1 = this.connection.setSavepoint();

		CallableStatement cs = null;
		try {
			cs = this.connection.prepareCall("{call DROPANALYSEHELPRES()}");
			cs.execute();
			this.connection.commit();
		} catch ( SQLException e ){
			e.printStackTrace();
			this.connection.rollback(savePoint1);
		} finally {
			if ( cs != null ) {
				try{
					cs.close();
				} catch (SQLException e){
					e.printStackTrace();
				}
			}
			this.connection.setAutoCommit(true);
		}
	}

	/**
	 *
	 * @param pid
	 * @param menge
	 * @return
	 * @throws NotExistInDatabaseException
	 * @throws SQLException
	 */
	public double calcTotalPrice(String pid, int menge) throws NotExistInDatabaseException, SQLException {
		// this.connection.setAutoCommit(false);
		double totalPrice = -1;
		Statement stmt = null;
		String sql_query;
		try {
			stmt = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			sql_query = "SELECT einzelverkaufspreis FROM " + TABLE_OWNER + ".PRODUKT " +
						"WHERE pid = " + pid;
			ResultSet rs = stmt.executeQuery(sql_query);
			if ( !rs.first() ) {
				throw new NotExistInDatabaseException("Invalid value. This PID does not exist in the database.");
			}
			double einzelverkaufspreis = rs.getDouble(1);
			rs.close();
			totalPrice = menge * einzelverkaufspreis;
		} catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			if ( stmt != null ) {
				stmt.close();
			}
		}
		return totalPrice;
	}


	/**
	 * This method generates a new BSTID (Bestellungs-ID) from a sequence object.
	 *
	 * @return a self generated bstid (Bestellungs-ID)
	 */
	public int generateBSTID() {
		if ( needNextBSTID ) {
			String sql_query = "SELECT " + TABLE_OWNER + "." + SEQUENCE_BESTELLUNG_BSTID + ".NEXTVAL FROM DUAL";
			Statement stmt = null;
			try {
				stmt = this.connection.createStatement();
				ResultSet rs = stmt.executeQuery(sql_query);
				rs.next();
				this.bstid_buffered = rs.getInt(1);
				rs.close();
			} catch ( SQLException e ) {
				e.printStackTrace();
			} finally {
				if ( stmt != null) {
					try {
						stmt.close();
					} catch ( SQLException e ) {
						e.printStackTrace();
					}
				}
				this.needNextBSTID = false;
			}
		}
		return this.bstid_buffered;
	}

	/**
	 * Diese Methode legt eine neue Bestellung an und speichert sie auf der Datenbank.
	 *
	 * 	BESTELLUNG : [BSTID], Bestelltext, Anleger, Anlagedatum, Aenderungsdatum, Status, Bestellterminm,
	 * 						  Erledigt_Termin, KID
	 *
	 * @param bstid
	 * @param kid
	 * @param bestelltext
	 * @param anleger
	 * @param status : OFFEN oder BESTAETIGT
	 * @param bestelltermin
	 * @param bpos : Bestellpositionen
	 * @throws NotExistInDatabaseException
	 */
	public void bestellungSpeichern(String bstid, String bestelltext, String anleger, String status,
									String bestelltermin, String kid, String[][] bpos) throws SQLException, NotExistInDatabaseException {
		if ( !checkIfElementExists(TABLE_KUNDE, "kid", kid) ) {
			throw new NotExistInDatabaseException("Kunde mit der ID " + kid + " existiert nicht in der Datenbank.");
		}
		this.connection.setAutoCommit(false);
		Statement stmt = null;
		String sql_query;

		try {
			stmt = this.connection.createStatement();
			// Anlagedatum.
			Date date = new Date(System.currentTimeMillis()); // aktuelle Zeit
			String anlagedatum = dateFormat(date, "dd.mm.yy"); // yyyy-mm-dd
			// Aenderungsdatum
			String aenderungsdatum = dateFormat(date, "dd.mm.yy");

			sql_query = "INSERT INTO " + TABLE_OWNER + ".BESTELLUNG " +
						"VALUES (" + bstid + ", '" + ((bestelltext.length() <= 0) ? "NULL" : bestelltext) + "', '" + anleger + "', "
								   + "to_date('" + anlagedatum + "', 'dd.mm.yy'), "
								   + "to_date('" + aenderungsdatum + "', 'dd.mm.yy'), '"
								   + status + "', to_date('" + bestelltermin + "', 'dd.mm.yy'), NULL, " + kid + ")";
			stmt.executeUpdate(sql_query); // Neue Bestellung mit dem Status OFFEN wird auf DB angelegt.

			/*
			 * BESTELLPOSITION : [POSNR], Anzahl, Preis, Positionstext, BSTID, PID
			 */
			if ( bpos != null ) { // INSERT Bestellpositionen
				for ( int i = 0; i < bpos.length; i++ ) {
					int anzahl = Integer.parseInt(bpos[i][1]);
					String pid = bpos[i][0];
					double preis = calcTotalPrice(pid, anzahl);

					sql_query = "INSERT INTO " + TABLE_OWNER + ".BESTELLPOSITION " +
								"VALUES (" + (i+1) + ", "  // posnr
										   + bstid + ", "  // bstid
										   + pid  + ", "  // pid
										   + anzahl + ", "  // anzahl
										   + preis + ", "  // preis
										   + "'" + bpos[i][2] + "')";  // positionstext
					System.out.println(sql_query);
					stmt.executeUpdate(sql_query);
				}
			}
			this.connection.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			this.connection.rollback();
		} finally {
			if ( stmt != null ) {
				stmt.close();
			}
			this.connection.setAutoCommit(true);
		}
	}

	/**
	 * Diese Methode best&auml;tigt eine neue Bestellung, wenn deren Liefertermin (Bestelltermin) haltbar ist.
	 * Anschlie&szlig;end wird diese Bestellung in der Datenbank abgelegt.
	 *
	 * @param bstid
	 * @param bestelltext
	 * @param anleger
	 * @param bestelltermin
	 * @param kid
	 * @param bpos
	 * @throws SQLException
	 * @throws NotExistInDatabaseException
	 *
	 */
	public boolean bestellungBestaetigen(String bstid, String bestelltext, String anleger,
										 String bestelltermin, String kid, String[][] bpos ) throws SQLException, NotExistInDatabaseException {
		boolean lieferterminHaltbar = false;
		/*
		 * BESTELLPOSITION : [POSNR], Anzahl, Preis, Positionstext, BSTID, PID
		 */
		for ( int i = 0; i < bpos.length; i++ ) {
			// [pid, menge, text]
			int pid = Integer.parseInt(bpos[i][0]);
			int menge = Integer.parseInt(bpos[i][1]);
			lieferterminHaltbar = this.callProcedureCheckLiefertermin(pid, menge, bestelltermin);
			if ( !lieferterminHaltbar ) {
				return false;
			}
		}
//		this.bestellungAendern(bstid, bestelltext, anleger, "BESTAETIGT", bestelltermin, kid, bpos); // Speichern mit dem Status BESTAETIGT.
		Statement stmt = null;
		Date now = new Date(System.currentTimeMillis());
		String aenderungsdatum = dateFormat(now, "dd.mm.yy");
		String sql_query = "UPDATE " + TABLE_OWNER + ".BESTELLUNG " +
							"SET bestelltext = '" + bestelltext + "', anleger = '" + anleger + "', status = 'BESTAETIGT'" +
							",   aenderungsdatum = to_date('" + aenderungsdatum+ "', 'dd.mm.yy')" + ", bestelltermin = to_date('" + bestelltermin + "','dd.mm.yy') " +
							"WHERE bstid = " + bstid;
		try {
			stmt = this.connection.createStatement();
			stmt.executeUpdate(sql_query);
			this.connection.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			if ( stmt != null ) {
				stmt.close();
			}
		}
		return lieferterminHaltbar;
	}

	/**
	 * Diese Methode &auml;ndert eine bestehende Bestellungsdetails.
	 *
	 * @param bstid
	 * @param bestelltext
	 * @param anleger
	 * @param bestelltermin
	 * @return
	 * @throws SQLException
	 * @throws NotExistInDatabaseException
	 */
	public boolean bestellungAendern(String bstid, String bestelltext, String anleger,
									 String bestelltermin, String kid, String[][] bpos) throws SQLException, NotExistInDatabaseException{

		if ( !checkIfElementExists(TABLE_KUNDE, "kid", kid) ) {
			throw new NotExistInDatabaseException("Kunde mit der ID " + kid + " existiert nicht in der Datenbank.");
		}

		this.connection.setAutoCommit(false);
		Statement stmt = null;
		String sql_query;

		Savepoint savepoint1 = this.connection.setSavepoint();
		boolean success = true;
		try {
			stmt = this.connection.createStatement();
			sql_query = "SELECT status FROM " + TABLE_OWNER + ".BESTELLUNG " +
						"WHERE bstid = " + bstid;
			ResultSet rs = stmt.executeQuery(sql_query);
			if ( !rs.next() ) {
				throw new NotExistInDatabaseException("<html>Invalid BSTID. Ung&uuml;ltige Bestellungs-ID.</html>");
			}
			String status = rs.getString("STATUS");
			if ( status.trim().equals("ERLEDIGT") ) {
				return false;
			}

			if ( status.trim().equals("BESTAETIGT") ) {
				/*
				 * BESTELLPOSITION : [POSNR], Anzahl, Preis, Positionstext, BSTID, PID
				 */
				for ( int i = 0; i < bpos.length; i++ ) {
					// [pid, menge, text]
					int pid = Integer.parseInt(bpos[i][0]);
					int menge = Integer.parseInt(bpos[i][1]);
					boolean lieferterminHaltbar = this.callProcedureCheckLiefertermin(pid, menge, bestelltermin);
					if ( !lieferterminHaltbar ) {
						return false; // << Liefertermin nicht haltbar. Daher keine Aenderung.
					}
				}
			}
			// sonst, nur noch OFFENe Bestellungen. Einfach alle aendern.
			Date now = new Date(System.currentTimeMillis());
			String aenderungsdatum = dateFormat(now, "dd.mm.yy");
			sql_query = "UPDATE " + TABLE_OWNER + ".BESTELLUNG " +
						"SET bestelltext = '" + bestelltext + "', anleger = '" + anleger +
						"',   aenderungsdatum = to_date('" + aenderungsdatum+ "', 'dd.mm.yy')" + ", bestelltermin = to_date('" + bestelltermin + "','dd.mm.yy') " +
						"WHERE bstid = " + bstid;
			stmt.executeQuery(sql_query);
			/*
			 * BESTELLPOSITION : [POSNR], Anzahl, Preis, Positionstext, BSTID, PID
			 */
			if ( bpos != null ) { // UPDATE Bestellpositionen
				for ( int i = 0; i < bpos.length; i++ ) {
					int anzahl = Integer.parseInt(bpos[i][1]);
					String pid = bpos[i][0];
					double preis = calcTotalPrice(pid, anzahl);
					String positionstext = bpos[i][2];

					sql_query = "UPDATE " + TABLE_OWNER + ".BESTELLPOSITION " +
								"SET anzahl = " + anzahl + ", " +
									"preis = "	+ preis + ", " +
									"positionstext = " + positionstext +  ", " +
									"pid = " + pid + " " +
								"WHERE posnr = " + i + " AND bstid = " + bstid;
					stmt.executeUpdate(sql_query);
				}
			}
			this.connection.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			this.connection.rollback(savepoint1);
		} finally {
			if ( stmt != null ) {
				stmt.close();
			}
			this.connection.setAutoCommit(true);
		}

		return success;
	}


	/**
	 * Diese Methode liefert eine bereits best&auml;tigte Bestellung aus.
	 *
	 * @param bstid
	 * @return <i>true</i>, falls die Bestellung ausgeliefert werden kann. <i>false</i> sonst.
	 * @throws NotExistInDatabaseException
	 * @throws SQLException
	 * @throws ParseException
	 */
	public boolean bestellungAusliefern(String bstid) throws NotExistInDatabaseException, SQLException, ParseException {
		this.connection.setAutoCommit(false);
		Statement stmt = null;
		String sql_query;

		Savepoint savePoint1 = this.connection.setSavepoint();
		boolean success = true; // true = Bestellung kann augeliefert werden.
		try {
			stmt = this.connection.createStatement();
			sql_query = "SELECT bestelltermin, status FROM " + TABLE_OWNER + ".BESTELLUNG " +
						"WHERE bstid = " + bstid + " FOR UPDATE NOWAIT";

			ResultSet rs = stmt.executeQuery(sql_query);
			if ( !rs.next() ) {
				success = false;
				throw new NotExistInDatabaseException("<html>Invalid BSTID. Ung&uuml;ltige Bestellungs-ID.</html>");
			}

			String status = rs.getString("STATUS");
			Date bestelltermin = rs.getDate("BESTELLTERMIN");

			// Wenn die Bestellung noch OFFEN oder bereits ERLEDIGT ist, kann sie nicht mehr ausgeliefert werden.
			if ( status.trim().equals("OFFEN") || status.trim().equals("ERLEDIGT") ) {
				return false;
			}

			// wenn der Bestelltermin (bzw. Liefertermin) schon vorbei ist, und der Kunde hat seine bestellte Ware
			// noch nicht ausgeliefert bekommt. Tja not good babe :P
			Date now = new Date(System.currentTimeMillis());
			if ( bestelltermin.before(now) ) {
				return false;
			}

			// Setze den Status auf ERLEDIGT.
			sql_query = "UPDATE " + TABLE_OWNER + ".BESTELLUNG " +
						"SET status = 'ERLEDIGT', erledigt_termin = to_date('" + dateFormat(now, "dd.MM.yy") + "', 'DD.MM.YY') " +
						"WHERE bstid = " + bstid;
			stmt.executeUpdate(sql_query);

			sql_query = "SELECT posnr, pid, anzahl FROM " + TABLE_OWNER + ".BESTELLPOSITION " +
						"WHERE bstid = " + bstid;
			rs = stmt.executeQuery(sql_query);
			while ( rs.next() ) {
				int pid = rs.getInt("PID"); // Produkt-ID.
				int menge = rs.getInt("ANZAHL"); // Zu lieferende Menge.
				sql_query = "SELECT lagid, pid, anzahl FROM " + TABLE_OWNER + ".LAGERT " +
							"WHERE pid = " + pid;
				ResultSet _rs_ = stmt.executeQuery(sql_query);
				inner_while: while ( _rs_.next() ) {
					int lagid = _rs_.getInt("LAGID");
					int aktuellerBestand = _rs_.getInt("ANZAHL");
					// UPDATE LAGERT.
					if ( menge <= aktuellerBestand ) {
						aktuellerBestand -= menge;
						sql_query = "UPDATE " + TABLE_OWNER + ".LAGERT " +
									"SET anzahl = " + aktuellerBestand + " " +
									"WHERE lagid = " + lagid + " AND pid = " + pid;
						menge = 0;
						stmt.executeUpdate(sql_query);
						break inner_while;
					}
					else { // menge > aktuellerBestand
						sql_query = "UPDATE " + TABLE_OWNER + ".LAGERT " +
									"SET anzahl = 0 " +
									"WHERE lagid = " + lagid + " AND pid = " + pid;
						menge -= aktuellerBestand;
					}
					stmt.executeUpdate(sql_query);
				}
				_rs_.close();
				if ( menge > 0 ) {
					return false; // Die bestellten Produkte koennen nicht genuegend beliefert werden.
				}
			}
			rs.close();
			this.connection.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			this.connection.rollback(savePoint1);
		} finally {
			if ( stmt != null ) {
				stmt.close();
			}
			this.connection.setAutoCommit(true);
		}
		return success;
	}

	public boolean deleteRow(String table, String condition) throws SQLException {
		this.connection.setAutoCommit(false);
		Savepoint savePoint1 = this.connection.setSavepoint();

		Statement stmt = null;
		String sql_query;

		boolean success = false;

		try {
			stmt = this.connection.createStatement();
			sql_query = "DELETE FROM " + TABLE_OWNER + "." + table + " " +
						"WHERE " + condition;
			success = true;
			this.connection.commit();
		} catch ( SQLException e) {
			e.printStackTrace();
			this.connection.rollback(savePoint1);
		} finally {
			if ( stmt != null ) {
				stmt.close();
			}
			this.connection.setAutoCommit(true);
		}

		return success;
	}

	/**
	 *
	 * @param bstid
	 * @return
	 * @throws NotExistInDatabaseException
	 * @throws SQLException
	 * @throws ParseException
	 */
	public boolean bestellungLoeschen(String bstid) throws NotExistInDatabaseException, SQLException, ParseException {
		this.connection.setAutoCommit(false);
		Statement stmt = null;
		String sql_query;

		Savepoint savePoint1 = this.connection.setSavepoint();
		boolean success = false;

		try {
			stmt = this.connection.createStatement();
			sql_query = "SELECT status FROM " + TABLE_OWNER + ".BESTELLUNG " +
						"WHERE bstid = " + bstid + " FOR UPDATE NOWAIT";
			ResultSet rs = stmt.executeQuery(sql_query);
			if ( !rs.next() ) {
				success = false;
				throw new NotExistInDatabaseException("<html>Invalid BSTID. Ung&uuml;ltige Bestellungs-ID.</html>");
			}

			String status = rs.getString("STATUS");
			// Wenn die Bestellung ERLEDIGT ist, kann sie nicht geloescht werden.
			if ( status.trim().equals("ERLEDIGT") ) {
				return false;
			}

			// Loesche alle Bestellpositionen.
			sql_query = "DELETE FROM " + TABLE_OWNER + ".BESTELLPOSITION " +
						"WHERE bstid = " + bstid;
			stmt.executeUpdate(sql_query);

			// Loesche die Bestellung.
			sql_query = "DELETE FROM " + TABLE_OWNER + ".BESTELLUNG " +
					    "WHERE bstid = " + bstid;
			stmt.executeUpdate(sql_query);

			success = true;
			this.connection.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			this.connection.rollback(savePoint1);
		} finally {
			if ( stmt != null ) {
				stmt.close();
			}
			this.connection.setAutoCommit(true);
		}
		return success;
	}

	/**
	 * Diese Methode formattiert ein Datum (Date) nach einem vorgegebenen Muster.
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public String dateFormat(Date date, String pattern) {
		DateFormat df;
		df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("de", "DE"));
		return df.format(new Date(date.getTime()));
	}

	public String dateFormat(Timestamp timestamp){
		Date d = new Date(timestamp.getTime());
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("de", "DE"));
		return df.format(new Date(d.getTime()));
	}

	public String dateFormat(String date, String pattern) throws ParseException {
		Date d = (Date) (new SimpleDateFormat(pattern).parse(date));

		DateFormat df;
		df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("de", "DE"));
		//Testdfsa

		return df.format(new Date(d.getTime()));
	}

	public String dateFormat(String date) throws ParseException {
		return dateFormat(date, "dd.MM.yy");
	}

	public void alterDateFormat() {

		String sql_query = "alter session set NLS_DATE_FORMAT='dd.mm.yy'";
		Statement stmt = null;
		try {
			stmt = this.connection.createStatement();
			stmt.execute(sql_query);
			this.connection.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			if ( stmt != null) {
				try {
					stmt.close();
				} catch ( SQLException e ) {
					e.printStackTrace();
				}
			}
		}
	}
}
