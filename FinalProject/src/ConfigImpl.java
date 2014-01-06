/**
 * Interface, in welchem alle Konstanten und Strings gespeichert sind, um sie
 * zentral an einer Stelle verwalten/aendern zu koennen.
 *
 * @author borecki
 *
 */
public interface ConfigImpl {
	// Connection
	static final String TITLE = "Haufkof Client";
	static final String DRIVER = "oracle.jdbc.OracleDriver";
	//static final String CONNECTION_THIN = "jdbc:oracle:thin:@flores.dbs.ifi.lmu.de:1521:";
	static final String CONNECTION_THIN = "jdbc:oracle:thin:@localhost:1521:";
	//static final String CONNECTION_OCI = "jdbc:oracle:oci@flores.dbs.ifi.lmu.de:1521:";
	static final String CONNECTION_OCI = "jdbc:oracle:oci@localhost:1521:";

	// Allgemein
	static final String BORDER_TITLE_INFO = "Info";
	static final String BORDER_TITLE_WARNING = "Warnung";
	static final String BORDER_TITLE_SUCCESS = "Erfolg";

	// Login-View
	static final String LABEL_LOGIN = "Login";
	static final String LABEL_CLEAR = "Clear";
	static final String LABEL_USERNAME = "Benutzername: ";
	static final String LABEL_PASSWORD = "Passwort: ";
	static final String LABEL_LOGIN_FEEDBACK_INFO = "<html>Bitte geben Sie ihre Zugangsdaten ein.</html>";
	static final String LABEL_LOGIN_FEEDBACK_WARNING = "<html>Login fehlgeschlagen!</html>";
	static final String LABEL_LOGIN_FEEDBACK_SUCCESS = "Anmeldung erfolgreich.";

	// Main-View
	// Tab 1
	static final String LABEL_PFLEGETRANSAKTION = "<html>Kunde anlegen und &auml;ndern</html>";
	static final String LABEL_EXECUTE = "<html>Ausf&uuml;hren</html>";
	static final String LABEL_BUTTON_EINBUCHEN = "<html>Einbuchen</html>";
	static final String LABEL_LOGOUT = "Logout";
	static final String LABEL_KID = "Kunden-ID: ";
	static final String LABEL_NAME = "Name: ";
	static final String LABEL_ADRESSE = "Adresse: ";
	static final String LABEL_TEL = "Telefonnr.: ";
	static final String LABEL_NATION = "Nation: ";
	static final String LABEL_KONTO = "Konto: ";
	static final String LABEL_BRANCHE = "Branche: ";

	// Tab 2
	static final String LABEL_PRODUKTTRANSAKTIONEN_ZULIEFERUNG = "<html>Zulieferung einbuchen: </html>";
	static final String LABEL_PRODUKTTRANSAKTIONEN_BESTAND = "<html>Bestand umbuchen: </html>";
	static final String LABEL_ZID = "Zulieferungs-ID: ";
	static final String LABEL_POSNR = "Zulieferungsposition: ";
	static final String LABEL_PID = "Produkt-ID: ";
	static final String LABEL_STATUS = "Status: ";
	static final String LABEL_ANZAHL = "Anzahl: ";
	static String ERROR_BESTAND = "<html>Bestand reicht nicht aus! Nur ###x### St&uuml;ck vorr&auml;tig!</html>";


	// Old
	static final String LABEL_MAIN_FEEDBACK_INFO = "<html>Bitte geben Sie eine Unterkunfts-ID ein.<br /><br /></html>";
	static final String LABEL_MAIN_FEEDBACK_WARNING = "<html>Diese ID ist ung&uuml;ltig. <br /> Geben Sie eine g&uuml;ltige Unterkunfts-ID ein.</html>";
	static String LABEL_MAIN_FEEDBACK_SUCCESS = "<html>Ergebnisse f&uuml;r die Unterkunft: ###ID### <br /> Geben Sie eine weitere Unterkunfts-ID ein.</html>";
	static String QUERY = "SELECT b_id,name,personen,von,bis FROM reise_tables_2013.unterkunft u JOIN reise_tables_2013.buchung b ON b.unterkunft = u.u_id WHERE u.u_id = ";
}
