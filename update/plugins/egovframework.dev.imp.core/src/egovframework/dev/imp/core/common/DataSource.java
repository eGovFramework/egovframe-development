package egovframework.dev.imp.core.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
	
	private static String errorMessage = null;
	
	public boolean isConnect(String url, String userName, String password,
			String driverClassName) {

		boolean isConnect = false;
		Connection conn = null;
		try {
			Class.forName(driverClassName);
			conn = (Connection) DriverManager.getConnection(url, userName,
					password);
		} catch (ClassNotFoundException e) {
			errorMessage = "Driver does not exist.";
			CoreLog.logError(e.getMessage(), e);
		} catch (SQLException e) {
			errorMessage = e.getMessage();
			CoreLog.logError(e.getMessage(), e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
					isConnect = true;
				} catch (SQLException e) {
					CoreLog.logError(e);
					isConnect = false;
				}
			} else {
				
				setErrorMessage(errorMessage);
				isConnect = false;
			}
		}
		return isConnect;
	}
	
	public Connection getConnection(String url, String userName,
			String password, String driverClassName) {

		Connection conn = null;
		try {
			Class.forName(driverClassName);
			conn = (Connection) DriverManager.getConnection(url, userName,
					password);
			
		} catch (ClassNotFoundException e) {
			CoreLog.logError(e);
		} catch (SQLException e) {
			CoreLog.logError(e);
		}
		return conn;
	}
	
	/**
	 * @return the errorMessage
	 */
	public static String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		DataSource.errorMessage = errorMessage;
	}

	
}