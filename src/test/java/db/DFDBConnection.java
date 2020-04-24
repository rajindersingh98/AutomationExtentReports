package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DFDBConnection {
	private static Connection conn = null;
	private static Statement stmt = null;
	
	public static Connection getConnection(String dbName) {

		try {
			if (conn != null && !conn.isClosed()) {
				return conn;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn = makeAConnection(dbName);
		return conn;

	}
	
	public static void closeConnection() {

		if (conn != null) {
			try {
				conn.close();
				System.out.println("Connection closed.");
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(stmt !=null){
			try {
				stmt.close();
				System.out.println("Connection stmt closed.");
				stmt = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}

	}
	
	
	private static Connection makeAConnection(String dbName){
		try {
			String url = null;
			Class.forName("oracle.jdbc.driver.OracleDriver");
		
		  String username = "automation";
          String password = "drfirstautomation";
          if(dbName.equals("QA2MAIN")) {
        	 url = "jdbc:oracle:thin:@172.16.10.220:1521:qa2main"; 
          }
                 
         
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			System.out.println("Connection successful");
			return conn;
	}
	
	public static Statement getStatement(String dbName) {
		if (stmt != null) {
			return stmt;
		}
		try {
			stmt = DFDBConnection.getConnection(dbName)
			.createStatement();
			return stmt; 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

}
