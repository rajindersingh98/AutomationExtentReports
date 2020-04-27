package com.jenkins.newtest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jenkins.newtest.CreateRequest.CreateRequestFromExcelData;
import com.jenkins.newtest.CreateRequest.Request;

import db.DBVerification;
import db.DFDBConnection;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;


import static io.restassured.RestAssured.*;

public class ApiRequest{
	Request req;
	String randomName = null;
	public ApiRequest() {
		CreateRequestFromExcelData.getInstance().setXMLXLSNamePath("add_practice");
		req = CreateRequestFromExcelData.getInstance().createRequest("Email_1","SRP");
		
	}
	
	@Test
	public void checkAPI() {
		String request = req.getRequest();
		request = request.replace("RandomExt", "Rohit_"+Math.random());
		randomName = getRandomName();
		request = request.replace("RandomName", randomName);
		request = request.replace("<LicenseCapCount>deleted</LicenseCapCount>","");
		request = request.replace("<PhoneExt>deleted</PhoneExt>","");
		request = request.replace("<BackPhoneExt>deleted</BackPhoneExt>","");
				    
		System.out.println(request);
		Response res = given().contentType(ContentType.XML).accept(ContentType.XML).body(request).when().post("https://register.qa.drfirst.com/ws/wsAddPractice");
		System.out.println(res.prettyPrint().toString());
		System.out.println("username:::::::::::::::"+XmlPath.from(res.asString()).get("RegistrationApiResponse.RegistrationApiResponseBody.WsAddPracticeResponse.Practice.PracticeIDSet.RcopiaUserName"));
	   
		//res.then().body("RegistrationApiResponse.RegistrationApiResponseBody.ErrorResponse.ErrorResponseList.Error.ErrorText", Matchers.is("ContactEmail Validation Failed"));
		HashMap h = (HashMap)req.getH();
		String finalQuery = null;
		DBVerification d = new DBVerification();
		Map p = d.getDBAndQueryDetails(h, res);
		Iterator entries = p.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry thisEntry = (Map.Entry) entries.next();
			String key = thisEntry.getKey().toString();
			ArrayList<String> value = (ArrayList)thisEntry.getValue();
			try {
				ResultSet r = d.getResultSet(value.get(0), value.get(1) ,value.get(2));
				SoftAssert softAssert = new SoftAssert();
				 while (r.next()) {
					  String verificationArray []  = value.get(2).split(":");
					  for(int verificationArrayIndex = 0 ;verificationArrayIndex <verificationArray.length ;verificationArrayIndex++) {
						 String resulsetVerificationArray [] = verificationArray[verificationArrayIndex].split("=");
						 String resultsetColumn = resulsetVerificationArray[0];
						 String valuetoAssert = h.get(resulsetVerificationArray[1]).toString();
					/*	 if("ContactEmail7".equalsIgnoreCase(resulsetVerificationArray[1].toString())) {
							 valuetoAssert= "hdhdh@jjdjd.com";
						 }*/
						 if("RandomName".equals(valuetoAssert)) {
							 valuetoAssert = randomName;
						 }
						 String resultSetColumnAndType[] = resultsetColumn.split(" TYPE ");
						 if("STRING".equals(resultSetColumnAndType[1])) {
						 softAssert.assertEquals(r.getString(resultSetColumnAndType[0]), valuetoAssert , "values not equal +"+r.getString(resultSetColumnAndType[0])+"   "+valuetoAssert + "\n");
						 }
						 else if("INT".equals(resultSetColumnAndType[1])) {
							 softAssert.assertEquals(String.valueOf(r.getInt(resultSetColumnAndType[0])), valuetoAssert , "values not equal +"+r.getString(resultSetColumnAndType[0])+"   "+valuetoAssert + "\n");
							 }
					  }
					
					  } 
				  softAssert.assertAll();
			}
			catch(Exception e) {
				
			}
			 finally {

	             DFDBConnection.closeConnection();
	       }
			
		}
	}
	
	public String getRandomName(){
		String letter[] =
		{ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
		Date date = new Date();
		String timeString = String.valueOf(date.getTime());
		String randomString = "";
		for (int index = 0; index < timeString.length(); index++)
		{
			randomString = randomString
					+ letter[Integer.parseInt(timeString.substring(index,
							index + 1))];
		}
		String lastName =  randomString;
		return lastName;
	}
	

}
