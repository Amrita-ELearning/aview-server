/*
 * 
 */
package edu.amrita.aview.common.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.engine.SessionFactoryImplementor;


/**
 * The Class JDBCUtils.
 */
public class JDBCUtils 
{
	
	/**
	 * Gets the sql connection.
	 *
	 * @param session the session
	 * @return the SQL connection
	 */
	public static Connection getSQLConnection(Session session) 
	{
		Connection con = null;
		try
		{
			SessionFactoryImplementor sessionFactoryImplementation = (SessionFactoryImplementor) session.getSessionFactory();

			ConnectionProvider connectionProvider = sessionFactoryImplementation.getConnectionProvider();
			con = connectionProvider.getConnection();			
		}
		catch(Exception se)
		{
			se.printStackTrace();
		}
		return con;
	}

	/**
	 * Close everything.
	 *
	 * @param rs the rs
	 * @param stmt the stmt
	 * @param con the con
	 */
	public static void closeEverything(ResultSet rs, Statement stmt, Connection con) 
	{
		if (rs != null) 
		{
			try 
			{
				rs.close();
			}
			catch (SQLException ignore) 
			{
				ignore.printStackTrace();
			}
		}
		if (stmt != null) 
		{
			try 
			{
				stmt.close();
			}
			catch (SQLException ignore) 
			{
				ignore.printStackTrace();
			}
		}
		if (con != null) 
		{
			try 
			{
				con.close();
			}
			catch (SQLException ignore) 
			{
				ignore.printStackTrace();
			}
		}
	}

	/**
	 * Execute sql.
	 *
	 * @param sqlQuery the sql query
	 * @return the list
	 */
	public static List executeSQL(String sqlQuery)
	{
		Session session = HibernateUtils.getHibernateConnection();
		Connection con = getSQLConnection(session);
		Statement stmnt = null;
		List result = null;
		ResultSet rs = null;
		try
		{
			stmnt = con.createStatement();
			rs = stmnt.executeQuery(sqlQuery);
			result = rsToList(rs);
		}
		catch(SQLException se)
		{
			
		}
		finally
		{
			closeEverything(rs, stmnt, con);
			HibernateUtils.closeConnection(session);	
		}
		return result;
	}

	/**
	 * Rs to list.
	 *
	 * @param rs the rs
	 * @return the list
	 */
	public static List rsToList(ResultSet rs)
	{
		List list = new ArrayList();
		HashMap row = null;
		try
		{
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			
			while (rs.next())
			{
				row = new HashMap();
				for (int i = 1; i <= colCount; i++)
				{
					row.put(rsmd.getColumnName(i), rs.getString(i));
				}
				list.add(row);
			}
		}
		catch(SQLException se)
		{
			
		}
		return list;
	}

}
