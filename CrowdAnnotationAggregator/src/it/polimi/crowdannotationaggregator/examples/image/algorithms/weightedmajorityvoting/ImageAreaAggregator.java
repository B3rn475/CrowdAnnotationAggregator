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
		final int length = getContent().getWidth() * getContent().getHeight();
		final double[] totalSum = new double[length];
		final double totalWeight = getTotalWeight();
		
		for (ImageAreaAnnotation a : this){
			final double weight = getWeights().get(a);
			for (int i=0; i<length; i++)
			{
				totalSum[i] += a.getPixel(i) * weight;
			}
		}
		
		for (int i=0; i<length; i++)
		{
			totalSum[i] /= totalWeight;
		}
		
		postSumAllAnnotations(new ImageAreaAnnotation(getContent(), Annotator.NONE, totalSum));
	}

	@Override
	protected void subtractAnnotation(
			final ImageAreaAnnotation aggregatedAnnotation,
			final ImageAreaAnnotation annotation,
			final double weight) {
		final int length = getContent().getWidth() * getContent().getHeight();
		final double[] totalSum = new double[length];
		final double totalWeight = getTotalWeight();
		
		for (int i=0; i<length; i++)
		{
				totalSum[i] = (aggregatedAnnotation.getPixel(i) * totalWeight - annotation.getPixel(i) * weight) / (totalWeight - weight);
		}
		
		postSubtractAnnotation(new ImageAreaAnnotation(getContent(), annotation.getAnnotator(), totalSum));
	}

}
