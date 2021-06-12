package org.warn.misc.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DBUtils.class);

	public static synchronized Connection getConnection(String jndiFile) throws SQLException, NamingException {
		Connection con = null;
		try {
			Context initCtx = new InitialContext();
			DataSource ds = (DataSource) initCtx.lookup("java:" + jndiFile);
			con = ds.getConnection();
		} catch( SQLException | NamingException e ) {
			LOGGER.error("Error while openning database connection", e);
			throw e;
		}
		return con;
	}
	
	/**
	 * Tries to establish a connection to the provided database and returns a {@link Connection} object 
	 * 
	 * @param connectionUrl String with connection URL. Eg: jdbc:oracle:thin:@100.100.100.100:1521:db10g or jdbc:hsqldb:mem:public or jdbc:postgresql://localhost/db1
	 * @param username
	 * @param password 
	 * @param classDriver Database driver class name. Eg: oracle.jdbc.driver.OracleDriver, org.hsqldb.jdbcDriver, org.postgresql.Driver, etc.
	 * @return
	 * @throws Exception
	 */
	public static synchronized Connection getConnection( String connectionUrl, String username, String password, String classDriver ) 
			throws SQLException, ClassNotFoundException {
		Connection con = null;
		try {
			Class.forName(classDriver);
			con = DriverManager.getConnection( connectionUrl, username, password );
		} catch( SQLException | ClassNotFoundException e ) {
			LOGGER.error("Error while openning database connection", e);
			throw e;
		}
		return con;
	}

	public ResultSet search(Connection con, String strQuery) {
		ResultSet rs = null;
		try {
			PreparedStatement pstmt = con.prepareStatement(strQuery);
			rs = pstmt.executeQuery();
			pstmt.close();
			/*
			SQLWarning warning = stmt.getWarnings();
			if( warning != null ) {
				while (warning != null) { 
					LOGGER.info("Message: " + warning.getMessage()); 
					LOGGER.info("SQLState: " + warning.getSQLState());
					LOGGER.info( "Vendor error code: " + warning.getErrorCode()); warning =
					warning.getNextWarning();
				}
			}
			*/
		} catch( Exception e ) {
			LOGGER.error("Error while querying database", e);
		}
		return rs;
	}

	public ResultSet searchWithStringParameters( Connection con, String strQuery, ArrayList<String> parameters ) {
		ResultSet rs = null;
		try {
			PreparedStatement pstmt = con.prepareStatement(strQuery);
			for( int i = 0; i < parameters.size(); i++ ) {
				String param = (String) parameters.get(i);
				pstmt.setString(i + 1, param);
			}
			rs = pstmt.executeQuery();
			pstmt.close();
		} catch( Exception e ) {
			LOGGER.error("Error while querying database", e);
		}
		return rs;
	}

}