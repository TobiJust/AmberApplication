package de.thwildau.model;

import java.io.Serializable;

/**
 * This class is the basis for happening events. It contains attributes to
 * specify events with particular data. It contains methods to set and get the
 * attributes. The class is using the serializable interface to can be 
 * interchanged with the server.
 * @author Kulla, Just
 * @version 1.0
 * @since 2015-01-12
 * @see Serializable
 */
public class Event implements Serializable {

	private static final long serialVersionUID = 8679932140958446049L;

	private static final byte LAT    = 0x07;
	private static final byte LON    = 0x01;
	private static final byte TIME    = 0x02;
	private static final byte SPEED   = 0x03;
	private static final byte RPM    = 0x04;
	private static final byte COOL_TEMP  = 0x05;
	private static final byte FUEL    = 0x06;
	private static final byte ACC    = 0x00;
	private static final byte GYRO = 0x01;

	private int id;
	private String eventType;
	private String timeStamp;
	private int vehicleID;
	private double lat;
	private double lon;
	private byte[] eventImage;
	private String vehicleName;
	private String eventMessage;
	private int eventIdentifier;

	/**
	 * Constructor
	 */
	public Event(){	
	}

	/**
	 * Constructor to initialize an event.
	 * @param eventType The type of the event.
	 * @param timeStamp The timestamp of the event.
	 * @param lat The latitude of the event.
	 * @param lon The longitude of the event.
	 * @param image The image of the event.
	 */
	public Event(String eventType, String timeStamp, double lat, double lon, byte[] image){
		this.eventType = eventType;
		this.timeStamp = timeStamp;
		this.lat = lat;
		this.lon = lon;
		this.eventImage = image;
	}

	/**
	 * To set the id of the event.
	 * @param id The id of the event.
	 */
	public void setID(int id){
		this.id = id;
	}

	/**
	 * To set the vehicle id of the event.
	 * @param id The vehicle id of the event.
	 */
	public void setVehicleID(int id){
		this.vehicleID = id;
	}

	/**
	 * To set the latitude of the event.
	 * @param lat The latitude of the event.
	 */
	public void setLatitude(double lat){
		this.lat = lat;
	}

	/**
	 * To set the longitude of the event.
	 * @param lon The longitude of the event.
	 */
	public void setLongitude(double lon){
		this.lon = lon;
	}

	/**
	 * To set the message of the event.
	 * @param message The message of the event.
	 */
	public void setEventMessage(String message){
		this.eventMessage = message;
	}

	/**
	 * To get the id of the event.
	 * @return int The id of the event.
	 */
	public int getID(){
		return this.id;
	}

	/**
	 * To get the name of the event.
	 * @return String The name of the event.
	 */
	public String getName(){
		return this.vehicleName;
	}

	/**
	 * To get the id of the vehicle.
	 * @return int The id of the vehicle.
	 */
	public int getVehicleID(){
		return this.vehicleID;
	}

	/**
	 * To get the latitude of the event.
	 * @return double The latitude of the event.
	 */
	public double getLatitude(){
		return this.lat;
	}

	/**
	 * To get the longitude of the event.
	 * @return double The longitude of the event.
	 */
	public double getLongitude(){
		return this.lon;
	}

	/**
	 * To get the timestamp of the event.
	 * @return String The timestamp of the event.
	 */
	public String getTimeStamp(){
		return this.timeStamp;
	}

	/**
	 * To get the image of the event.
	 * @return byte[] The image as byte array of the event.
	 */
	public byte[] getImage(){
		return this.eventImage;
	}

	/**
	 * To get the type of the event.
	 * @return String The type of the event.
	 */
	public String getType(){
		return this.eventType;
	}

	/**
	 * To get the name of the vehicle of the event.
	 * @return String The name of the vehicle of the event.
	 */
	public String getVehicleName(){
		return this.vehicleName;
	}

	/**
	 * To get the message of the event.
	 * @return String The message of the event.
	 */
	public String getEventMessage(){
		return this.eventMessage;
	}

	/**
	 * To get the identifier of the event.
	 * @return int The identifier of the event.
	 */
	public int getEventIdentifier(){
		return this.eventIdentifier;
	}
}