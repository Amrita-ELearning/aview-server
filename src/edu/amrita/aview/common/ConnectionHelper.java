/*
 * 
 */
package edu.amrita.aview.common;
/*    */ import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*    */ 
/*    */ /**
 * The Class ConnectionHelper.
 */
public class ConnectionHelper
/*    */ {
/*    */   /** The instance. */
private static ConnectionHelper instance;
/*    */ 
/*    */   /**
 * The Constructor.
 */
private ConnectionHelper()
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
public static ConnectionHelper getInstance()
/*    */   {
/* 28 */     if (instance == null)
/*    */     {
/* 30 */       instance = new ConnectionHelper();
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
/*    */     try
/*    */     {
/* 50 */       Connection connection = DriverManager.getConnection("jdbc:mysql://"+databaseIP+":3306/" + databaseName, "root", "amma");
/*    */ 
/* 52 */       return connection;
/*    */     }
/*    */     catch (SQLException e)
/*    */     {
/* 61 */       e.printStackTrace();
/* 62 */	   throw e;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\workspace\aview_classroom_server\Setup\Files\LCDS\aview_classroom\classes\
 * Qualified Name:     ConnectionHelper
 * JD-Core Version:    0.6.0
 */