/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import java.util.HashMap;


/**
 * The Class CacheHelper.
 */
public class CacheHelper {
	
	/** The Constant HR_IN_MS. */
	public static final long HR_IN_MS = 1*60*60*1000; //1 hr
	
	/** The Constant DAY_IN_MS. */
	public static final long DAY_IN_MS = 24*60*60*1000; //1 Day
	
	/** The cache details. */
	private static HashMap<String,CacheDetail> cacheDetails = new HashMap<String,CacheDetail>();

	/**
	 * Sets the cache.
	 *
	 * @param cacheCode the new cache
	 */
	public static void setCache(String cacheCode)
	{
		setCache(cacheCode,HR_IN_MS);
	}
	
	/**
	 * Sets the cache.
	 *
	 * @param cacheCode the cache code
	 * @param maxCacheTime the max cache time
	 */
	public static void setCache(String cacheCode,long maxCacheTime)
	{
		cacheDetails.put(cacheCode, new CacheDetail(cacheCode,maxCacheTime));
	}
	
	/**
	 * Reset cache.
	 *
	 * @param cacheCode the cache code
	 */
	public static void resetCache(String cacheCode)
	{
		CacheDetail detail = cacheDetails.get(cacheCode);
		if(detail != null)
		{
			detail.resetCache();
		}
	}
	
	/**
	 * Checks if is cache valid.
	 *
	 * @param cacheCode the cache code
	 * @return true, if is cache valid
	 */
	public static boolean isCacheValid(String cacheCode)
	{
		boolean valid = false;
		
		CacheDetail detail = cacheDetails.get(cacheCode);
		if(detail != null)
		{
			valid = detail.isCacheValid();
		}
		
		return valid;
	}
	
	/**
	 * Invalidate cache.
	 *
	 * @param cacheCode the cache code
	 */
	public static void invalidateCache(String cacheCode)
	{
		CacheDetail detail = cacheDetails.get(cacheCode);
		if(detail != null)
		{
			detail.invalidateCache();
		}
	}
}
