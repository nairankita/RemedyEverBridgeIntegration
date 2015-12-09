package com.eb.utils;

import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;

public class Authenticator {
	
	private String username;
	private String password;
	
	public Authenticator(String l_username, String l_password) {
		this.username = l_username;
		this.password = l_password;
	}
	
	public String getEncodedCredentials() {
		String token = this.username + ":" + this.password;
        try {
            return "Basic " + DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Cannot encode with UTF-8", ex);
        }
	}

}
