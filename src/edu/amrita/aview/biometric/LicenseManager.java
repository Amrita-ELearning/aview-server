/*
 * 
 */
package edu.amrita.aview.biometric;

import java.io.IOException;
import java.util.ArrayList;

import com.neurotec.licensing.NLicense;


/**
 * The Class LicenseManager.
 */
public class LicenseManager {

	/** The instance. */
	private static LicenseManager instance = null;
	
	/** The licenses. */
	private ArrayList<String> licenses;
	
	/** The progress. */
	private int progress;
	
	/** The address. */
	private String address = "/local";
	
	/** The port. */
	private String port = "5000";

	/**
	 * Instantiates a new license manager.
	 */
	private LicenseManager() {
		licenses = new ArrayList<String>();
		licenses.add("Biometrics.FaceExtraction");
		licenses.add("Biometrics.FaceMatching");
	}

	/**
	 * Gets the single instance of LicenseManager.
	 *
	 * @return single instance of LicenseManager
	 */
	public static LicenseManager getInstance() {
		if (instance == null) {
			instance = new LicenseManager();
		}
		return instance;
	}

	/**
	 * Obtain.
	 *
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public synchronized boolean obtain() throws Exception {
		return obtain(address, port);
	}
	
	/**
	 * Obtain.
	 *
	 * @param address the address
	 * @param port the port
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public synchronized boolean obtain(String address, String port) throws Exception {
		
		//System.out.format("Obtaining licenses from server %s:%s\n", address, port);
		boolean result = false;
		try {
			for (String license : licenses) {
				boolean state = false;
				try {
					state = NLicense.obtainComponents(address, port, license);
					result |= state;
				} finally {
					//System.out.println(license + ": " + (state ? "obtainted" : "not obtained"));
				}
			}
		} 
		catch(Exception e)
		{
			//System.out.println(e.getMessage());
		}
		return result;
	}

	/**
	 * Release.
	 */
	public synchronized void release() {
		if (isProgress()) return;
		String components = licenses.toString().replace("[", "").replace("]", "").replace(" ", "");
		try {
			NLicense.releaseComponents(components);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if is progress.
	 *
	 * @return true, if is progress
	 */
	public boolean isProgress() {
		return progress != 0 && progress != 100;
	}

	/**
	 * Gets the progress.
	 *
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}
}
