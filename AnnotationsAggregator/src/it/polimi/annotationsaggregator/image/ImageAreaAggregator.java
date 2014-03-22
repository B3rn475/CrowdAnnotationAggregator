/**
 * 
 */
package it.polimi.annotationsaggregator.image;

import java.util.Map;

import it.polimi.annotationsaggregator.Annotator;
import it.polimi.annotationsaggregator.LinearAggregator;

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
	protected ImageAreaAggregator(
			OnAggregationCompletedListener<ImageAreaAnnotation, ImageContent> listener,
			ImageContent content) {
		super(listener, content);
	}

	@Override
	protected void sumAllAnnotations(
			Map<ImageAreaAnnotation, Double> weights) {
		final int length = content.width*content.height;
		final double[] totalSum = new double[length];
		
		for (ImageAreaAnnotation a : this){
			final double weight = weights.get(a);
			for (int i=0; i<length; i++)
			{
				totalSum[i] += a.getPixel(i) * weight;
			}
		}
		postSumAllAnnotations(weights, new ImageAreaAnnotation(content, Annotator.NONE, totalSum));
	}

	@Override
	protected void subtractAnnotation(
			ImageAreaAnnotation aggregatedAnnotation,
			ImageAreaAnnotation annotation,
			double weight) {
		final int length = content.width*content.height;
		final double[] totalSum = new double[length];
		
		for (int i=0; i<length; i++)
		{
				totalSum[i] = aggregatedAnnotation.getPixel(i) - annotation.getPixel(i) * weight;
		}
		postSubtractAnnotation(new ImageAreaAnnotation(content, annotation.annotator, totalSum));
	}

}
