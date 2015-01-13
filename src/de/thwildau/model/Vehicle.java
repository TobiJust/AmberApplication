package de.thwildau.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is the basis for the users vehicles. It contains attributes to
 * specify the vehicles with particular data. It contains methods to set and 
 * get the attributes. The class is using the serializable interface to can be 
 * interchanged with the server.
 * @author Kulla, Just
 * @version 1.0
 * @since 2015-01-12
 * @see Serializable
 */
public class Vehicle implements Serializable{

	public int vehicleID;
	public String vehicleName;
	public String date;
	public byte[] image;
	public boolean boxed;
	private static final long serialVersionUID = -1208507633758359464L;
	public ArrayList<Event> eventList;

	/**
	 * Constructor to initialize.
	 */
	public Vehicle(){
		eventList = new ArrayList<Event>();
	}    

	/**
	 * Constructor to create vehicle object.
	 * @param name The name of the vehicle.
	 * @param date The date at which the vehicle was added to the list.
	 * @param image The vehicle brand icon.
	 * @param boxed The state of the users notification wish.
	 */
	public Vehicle(String name, String date, byte[] image, boolean boxed) {
		this.vehicleName = name;
		this.date = date;
		this.image = image;
		this.boxed = boxed;
	}

	/**
	 * To set the name of the vehicle.
	 * @param name The name of the vehicle.
	 */
	public void setName(String name){
		this.vehicleName = name;
	}

	/**
	 * To set the date on which the vehicle was added to the list.
	 * @param date The date on which the vehicle was added to the list.
	 */
	public void setDate(String date){
		this.date = date;
	}

	/**
	 * To set the vehicle image icon.
	 * @param image The vehicle brand icon.
	 */
	public void setImage(byte[] image){
		this.image = image;
	}

	/**
	 * To set the state for the vehicle notification alarm.
	 * @param boxed The state of the users notification wish.
	 */
	public void setBoxed(boolean boxed){
		this.boxed = boxed;
	}

	/**
	 * To set the list of events for a vehicle.
	 * @param eventList List of events for a vehicle.
	 */
	public void setEventList(ArrayList<Event> eventList){
		this.eventList = eventList;
	}   

	/**
	 * To get the id of the vehicle.
	 * @return int The ID of the vehicle.
	 */
	public int getID(){
		return this.vehicleID;
	}

	/**
	 * To get the name of the vehicle.
	 * @return String The name of the vehicle.
	 */
	public String getName(){
		return this.vehicleName;
	}

	/**
	 * To get the date on which the vehicle was added to the list.
	 * @return String The date on which the vehicle was added to the list.
	 */
	public String getDate(){
		return this.date;
	}

	/**
	 * To get the vehicle image.
	 * @return byte[] The vehicle brand icon as byte array.
	 */
	public byte[] getImage(){
		return this.image;
	}

	/**
	 * To get the state of the notification alarm for a verhicle.
	 * @return boolean The state of the users notification wish.
	 */
	public boolean getBoxed(){
		return this.boxed;
	}

	/**
	 * To get list of events from a vehicle.
	 * @return ArrayList The list of events from a vehicle.
	 */
	public ArrayList<Event> getEventList(){
		return this.eventList;
	}

}
