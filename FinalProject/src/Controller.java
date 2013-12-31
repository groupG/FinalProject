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
}
