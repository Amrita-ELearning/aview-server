/*
 * 
 */
package edu.amrita.aview.common;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * The Class GenDirectory.
 */
public class GenDirectory  
{  
	
	/** The blazdspath. */
	private static String blazdspath="";
	
	/** The fms source path. */
	private static String fmsSourcePath="";
	
	/** The fms destination path. */
	private static String fmsDestinationPath="";

	/**
	 * Xml folder create.
	 *
	 * @param path the path
	 * @return the string
	 */
	public String xmlFolderCreate(String path)
	{
		setPath();
		//System.out.println("path="+path);
		String newpath = blazdspath+path;

		File newfolder = new File(newpath);
		try
		{
			newfolder.mkdirs();
			if(newfolder.exists())
			{
				//System.out.println("xml folder creation Path: "+newfolder.getPath());
				return newpath;
			}
			else
			{
				//System.out.println("xml folder is not created: "+newfolder.getPath());
				return null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * Fms folder create.
	 *
	 * @param path the path
	 * @return true, if fms folder create
	 */
	public Boolean fmsFolderCreate(String path)
	{
		setPath();
		String fmsPath = fmsDestinationPath+path;
		//System.out.println("FMS PATH:"+fmsPath);
		File fmsFolder = new File(fmsPath);
		try
		{
			fmsFolder.mkdirs();
			
			if(fmsFolder.exists())
			{
				//System.out.println("fms folder creation Path: "+fmsFolder.getPath());
				return true;
			}
			else
			{
				//System.out.println("fms folder is not created: "+fmsFolder.getPath());
				return false;
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

	/**
	 * Flv file move.
	 *
	 * @param source the source
	 * @param Detination the Detination
	 * @return true, if flv file move
	 */
	public boolean flvFileMove(String source,String Detination)
	{   
		setPath();
		String sourcePath =fmsSourcePath+source;
		String DestinationPath =fmsDestinationPath+Detination;
		
		File file = new File(sourcePath);
		// Destination directory
		File dir = new File(DestinationPath);
		if(!file.exists())
		{
			return false;
		}
		// Move file to new directory
		boolean success = file.renameTo(new File(dir, file.getName()));
		if (!success) {
			// File was not successfully moved
			System.out.println("File------:"+source+"is not moved"); 
			return false;
		}
		else
		{
			//System.out.println("File-------:"+source+" is moved sucessfully");
			return true;
		}
	}
	
	/**
	 * Flv files move.
	 *
	 * @param sources the sources
	 * @param Detination the Detination
	 * @return the string[]
	 */
	public String[] flvFilesMove(String[] sources,String Detination)
	{   
		setPath();
		String msg="";
		String[] fileStaus = new String[sources.length];
		int count=0;
		for(String source : sources)
		{
			String sourcePath =fmsSourcePath+source;
			String DestinationPath =fmsDestinationPath+Detination;
			File file = new File(sourcePath);
			
			if(!file.exists())
			{
				fileStaus[count]="File is Not Exist:"+source;
				++count;
				continue ;
			}
			// Destination directory
			File dir = new File(DestinationPath);
			// Move file to new directory
			boolean success = file.renameTo(new File(dir, file.getName()));
			if (!success) {
				// File was not successfully moved
				//System.out.println("File=========:"+source+"is not moved"); 
				//return ("File is not moved");
				//msg="video Files are not moved";
				fileStaus[count]="File is Not moved:"+source;
						++count;
			}
			else
			{
				//System.out.println("File ========:"+source+" is moved sucessfully");
				//return ("file is moved");
				fileStaus[count]="File has moved:"+source;
				++count;
			}
		}
		return fileStaus;
	}
	
	/**
	 * Adds the xml tag.
	 *
	 * @param xmldata the xmldata
	 * @param fileName the file name
	 * @param filePath the file path
	 * @return the string
	 */
	public String addXMLTag(String xmldata,String fileName,String filePath)
	{      
		FileOutputStream fis;
		try
		{
			fis = new FileOutputStream(filePath+"/"+fileName);
			DataOutputStream in = new DataOutputStream(fis);
			in.write(xmldata.getBytes());
			in.close();
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Error: File doesn't exists");
		}
		catch (IOException e)
		{
			System.err.println("Error in reading file");
		}

		return("xml tag is added"+filePath+"/"+fileName);
	}
	
	/** The Constant DATE_FORMAT_NOW. */
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	/**
	 * Gets the server time.
	 *
	 * @return the server time
	 */
	public String getServerTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}
	
	/**
	 * Sets the path.
	 *
	 * @return the string
	 */
	public static String setPath()
	{
		try {

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

			URL url = classLoader.getResource("AVM_Path.xml");
			File fXmlFile = new File(url.toURI());

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			String os = System.getProperty("os.name");
			
			NodeList nList = doc.getElementsByTagName("ContentServerPath");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					if (os.indexOf("Windows") != -1)
					{
						fmsSourcePath = getTagValue("winFmsSourcePath", eElement);
						fmsDestinationPath = getTagValue("winFmsDestinationPath", eElement);
						blazdspath = getTagValue("winXmlPath", eElement);
					}
					else
					{
						fmsSourcePath = getTagValue("linFmsSourcePath", eElement);
						fmsDestinationPath = getTagValue("linFmsDestinationPath", eElement);
						blazdspath = getTagValue("linXmlPath", eElement);
					}
					String[] rootContent=blazdspath.split("/");
					//System.out.println(rootContent[rootContent.length-1]);

					return rootContent[rootContent.length-1];
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the tag value.
	 *
	 * @param sTag the s tag
	 * @param eElement the e element
	 * @return the tag value
	 */
	private static String getTagValue(String sTag, Element eElement) {

		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}
}





