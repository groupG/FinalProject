package model;

/**
 * Interface, in welchem alle Konstanten und Strings gespeichert sind, um sie
 * zentral an einer Stelle verwalten/aendern zu koennen.
 *
 * @author borecki
 *
 */
public interface Configuration {
	// Connection
	static final String TITLE = "Haufkof Client";
	static final String DRIVER = "oracle.jdbc.OracleDriver";
//	 static final String CONNECTION_THIN = "jdbc:oracle:thin:@flores.dbs.ifi.lmu.de:1521:";
	static final String CONNECTION_THIN = "jdbc:oracle:thin:@localhost:1521:";
//	 static final String CONNECTION_OCI = "jdbc:oracle:oci@flores.dbs.ifi.lmu.de:1521:";

	// Allgemein
	static final String BORDER_TITLE_INFO = "Info";
	static final String BORDER_TITLE_WARNING = "Warnung";
	static final String BORDER_TITLE_SUCCESS = "Erfolg";
	static final String LABEL_DELIMITER = ": ";
	static final String TABLE_OWNER = "PROJEKT_2013";
	static final int CLIENT_WIDTH = 1280;
	static final int CLIENT_HEIGHT = 900;

	// Login
	static final String LABEL_LOGIN = "<html>Login</html>";
	static final String LABEL_CLEAR = "<html>Clear</html>";
	static final String LABEL_USERNAME = "<html>Benutzername</html>";
	static final String LABEL_PASSWORD = "<html>Passwort</html>";
	static final String LABEL_LOGIN_FEEDBACK_INFO = "<html>Bitte geben Sie ihre Zugangsdaten ein.</html>";
	static final String LABEL_LOGIN_FEEDBACK_WARNING = "<html>Login fehlgeschlagen!</html>";
	static final String LABEL_LOGIN_FEEDBACK_SUCCESS = "<html>Anmeldung erfolgreich.</html>";

	// Transaktionen
	// Kundenpflege
	static final String KUNDENPFLEGE_CARD_TITLE = "<html>Kunden anlegen und editieren</html>";
	static final String[] KUNDENPFLEGE_COMBO_STRINGS = { "Neuer Kunde",
			"Kunde editieren" };
	static final String KUNDENPFLEGE_TITLE_KUNDE_NEU = "<html>Neuen Kunden anlegen</html>";
	static final String KUNDENPFLEGE_TITLE_KUNDE_EDIT = "<html>Vorhandenen Kunden editieren</html>";
	static final String KUNDENPFLEGE_LABEL_KID = "<html>Kunden-ID</html>";
	static final String KUNDENPFLEGE_LABEL_NAME = "<html>Name</html>";
	static final String KUNDENPFLEGE_LABEL_ADRESSE = "<html>Adresse</html>";
	static final String KUNDENPFLEGE_LABEL_TEL = "<html>Telefonnr.</html>";
	static final String KUNDENPFLEGE_LABEL_NATION = "<html>Nation</html>";
	static final String KUNDENPFLEGE_LABEL_KONTO = "<html>Konto</html>";
	static final String KUNDENPFLEGE_LABEL_BRANCHE = "<html>Branche</html>";
	static final String KUNDENPFLEGE_BUTTON_EXECUTE = "<html>Ausf&uuml;hren</html>";
	static final String KUNDENPFLEGE_BUTTON_SUCHE = "<html>Suchen</html>";

	// Produktverwaltung
	static final String PRODUKTVERWALTUNG_CARD_TITLE = "<html>Zulieferung einbuchen und Best&auml;nde umbuchen</html>";
	static final String[] PRODUKTVERWALTUNG_COMBO_STRINGS = {
			"Zulieferung einbuchen", "Bestand umbuchen" };
	static final String PRODUKTVERWALTUNG_TITLE_ZULIEFERUNG_NEU = "<html>Neue Zulieferung einbuchen</html>";
	static final String PRODUKTVERWALTUNG_TITLE_BESTAND_EDIT = "<html>Bestand auf ein anderes Lager umbuchen</html>";
	static final String PRODUKTVERWALTUNG_LABEL_ZLID = "<html>Zulieferungs-ID</html>";
	static final String PRODUKTVERWALTUNG_LABEL_SRCLAGER = "<html>Ursprungslager</html>";
	static final String PRODUKTVERWALTUNG_LABEL_DESTLAGER = "<html>Ziellager</html>";
	static final String PRODUKTVERWALTUNG_BUTTON_SUCHE = "<html>Suchen</html>";
	static final String PRODUKTVERWALTUNG_BUTTON_EINBUCHEN = "<html>Einbuchen</html>";
	static final String PRODUKTVERWALTUNG_BUTTON_UMBUCHEN = "<html>Umbuchen</html>";
	static String PRODUKTVERWALTUNG_ERROR_BESTAND = "<html>Bestand reicht nicht aus! Nur ###x### St&uuml;ck vorr&auml;tig!</html>";

