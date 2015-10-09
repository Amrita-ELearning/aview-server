/*
 * 
 */
package edu.amrita.aview.common.utils;

import java.io.PrintStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * The Class OutErrLogger.
 */
public class OutErrLogger {
	
	/**
	 * Sets the out and err to log.
	 */
	public static void setOutAndErrToLog()
	{
//	    setOutToLog();
	    setErrToLog();
	}

	/**
	 * Sets the out to log.
	 */
	public static void setOutToLog()
	{
	    System.setOut(new PrintStream(new LoggerStream(Logger.getLogger("out"), Level.INFO, System.out)));
	}

	/**
	 * Sets the err to log.
	 */
	public static void setErrToLog()
	{
	    System.setErr(new PrintStream(new LoggerStream(Logger.getLogger("err"), Level.ERROR, System.err)));
	}

}
