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

	private final float[] image;
	
	/**
	 * @param content
	 * @param annotator
	 */
	public ImageAreaAnnotation(ImageContent content, Annotator annotator, double[] image) {
		this(content, annotator, toFloatArray(image));
	}
	
	/**
	 * @param content
	 * @param annotator
	 */
	public ImageAreaAnnotation(ImageContent content, Annotator annotator, float[] image) {
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
	public ImageAreaAnnotation(ImageContent content, Annotator annotator, boolean[] image) {
		super(content, annotator);
		if (image == null)
			throw new IllegalArgumentException("The image cannot be empty");
		if (image.length != content.getWidth() * content.getHeight())
			throw new IllegalArgumentException("Image Size mismatch");
		this.image = new float[content.getWidth() * content.getHeight()];
		final int length = content.getWidth() * content.getHeight();
		for (int i=0; i<length; i++){
			this.image[i] = image[i]?1:-1;
		}
	}
	
	public float getPixel(int x, int y){
		return getPixel(y+x*getContent().getWidth());
	}
	
	public float getPixel(int index){
		return image[index];
	}
	
	public boolean getPixelValue(int x, int y){
		return getPixelValue(y+x*getContent().getWidth());
	}
	
	public boolean getPixelValue(int x, int y, float threshold){
		return getPixelValue(y+x*getContent().getWidth(), threshold);
	}
	
	public boolean getPixelValue(int index){
		return getPixelValue(index, 0.0f);
	}
	
	public boolean getPixelValue(int index, double threshold){
		return getPixelValue(index, (float) threshold);
	}
	
	public boolean getPixelValue(int index, float threshold){
		return image[index] >= threshold;
	}
	
	private static float[] toFloatArray(double[] input){
		float[] output = new float[input.length];
		for (int i = 0; i < input.length; i++){
			output[i] = (float) input[i];
		}
		return output;
	}
}
