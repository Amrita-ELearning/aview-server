/*
 * 
 */
package edu.amrita.aview.common.utils;


/**
 * The Class MathUtils.
 */
public class MathUtils {

	/**
	 * Round.
	 *
	 * @param number the number
	 * @param decimals the decimals
	 * @return the double
	 */
	public static double round(Double number,int decimals)
	{
		double tens = Math.pow(10, decimals);
		return Math.round(number*tens)/tens;
	}
}

