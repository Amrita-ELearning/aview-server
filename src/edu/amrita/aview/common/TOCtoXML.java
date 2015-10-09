/*
 * 
 */
package edu.amrita.aview.common;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



/**
 * The Class TOCtoXML.
 */
public class TOCtoXML {

	/** The Constant ROOT_TAG. */
	private static final String ROOT_TAG =  "A-View Class room Topics";
	
	/** The Constant FOLDER_TAG. */
	private static final String FOLDER_TAG = "menuFolder";
	
	/** The Constant ITEM_TAG. */
	private static final String ITEM_TAG = "menuItem";
	
	/** The Constant NA. */
	private static final String NA = "N/A";
	
	/** The Constant ATTR_LABEL. */
	private static final String ATTR_LABEL = "label";
	
	/** The Constant ATTR_PRESENTER_VIDEO_FILE. */
	private static final String ATTR_PRESENTER_VIDEO_FILE = "pFile";
	
	/** The Constant ATTR_PRESENTER_START_TIME. */
	private static final String ATTR_PRESENTER_START_TIME = "pStTime";
	
	/** The Constant ATTR_PRESENTER_END_TIME. */
	private static final String ATTR_PRESENTER_END_TIME = "pEndTime";
	
	/** The Constant ATTR_VIEWER_VIDEO_FILE. */
	private static final String ATTR_VIEWER_VIDEO_FILE = "vFile";
	
	/** The Constant ATTR_VIEWER_START_TIME. */
	private static final String ATTR_VIEWER_START_TIME = "vStTime";
	
	/** The Constant ATTR_VIEWER_END_TIME. */
	private static final String ATTR_VIEWER_END_TIME = "vEndTime";
	
	/** The Constant ATTR_DESCRIPTION. */
	private static final String ATTR_DESCRIPTION = "description";

	/** The lines. */
	private static ArrayList<String[]> lines = new ArrayList<String[]>();
	
	/** The line counter. */
	private static int lineCounter = 0;
	
	/** The xml nodes. */
	private static ArrayList<String> xmlNodes = new ArrayList<String>();
	
	/**
	 * The main method.
	 *
	 * @param args the args
	 */
	public static void main(String[] args) {
		
		readTheTOCTxtFile();
		xmlNodes.add(getTagFromName(ROOT_TAG,false,true,null,null,null,null,null,null,null));
		convertLinesToXML();
		xmlNodes.add(getTagFromName(ROOT_TAG,true,true,null,null,null,null,null,null,null));
		writeTheTOCXMLFile();
	}
	
