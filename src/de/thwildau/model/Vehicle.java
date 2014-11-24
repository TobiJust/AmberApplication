package de.thwildau.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Vehicle implements Serializable{

	public String vehicleName;
	public String date;
	public int image;
	public boolean boxed;
    private static final long serialVersionUID = -1208507633758359464L;
    private ArrayList<Event> eventList;
	
	public Vehicle(){
		eventList = new ArrayList<Event>();
	}    
	
	public Vehicle(String name, String date, int image, boolean boxed) {
    	this.vehicleName = name;
    	this.date = date;
    	this.image = image;
    	this.boxed = boxed;
    }

	public void setName(String name){
    	this.vehicleName = name;
    }
    
    public void setDate(String date){
    	this.date = date;
    }
    
    public void setImage(int image){
    	this.image = image;
    }
    
    public void setBoxed(boolean boxed){
    	this.boxed = boxed;
    }
    
    public String getName(){
    	return this.vehicleName;
    }
    
    public String getDate(){
    	return this.date;
    }
    
    public int getImage(){
    	return this.image;
    }
    
    public boolean getBoxed(){
    	return this.boxed;
    }
	
	public ArrayList<Event> getEventList(){
		return this.eventList;
	}

}
