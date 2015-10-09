/*
 * QbSubcategoryListVO.java 4.0 2013/10/17
 */
package edu.amrita.aview.evaluation.vo;

import edu.amrita.aview.evaluation.entities.QbSubcategory;




/**
 * This class is used to populate transient attributes of sub category.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QbSubcategoryListVO 
{
	
	/** The qb subcategory. */
	public QbSubcategory qbSubcategory = null ;
	
	/** The created by user name. */
	public String createdByUserName = null ;
	
	/** The modified by user name. */
	public String modifiedByUserName = null ;
	
	/** The total questions. */
	public Integer totalQuestions = 0 ;
}
