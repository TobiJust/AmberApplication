package de.thwildau.util;


/**
 * This class manages the constant String attributes of the application.
 * @author Kulla
 * @version 1.0
 * @since 2015-01-12
 *
 */
public class Constants {

	/*network attributes*/

	public final static String HOST_NAME = "192.168.137.122";
	public final static String CONNECTION_TIMEOUT = "5000";
	public final static String PORT = "3000";

	/*event texts*/

	public final static String EVENT_TEXT_TURN = "Kurve erkannt.";
	public final static String EVENT_TEXT_ACC = "Beschleunigungswarnung.";
	public final static String EVENT_TEXT_SPEED = "Geschwindigkeitswarnung.";
	public final static String EVENT_TEXT_FUEL = "Tankfüllstandswarnung.";
	public final static String EVENT_TEXT_COOLANT = "Kühlmittelwarnung.";
	public final static String EVENT_TEXT_GYRO = "Gyroskopwarnung.";
	public final static String EVENT_TEXT_DEFAULT = "Event eingetreten.";

	/*event types*/

	public final static String EVENT_TYPE_TURN = "Turn";
	public final static String EVENT_TYPE_ACC = "Accelerometer";
	public final static String EVENT_TYPE_SPEED = "Speed";
	public final static String EVENT_TYPE_FUEL = "Fuel";
	public final static String EVENT_TYPE_COOLANT = "Coolant";
	public final static String EVENT_TYPE_GYRO = "Gyro";

	public final static String EVENT_TYPE_TURN_GER = "Kurve";
	public final static String EVENT_TYPE_ACC_GER = "Beschleunigung";
	public final static String EVENT_TYPE_SPEED_GER = "Geschwindigkeit";
	public final static String EVENT_TYPE_FUEL_GER = "Tankfüllstand";
	public final static String EVENT_TYPE_COOLANT_GER = "Kühlmittel";
	public final static String EVENT_TYPE_GYRO_GER = "Gyroskop";
	public final static String EVENT_TYPE_DEFAULT_GER = "Event";

	/*toast texts*/

	public final static String TOAST_NOSERVER = "Server nicht verfügbar. Zum Laden erneut auf den Bildschirm drücken.";
	public final static String TOAST_RECONNECT = "Sucht Server ...";
	public final static String TOAST_LOGOUT = "Zum Ausloggen erneut drücken.";
	public final static String TOAST_WELCOME = "Willkommen bei Amber.";
	public final static String TOAST_LOGIN_USERNAME_MISSING = "Bitte Benutzername eingeben.";
	public final static String TOAST_LOGIN_PASSWORD_MISSING = "Bitte Passwort eingeben.";
	public final static String TOAST_REGISTER_SUCESS = "Registrierung erfolgreich.";
	public final static String TOAST_BACKHOME = "Zum Home-Bildschirm erneut drücken.";
	public final static String TOAST_ADDVEHICLE_DUPLICATE = "Das Fahrzeug befindet sich schon in der Liste.";
	public final static String TOAST_ADDVEHICLE_SUCCESS = "Fahrzeug hinzugefügt.";
	public final static String TOAST_ADDVEHICLE_NOINT = "Das Format der FahrzeugID ist falsch.";
	public final static String TOAST_REMOVEVEHICLE_SUCCESS = "Fahrzeug entfernt.";
	public final static String TOAST_REMOVEVEHICLE_ERROR = "Fehler beim Entfernen des Fahrzeugs.";
	public final static String TOAST_ERROR = "Es ist ein Fehler aufgetreten.";

	/*application titles*/

	public final static String APP_TITLE_AMBER = "Amber";
	public final static String APP_TITLE_LOGIN = "Login";
	public final static String APP_TITLE_REGISTER = "Registrierung";
	public final static String APP_TITLE_VEHICLELIST = "Fahrzeugliste";
	public final static String APP_TITLE_EVENTLIST = "Events von ";
	public final static String APP_TITLE_ADDVEHICLEPOPUP = "Fahrzeug hinzufügen";

	/*ProgressDialog Label*/

	public final static String PD_LOADING = "Bitte warten ...";

	/*empty lists*/

	public final static String ELIST_VEHICLE = "Bitte ein Fahrzeug hinzufügen.";
	public final static String ELIST_EVENT = "Das Fahrzeug besitzt keine Events.";

	/*eventdetail textfields*/

	public final static String TEXT_EDETAIL_DATE = "Datum: ";
	public final static String TEXT_EDETAIL_TIME = "Zeit: ";
	public final static String TEXT_EDETAIL_LONG = "Längengrad: ";
	public final static String TEXT_EDETAIL_LAT = "Breitengrad: ";


}
