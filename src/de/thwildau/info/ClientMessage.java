package de.thwildau.info;

import java.io.Serializable;

/**
 * This class defines the client message which will be interchanged with the server.
 * The client message objects are serializable.
 * @author Kulla, Just
 * @version 1.0
 * @since 2015-01-12
 */
public class ClientMessage implements Serializable{
	
	private static final long serialVersionUID = 5241215198936208524L;

	public static enum Ident {
		EVENT_REQUEST, LOGIN, ERROR, REGISTER, LOGIN_CHECK, 
		REGISTER_VEHICLE, UNREGISTER_VEHICLE, TOGGLE_ALARM, 
		GET_EVENTLIST, GET_EVENTLIST_BACKPRESS, 
		GET_VEHICLELIST_BACKPRESS, EVENT_DETAIL, LOGOUT
	}
	
	private Ident id;
	private Object content;

	/**
	 * The constructor initializes the client message.
	 * @param id The specified type of the client message.
	 * @param content The content data of the client message.
	 */
	public ClientMessage(Ident id, Object content){
		this.setId(id);
		this.setContent(content);
	}

	/**
	 * The method returns the ident of the client message.
	 * @return Ident The specified type of the client message.
	 */
	public Ident getId() {
		return id;
	}

	/**
	 * The method sets the ident of the client message.
	 * @param id The specified type of the client message.
	 */
	public void setId(Ident id) {
		this.id = id;
	}

	/**
	 * The method returns the content data of the client message.
	 * @return Object The content data of the client message.
	 */
	public Object getContent() {
		return content;
	}

	/**
	 * The method sets the content data of the client message.
	 * @param content The content data of the client message.
	 */
	public void setContent(Object content) {
		this.content = content;
	}
	
}