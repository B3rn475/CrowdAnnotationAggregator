/**
 * 
 */
package it.polimi.crowdannotationaggregator.image;

import java.util.Arrays;

import it.polimi.crowdannotationaggregator.Annotation;
import it.polimi.crowdannotationaggregator.Annotator;

/**
 * @author B3rn475
 *
 */
public class ImageAreaAnnotation extends Annotation<ImageContent, Annotator> {

	private final double[] image;
	
	/**
	 * @param content
	 * @param annotator
	 */
	public ImageAreaAnnotation(ImageContent content, Annotator annotator, double[] image) {
		super(content, annotator);
		if (image == null)
			throw new IllegalArgumentException("The image cannot be empty");
		if (image.length != content.width * content.height)
			throw new IllegalArgumentException("Image Size mismatch");
		this.image = Arrays.copyOf(image, image.length);
	}
	
	/**
	 * @param content
	 * @param annotator
	 */
	public ImageAreaAnnotation(ImageContent content, Annotator annotator, int width, int height, boolean[] image) {
		super(content, annotator);
		if (image == null)
			throw new IllegalArgumentException("The image cannot be empty");
		if (image.length != content.width * content.height)
			throw new IllegalArgumentException("Image Size mismatch");
		this.image = new double[width*height];
		final int length = content.width * content.height;
		for (int i=0; i<length; i++){
			this.image[i] = image[i]?1:-1;
		}
	}
	
	public double getPixel(int x, int y){
		return getPixel(y+x*content.height);
	}
	
	public double getPixel(int index){
		return image[index];
	}
	
	public boolean getPixelValue(int x, int y){
		return getPixelValue(y+x*content.height);
	}
	
	public boolean getPixelValue(int index){
		return image[index] >= 0;
	}
}