	// Bestellverwaltung
	static final String BESTELLVERWALTUNG_CARD_TITLE = "<html>Bestellungen anlegen, editieren &amp; ausliefern</html>";
	static final String[] BESTELLVERWALTUNG_COMBO_STRINGS = {
			"Bestellung anlegen", "Bestellungen editieren", "Bestellung ausliefern" };
	static final String BESTELLVERWALTUNG_TITLE_BESTELLUNG_NEU = "<html>Neue Bestellung anlegen</html>";
	static final String BESTELLVERWALTUNG_TITLE_BESTELLUNG_EDIT = "<html>Vorhandene Bestellung editieren</html>";
	static final String BESTELLVERWALTUNG_TITLE_BESTELLUNG_GO = "<html>Bestellung an Kunden ausliefern</html>";
	static final String BESTELLVERWALTUNG_LABEL_BSTID = "<html>Bestellungs-ID</html>";
	static final String BESTELLVERWALTUNG_LABEL_BSTTEXT = "<html>Bestelltext</html>";
	static final String BESTELLVERWALTUNG_LABEL_ANLEGER = "<html>Anleger</html>";
	static final String BESTELLVERWALTUNG_LABEL_ANLAGEDATUM = "<html>Anlagedatum</html>";
	static final String BESTELLVERWALTUNG_LABEL_AENDERUNGSDATUM = "<html>&Auml;nderungsdatum</html>";
	static final String BESTELLVERWALTUNG_LABEL_STATUS = "<html>Bestellstatus</html>";
	static final String BESTELLVERWALTUNG_LABEL_BSTTERMIN = "<html>Bestelltermin</html>";
	static final String BESTELLVERWALTUNG_LABEL_ERLEDIGTTERMIN = "<html>Erledigt am</html>";
	static final String BESTELLVERWALTUNG_LABEL_BSTPOSNR = "<html>Bestellposition</html>";
	static final String BESTELLVERWALTUNG_LABEL_BSTMENGE = "<html>Menge</html>";
	static final String BESTELLVERWALTUNG_LABEL_BSTPREIS = "<html>Preis</html>";
	static final String BESTELLVERWALTUNG_LABEL_BSTPOSTEXT = "<html>Positionstext</html>";
	static final String BESTELLVERWALTUNG_LABEL_BSTPID = "<html>Produkt-ID</html>";
	static final String BESTELLVERWALTUNG_BUTTON_BESTELLUNG_SPEICHERN = "<html>Speichern</html>";
	static final String BESTELLVERWALTUNG_BUTTON_BESTELLUNG_BESTAETIGEN = "<html>Best&auml;tigen</html>";

	// Analyse
	static final String ANALYSE_CARD_TITLE = "<html>Bestellungen anlegen, editieren und verschicken</html>";
	static final String ANALYSE_TITLE_PRODUKTANALYSE = "<html>Produktanalyse starten</html>";
	static final String ANALYSE_TITLE_LIEFERKOSTENSENKUNG = "<html>Senken der Lieferkosten</html>";
	static final String ANALYSE_LABEL_TYP = "<html>Typ</html>";
	static final String ANALYSE_LABEL_GROESSE = "<html>Gr&ouml;&szlig;e</html>";
	static final String ANALYSE_LABEL_PID = "<html>Produkt-ID</html>";

	// Componentnames
	// Panels
	static final String COMPONENT_PANEL_KUNDENPFLEGE = "panel_kundenpflege";
	static final String COMPONENT_PANEL_PRODUKTVERWALTUNG = "panel_produktverwaltung";
	static final String COMPONENT_PANEL_BESTELLVERWALTUNG = "panel_bestellungverwaltung";
	static final String COMPONENT_PANEL_KUNDENPFLEGE_ACTIONS = "panel_kundenpflege_actions";
	static final String COMPONENT_PANEL_KUNDENPFLEGE_NEU = "panel_kundenpflege_neu";
	static final String COMPONENT_PANEL_KUNDENPFLEGE_EDIT = "panel_kundenpflege_edit";
	static final String COMPONENT_PANEL_PRODUKTVERWALTUNG_ACTIONS = "panel_produktverwaltung_actions";
	static final String COMPONENT_PANEL_PRODUKTVERWALTUNG_ZULIEFERUNG_NEU = "panel_produktverwaltung_zulieferung_neu";
	static final String COMPONENT_PANEL_PRODUKTVERWALTUNG_BESTAND_EDIT = "panel_produktverwaltung_zulieferung_edit";
	static final String COMPONENT_PANEL_BESTELLVERWALUNG_ACTIONS = "panel_bestellungverwaltung_actions";
	static final String COMPONENT_PANEL_BESTELLVERWALTUNG_NEU = "panel_bestellungverwaltung_neu";
	static final String COMPONENT_PANEL_BESTELLVERWALTUNG_EDIT = "panel_bestellungverwaltung_edit";
	static final String COMPONENT_PANEL_BESTELLVERWALTUNG_GO = "panel_bestellungverwaltung_go";
	static final String COMPONENT_PANEL_PRODUKTANALYSE = "panel_produktanalyse";
	static final String COMPONENT_PANEL_LIEFERKOSTENSENKUNG = "panel_lieferkostensenkung";

