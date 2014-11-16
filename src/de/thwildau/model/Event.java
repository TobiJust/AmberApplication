package de.thwildau.model;

import java.io.Serializable;
import java.util.Date;

import android.media.Image;

public class Event implements Serializable{

	private static final long serialVersionUID = 8679932140958446049L;

	public static enum Type {
		TURN, ACCIDENT, ERROR
	}

	private Date timeStamp;
	private String vehicleID;
	private double lat;
	private double lon;
	private Image eventImage;

	public Image getEventImage() {
		return eventImage;
	}


}