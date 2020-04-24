package com.jenkins.newtest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		String dbVerification[] = h.get("DBVERIFICATION").toString().split(":VERIFICATIONSSEPERATOR:");
		for(int dbVerificationIndex = 0 ;dbVerificationIndex < dbVerification.length ; dbVerificationIndex++) {
			String verificationDetails[] = dbVerification[dbVerificationIndex].split(":");
				String dbName = verificationDetails[0].toString();
				String queryColumnValue = h.get(verificationDetails[1]).toString();
				String verificationColumnValue = h.get(verificationDetails[2]).toString();
				String queryParameterExists = h.get(verificationDetails[1]+"PARAMETERSEXIST").toString();
				String [] parametersAndValueToReplace = null;
				if("y".equalsIgnoreCase(queryParameterExists)){
					parametersAndValueToReplace =  h.get(verificationDetails[1]+"PARAMETERS").toString().split(":PARAMETER:");
				
				for(int parametersAndValueToReplaceIndex = 0 ;parametersAndValueToReplaceIndex < parametersAndValueToReplace.length ; parametersAndValueToReplaceIndex++) {
					String parametersAndValueToReplaceArray [] = parametersAndValueToReplace[parametersAndValueToReplaceIndex].split("=");
					String parameter = parametersAndValueToReplaceArray[0];
					String xmlPath = parametersAndValueToReplaceArray[1];
					String value = XmlPath.from(res.asString()).get(xmlPath);
					queryColumnValue = queryColumnValue.replace(parameter, value)	;
					
				}
				}
				//jjjjj
			//	System.out.println("dbName::::"+dbName);
				System.out.println("QueryColumn::::"+dbVerificationIndex+"   "+queryColumnValue);
				System.out.println("verificationColumn::::"+verificationColumnValue);
				System.out.println(finalQuery);
				Statement st = DFDBConnection.getStatement(dbName);
				SoftAssert softAssert = new SoftAssert();
				try {
					  ResultSet rs = st.executeQuery(queryColumnValue);
					  while (rs.next()) {
					  String verificationArray []  = verificationColumnValue.split(":");
					  for(int verificationArrayIndex = 0 ;verificationArrayIndex <verificationArray.length ;verificationArrayIndex++) {
						 String resulsetVerificationArray [] = verificationArray[verificationArrayIndex].split("=");
						 String resultsetColumn = resulsetVerificationArray[0];
						 String valuetoAssert = h.get(resulsetVerificationArray[1]).toString();
						 if("RandomName".equals(valuetoAssert)) {
							 valuetoAssert = randomName;
						 }
						 String resultSetColumnAndType[] = resultsetColumn.split(" TYPE ");
						 if("STRING".equals(resultSetColumnAndType[1])) {
						 softAssert.assertEquals(rs.getString(resultSetColumnAndType[0]), valuetoAssert , "values not equal +"+rs.getString(resultSetColumnAndType[0])+"   "+valuetoAssert + "\n");
						 }
						 else if("INT".equals(resultSetColumnAndType[1])) {
							 softAssert.assertEquals(String.valueOf(rs.getInt(resultSetColumnAndType[0])), valuetoAssert , "values not equal +"+rs.getString(resultSetColumnAndType[0])+"   "+valuetoAssert + "\n");
							 }
					  }
					  softAssert.assertAll();
					  }  
					  
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		String lastName = "Last" + randomString;
		return lastName;
	}
	

}
