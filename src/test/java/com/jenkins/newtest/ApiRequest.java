package com.jenkins.newtest;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.jenkins.newtest.CreateRequest.CreateRequestFromExcelData;
import com.jenkins.newtest.CreateRequest.Request;

import db.DFDBConnection;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


import static io.restassured.RestAssured.*;

public class ApiRequest{
	Request req;
	public ApiRequest() {
		CreateRequestFromExcelData.getInstance().setXMLXLSNamePath("add_practice");
		req = CreateRequestFromExcelData.getInstance().createRequest("Email_1","SRP");
		
	}
	
	@Test
	public void checkAPI() {
		String request = req.getRequest();
		request = request.replace("RandomExt", "Rohit_"+Math.random());
		request = request.replace("RandomName", getRandomName());
		request = request.replace("<LicenseCapCount>deleted</LicenseCapCount>","");
		request = request.replace("<PhoneExt>deleted</PhoneExt>","");
		request = request.replace("<BackPhoneExt>deleted</BackPhoneExt>","");
		HashMap h = (HashMap)req.getH();
		h.get("DBTOCONNECT");
		
				    
		System.out.println(request);
		Response res = given().contentType(ContentType.XML).accept(ContentType.XML).body(request).when().post("https://register.qa.drfirst.com/ws/wsAddPractice");
		System.out.println(res.asString());
		res.then().body("RegistrationApiResponse.RegistrationApiResponseBody.ErrorResponse.ErrorResponseList.Error.ErrorText", Matchers.is("ContactEmail Validation Failed")).and().assertThat();
		try {
			Statement st = DFDBConnection.getConnection("", "jdbc:oracle:thin:@172.16.10.220:1521:qa2main").createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		String lastName = "Last" + randomString;
		return lastName;
	}
	

}
