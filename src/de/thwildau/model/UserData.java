package de.thwildau.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is the basis for the user data object. It contains the user id and 
 * list of vehicles of an user. The class is using the serializable interface to 
 * can be interchanged with the server.
 * @author Kulla, Just
 * @version 1.0
 * @since 2015-01-12
 * @see Serializable
 */
public class UserData implements Serializable{

	private static final long serialVersionUID = 465312928422935320L;

	private ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();	
	private int userID;

	/**
	 * To get the vehicle objects of the user.
	 * @return ArrayList The list of vehicle objects of the user.
	 */
	public ArrayList<Vehicle> getVehicles(){
		return this.vehicleList;
	}

	/**
	 * To get the user id.
	 * @return int The id of the user.
	 */
	public int getUserID() {
		return this.userID;
	}

}
