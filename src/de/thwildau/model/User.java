package de.thwildau.model;

import java.io.Serializable;

/**
 * This class is the basis for user objects. It contains attributes to
 * specify users with particular data. It contains methods to set and get the
 * attributes. The class is using the serializable interface to can be 
 * interchanged with the server.
 * @author Kulla, Just
 * @version 1.0
 * @since 2015-01-12
 * @see Serializable
 */
public class User implements Serializable{

	private static final long serialVersionUID = -3989078864083454472L;
	private String userName;
	private byte[] pass;
	private String regID;

	/**
	 * Constructor to initialize a user.
	 * @param userName The name of the user.
	 * @param pass The hashed password as byte array.
	 */
	public User(String userName, byte[] pass) {
		super();
		this.userName = userName;
		this.pass = pass;
	}

	/**
	 * To get the name of the user.
	 * @return String The name of the user.
	 */
	public String getName() {		
		return this.userName;
	}

	/**
	 * To get the hashed password of the user.
	 * @return byte[] The hashed password of the user.
	 */
	public byte[] getPass() {
		return this.pass;

	}

	/**
	 * Method to set registration id.
	 * @param regID Registration id of the user.
	 */
	public void setRegistrationID(String regID){
		this.regID = regID;
	}

}
