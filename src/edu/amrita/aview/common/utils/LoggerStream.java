/*
 * 
 */
package edu.amrita.aview.common.utils;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;


/**
 * The Class LoggerStream.
 */
public class LoggerStream extends OutputStream {

	/** The logger. */
	private final Logger logger;
	
	/** The log level. */
	private final Level logLevel;
	
	/** The output stream. */
	private final OutputStream outputStream;

	/**
	 * The Constructor.
	 *
	 * @param logger the logger
	 * @param logLevel the log level
	 * @param outputStream the output stream
	 */
	public LoggerStream(Logger logger, Level logLevel, OutputStream outputStream)
	{
	    super();

	    this.logger = logger;
	    this.logLevel = logLevel;
	    this.outputStream = outputStream;
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) throws IOException
	{
	    outputStream.write(b);
	    String string = new String(b);
	    if (!string.trim().isEmpty())
	        logger.log(null,(Priority)logLevel, (Object)string,null);
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
	    outputStream.write(b, off, len);
	    String string = new String(b, off, len);
	    if (!string.trim().isEmpty())
	        logger.log(null,(Priority)logLevel, (Object)string,null);
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException
	{
	    outputStream.write(b);
	    String string = String.valueOf((char) b);
	    if (!string.trim().isEmpty())
	        logger.log(null,(Priority)logLevel, (Object)string,null);
	}
}
