/*
 * HashCodeUtils.java 4.0 2013/10/17
 */
package edu.amrita.aview.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;




/**
 * This class generates hash code for a given string using SHA-1.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 * {@link http://www.anyexample.com/programming/java/java_simple_class_to_compute_sha_1_hash.xml}
 */
public class HashCodeUtils
{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(HashCodeUtils.class);
	
	/**
	 * Convert to hex.
	 *
	 * @param data the data
	 * @return the string
	 */
	private static String convertToHex(byte[] data) 
	{ 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9))
                {
                    buf.append((char) ('0' + halfbyte));
                }
                else
                {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    } 
 
    /**
     * SH a1.
     *
     * @param text the text
     * @return the string
     * @throws AViewException
     */
    public static String SHA1(String text) throws AViewException  
    { 
	    MessageDigest md;
	    byte[] sha1hash = new byte[40];
	    try
	    {
		    md = MessageDigest.getInstance("SHA-1");		    
		    md.update(text.getBytes("iso-8859-1"), 0, text.length());
		    sha1hash = md.digest();
	    }    
	    catch(NoSuchAlgorithmException ae)
	    {
	    	String exceptionMessage = "No such algorithm exception";
	    	logger.error(exceptionMessage, ae);
	    	throw (new AViewException(exceptionMessage));
	    } 
	    catch (UnsupportedEncodingException ue) 
	    {			
	    	String exceptionMessage = "UnsupportedEncodingException";
	    	logger.error(exceptionMessage, ue);
	    	throw (new AViewException(exceptionMessage));
		}
	    return convertToHex(sha1hash);
    } 
}
