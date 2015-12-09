package com.vyomlabs.utils;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;

public class AuthenticationService {
	
	private final static Logger log = Logger.getLogger(AuthenticationService.class.getName());
	
	public boolean authenticate(String authCredentials) {
		if (null == authCredentials)
			return false;
		
		// header value format will be "Basic <Encoded String>" for Basic authentication.
		// Example "Basic YWRtaW46YWRtaW4="		
		final String encodedUserPassword = authCredentials.replaceFirst("Basic ", "");
		
		String usernameAndPassword = null;
		
		try {
			byte[] decodedBytes = Base64.decodeBase64(encodedUserPassword);
			usernameAndPassword = new String(decodedBytes, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			log.info("Decoding Exception : "+e);
			return false;
		}
		final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();

		// Username and password here is admin/admin
		// call some UserService/LDAP here
		boolean authenticationStatus = "EverbridgeRest".equals(username) && "EverbridgeRest".equals(password);
		return authenticationStatus;
	}
}
