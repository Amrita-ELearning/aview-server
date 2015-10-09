/*
 * 
 */
package edu.amrita.aview.common.utils;

import java.util.Properties;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.CurrentSessionContext;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import edu.amrita.aview.common.AViewException;

/**
 * The Class HibernateUtils.
 */
public class HibernateUtils {
	
	/** The SessionFactory for main database. */
	private static SessionFactory sf = null;
	/** The SessionFactory for audit database.  */
	private static SessionFactory sfAudit = null;
	/** The SessionFactory for both databases, used in batch job. */
	private static SessionFactory sfAuditBatch = null;
	
	/** The csc. */
	private static CurrentSessionContext csc = null;
	
	/** The Constant HIBERNATE_BATCH_SIZE_DEFAULT. */
	private static final int HIBERNATE_BATCH_SIZE_DEFAULT = 50;
	
	/** The hibernate batch size. */
	public static int HIBERNATE_BATCH_SIZE = 50;
	
	/** Store the main database name, used in batch job query */
	public static String mainDatabaseName = null;
	static {
		try {
			
			Configuration conf = new Configuration().configure();
			String batchSize = conf.getProperty("jdbc.batch-size");
			try
			{
				HIBERNATE_BATCH_SIZE = Integer.parseInt(batchSize);
			}
			catch(Exception e)
			{
				HIBERNATE_BATCH_SIZE = HIBERNATE_BATCH_SIZE_DEFAULT;
			}
			sf = buildSessionFactory("mysql.",true);
			
			//Audit Connection
			sfAudit = buildSessionFactory("mysqlAudit.",false);
			//Audit Batch Job Connection
			sfAuditBatch = buildSessionFactory("mysqlAuditBatch.",false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		catch(Throwable err)
		{
			err.printStackTrace();
		}
	}
	
	private static SessionFactory buildSessionFactory(String propertyPrefix,Boolean isMainDb) throws Exception
	{
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		Properties properties = new EncryptableProperties(encryptor);
		try
		{
			properties.load(HibernateUtils.class.getClassLoader().getResourceAsStream("mysql.properties"));
		}
		catch (Exception fnfe)
		{
			fnfe.printStackTrace();
			throw fnfe;
		}
       	String databaseIP = properties.getProperty(propertyPrefix+"databaseIP");
       	String databaseName = properties.getProperty(propertyPrefix+"databaseName");
       	String username = properties.getProperty(propertyPrefix+"user");
		encryptor.setPassword(username); // could be got from web, env variable...
       	String passwd = properties.getProperty(propertyPrefix+"password");
       	String port = properties.getProperty(propertyPrefix+"port");
       	
       	if(isMainDb)
       	{
       		mainDatabaseName = databaseName;
       	}
       	
		Configuration conf = new Configuration().configure();
		conf.addSqlFunction("group_concat",new StandardSQLFunction("group_concat")) ;
		String url =  "jdbc:mysql://" + databaseIP + ":" + port +((databaseName.length()>0)?( "/" + databaseName):"");
		conf.setProperty("hibernate.connection.url",url);
		conf.setProperty("hibernate.connection.username", username);
		conf.setProperty("hibernate.connection.password", passwd);
		conf = conf.configure();
		return conf.buildSessionFactory();
	}

	/**
	 * Gets the hibernate connection for main database.
	 *
	 * @return the hibernate connection
	 */
	public static Session getHibernateConnection() {
		Session con = null;
		try {
			con = sf.openSession();
		} catch (Exception se) {
			se.printStackTrace();
		}
		return con;
	}

	/**
	 * Gets the hibernate connection for Audit databse.
	 *
	 * @return the hibernate connection
	 */
	public static Session getAuditHibernateConnection() {
		Session con = null;
		try {
			con = sfAudit.openSession();
		} catch (Exception se) {
			se.printStackTrace();
		}
		return con;
	}

	/**
	 * Gets the hibernate connection for both databases, used in batch job.
	 *
	 * @return the hibernate connection
	 */
	public static Session getAuditBatchHibernateConnection() {
		Session con = null;
		try {
			con = sfAuditBatch.openSession();
		} catch (Exception se) {
			se.printStackTrace();
		}
		return con;
	}

	/**
	 * Gets the current hibernate connection.
	 *
	 * @return the current hibernate connection
	 */
	public static Session getCurrentHibernateConnection() {
		Session con = null;
		try {
			con = sf.getCurrentSession();
		} catch (Exception se) {
			se.printStackTrace();
		}
		return con;
	}

	/**
	 * Close connection.
	 *
	 * @param con the con
	 */
	public static void closeConnection(Session con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception ignore) {
		}
	}
	
	/**
	 * function for setting database dynamically 
	 * @param session
	 * @throws AViewException
	 * 
	 **/
	public static void setDatabase(Session session) throws AViewException
	{
		String queryDb = "use "+HibernateUtils.mainDatabaseName;
		Query query = session.createSQLQuery(queryDb);
		query.executeUpdate();
	}
}
