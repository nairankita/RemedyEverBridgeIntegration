package com.vyomlabs.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vyomlabs.model.NotificationDelivery;
import com.vyomlabs.model.RestPostResponse;
import com.vyomlabs.services.NotificationDeliveryService;

public class NotificationDeliveryREST extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger log = Logger.getLogger(NotificationDeliveryREST.class.getName());
	NotificationDeliveryService nds_instance = NotificationDeliveryService.getInstance();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("Inside POST request @ "+new Date());
		log.info("Request URI : "+req.getRequestURI());
		
		StringBuffer jb = new StringBuffer();
		String line = null;
		  try {
		    BufferedReader reader = req.getReader();
		    while ((line = reader.readLine().trim()) != null)
		      jb.append(line);
		    reader.close();
		  } catch (Exception e) { /*report an error*/ }
		  String jsonInput = jb.toString();
		  log.info("Body : "+jsonInput);
		  		  
		  GsonBuilder builder = new GsonBuilder();
		  Gson gson = builder.create();
		  NotificationDelivery nd = gson.fromJson(jsonInput,NotificationDelivery.class);
		  if(nd == null) {
			  log.info("NotificationDelivery NULL");
		  } else {		  
			  resp.setContentType("application/json");
			  RestPostResponse restPostresponse =nds_instance.createNotifDelivery(nd);
				log.info("RestPostResponse : \n"+restPostresponse.toString());
			     PrintWriter out = resp.getWriter();
			     out.println(gson.toJson(restPostresponse));		     
				
				if(restPostresponse.getStatus().equals("Success")) {
					log.info( "POST request successfully completed.");
					resp.setStatus(200);
				}
				else {
					log.info( "POST request failed.");
					resp.setStatus(400);
				}
		  }
		}
}
