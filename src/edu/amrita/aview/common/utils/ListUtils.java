/*
 * 
 */
package edu.amrita.aview.common.utils;

import java.util.ArrayList;
import java.util.List;


/**
 * The Class ListUtils.
 */
public class ListUtils {
	
	/** The Constant COMMA_DELIMITER. */
	public static final String COMMA_DELIMITER = ",";
	
	/** The Constant SQL_QUOTE. */
	public static final String SQL_QUOTE = "'";

	/**
	 * Gets the numeric list as comma delimited string.
	 *
	 * @param items the items
	 * @return the numeric list as comma delimited string
	 */
	public static String getNumericListAsCommaDelimitedString(List items)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(items.get(0));
		for(int i=1;i<items.size();i++)
		{
			sb.append(COMMA_DELIMITER);
			sb.append(items.get(i));
		}
		return sb.toString();
	}
	
	/**
	 * Gets the string list as comma delimited string.
	 *
	 * @param items the items
	 * @return the string list as comma delimited string
	 */
	public static String getStringListAsCommaDelimitedString(List items)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(SQL_QUOTE);
		sb.append(items.get(0));
		sb.append(SQL_QUOTE);
		for(int i=1;i<items.size();i++)
		{
			sb.append(COMMA_DELIMITER);
			sb.append(SQL_QUOTE);
			sb.append(items.get(i));
			sb.append(SQL_QUOTE);
		}
		return sb.toString();
	}
	
	/**
	 * Break list into1000s.
	 *
	 * @param hugeList the huge list
	 * @return the list< list>
	 */
	public static List<List> breakListInto1000s(List hugeList)
	{
		List<List> container = new ArrayList<List>();
		
		List brokenList = new ArrayList();
		int i=0;
		for(;i<hugeList.size();i++)
		{
			brokenList.add(hugeList.get(i));
			if((i+1) % 1000 == 0)
			{
				container.add(brokenList);
				brokenList = new ArrayList();
			}
		}

		//Add any exta items
		if(brokenList.size() > 0)
		{
			container.add(brokenList);
		}

		return container;
	}
	
}
