/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator.examples.image.models;

import it.polimi.crowdannotationaggregator.models.Annotation;
import it.polimi.crowdannotationaggregator.models.Annotator;

import java.util.Arrays;

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
		if (image.length != content.getWidth() * content.getHeight())
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
		if (image.length != content.getWidth() * content.getHeight())
			throw new IllegalArgumentException("Image Size mismatch");
		this.image = new double[width*height];
		final int length = content.getWidth() * content.getHeight();
		for (int i=0; i<length; i++){
			this.image[i] = image[i]?1:-1;
		}
	}
	
	public double getPixel(int x, int y){
		return getPixel(y+x*getContent().getWidth());
	}
	
	public double getPixel(int index){
		return image[index];
	}
	
	public boolean getPixelValue(int x, int y){
		return getPixelValue(y+x*getContent().getWidth());
	}
	
	public boolean getPixelValue(int x, int y, double threshold){
		return getPixelValue(y+x*getContent().getWidth(), threshold);
	}
	
	public boolean getPixelValue(int index){
		return getPixelValue(index, 0);
	}
	
	public boolean getPixelValue(int index, double threshold){
		return image[index] >= threshold;
	}
}
