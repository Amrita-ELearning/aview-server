/*
 * 
 */
package edu.amrita.aview.common;
/*    */ import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

/*    */ 
/*    */ /**
 * The Class ConnectionHelperWithPwd.
 */
public class ConnectionHelperWithPwd
/*    */ {
/*    */   /** The instance. */
private static ConnectionHelperWithPwd instance;
/*    */ 
/*    */   /**
 * The Constructor.
 */
private ConnectionHelperWithPwd()
/*    */   {
/*    */     try
/*    */     {
/* 18 */       Class.forName("com.mysql.jdbc.Driver");
/*    */     }
/*    */     catch (ClassNotFoundException e)
/*    */     {
/* 22 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   /**
 * Gets the instance.
 *
 * @return the instance
 */
public static ConnectionHelperWithPwd getInstance()
/*    */   {
/* 28 */     if (instance == null)
/*    */     {
/* 30 */       instance = new ConnectionHelperWithPwd();
/*    */     }
/* 32 */     return instance;
/*    */   }
/*    */ 
/*    */   /**
 * Gets the connection.
 *
 * @param databaseName the database name
 * @param databaseIP the database ip
 * @return the connection
 * @throws Exception the exception
 */
public Connection getConnection(String databaseName, String databaseIP)
/*    */     throws Exception
/*    */   {
				StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
				Properties properties = new EncryptableProperties(encryptor);
				try
				{
					properties.load(this.getClass().getClassLoader().getResourceAsStream("mysql.properties"));
				}
				catch (FileNotFoundException fnfe)
				{
					fnfe.printStackTrace();
					throw fnfe;
				}
				catch (NullPointerException fnfe)
				{
					fnfe.printStackTrace();
					throw fnfe;
				}
			   	String username = properties.getProperty("mysql.user");
				encryptor.setPassword(username); // could be got from web, env variable...
			   	String passwd = properties.getProperty("mysql.password");
			   	String port = properties.getProperty("mysql.port");
			   	databaseIP = properties.getProperty("mysql.databaseIP", databaseIP);
			   	databaseName = properties.getProperty("mysql.databaseName", databaseName);
/*    */ 
			try
			{
/* 49 */       Connection connection = DriverManager.getConnection("jdbc:mysql://" + databaseIP + ":" + port + "/" + databaseName, username, passwd);
/* 50 */       return connection;
/*    */     }
/*    */     catch (SQLException e)
/*    */     {
/* 59 */       e.printStackTrace();
/* 60 */	   throw e;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\workspace\aview_classroom_server\Setup\Files\LCDS\aview_classroom\classes\
 * Qualified Name:     ConnectionHelperWithPwd
 * JD-Core Version:    0.6.0
 */