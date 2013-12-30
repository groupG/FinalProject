import java.sql.Connection;
import java.sql.SQLException;


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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
