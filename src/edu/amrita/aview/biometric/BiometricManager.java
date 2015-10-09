/*
 * 
 */
package edu.amrita.aview.biometric;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.neurotec.biometrics.NLEFace;
import com.neurotec.biometrics.NLETemplateSize;
import com.neurotec.biometrics.NLExtractor;
import com.neurotec.biometrics.NLTemplate;
import com.neurotec.biometrics.NMatcher;
import com.neurotec.images.NGrayscaleImage;
import com.neurotec.images.NImage;


/**
 * The Class BiometricManager.
 */
public class BiometricManager{

	/** The extractor. */
	private NLExtractor extractor = null;
	
	/** The matcher. */
	private NMatcher matcher = null;
	
	/** The error message. */
	private String errorMessage = null;
	
	/**
	 * This method is used to initialize face feature extraction process.
	 *
	 * @return errorMessage
	 */
	public synchronized String initializeExtractor(){
		try {
			extractor = new NLExtractor();
			matcher = new NMatcher();
			extractor.setTemplateSize(NLETemplateSize.MEDIUM);
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		return errorMessage;
	}
	
	/**
	 * This method is used to initialize face matching process.
	 *
	 * @return errorMessage
	 */
	public synchronized String initializeMatcher(){
		try {
			extractor = new NLExtractor();
			matcher = new NMatcher();
			extractor.setTemplateSize(NLETemplateSize.LARGE);
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}

		return errorMessage;
	}
	
	/**
	 * This method is used to detect the facial feature.
	 *
	 * @param imageFilePath the image file path
	 * @return templateList
	 */
	public synchronized NLExtractor.ExtractResult detectFacialFeatures(String imageFilePath) {
		NImage image = null;
		NGrayscaleImage grayscaleImage = null;
		NLExtractor.ExtractResult extractionResult = null;
		try {
			image = NImage.fromFile(imageFilePath);
			grayscaleImage = image.toGrayscale();
			extractionResult = extractor.extract(grayscaleImage);
		} catch (IOException e) {
			extractionResult = null;
		} finally {
			if (image != null) {
				image.dispose();
			}
			if (grayscaleImage != null) {
				grayscaleImage.dispose();
			}
		}
		return extractionResult;
	}
	
	/**
	 * This method is used to detect and resize the face from the input image.
	 *
	 * @param bimage the bimage
	 * @param face the face
	 * @return faceIcon
	 */
	public synchronized BufferedImage drawFace(BufferedImage bimage, NLEFace face) {
		BufferedImage faceIcon = bimage.getSubimage(face.getRectangle().x - 20, face.getRectangle().y - 60, face.getRectangle().width + 40, face.getRectangle().height + 70);
		BufferedImage resizedImage = new BufferedImage(60, 70, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(faceIcon, 0, 0, 60, 70, null);
		graphics2D.dispose();
		return resizedImage;
	}
	
	/**
	 * This method is used to find the matching face in the database.
	 *
	 * @param faceTemplate the face template
	 * @param dbRecordList the db record list
	 * @return record
	 */
	public synchronized List<Templates> detectFace(NLTemplate faceTemplate, List<Templates> dbRecordList) {
		Templates record = null;
		List<Templates> recordList = new ArrayList<Templates>();
		ByteBuffer buffer = faceTemplate.save();
		byte[] faceTemplateBytes = new byte[buffer.capacity()];
		buffer.clear();
		buffer.get(faceTemplateBytes, 0, faceTemplateBytes.length);
		try {
			matcher.identifyStart(ByteBuffer.wrap(faceTemplateBytes));
			List<Templates> frs = dbRecordList;
			
			int maxScore = 0;
			int maxIndex = 0;
			int i = 0;
			for (Templates faceRecord : frs) {
				int score = matcher.identifyNext(ByteBuffer.wrap(faceRecord.getFaceTempalte()));
				if (maxScore < score){
					maxScore = score;
					maxIndex = i;
				}
				i++;
			}
			if (maxScore > 0) {
				record = frs.get(maxIndex);
				recordList.add(record);
			}
			matcher.identifyEnd();
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		return recordList;
	}

}
