/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator.examples.image.algorithms.weightedmajorityvoting;

import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.LinearAggregator;
import it.polimi.crowdannotationaggregator.examples.image.models.ImageAreaAnnotation;
import it.polimi.crowdannotationaggregator.examples.image.models.ImageContent;
import it.polimi.crowdannotationaggregator.models.Annotator;

/**
 * @author B3rn475
 *
 */
public final class ImageAreaAggregator extends LinearAggregator<ImageAreaAnnotation, ImageContent> {
	
	/**
	 * 
	 * @param listener
	 * @param content
	 * @param container
	 */
	public ImageAreaAggregator(
			OnAggregationCompletedListener<ImageAreaAnnotation, ImageContent> listener,
			ImageContent content) {
		super(listener, content);
	}

	@Override
	protected void sumAllAnnotations() {
		final int length = getContent().width*getContent().height;
		final double[] totalSum = new double[length];
		
		for (ImageAreaAnnotation a : this){
			final double weight = getWeights().get(a);
			for (int i=0; i<length; i++)
			{
				totalSum[i] += a.getPixel(i) * weight;
			}
		}
		postSumAllAnnotations(new ImageAreaAnnotation(getContent(), Annotator.NONE, totalSum));
	}

	@Override
	protected void subtractAnnotation(
			ImageAreaAnnotation aggregatedAnnotation,
			ImageAreaAnnotation annotation,
			double weight) {
		final int length = getContent().width*getContent().height;
		final double[] totalSum = new double[length];
		
		for (int i=0; i<length; i++)
		{
				totalSum[i] = aggregatedAnnotation.getPixel(i) - annotation.getPixel(i) * weight;
		}
		postSubtractAnnotation(new ImageAreaAnnotation(getContent(), annotation.getAnnotator(), totalSum));
	}

}
