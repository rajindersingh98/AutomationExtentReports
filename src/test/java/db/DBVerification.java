package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

public class DBVerification {

	public Map getDBAndQueryDetails( Map h, Response res) {
		Map dbVerifications = new HashMap();
		ArrayList<String> queryDetails= null;

		String dbVerification[] = h.get("DBVERIFICATION").toString().split(":VERIFICATIONSSEPERATOR:");
		for (int dbVerificationIndex = 0; dbVerificationIndex < dbVerification.length; dbVerificationIndex++) {
			String verificationDetails[] = dbVerification[dbVerificationIndex].split(":");
			String dbName = verificationDetails[0].toString();
			String queryColumnValue = h.get(verificationDetails[1]).toString();
			String verificationColumnValue = h.get(verificationDetails[2]).toString();
			String queryParameterExists = h.get(verificationDetails[1] + "PARAMETERSEXIST").toString();
			String[] parametersAndValueToReplace = null;
			
			if ("y".equalsIgnoreCase(queryParameterExists)) {
				parametersAndValueToReplace = h.get(verificationDetails[1] + "PARAMETERS").toString()
						.split(":PARAMETER:");

				for (int parametersAndValueToReplaceIndex = 0; parametersAndValueToReplaceIndex < parametersAndValueToReplace.length; parametersAndValueToReplaceIndex++) {
					String parametersAndValueToReplaceArray[] = parametersAndValueToReplace[parametersAndValueToReplaceIndex]
							.split("=");
					String parameter = parametersAndValueToReplaceArray[0];
					String xmlPath = parametersAndValueToReplaceArray[1];
					String value = XmlPath.from(res.asString()).get(xmlPath);
					queryColumnValue = queryColumnValue.replace(parameter, value);

				}
			}
			// jjjjj
			// System.out.println("dbName::::"+dbName);
			System.out.println("QueryColumn::::" + dbVerificationIndex + "   " + queryColumnValue);
			System.out.println("verificationColumn::::" + verificationColumnValue);
			queryDetails = new ArrayList<>();
			queryDetails.add(dbName);
			queryDetails.add(queryColumnValue);
			queryDetails.add(verificationColumnValue);
			dbVerifications.put(dbVerificationIndex, queryDetails);
		}
		return dbVerifications;
	}
	
	public ResultSet getResultSet(String dbName , String query, String columnsToGetValue) {
		Statement st = DFDBConnection.getStatement(dbName);
		ResultSet rs = null;
		try {
			rs = st.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
}
