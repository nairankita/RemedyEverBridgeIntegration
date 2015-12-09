package com.eb.incident.plugins;

import java.util.ArrayList;
import java.util.List;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.Value;
import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.eb.incident.controllers.EbIncidentClose;

public class EbIncidentClosePlugin extends ARFilterAPIPlugin {

	public static void main(String[] args) {
	}
	
	private static int EXPECTED_ARG_NUMBER = 5;

	public void initialize(ARPluginContext paramARPluginContext) throws ARException {
		super.initialize(paramARPluginContext);
	}
	
	public List<Value> filterAPICall(ARPluginContext paramARPluginContext, List<Value> paramList) throws ARException {
		ArrayList<Value> returnList = new ArrayList<Value>();
		
		System.out.println("Start EbIncidentClosePlugin");			
		System.out.println("EXPECTED_ARG_NUMBER : " + paramList.size());
		
		if (paramList.size() != EXPECTED_ARG_NUMBER) {
			String str1 = "Invalid argument when calling the plugin. Expected: " + EXPECTED_ARG_NUMBER + " Actual: "+ paramList.size();
			String str2 = str1 + "\n";
			for (int i = 0; i < paramList.size(); i++) {
				str2 = str2 + "arg" + i + ": " + ((Value) paramList.get(i)).getValue() + "\n";
			}
			
			System.out.println("ERROR : "+str2);		
			throw new ARException(0, 0, str1);
		}	
		
		EbIncidentClose ebIncidentClose = EbIncidentClose.getInstance();
		returnList = ebIncidentClose.closeIncidentInEB(paramList);
		
		System.out.println("End EbIncidentClosePlugin");
		
		return returnList;
	}

}
