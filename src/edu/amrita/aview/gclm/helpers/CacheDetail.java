/*
 * 
 */
package edu.amrita.aview.gclm.helpers;


/**
 * The Class CacheDetail.
 */
public class CacheDetail {
	
	/** The last refresh time. */
	private long lastRefreshTime = 0l;
	
	/** The valid. */
	private boolean valid = true;
	
	/** The max cache time. */
	private long maxCacheTime = 0l;
	
	/** The cache code. */
	private String cacheCode = null;
	
	/**
	 * Instantiates a new cache detail.
	 *
	 * @param cacheCode the cache code
	 * @param maxCacheTime the max cache time
	 */
	public CacheDetail(String cacheCode,long maxCacheTime)
	{
		this.cacheCode = cacheCode;
		this.maxCacheTime = maxCacheTime;
		resetCache();
	}
	
	/**
	 * Checks if is cache valid.
	 *
	 * @return true, if is cache valid
	 */
	public boolean isCacheValid()
	{
		if(valid)
		{
			if(System.currentTimeMillis() - lastRefreshTime > maxCacheTime)
			{
				valid = false;
			}
		}
		return valid;
	}
	
	/**
	 * Reset cache.
	 */
	public void resetCache()
	{
		this.lastRefreshTime = System.currentTimeMillis();
		this.valid = true;
	}
	
	/**
	 * Invalidate cache.
	 */
	public void invalidateCache()
	{
		this.valid = false;
	}

}
