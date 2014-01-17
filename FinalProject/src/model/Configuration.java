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
	static final String CONNECTION_THIN = "jdbc:oracle:thin:@flores.dbs.ifi.lmu.de:1521:";
	static final String CONNECTION_THIN_LOCALHOST = "jdbc:oracle:thin:@localhost:1521:";
	// static final String CONNECTION_OCI = "jdbc:oracle:oci@flores.dbs.ifi.lmu.de:1521:";

	// Allgemein
	static final String BORDER_TITLE_INFO = "Info";
	static final String BORDER_TITLE_WARNING = "Warnung";
	static final String BORDER_TITLE_SUCCESS = "Erfolg";
	static final String LABEL_DELIMITER = ": ";
//	static final String TABLE_OWNER = "BORECKI";
	static final String TABLE_OWNER = "PROJEKT_2013_G";
	static final int CLIENT_WIDTH = 1280;
	static final int CLIENT_HEIGHT = 900;

	// Login
	static final String LABEL_LOGIN = "<html>Login</html>";
	static final String LABEL_CLEAR = "<html>Clear</html>";
	static final String LABEL_USERNAME = "<html>Benutzername</html>";
	static final String LABEL_PASSWORD = "<html>Passwort</html>";
	static final String LABEL_CONNECTION = "<html>Verbindung</html>";
	static final String LABEL_LOGIN_FEEDBACK_INFO = "<html>Bitte geben Sie ihre Zugangsdaten ein.</html>";
	static final String LABEL_LOGIN_FEEDBACK_WARNING = "<html>Login fehlgeschlagen!</html>";
	static final String LABEL_LOGIN_FEEDBACK_SUCCESS = "<html>Anmeldung erfolgreich.</html>";

	// MenuBar
	static final String MENU_OPTIONS = "<html>Optionen</html>";
	static final String MENU_ABOUT = "<html>Hilfe</html>";
	static final String ITEM_INFO = "<html>&Uuml;ber</html>";
	static final String ITEM_LOGOUT = "<html>Abmelden</html>";
	static final String ITEM_EXIT = "<html>Beenden</html>";


	/* ##########################*/
	/* ##### Transaktionen  #####*/
	/* ##########################*/


	/* ##### Kundenpflege  #####*/
	static final String KUNDENPFLEGE_CARD_TITLE = "<html>Kunden anlegen und editieren</html>";
	static final String[] KUNDENPFLEGE_COMBO_STRINGS = {">> Wählen Sie eine Kunden-Transaktion aus <<", "Neuer Kunde",	"Kunde editieren" };
	static final String KUNDENPFLEGE_LABEL_INFOTEXT = "<html><div style='border:1px solid #000000;padding:25px;margin-top:-30px;'><center><p><b>In diesem Reiter k&ouml;nnen Sie neue Kunden anlegen oder bearbeiten.</b></p></center>"
			+ " <br><br>"
			+ " <p>Wenn Sie einen neuen Kunden anlegen, dann wird Ihnen vom System automatisch eine passende Kundennummer vorgeschlagen, die Sie bei Bedarf &auml;ndern k&ouml;nnen."
			+ " Achten Sie aber darauf, dass sie eine g&uuml;tige numerische Zeichenfolge eingeben, sonst wird ihre Transaktion vom System verworfen."
			+ " Beachten Sie, dass Sie die Branche wahlweise aus den vorgeschlagenen Optionen aus&auml;hlen oder aber textuell eingeben k&ouml;nnen. </p>"
			+ "<br><br>"
			+ "<p>Wenn Sie einen Kunden editieren wollen, dann geben Sie zuerst die gew&uuml;nschte Kunden-ID in das vorgesehene Feld ein und dr&uuml;cken Sie den 'Suchen & &auml;ndern'-Button." +
			" Wenn es sich um eine g&uuml;tige Kunden-ID handelt, dann werden die entsprechenden Daten aus der Datenbank direkt in die vorgesehenen Felder eingelesen und sie k&ouml;nnen diese bearbeiten." +
			" Ein Klick auf den 'Ausf&uuml;hren'-Button sperrt die entsprechende Zeile in der Datenbank und der 'Fertig'-Button f&uuml;hrt die &Auml;nderungen aus.</p>"
			+ "</div></html>";
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
	static final String KUNDENPFLEGE_BUTTON_SUCHE = "<html>Suchen & &Auml;ndern</html>"; // Soni
	static final String KUNDENPFLEGE_BUTTON_EXECUTE_FERTIG = "<html>Fertig</html>"; // Soni
	static final String KUNDENPFLEGE_MESSAGE_INVALID_KID = "<html>Ung&uuml;ltige Eingabe. Kunden-KID darf nur positive numerische Werte haben, z.B. 1, 2, 16, 89, 432 ...</html>";
	static final String KUNDENPFLEGE_MESSAGE_FILL_ALL_FIELDS = "<html>Bitte f&uuml;llen Sie alle Felder aus.</html>";

	/* ##### Produktverwaltung  #####*/
	static final String PRODUKTVERWALTUNG_CARD_TITLE = "<html>Zulieferung einbuchen und Best&auml;nde umbuchen</html>";
	static final String[] PRODUKTVERWALTUNG_COMBO_STRINGS = {">> Wählen Sie eine Produkt-Transaktion aus <<", "Zulieferung einbuchen", "Bestand umbuchen" };
	static final String PRODUKTVERWALTUNG_LABEL_INFOTEXT = "<html><div style='border:1px solid #000000;padding:25px;margin-top:-30px;'><center><p><b>In diesem Reiter k&ouml;nnen Sie neue Produkt-Zulieferungen ein- oder bereits bestehene Best&auml;nde auf ein anderes Lager umbuchen.</b></p></center>"
			+ " <br>"
			+ "<p>Wenn Sie eine neue Zulieferung einbuchen und best&auml;tigen wollen, dann geben Sie einfach die Zulieferungs-ID in das vorgesehene Feld ein und dr&uuml;cken Sie den 'Einbuchen'-Button. Das System pr&uuml;ft, ob es sich um eine g&uuml;tige ID handelt und f&uuml;hrt bei erfolgreicher Pr&uuml;fung die Transaktion aus.</p>"
			+ "<br>"
			+ "<p>Bei der Umbuchung von Best&auml;nden von einem Lager auf ein anderes m&uuml;ssen Sie die IDs beider Lager angeben, sowie die ID und Menge des Produktes, welches Sie umbuchen wollen. Das System pr&uuml;ft Ihre Eingaben und f&uuml;hrt nach erfolgreicher Pr&uuml;fung die Transaktion aus.</p>"
			+ "</div></html>";
	static final String PRODUKTVERWALTUNG_TITLE_ZULIEFERUNG_NEU = "<html>Neue Zulieferung einbuchen</html>";
	static final String PRODUKTVERWALTUNG_TITLE_BESTAND_EDIT = "<html>Bestand auf ein anderes Lager umbuchen</html>";
	static final String PRODUKTVERWALTUNG_LABEL_ZLID = "<html>Zulieferungs-ID</html>";
	static final String PRODUKTVERWALTUNG_LABEL_SRCLAGER = "<html>Ursprungslager</html>";
	static final String PRODUKTVERWALTUNG_LABEL_DESTLAGER = "<html>Ziellager</html>";
	static final String PRODUKTVERWALTUNG_LABEL_PRODUKT = "<html>Produkt-ID</html>"; // Soni
	static final String PRODUKTVERWALTUNG_LABEL_MENGE = "<html>Menge</html>";
	static final String PRODUKTVERWALTUNG_BUTTON_SUCHE = "<html>Suchen</html>";
	static final String PRODUKTVERWALTUNG_BUTTON_EINBUCHEN = "<html>Einbuchen</html>";
	static final String PRODUKTVERWALTUNG_BUTTON_UMBUCHEN = "<html>Umbuchen</html>";
	static String PRODUKTVERWALTUNG_ERROR_BESTAND = "<html>Bestand reicht nicht aus! Nur ###x### St&uuml;ck vorr&auml;tig!</html>";

	static final String PRODUKTVERWALTUNG_MESSAGE_INVALID_ZLID = "<html>Ung&uuml;ltige Eingabe. Zulieferungs-ID darf nur positive numerische Werte haben, z.B. 1, 2, 16, 89, 432 ...</html>"; // Soni

	/* ##### Bestellverwaltung  #####*/
	static final String BESTELLVERWALTUNG_CARD_TITLE = "<html>Bestellungen anlegen, editieren &amp; ausliefern</html>";
	static final String[] BESTELLVERWALTUNG_COMBO_STRINGS = {">> Wählen Sie eine Bestell-Transaktion aus <<", "Bestellung anlegen", "Bestellungen editieren", "Bestellung ausliefern" };
	static final String BESTELLVERWALTUNG_LABEL_INFOTEXT = "<html><div style='border:1px solid #000000;padding:15px'><center><p><b>In diesem Reiter k&ouml;nnen Sie neue Bestellungen anlegen, bearbeiten und auslifern.</b></p></center>"
			+ " <br>"
			+ "<p>Wenn Sie eine neue Bestellung anlegen m&ouml;chten, dann f&uuml;llen Sie zun&auml;chst den Bestellkopf aus (Kunden-ID, Anleger, Bestelltermin, Bestelltext). Dannach k&ouml;nnen Sie eine oder mehrere Bestellposition hinzuf&uuml;gen. <br> Achten Sie bei den Feldern Bestelltermin und Bestellposition bitte darauf, dass Sie die richtige Formattierung verwenden:" +
			"<ul><li><b>Bestelltermin:</b> dd.mm.yy, z.B. 31.12.14 oder 01.03.14</li><li><b>Bestellposition:</b> Eine Position erfordert die Angabe von Produkt-ID und der gew&uuml;nschten Menge. Der Positionstext ist optional. Achten Sie bei der Eingabe darauf, dass Sie die Werte mit einem <i>Semikolon (';')</i> trennen. Falls Sie keinen Positionstext eingeben ist kein abschlie&szlig;endes Semikolon erforderlich.</li></ul>" +
			"Das System pr&uuml;ft die Produkt-ID und berechnet bei erfolgreicher Pr&uuml;fung den Gesamtpreis der Position." +
			"<br> Nachdem Sie alle Eingabe get&auml;tigt haben, k&ouml;nnen Sie die Bestellung entweder <i>speichern</i> oder <i>best&auml;tigen</i>. 'Speichern' hinterlegt die Bestellung mit dem Status 'OFFEN' in der Datenbank, wohingegen 'Best&auml;tigen' pr&uuml;ft, ob der gew&uuml;nschte Liefertermin eingehalten werden kann. Falls ja, dann wird die Bestellung mit dem Stauts 'BESTAETIGT' abgespeichert oder es wird angeboten, den Bestelltermin zu &auml;ndern.</p>"
			+ "<br>"
			+ "<p>Sie k&ouml;nnen alle nicht-best&auml;tigten Bestellungen editieren. Bei einer offenen Bestellungen steht es Ihnen frei alle Felder zu bearbeiten, bei einer best&auml;tigten Bestellung wird hingegen wieder eine Pr&uuml;fung durchgef&uuml;hrt, ob der aktuelle Liefertermin eingehalten werden kann.</p>"
			+ "<br>"
			+ "<p>Es steht Ihnen ebenfalls offen, bereits best&auml;tigte Bestellungen auszuliefern. Hierf&uuml;r geben Sie bitte die gew&uuml;nschte Bestellungs-ID ein, danach pr&uuml;ft das System ob die ID g&uuml;tig und die Bestellung best&auml;tigt ist und f&uuml;hrt bei erfolgreicher Pr&uuml;fung die Transaktion aus.</p>"
			+ "</div></html>";
	static final String BESTELLVERWALTUNG_TITLE_BESTELLUNG_NEU = "<html>Neue Bestellung anlegen</html>";
	static final String BESTELLVERWALTUNG_TITLE_BESTELLUNG_EDIT = "<html>Vorhandene Bestellung editieren</html>";
	static final String BESTELLVERWALTUNG_TITLE_BESTELLUNG_GO = "<html>Bestellung an Kunden ausliefern</html>";
	static final String BESTELLVERWALTUNG_LABEL_BSTID = "<html>Bestellungs-ID</html>";
	static final String BESTELLVERWALTUNG_LABEL_KID = "<html>Kunden-ID</html>";
	static final String BESTELLVERWALTUNG_LABEL_BSTTEXT = "<html>Bestelltext</html>";
	static final String BESTELLVERWALTUNG_LABEL_ANLEGER = "<html>Anleger</html>";
	static final String BESTELLVERWALTUNG_LABEL_ANLAGEDATUM = "<html>Anlagedatum</html>";
	static final String BESTELLVERWALTUNG_LABEL_AENDERUNGSDATUM = "<html>&Auml;nderungsdatum</html>";
	static final String BESTELLVERWALTUNG_LABEL_STATUS = "<html>Bestellstatus</html>";
	static final String BESTELLVERWALTUNG_LABEL_BSTTERMIN = "<html>Bestelltermin</html>";
	static final String BESTELLVERWALTUNG_LABEL_ERLEDIGTTERMIN = "<html>Erledigt am</html>";
	static final String BESTELLVERWALTUNG_LABEL_BESTELLPOS = "<html>Bestellposition</html>";
	static final String BESTELLVERWALTUNG_LABEL_POSNR = "<html>Positionsnr.</html>";
	static final String BESTELLVERWALTUNG_LABEL_MENGE = "<html>Menge</html>";
	static final String BESTELLVERWALTUNG_LABEL_PREIS = "<html>Preis</html>";
	static final String BESTELLVERWALTUNG_LABEL_POSTEXT = "<html>Positionstext</html>";
	static final String BESTELLVERWALTUNG_LABEL_PRODUKT = "<html>Produkt-ID</html>";
	static final String BESTELLVERWALTUNG_BUTTON_SUCHE = "<html>Suchen & &Auml;ndern</html>";
	static final String BESTELLVERWALTUNG_BUTTON_SPEICHERN = "<html>Speichern</html>";
	static final String BESTELLVERWALTUNG_BUTTON_BESTAETIGEN = "<html>Best&auml;tigen</html>";
	static final String BESTELLVERWALTUNG_BUTTON_AUSLIEFERN = "<html>Ausliefern</html>";
	static final String BESTELLVERWALTUNG_BUTTON_EXECUTE_FERTIG = "<html>Fertig</html>"; // Soni

	static final String BESTELLVERWALTUNG_MESSAGE_INVALID_BSTID = "<html>Ung&uuml;ltige Eingabe. Die Bestellungs-ID darf nur positive numerische Werte haben, z.B. 1, 2, 16, 89, 432 ...</html>";
	static final String BESTELLVERWALTUNG_MESSAGE_INVALID_BSTTERMIN = "<html>Bitte geben Sie ein Datum mit dem Format dd.mm.yy ein, z.B. 31.12.14.</html>";
	static final String BESTELLVERWALTUNG_MESSAGE_FILL_ALL_FIELDS = "<html>Bitte f&uuml;llen Sie alle Felder aus.</html>";


	/* ##########################*/
	/* ####### Auswertung #######*/
	/* ##########################*/

	/* ##### Produktanalyse  #####*/
	static final String ANALYSE_CARD_TITLE = "<html>Bestellungen anlegen, editieren und verschicken</html>";
	static final String ANALYSE_TITLE_PRODUKTANALYSE = "<html>Produktanalyse starten</html>";
	static final String ANALYSE_TITLE_LIEFERKOSTENSENKUNG = "<html>Senken der Lieferkosten</html>";
	static final String ANALYSE_LABEL_TYP = "<html>Typ</html>";
	static final String ANALYSE_LABEL_GROESSE = "<html>Gr&ouml;&szlig;e</html>";
	static final String ANALYSE_LABEL_PRODUKT = "<html>Produkt-Name</html>";
	static final String ANALYSE_BUTTON_AUSFUEHREN = "<html>Ausf&uuml;hren</html>";

	/* ##### Senkung der Lieferkosten  #####*/


	/* ##########################*/
	/* ####### Komponenten ######*/
	/* ##########################*/


	/* ##### Panels #####*/
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

	/* ##### Container #####*/
	static final String COMPONENT_PANEL_BESTELLVERWALTUNG_NEU_BSTPOS = "panel_bestellverwaltung_neu_bstpos";
	static final String COMPONENT_PANEL_BESTELLVERWALTUNG_EDIT_BSTPOS = "panel_bestellverwaltung_edit_bstpos";

	/* ##### ScrollPanes #####*/
	static final String COMPONENT_SCROLLPANE_EXPLORER = "scrollpane_explorer";
	static final String COMPONENT_SCROLLPANE_DBOUTPUT = "scrollpane_dboutput";

	/* ##### TabbedPanes #####*/
	static final String COMPONENT_TABBEDPANE_TRANSAKTIONEN = "tabbedpane_transaktionen";
	static final String COMPONENT_TABBEDPANE_AUSWERTUNG = "tabbedpane_auswertung";

	/* ##### Textfields #####*/
	// DBOutput
	static final String COMPONENT_TEXTFIELD_DBOUTPUT_SUCHE = "textfield_dboutput_suche";
	// Kundenpflege
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
	// Produktverwaltung
	static final String COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_NEU_ZLID = "textfeld_produktverwaltung_neu_zlid";
	static final String COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_ZLID = "textfeld_produktverwaltung_edit_zlid";
	static final String COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_SRCLAGER = "textfield_produktverwaltugn_edit_srclager";
	static final String COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_DESTLAGER = "textfield_produktverwaltung_edit_destlager";
	static final String COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_PRODUKT = "textfield_produktverwaltung_edit_produkt";
	static final String COMPONENT_TEXTFIELD_PRODUKTVERWALTUNG_EDIT_MENGE = "textfield_produktverwaltung_edit_menge";
	// Bestellverwaltung
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTID = "textfield_bestellverwaltung_neu_bstid";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_KID = "textfield_bestellverwaltung_neu_kid";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTEXT = "textfield_bestellverwaltung_neu_bsttext";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_ANLEGER = "textfield_bestellverwaltung_neu_anleger";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTTERMIN = "textfield_bestellverwaltung_neu_bsttermin";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTPOS = "textfield_bestellverwaltung_neu_bestellpositionen";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTPOS_PID = "textfield_bestellverwaltung_neu_bstpos_pid";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTPOS_MENGE = "textfield_bestellverwaltung_neu_bstpos_menge";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_NEU_BSTPOS_PREIS = "textfield_bestellverwaltung_neu_bstpos_preis";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTID = "textfield_bestellverwaltung_edit_bstid";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_KID = "textfield_bestellverwaltung_edit_kid";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTEXT = "textfield_bestellverwaltung_edit_bsttext";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_ANLEGER = "textfield_bestellverwaltung_edit_anleger";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTTERMIN = "textfield_bestellverwaltung_edit_bsttermin";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTPOS = "textfield_bestellverwaltung_edit_bestellpositionen";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTPOS_PID = "textfield_bestellverwaltung_edit_bstpos_pid";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTPOS_MENGE = "textfield_bestellverwaltung_edit_bstpos_menge";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_EDIT_BSTPOS_PREIS = "textfield_bestellverwaltung_edit_bstpos_preis";
	static final String COMPONENT_TEXTFIELD_BESTELLVERWALTUNG_GO_BSTID = "textfield_bestellverwaltung_go_bstid";
	// Produktanalyse
	static final String COMPONENT_TEXTFIELD_PRODUKTANALYSE_TYP = "textfield_analyse_typ";
	static final String COMPONENT_TEXTFIELD_PRODUKTANALYSE_GROESSE = "textfield_analyse_groesse";
	// Lieferkostensenkung
	static final String COMPONENT_TEXTFIELD_LIEFERKOSTEN_PRODUKT = "textfield_lieferkosten_produkt";

	/* ##### Textareas #####*/
	static final String COMPONENT_TEXTAREA_BESTELLVERWALTUNG_NEU_BSTPOS_POSTEXT = "textarea_bestellverwaltung_neu_bstpos_postext";
	static final String COMPONENT_TEXTAREA_BESTELLVERWALTUNG_EDIT_BSTPOS_POSTEXT = "textarea_bestellverwaltung_edit_bstpos_postext";

	/* ##### Labels #####*/
	static final String COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_STATUS = "label_bestellverwaltung_neu_status";
	static final String COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_STATUS = "label_bestellverwaltung_edit_status";
	static final String COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_ANLAGEDATUM = "label_bestellverwaltung_neu_anlagedatum";
	static final String COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_AENDERUNGSDATUM = "label_bestellverwaltung_neu_aenderungsdatum";
	static final String COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_ERLEDIGTTERMIN = "label_bestellverwaltung_neu_erledigttermin";
	static final String COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_ANLAGEDATUM = "label_bestellverwaltung_edit_anlagedatum";
	static final String COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_AENDERUNGSDATUM = "label_bestellverwaltung_edit_aenderungsdatum";
	static final String COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_ERLEDIGTTERMIN = "label_bestellverwaltung_edit_erledigttermin";
	static final String COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_BSTPOS_POSNR = "label_bestellverwaltung_neu_bstpos_posnr";
	static final String COMPONENT_LABEL_BESTELLVERWALTUNG_NEU_BSTPOS_BSTID = "label_bestellverwaltung_neu_bstpos_bstid";
	static final String COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_BSTPOS_POSNR = "label_bestellverwaltung_edit_bstpos_posnr";
	static final String COMPONENT_LABEL_BESTELLVERWALTUNG_EDIT_BSTPOS_BSTID = "label_bestellverwaltung_edit_bstpos_bstid";

	/* ##### Comboboxen #####*/
	static final String COMPONENT_COMBO_KUNDENPFLEGE_ACTIONS = "combo_kundenpflege_actions";
	static final String COMPONENT_COMBO_KUNDENPFLEGE_NEU_NATION = "combo_kundenpflege_neu_nation";
	static final String COMPONENT_COMBO_KUNDENPFLEGE_NEU_BRANCHE = "combo_kundenpflege_neu_branche";
	static final String COMPONENT_COMBO_KUNDENPFLEGE_EDIT_NATION = "combo_kundenpflege_edit_nation";
	static final String COMPONENT_COMBO_KUNDENPFLEGE_EDIT_BRANCHE = "combo_kundenpflege_edit_branche";
	static final String COMPONENT_COMBO_PRODUKTVERWALTUNG_ACTIONS = "combo_produktverwaltung_actions";
	static final String COMPONENT_COMBO_BESTELLVERWALTUNG_ACTIONS = "combo_bestellverwaltung_actions";

	/* ##### Trees #####*/
	static final String COMPONENT_TREE_EXPLORER = "tree_explorer";

	/* ##### Buttons #####*/
	static final String COMPONENT_BUTTON_DBOUTPUT_SUCHEN = "button_dboutput_suchen";

	static final String COMPONENT_BUTTON_KUNDENPFLEGE_NEU_AUSFUEHREN = "button_kundenpflege_neu_ausfuehren";
	static final String COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN = "button_kundenpflege_edit_aendern";
	static final String COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_SUCHEN = "button_kundenpflege_edit_suchen";
	static final String COMPONENT_BUTTON_KUNDENPFLEGE_EDIT_AENDERN_FERTIG = "button_kundenpflege_edit_aendern_fertig"; // Soni

	static final String COMPONENT_BUTTON_PRODUKTVERWALTUNG_NEU_EINBUCHEN = "button_produktverwaltung_neu_einbuchen";
	static final String COMPONENT_BUTTON_PRODUKTVERWALTUNG_NEU_SUCHEN = "button_produktverwaltung_neu_suchen";
	static final String COMPONENT_BUTTON_PRODUKTVERWALTUNG_EDIT_UMBUCHEN = "button_produktverwaltung_edit_umbuchen";

	static final String COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_BSTPOSHINZUFUEGEN = "button_bestellverwaltung_neu_bstposhinzufuegen";
	static final String COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_BSTPOSAENDERN = "button_bestellverwaltung_neu_bstposaendern";
	static final String COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_BSTPOSLOESCHEN = "button_bestellverwaltung_neu_loeschen";
	static final String COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_SPEICHERN = "button_bestellverwaltung_neu_speichern";
	static final String COMPONENT_BUTTON_BESTELLVERWALTUNG_NEU_BESTAETIGEN = "button_bestellverwaltung_neu_bestaetigen";
	static final String COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_BSTPOSHINZUFUEGEN = "button_bestellverwaltung_edit_bstposhinzufuegen";
	static final String COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_BSTPOSAENDERN = "button_bestellverwaltung_edit_bstposaendern";
	static final String COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_BSTPOSLOESCHEN = "button_bestellverwaltung_edit_loeschen";
	static final String COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_SUCHEN = "button_bestellverwaltung_edit_suchen";
	static final String COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_SPEICHERN = "button_bestellverwaltung_edit_speichern";
	static final String COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_BESTAETIGEN = "button_bestellverwaltung_edit_bestaetigen";
	static final String COMPONENT_BUTTON_BESTELLVERWALTUNG_GO_AUSLIEFERN = "button_bestellverwaltung_bestellung_go_ausliefern";
	static final String COMPONENT_BUTTON_BESTELLVERWALTUNG_EDIT_AENDERN_FERTIG = "button_bestellverwaltung_edit_aendern_fertig"; // Soni

	static final String COMPONENT_BUTTON_PRODUKTANALYSE_AUSFUEHREN = "button_produktanalyse_ausfuehren";
	static final String COMPONENT_BUTTON_LIEFERKOSTEN_AUSFUEHREN = "button_lieferkosten_ausfuehren";

	/* ##### Menu Items #####*/
	static final String COMPONENT_MENU = "menu";
	static final String COMPONENT_ITEM_MENU_LOGOUT = "item_menu_logout";
	static final String COMPONENT_ITEM_MENU_EXIT = "item_menu_exit";
	static final String COMPONENT_ITEM_MENU_ABOUT = "item_menu_about";
	static final String COMPONENT_ITEM_MENU_INFO = "item_menu_info";

	/* #### Tabellen, Sequence #### */
	static final String TABLE_KUNDE = "KUNDE";
	static final String TABLE_BESTELLUNG = "BESTELLUNG";
	static final String TABLE_BESTELLPOSITION = "BESTELLPOSITION";
	static final String TABLE_PRODUKT = "PRODUKT";
	static final String TABLE_LAGER = "LAGER";
	static final String SEQUENCE_KUNDE_KID = "SEQ_KUNDE_KID";
	static final String SEQUENCE_BESTELLUNG_BSTID = "SEQ_BESTELLUNG_BSTID";

}
