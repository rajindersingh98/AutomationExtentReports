package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DFDBConnection {
	private static Connection conn = null;
	
	public static Connection getConnection(String dbName ,String url) {

		try {
			if (conn != null && !conn.isClosed()) {
				return conn;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn = makeAConnection(dbName ,url);
		return conn;

	}
	
	
	private static Connection makeAConnection(String dbName , String url){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		
		  String username = "automation";
          String password = "drfirstautomation";
                 
         
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			System.out.println("Connection successful");
			return conn;
	}

}
