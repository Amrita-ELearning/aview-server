/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.gclm.daos.BrandingAttributeDAO;
import edu.amrita.aview.gclm.entities.BrandingAttribute;


/**
 * The Class BrandingAttributeHelper.
 */
public class BrandingAttributeHelper {
	//Cache code
	/** The branding attributes map. */
	private static HashMap<Integer,BrandingAttribute> brandingAttributesMap = new HashMap<Integer,BrandingAttribute>();
	
	/** The Constant CACHE_CODE. */
	private static final String CACHE_CODE = "BrandingAttributeHelper";
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(BrandingAttributeHelper.class);
	
	/**
	 * Populate cache.
	 *
	 * @param brandingAttributes the branding attributes
	 */
	private static synchronized void populateCache(List<BrandingAttribute> brandingAttributes)
	{
		brandingAttributesMap.clear();
		for(BrandingAttribute brandingAttribute:brandingAttributes)
		{
			brandingAttributesMap.put(brandingAttribute.getBrandingAttributeId(), brandingAttribute);
		}
		CacheHelper.setCache(CACHE_CODE);
	}
	
	
	/**
	 * Gets the branding attributes id map.
	 *
	 * @return the branding attributes id map
	 * @throws AViewException
	 */
	public static synchronized HashMap<Integer,BrandingAttribute> getBrandingAttributesIdMap() throws AViewException
	{
		if(!CacheHelper.isCacheValid(CACHE_CODE))
		{
			populateCache(BrandingAttributeDAO.getBrandingAttributes(StatusHelper.getActiveStatusId()));
		}
		return brandingAttributesMap;
	}
	
	/**
	 * Adds the item to cache.
	 *
	 * @param brandingAttribute the branding attribute
	 */
	private static synchronized void addItemToCache(BrandingAttribute brandingAttribute)
	{
		brandingAttributesMap.put(brandingAttribute.getBrandingAttributeId(), brandingAttribute);
	}
	

	/**
	 * Gets the branding attributes.
	 *
	 * @return the branding attributes
	 * @throws AViewException
	 */
	public static List<BrandingAttribute> getBrandingAttributes() throws AViewException{
		List<BrandingAttribute> brandingAttributes = new ArrayList<BrandingAttribute>();
		brandingAttributes.addAll(getBrandingAttributesIdMap().values());
		return brandingAttributes;
	}
	
	/**
	 * Gets the branding attribute.
	 *
	 * @param brandingAttributeId the branding attribute id
	 * @return the branding attribute
	 * @throws AViewException
	 */
	public static BrandingAttribute getBrandingAttribute(Integer brandingAttributeId) throws AViewException
	{
		return getBrandingAttributesIdMap().get(brandingAttributeId);
	}

	/**
	 * Creates the branding attribute.
	 *
	 * @param brandingAttribute the branding attribute
	 * @throws AViewException
	 */
	public static void createBrandingAttribute(BrandingAttribute brandingAttribute) throws AViewException
	{
		BrandingAttributeDAO.createBrandingAttribute(brandingAttribute);
		addItemToCache(brandingAttribute);
	}
	
	/**
	 * Delete branding attribute.
	 *
	 * @param brandingAttribute the branding attribute
	 * @throws AViewException
	 */
	public static void deleteBrandingAttribute(BrandingAttribute brandingAttribute) throws AViewException
	{
		int deleteStatus = StatusHelper.getDeletedStatusId();
		brandingAttribute.setStatusId(deleteStatus);
		BrandingAttributeDAO.updateBrandingAttribute(brandingAttribute);
		addItemToCache(brandingAttribute);
	}
	
	/**
	 * Update branding attribute.
	 *
	 * @param brandingAttribute the branding attribute
	 * @throws AViewException
	 */
	public static void updateBrandingAttribute(BrandingAttribute brandingAttribute) throws AViewException
	{
		BrandingAttributeDAO.updateBrandingAttribute(brandingAttribute);
	}
	
	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entered BrandingAttributeHelper::clearCache");
		brandingAttributesMap = null;		
		logger.debug("Exit BrandingAttributeHelper::clearCache");
	}
}
