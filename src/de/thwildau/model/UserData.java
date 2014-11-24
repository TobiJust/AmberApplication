package de.thwildau.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UserData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 465312928422935320L;

	private ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();	
	private ArrayList<String> vehicleNames = new ArrayList<String>();
	
	public ArrayList<Vehicle> getVehicles(){
		return this.vehicleList;
	}
	
	public ArrayList<String> getVehicleNames(){
		for(Vehicle v: vehicleList){
			vehicleNames.add(v.getName());
		}
		return this.vehicleNames;
	}
	
	
	
}
