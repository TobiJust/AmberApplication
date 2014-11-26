package de.thwildau.model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class UserData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 465312928422935320L;

	private ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();	
	
	public ArrayList<Vehicle> getVehicles(){
		return this.vehicleList;
	}
	
}