	// ScrollPanes
	static final String COMPONENT_SCROLLPANE_EXPLORER = "scrollpane_explorer";
	static final String COMPONENT_SCROLLPANE_DBOUTPUT = "scrollpane_dboutput";

	// TabbedPanes
	static final String COMPONENT_TABBEDPANE_TRANSAKTIONEN = "tabbedpane_transaktionen";
	static final String COMPONENT_TABBEDPANE_AUSWERTUNG = "tabbedpane_auswertung";

	// Textfields
	static final String COMPONENT_TEXTFIELD_DBOUTPUT_SUCHE = "textfield_dboutput_suche";
	static final String COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KID = "textfield_kundenpflege_neu_kid";
	static final String COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_NAME = "textfield_kundenpflege_neu_name";
	static final String COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_ADRESSE = "textfield_kundenpflege_neu_adresse";
	static final String COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_TEL = "textfield_kundenpflege_neu_tel";
	static final String COMPONENT_TEXTFIELD_KUNDENPFLEGE_NEU_KONTO = "textfield_kundenpflege_neu_konto";
	static final String COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KID = "textfield_kundenpflege_edit_kid";
	static final String COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_NAME = "textfield_kundenpflege_edit_name";
	static final String COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_ADRESSE = "textfield_kundenpflege_edit_adresse";
	static final String COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_TEL = "textfield_kundenpflege_edit_tel";
	static final String COMPONENT_TEXTFIELD_KUNDENPFLEGE_EDIT_KONTO = "textfield_kundenpflege_edit_konto";
	static final String COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_ZLID = "textfeld_produktverwaltung_zlid";
	static final String COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_SRCLAGER = "textfield_produktverwaltugn_srclager";
	static final String COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_DESTLAGER = "textfield_produktverwaltung_destlager";
	static final String COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_PID = "textfield_produktverwaltung_pid";
	static final String COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_MENGE = "textfield_produktverwaltung_menge";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_WUNSCHTERMIN = "textfield_bestellverwaltung_wunschtermin";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_POSNR = "textfield_bestellverwaltung_posnr";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_MENGE = "textfield_bestellverwaltung_menge";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_PID = "textfield_bestellverwaltung_pid";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_PREIS = "textfield_bestellverwaltung_preis";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_POSTEXT = "textfield_bestellverwaltung_postext";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_AENDERUNGSDATUM = "textfield_bestellverwaltung_aenderungsdatum";
	static final String COMPONENT_TEXTFIELD_ANALYSE_TYP = "textfield_analyse_typ";
	static final String COMPONENT_TEXTFIELD_ANALYSE_GROESSE = "textfield_analyse_groesse";
	static final String COMPONENT_TEXTFIELD_ANALYSE_PID = "textfield_analyse_produkt";

	// Comboboxen
	static final String COMPONENT_COMBO_KUNDENPFLEGE_ACTIONS = "combo_kundenpflege_actions";
	static final String COMPONENT_COMBO_KUNDENPFLEGE_NEU_NATION = "combo_kundenpflege_neu_nation";
	static final String COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE = "combo_kundenpflege_neu_branche";
	static final String COMPONENT_COMBO_KUNDENPFLEGE_EDIT_NATION = "combo_kundenpflege_edit_nation";
	static final String COMPONENT_COMBO_KUNDENPFLEGE_EDIT_BRANCHE = "combo_kundenpflege_edit_branche";
	static final String COMPONENT_COMBO_PRODUKTVERWALTUNG_ACTIONS = "combo_produktverwaltung_actions";
	static final String COMPONENT_COMBO_BESTELLVERWALTUNG_ACTIONS = "combo_bestellverwaltung_actions";

	// Tree
	static final String COMPONENT_TREE_EXPLORER = "tree_explorer";

	// Button
	static final String COMPONENT_BUTTON_KUNDENPFLEGE_NEU_AUSFUEHREN = "button_kundenpflege_neu";
	static final String COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN = "button_kundenpflege_edit";
	static final String COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_SUCHEN = "button_kundenpflege_suchen";
	static final String COMPONENT_BUTTON_PRODUKTVERWALTUNG_NEU = "button_produktverwaltung_einbuchen";
	static final String COMPONENT_BUTTON_PRODUKTVERWALTUNG_EDIT = "button_produktverwaltung_umbuchen";
	static final String COMPONENT_BUTTON_PRODUKTVERWALTUNG_SUCHEN = "button_produktverwaltung_suchen";
	static final String COMPONENT_BUTTON_BESTELLVERWALTUNG_BESTELLPOS_NEU = "button_bestellverwaltung_bestellpos_neu";
	static final String COMPONENT_BUTTON_BESTELLVERWALTUNG_BESTELLUNG_SPEICHERN = "button_bestellverwaltung_bestellung_speichern";
	static final String COMPONENT_BUTTON_BESTELLVERWALTUNG_BESTELLUNG_BESTAETIGEN = "button_bestellverwaltung_bestellung_bestaetigen";
}
