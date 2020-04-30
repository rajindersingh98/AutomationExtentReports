package com.jenkins.newtest;

import java.util.HashMap;
import java.util.Map;

public class Utils {
	public Map getXmlVerificationMap(String[] xmlVerification) {
		Map h = new HashMap();
		for(int index =0;index < xmlVerification.length;index++) {
			String [] keyvalue = xmlVerification[index].split("=");
			h.put(keyvalue[0], keyvalue[1]);
		}
		return h;
	}

}
