/*
 * RandomNumberUtils.java 4.0 2013/10/17
 */
package edu.amrita.aview.common.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

import edu.amrita.aview.common.Constant;




/**
 * This generates random numbers.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class RandomUtils
{
	// Used for reset of password : alphabets ,numbers and special characters
	private static final String ALPHA_CAPS  = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA   = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUM     = "0123456789";
    private static final String SPL_CHARS   = " !\"#$%&'()*+,-./:;<=>?@[]^_`{|}~\\ ";
	/**
	 * Generate random numbers.
	 *
	 * @param samplesNeeded the samples needed
	 * @param poolRandomnumbers the pool randomnumbers
	 * @return the list< long>
	 */
	public static List<Long> generateRandomNumbers(Long samplesNeeded, List<Long> poolRandomnumbers)  
	 {
		List<Long> randomnumbers = new ArrayList<Long>();		
		for(int i=0;i<samplesNeeded;i++)
		{
			int rand1 = (int) ((poolRandomnumbers.size())* Math.random()); 
			randomnumbers.add(poolRandomnumbers.get(rand1));
			poolRandomnumbers.remove(rand1);
		}
		return randomnumbers;
	 }
	
	/**
	 * Generate random numbers.
	 *
	 * @param samplesNeeded the samples needed
	 * @param availableCount the available count
	 * @return the list< integer>
	 */
	public static List<Integer> generateRandomNumbers(Long samplesNeeded, int availableCount)
	{
		Set<Integer> tmpRandomnumbers = new HashSet<Integer>();
		int rand = 0; 
		do
		{
			rand = (int) (availableCount * Math.random());
			tmpRandomnumbers.add(rand);			
		}while(tmpRandomnumbers.size() < samplesNeeded);
		List<Integer> randomnumbers = new ArrayList<Integer>(tmpRandomnumbers);
		return randomnumbers;
	}
	
	/**
	 * Generate random string.
	 *
	 * @return the string
	 */
	public static String generateRandomString()
	{
		//String newPassword = RandomStringUtils.randomAlphanumeric(Constant.LOGIN_PASSWORD_LENGTH);
		 Random rnd = new Random();       
	    // Set the number of alphabets , number and special chars required in the password
        int noOfCAPSAlpha = 2;
        int noOfDigits = 2 ;
        int noOfSplChars = 2 ;
        
        char[] pswd = new char[Constant.LOGIN_PASSWORD_LENGTH];
        int index = 0 , i;
        for (i = 0; i < noOfCAPSAlpha; i++) {
            index = getNextIndex(rnd, Constant.LOGIN_PASSWORD_LENGTH, pswd);
            pswd[index] = ALPHA_CAPS.charAt(rnd.nextInt(ALPHA_CAPS.length()));
        }
        for (i = 0; i < noOfDigits; i++) {
            index = getNextIndex(rnd, Constant.LOGIN_PASSWORD_LENGTH, pswd);
            pswd[index] = NUM.charAt(rnd.nextInt(NUM.length()));
        }
        for (i = 0; i < noOfSplChars; i++) {
            index = getNextIndex(rnd, Constant.LOGIN_PASSWORD_LENGTH, pswd);
            pswd[index] = SPL_CHARS.charAt(rnd.nextInt(SPL_CHARS.length()));
        }
        for(i = 0; i < Constant.LOGIN_PASSWORD_LENGTH; i++) {
            if(pswd[i] == 0) {
                pswd[i] = ALPHA.charAt(rnd.nextInt(ALPHA.length()));
            }
        }
        return String.valueOf(pswd);
	}		

 /**
  *  Gets a random index
  * @param rnd type of Random
  * @param len type of int
  * @param pswd type of char[]
  * @return int
  */
 private static int getNextIndex(Random rnd, int len, char[] pswd) {
     int index = rnd.nextInt(len);
     while(pswd[index = rnd.nextInt(len)] != 0);
     return index;
 }
}


