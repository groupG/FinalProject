package master_old;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.CachedRowSet;


public class Controller {

	private DB db;

	public Controller(String driver, String connection, String user, String password) throws Exception {
		this.db = new DB(driver, connection, user, password);
	}

	public boolean checkIfElementexists(String table, String element,
			String value) throws SQLException{
		return this.db.checkIfElementexists(table, element, value);
	}

	public void showTables(){
		try {
			this.db.showTables();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<String> getTables(String owner){
		try {
			return this.db.getTables(owner);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<String> getColumns(String owner, String table){
		try {
			return this.db.getColumns(owner, table);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public CachedRowSet getContentsOfOutputTable(String query){
		return this.db.getContentsOfOutputTable(query);
	}


	/**
	 * Diese Methode erstellt einen neuen Kunden mit Namen, Adresse, Telefonnummer, Nation, Branche.
	 * Der Kontostand des neuen Kunden wird mit 0.00 initialisiert.
	 *
	 * This method creates/inserts a new customer with own information like name, address, phone number, counter, branch.
	 * The initial account balance is 0.00.
	 *
	 * @param kName : Name des Kunden.
	 * @param kAdresse : Adresse des Kunden.
	 * @param kTelefon : Telefonnummer des Kunden.
	 * @param kBranche : Branche, in dem der Kunde tätig ist.
	 * @param kNation : Name des Landes, in dem der Kunde lebt.
	 */
	public void insertKunde(String kName, String kAdresse, String kTelNr, String kBranche, String kNation) throws SQLException {
			this.db.insertKunde(kName, kAdresse, kTelNr, kBranche, kNation);
	}
}