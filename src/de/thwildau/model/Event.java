package de.thwildau.model;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Event implements Serializable {
	private static final long serialVersionUID = 8679932140958446049L;

	private String eventType;
	private String timeStamp;
	private String vehicleID;
	private double lat;
	private double lon;
	private byte[] eventImage;

	public Event(){	
	}
	
	public Event(String eventType, String timeStamp, double lat, double lon, byte[] image){
		this.eventType = eventType;
		this.timeStamp = timeStamp;
		//this.vehicleID = vehicleID;
		this.lat = lat;
		this.lon = lon;
		this.eventImage = image;
	}
	
	public void setVehicleID(String id){
		this.vehicleID = id;
	}

	public void setLatitude(double lat){
		this.lat = lat;
	}

	public void setLongitude(double lon){
		this.lon = lon;
	}

	public String getVehicleID(){
		return this.vehicleID;
	}

	public double getLatitude(){
		return this.lat;
	}

	public double getLongitude(){
		return this.lon;
	}

	public String getTimeStamp(){
		return this.timeStamp;
	}

	public byte[] getImage(){
		return this.eventImage;
	}
	
	public String getType(){
		return this.eventType;
	}


}