	/**
	 * Write the tocxml file.
	 */
	private static void writeTheTOCXMLFile()
	{
		File tocXMLFile = null;
		BufferedWriter bw = null;
		try
		{
			tocXMLFile = new File("c:/TOC-AVC.xml");
			bw = new BufferedWriter(new FileWriter(tocXMLFile));
			for(String xmlNode:xmlNodes)
			{
				bw.write(xmlNode);
				bw.write("\n");
			}
			bw.flush();
			bw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Read the toc txt file.
	 */
	private static void readTheTOCTxtFile()
	{
		File tocFile = null;
		BufferedReader br = null;
		try
		{
			tocFile = new File("c:/TOC-AVC.txt");
			br = new BufferedReader(new FileReader(tocFile));
			br.readLine();//Skip the heading
			String line = null;
			while((line = br.readLine()) != null)
			{
				String columns[] = line.split("\\t");
				lines.add(columns);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Gets the tag from name.
	 *
	 * @param label the label
	 * @param isEnd the is end
	 * @param isFolder the is folder
	 * @param pStTime the p st time
	 * @param pEndTime the p end time
	 * @param pFile the p file
	 * @param vStTime the v st time
	 * @param vEndTime the v end time
	 * @param vFile the v file
	 * @param description the description
	 * @return the tag from name
	 */
	private static String getTagFromName(String label,boolean isEnd,boolean isFolder,
			String pStTime,String pEndTime,String pFile,
			String vStTime,String vEndTime,String vFile,
			String description
			)
	{
		StringBuilder sb = new StringBuilder();
		if(isEnd)
		{
			sb.append("</");
			sb.append((isFolder)?FOLDER_TAG:ITEM_TAG);
			sb.append(">");
		}
		else
		{
			sb.append("<");
			sb.append((isFolder)?FOLDER_TAG:ITEM_TAG);
			sb.append(getAttributePair(ATTR_LABEL, label));
			sb.append(getAttributePair(ATTR_PRESENTER_VIDEO_FILE, pFile));
			sb.append(getAttributePair(ATTR_PRESENTER_START_TIME, pStTime));
			sb.append(getAttributePair(ATTR_PRESENTER_END_TIME, pEndTime));
			sb.append(getAttributePair(ATTR_VIEWER_VIDEO_FILE, vFile));
			sb.append(getAttributePair(ATTR_VIEWER_START_TIME, vStTime));
			sb.append(getAttributePair(ATTR_VIEWER_END_TIME, vEndTime));
			sb.append(getAttributePair(ATTR_DESCRIPTION, description));
			sb.append((isFolder)?">":"/>");
		}
		return sb.toString();
	}
	
	/**
	 * Gets the attribute pair.
	 *
	 * @param attrName the attr name
	 * @param attrVal the attr val
	 * @return the attribute pair
	 */
	private static String getAttributePair(String attrName,String attrVal)
	{
		if(attrVal != null && !attrVal.equalsIgnoreCase(NA))
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" ");
			sb.append(attrName);
			sb.append("=\"");
			sb.append(attrVal);
			sb.append("\"");
			return sb.toString();
		}
		return "";
	}
	
	/**
	 * Convert lines to xml.
	 */
	private static void convertLinesToXML()
	{
		for(;lineCounter<lines.size();lineCounter++)
		{
			convertLineToXml(lines.get(lineCounter));
		}
	}
	
	/**
	 * Checks if is folder.
	 *
	 * @param indent the indent
	 * @return true, if checks if is folder
	 */
	private static boolean isFolder(String indent)
	{
		boolean isFolder = false;
		String nextIndent = "";
		if(lineCounter+1 < lines.size())
		{
			nextIndent = separateIndentTopic(lines.get(lineCounter+1)[0])[0];
		}
		if(nextIndent.startsWith(indent))
		{
			isFolder = true;
		}
		return isFolder;
	}
	
	/**
	 * Convert line to xml.
	 *
	 * @param columns the columns
	 */
	private static void convertLineToXml(String[] columns)
	{
		String[] indentTopic = separateIndentTopic(columns[0]);
		
		String indent = indentTopic[0];
		
		//folder or item
		boolean isFolder = isFolder(indent);
		
		//Start tag
		String xmlTag = getTagFromName(indentTopic[0]+" "+indentTopic[1],false,isFolder,
				convertTimeToMS(columns[1]),convertTimeToMS(columns[2]),columns[3],
				convertTimeToMS(columns[4]),convertTimeToMS(columns[5]),columns[6],
				null);
		
		xmlNodes.add(xmlTag);
		
		if(isFolder)
		{
			while(isFolder(indent)) //Process all the childern
			{
				lineCounter++;
				convertLineToXml(lines.get(lineCounter));
			}
			//End tag
			xmlTag = getTagFromName(null,true,isFolder,
					null,null,null,
					null,null,null,
					null);
			
			xmlNodes.add(xmlTag);
		}
	}
	
	/**
	 * Convert time to ms.
	 *
	 * @param time the time
	 * @return the string
	 */
	private static String convertTimeToMS(String time)
	{
		if(time != null)
		{
			time = time.trim();
		}
		if(time != null && !time.equalsIgnoreCase(NA))
		{
			
			long ms = 0l;
			String[] times = time.split(":");
			if(times.length == 1)
			{
				ms = 1000*Integer.parseInt(times[0]);
			}
			if(times.length == 2)
			{
				ms = 1000*Integer.parseInt(times[1]);
				ms += 1000*Integer.parseInt(times[0])*60;
			}
			if(times.length == 3)
			{
				ms = 1000*Integer.parseInt(times[2]);
				ms += 1000*Integer.parseInt(times[1])*60;
				ms += 1000*Integer.parseInt(times[0])*60*60;
			}
			
			return ms+"";
		}
		return time;
	}
	
	/**
	 * Separate indent topic.
	 *
	 * @param topic the topic
	 * @return the string[]
	 */
	private static String[] separateIndentTopic(String topic)
	{
		//System.out.println("Processing topic:"+topic);
		String[] indentTopic = new String[2];
		int firstSpace = topic.indexOf(". ");
		indentTopic[0] = topic.substring(0,firstSpace+1).trim();
		indentTopic[1] = topic.substring(firstSpace+1).trim(); //Advance one for the .
		return indentTopic;
	}

}
