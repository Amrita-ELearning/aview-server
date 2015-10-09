/*
 * 
 */
package edu.amrita.aview.common;
 import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
				
 
/**
  * The Class MySQLAdmin.
  */
 public class MySQLAdmin
 {
   
   /** The days of week. */
   public static int DAYS_OF_WEEK = 7;
 
   /** The FM server ip. */
   public static ArrayList FMServerIP = null;
   
   /** The max limit. */
   public static int MAX_LIMIT = 2000;
   
   /** The round robin. */
   public static int roundRobin = 0;
   
   /** The connection. */
   Connection connection;
   
   /** The Constant DEFAULT_HOST. */
   private static final String DEFAULT_HOST = "localhost";
 
   /**
    * The Constructor.
    */
   public MySQLAdmin()
   {
     if (FMServerIP == null)
     {
       //System.out.println("Coming inside MySQLAdmin constructor");
     }
   }
 
   /**
    * Test.
    *
    * @param msg the msg
    * @return the string
    */
   public String test(String msg)
   {
     //System.out.println("SimpleRemoteObject.write: " + msg);
     return "success";
   }
 
   /**
    * Execute relations sql.
    *
    * @param databaseName the database name
    * @param queries the queries
    * @return the list
    * @throws Exception the exception
    */
   public List executeRelationsSQL(String databaseName, String[] queries) throws Exception
   {
     return executeRelationsSQL(databaseName, queries, "localhost");
   }
 
   /**
    * Execute relations sql.
    *
    * @param databaseName the database name
    * @param queries the queries
    * @param databaseIP the database ip
    * @return the list
    * @throws Exception the exception
    */
   public List executeRelationsSQL(String databaseName, String[] queries, String databaseIP)
     throws Exception
   {
     List list = null;
     try
     {
       //System.out.println("Incoming query size is " + queries.length);
       //System.out.println("Incoming query is " + queries[0]);
       //System.out.println("Incoming query is " + queries[1]);
       this.connection = ConnectionHelperWithPwd.getInstance().getConnection(databaseName, databaseIP);
       //System.out.println(this.connection);
       Statement stmt = this.connection.createStatement();				
       int insertedRows = stmt.executeUpdate(queries[0], Statement.RETURN_GENERATED_KEYS);
       //System.out.println("1st Query executed successfully. Rows inserted :" + insertedRows);
       long lastInsertedMasterRowId = 0;
       if (insertedRows > 0)
       {
		 ResultSet rsGenKey = stmt.getGeneratedKeys();
		 if(rsGenKey.next())
		 {
			 lastInsertedMasterRowId = rsGenKey.getLong(1);
		 }
		 //System.out.println("The id of the newly inserted row is " + lastInsertedMasterRowId);
		 PreparedStatement ps = this.connection.prepareStatement(queries[1]);
		 ps.setLong(1, lastInsertedMasterRowId);       
		 insertedRows = ps.executeUpdate();
         //System.out.println("Final (2nd) executed successfully. Rows inserted :" + insertedRows);
       }
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw e;
     }
     finally
     {
       try
       {
         this.connection.close();
       }
       catch (Exception e)
       {
         e.printStackTrace();
       }
 
     }
 
     return list;
   }
 
   /**
    * Execute sql.
    *
    * @param databaseName the database name
    * @param sql the sql
    * @return the list
    * @throws Exception the exception
    */
   public List executeSQL(String databaseName, String sql) throws Exception
   {
     //System.out.println("Coming inside execute SQL");
     return executeSQL(databaseName, sql, "localhost");
   }
 
   /**
    * Execute sql.
    *
    * @param databaseName the database name
    * @param sql the sql
    * @param databaseIP the database ip
    * @return the list
    * @throws Exception the exception
    */
   public List executeSQL(String databaseName, String sql, String databaseIP)
     throws Exception
   {
     List list = null;
     try
     {
       this.connection = ConnectionHelperWithPwd.getInstance().getConnection(databaseName, databaseIP);
       //System.out.println(this.connection);
       Statement stmt = this.connection.createStatement();
       //System.out.println("Incoming query is " + sql);
				
				boolean isUpdate = !sql.toLowerCase().startsWith("select ");
				if(isUpdate)
				{
					Integer affectedRows = stmt.executeUpdate(sql);
					list = new ArrayList();
					list.add(affectedRows);
				}
				else
				{
       boolean result = stmt.execute(sql);
       if (result)
       {
         list = rsToList(stmt.getResultSet());
				}
       else
       {
         HashMap map = new HashMap();
         if ((list = rsToList(stmt.getGeneratedKeys())) == null)
         {
           list = new ArrayList();
           map.put("AffectedRows", new Integer(stmt.getUpdateCount()));
           list.add(map);
         }
       }
				}
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw e;
     }
     finally
     {
       try
       {
         this.connection.close();
       }
       catch (Exception e)
       {
         e.printStackTrace();
       }
     }
     return list;
   }

   /**
    * Rs to list.
    *
    * @param rs the rs
    * @return the list
    * @throws Exception the exception
    */
   private List rsToList(ResultSet rs) throws Exception
   {
     List list = new ArrayList();
     ResultSetMetaData rsmd = rs.getMetaData();
     int colCount = rsmd.getColumnCount();
     HashMap row = null;
     try
     {
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
     catch (Exception e)
     {
       e.printStackTrace();
       throw e;
     }
     return list;
   }
 
   /**
    * Execute sq l1.
    *
    * @param databaseName the database name
    * @param s1 the s1
    * @return the hash map
    * @throws Exception the exception
    */
   public HashMap executeSQL1(String databaseName, String s1) throws Exception
   {
     //System.out.println("Incoming batch query is " + s1);
 
     HashMap hashmap = new HashMap();
     try
     {
       Statement statement = this.connection.createStatement();
       boolean flag = statement.execute(s1);
       if (flag)
       {
         hashmap.put("result", rsToList(statement.getResultSet()));
       }
       else
       {
         HashMap hashmap1 = new HashMap();
         hashmap1.put("AffectedRows", new Integer(statement.getUpdateCount()));
         hashmap.put("result", hashmap1);
       }
     }
     catch (Exception exception1)
     {
       exception1.printStackTrace();
       throw exception1;
     }
     return hashmap;
   }
 
   /**
    * Execute batch sql.
    *
    * @param databaseName the database name
    * @param queries the queries
    * @return the list
    * @throws Exception the exception
    */
   public List executeBatchSQL(String databaseName, String[] queries) throws Exception
   {
     return executeBatchSQL(databaseName, queries, "localhost");
   }
 
   /**
    * Execute batch sql.
    *
    * @param databaseName the database name
    * @param queries the queries
    * @param databaseIP the database ip
    * @return the list
    * @throws Exception the exception
    */
   public List executeBatchSQL(String databaseName, String[] queries, String databaseIP)
     throws Exception
   {
     //System.out.println("Executing Batch SQL");
 
     ArrayList arraylist = new ArrayList();
     try
     {
       this.connection = ConnectionHelperWithPwd.getInstance().getConnection(databaseName, databaseIP);
       for (int i = 0; i < queries.length; i++)
       {
         arraylist.add(executeSQL1(databaseName, queries[i]));
       }
     }
     catch (Exception exception1)
     {
       exception1.printStackTrace();
       throw exception1;
     }
 
     try
     {
       this.connection.close();
     }
     catch (Exception exception)
     {
       exception.printStackTrace();
     }
     try
     {
       this.connection.close();
     }
     catch (Exception exception3)
     {
       exception3.printStackTrace();
     }
     return arraylist;
   }
 
   /**
    * Login validation.
    *
    * @param databaseName the database name
    * @param query the query
    * @return the list
    * @throws Exception the exception
    */
   public List LoginValidation(String databaseName, String query) throws Exception
   {
     return LoginValidation(databaseName, query, "localhost");
   }
 
   /**
    * Login validation.
    *
    * @param databaseName the database name
    * @param query the query
    * @param databaseIP the database ip
    * @return the list
    * @throws Exception the exception
    */
   public List LoginValidation(String databaseName, String query, String databaseIP) throws Exception
   {
     //System.out.println("Login validation service");
     List result = executeSQL(databaseName, query, databaseIP);
     return result;
   }
 
   /**
    * Insert multiple lectures.
    *
    * @param databaseName the database name
    * @param databaseIP the database ip
    * @param lecturetopic the lecturetopic
    * @param keywrd the keywrd
    * @param selecday the selecday
    * @param user_id the user_id
    * @param class_id the class_id
    * @param startDateTimestr the start date timestr
    * @param endDateTimestr the end date timestr
    * @return the list
    * @throws Exception the exception
    */
   public List InsertMultipleLectures(String databaseName, String databaseIP, String lecturetopic, String keywrd, String selecday, int user_id, int class_id, String startDateTimestr, String endDateTimestr)
     throws Exception
   {
     List result = new ArrayList();
 
     String[] paramStartDateTime = startDateTimestr.split(" ");
     String startTime = paramStartDateTime[1];
     String[] startDate = paramStartDateTime[0].split("/");
     int fromyear = Integer.parseInt(startDate[0]);
     int frommonth = Integer.parseInt(startDate[1]) - 1;
     int fromdate = Integer.parseInt(startDate[2]);
 
     String[] paramEndDateTime = endDateTimestr.split(" ");
     String endTime = paramEndDateTime[1];
     String[] endDate = paramEndDateTime[0].split("/");
     int toyear = Integer.parseInt(endDate[0]);
     int tomonth = Integer.parseInt(endDate[1]) - 1;
     int todate = Integer.parseInt(endDate[2]);
 
     this.connection = ConnectionHelperWithPwd.getInstance().getConnection(databaseName, databaseIP);
 
     Calendar lectureStartDate = new GregorianCalendar(fromyear, frommonth, fromdate);
 
     Calendar nextSunday = new GregorianCalendar(fromyear, frommonth, fromdate);
     int dayOfWeek = nextSunday.get(7);
     Calendar lectureEndDate = new GregorianCalendar(toyear, tomonth, todate);
     int days = 1 - dayOfWeek;
     if (days < 0)
     {
       days += DAYS_OF_WEEK;
     }
     nextSunday.add(5, days);
 
     if (lectureStartDate.equals(lectureEndDate))
     {
       String dummyStartDateTime = lectureStartDate.get(1) + "-" + (lectureStartDate.get(2) + 1) + "-" + lectureStartDate.get(5) + " " + startTime + ":" + "00";
       String dummyEndDateTime = lectureEndDate.get(1) + "-" + (lectureEndDate.get(2) + 1) + "-" + lectureEndDate.get(5) + " " + endTime + ":" + "00";
       String sql = "INSERT INTO lectures(lecture_topic, startDate, teacher_id, course_id, keywords, isActive, endDate) VALUES ('" + 
         lecturetopic + "','" + dummyStartDateTime + "','" + 
         user_id + "','" + class_id + "','" + keywrd + "', 1 ,'" + dummyEndDateTime + "')";
 
       result = executeSQL(databaseName, sql, databaseIP);
     }
     else
     {
       int varweek = 0;
       while (lectureStartDate.before(nextSunday))
       {
         if ((lectureStartDate.after(nextSunday)) || (lectureStartDate.after(lectureEndDate)))
         {
           break;
         }
         for (int x = dayOfWeek - 1; x < DAYS_OF_WEEK; varweek++)
         {
           if ((lectureStartDate.after(nextSunday)) || (lectureStartDate.after(lectureEndDate)))
           {
             break;
           }
           if (selecday.charAt(x) == '1')
           {
             String dummyStartDateTime = lectureStartDate.get(1) + "-" + (lectureStartDate.get(2) + 1) + "-" + lectureStartDate.get(5) + " " + startTime + ":" + "00";
             String dummyEndDateTime = lectureStartDate.get(1) + "-" + (lectureStartDate.get(2) + 1) + "-" + lectureStartDate.get(5) + " " + endTime + ":" + "00";
             String str = "INSERT INTO lectures(lecture_topic, startDate, teacher_id, course_id, keywords, isActive, endDate) VALUES ('" + 
               lecturetopic + "','" + 
               dummyStartDateTime + "','" + user_id + "','" + class_id + "','" + keywrd + "', 1 ,'" + 
               dummyEndDateTime + "')";
 
             result.add(executeSQL(databaseName, str));
           }
           lectureStartDate.add(5, 1);
 
           x++;
         }
 
       }
 
       while (!nextSunday.after(lectureEndDate))
       {
         for (int x = 0; x < DAYS_OF_WEEK; x++)
         {
           if (selecday.charAt(x) == '1')
           {
             if (nextSunday.after(lectureEndDate))
             {
               break;
             }
 
             String dummyStartDateTime = nextSunday.get(1) + ":" + (nextSunday.get(2) + 1) + ":" + nextSunday.get(5) + " " + startTime + ":" + "00";
             String dummyEndDateTime = nextSunday.get(1) + ":" + (nextSunday.get(2) + 1) + ":" + nextSunday.get(5) + " " + endTime + ":" + "00";
             String str = "INSERT INTO lectures(lecture_topic, startDate, teacher_id, course_id, keywords, isActive, endDate) VALUES ('" + 
               lecturetopic + "','" + 
               dummyStartDateTime + "','" + user_id + "','" + class_id + "','" + 
               keywrd + "', 1 ,'" + dummyEndDateTime + "')";
 
             result.add(executeSQL(databaseName, str));
           }
           nextSunday.add(5, 1);
         }
       }
     }
     return result;
   }
 
   /**
    * New user registration.
    *
    * @param UserName the User name
    * @param password the password
    * @param role the role
    * @param fname the fname
    * @param lname the lname
    * @param mname the mname
    * @param mobileNum the mobile num
    * @param email the email
    * @param databaseName the database name
    * @param databaseIP the database ip
    * @return the int
    */
   public int NewUserRegistration(String UserName, String password, String role, String fname, String lname, String mname, String mobileNum, String email, String databaseName, String databaseIP)
   {
     int result = 0;
     Connection connection = null;
 
     String userInsertQuery = "insert into users(user_name, password, role, center, status) values ('" + 
       UserName + "', '" + password + "', '" + role + "', '" + 
       "NULL" + "', 0)";
     try
     {
       connection = ConnectionHelperWithPwd.getInstance().getConnection(databaseName, databaseIP);
       //System.out.println(connection);
       Statement stmt = connection.createStatement();
 
       boolean result1 = stmt.execute(userInsertQuery);
       int lastInsertedMasterRowId = 0;
       int lastInsertedDetailsRowId = 0;
 
       if (!result1)
       {
         ResultSet rs = stmt.getGeneratedKeys();
         while (rs.next())
         {
           lastInsertedMasterRowId = rs.getInt(1);
         }
         //System.out.println("The id of the newly inserted row is " + lastInsertedMasterRowId);
         String userDetailsQuery = "insert into user_details(fname, lname, email, mobile_number, user_id) values ('" + 
           fname + "', '" + lname + "', '" + email + 
           "', '" + mobileNum + "', " + lastInsertedMasterRowId + ");";
         stmt = connection.createStatement();
         result1 = stmt.execute(userDetailsQuery);
         if (!result1)
         {
           rs = stmt.getGeneratedKeys();
           while (rs.next())
           {
             lastInsertedDetailsRowId = rs.getInt(1);
           }
           //System.out.println("The id of the newly inserted row is " + lastInsertedDetailsRowId);
         }
         else
         {
           result = -1;
         }
 
       }
       else
       {
         result = -1;
       }
 
     }
     catch (Exception e)
     {
       result = -1;
       try
       {
         connection.close();
       }
       catch (Exception e2)
       {
         e2.printStackTrace();
       }
     }
     finally
     {
       try
       {
         connection.close();
       }
       catch (Exception e)
       {
         e.printStackTrace();
       }
     }
     return result;
   }
 
   /**
    * Gets the fm server ip.
    *
    * @return the string
    * @throws Exception the exception
    */
   public String GetFMServerIP() throws Exception
   {
     String ip = "";
     //System.out.println("round robin is " + roundRobin);
     ip = FMServerIP.get(roundRobin % FMServerIP.size()).toString();
     roundRobin += 1;
     //System.out.println("The alloted IP address to the client is " + ip);
 
     if (roundRobin > MAX_LIMIT)
     {
       roundRobin = 0;
     }
     return ip;
   }
 
   /**
    * Gets the fm server ip.
    *
    * @param userRole the user role
    * @return the list
    * @throws Exception the exception
    */
   public List GetFMServerIP(String userRole) throws Exception
   {
     return FMServerIP;
   }
 
   /**
    * The main method.
    *
    * @param args the args
    * @throws Exception the exception
    */
   public static void main(String[] args)
     throws Exception
   {
     MySQLAdmin sro = new MySQLAdmin();
 
     List list = sro.executeSQL("aview", "select * from user;");
     //System.out.println("Users :" + list);
 
     sro.GetFMServerIP();
   }
 }
