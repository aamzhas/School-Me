package com.aamir.schoolme;

/**
 * Created by Aamir on 25/02/17.
 */

public class User {

	private String email;
	private String username;
	private String name;
	private String id;

	public User() {

	}

	public User( String email, String username, String name, String id ) {
		this.email = email;
		this.username = username;
		this.name = name;
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}
}
