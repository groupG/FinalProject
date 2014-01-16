package model;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

import utils.NotExistInDatabaseException;

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
			System.out.println("Lock ResultSet r is : " + rs.getString(1));
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
				System.out.println(status);
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
			sql_query = "SELECT einzelverkaufspreis FROM PRODUKT " + 
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
}
