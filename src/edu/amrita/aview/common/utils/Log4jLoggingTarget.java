/*
 * 
 */
package edu.amrita.aview.common.utils;

import org.apache.log4j.Logger;

import flex.messaging.config.ConfigMap;
import flex.messaging.log.AbstractTarget;
import flex.messaging.log.LogEvent;


/**
 * The Class Log4jLoggingTarget.
 */
public class Log4jLoggingTarget extends AbstractTarget {

	 /**
 	 * The Constructor.
 	 */
 	public Log4jLoggingTarget() {
		    super();
		  }
		  
  		/**
  		 * No-op.
  		 *
  		 * @param s the s
  		 * @param pConfigMap the p config map
  		 */
		  public void initialize(final String s,
		                         final ConfigMap pConfigMap) {
		  }

		  /**
  		 * Translate the Flex/BlazeDS LogEvent into a Log4J log message.
  		 * 
  		 * Right now, we just inherit the category from the BlazeDS
  		 * logging system, so you can filter or use that as the
  		 * Logger in your Log4J setup
  		 *
  		 * @param pLogEvent the p log event
  		 */
		  public void logEvent(final LogEvent pLogEvent) {
			
			String message = "Message From Log4jLoggingTarget:"+pLogEvent.message;
			if(pLogEvent.throwable != null)
			{
				message += getExceptionTrace(pLogEvent.throwable);
		        Logger.getLogger(pLogEvent.logger.getCategory()).error(message, pLogEvent.throwable);
			}
		    switch(pLogEvent.level) {
		      case LogEvent.ALL:
		        Logger.getLogger(pLogEvent.logger.getCategory()).trace(message, pLogEvent.throwable);

		        break;
		      case LogEvent.DEBUG:
		        Logger.getLogger(pLogEvent.logger.getCategory()).debug(message, pLogEvent.throwable);

		        break;
		      case LogEvent.INFO:
		        Logger.getLogger(pLogEvent.logger.getCategory()).info(message, pLogEvent.throwable);

		        break;
		      case LogEvent.WARN:
		        Logger.getLogger(pLogEvent.logger.getCategory()).warn(message, pLogEvent.throwable);

		        break;
		      case LogEvent.ERROR:
		        Logger.getLogger(pLogEvent.logger.getCategory()).error(message, pLogEvent.throwable);

		        break;
		      case LogEvent.FATAL:
		        Logger.getLogger(pLogEvent.logger.getCategory()).fatal(message, pLogEvent.throwable);

		        break;
		      case LogEvent.NONE:
		        break;
		    }
		  }
		  
		  /**
  		 * Gets the exception trace.
  		 *
  		 * @param t the t
  		 * @return the exception trace
  		 */
  		private String getExceptionTrace(Throwable t)
		  {
			  StackTraceElement[] stackTraces =  t.getStackTrace();
			  StringBuilder sb = new StringBuilder(t.toString()+"\nStackTrace Follows\n");
			  for(StackTraceElement element:stackTraces)
			  {
				  sb.append(element.toString());
				  sb.append("\n");
			  }
			  return sb.toString();
		  }
		 
}
