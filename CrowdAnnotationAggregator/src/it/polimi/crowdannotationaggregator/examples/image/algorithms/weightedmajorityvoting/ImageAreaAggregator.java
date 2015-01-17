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

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.function.IntToDoubleFunction;

import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.LinearAggregator;
import it.polimi.crowdannotationaggregator.examples.image.models.ImageAreaAnnotation;
import it.polimi.crowdannotationaggregator.examples.image.models.ImageContent;
import it.polimi.crowdannotationaggregator.models.Annotator;

/**
 * @author B3rn475
 *
 */
public final class ImageAreaAggregator extends LinearAggregator<ImageAreaAnnotation, ImageContent> {
	
	private final ExecutorService executor;
	
	/**
	 * 
	 * @param listener
	 * @param content
	 * @param container
	 */
	public ImageAreaAggregator(
			OnAggregationCompletedListener<ImageAreaAnnotation, ImageContent> listener,
			ImageContent content,
			ExecutorService executor) {
		super(listener, content);
		this.executor = executor;
	}
	
	public ImageAreaAggregator(
			OnAggregationCompletedListener<ImageAreaAnnotation, ImageContent> listener,
			ImageContent content) {
		super(listener, content);
		this.executor = null;
	}

	@Override
	protected void sumAllAnnotations() {
		if (executor == null){
			sumAllAnnotationsInternal();
		} else {
			final ImageAreaAggregator self = this;
			executor.execute(new Runnable() {
				
				@Override
				public void run() {
					self.sumAllAnnotationsInternal();
				}
			});
		}
	}
		
	private void sumAllAnnotationsInternal() {
		final ImageAreaAnnotation annotation;
		{
			final int length = getContent().getWidth() * getContent().getHeight();
			final double[] totalSum = new double[length];
			final double totalWeight = getTotalWeight();
			
			final Collection<ImageAreaAnnotation> self = this;
			
			Arrays.parallelSetAll(totalSum, new IntToDoubleFunction() {
				
				@Override
				public double applyAsDouble(int i) {
					double ret = 0;
					for (ImageAreaAnnotation a : self){
						final double weight = getWeights().get(a);
						ret += a.getPixel(i) * weight;
					}
					return ret / totalWeight;
				}
			});
			annotation = new ImageAreaAnnotation(getContent(), Annotator.NONE, totalSum);
		}
		
		postSumAllAnnotations(annotation);
	}

	@Override
	protected void subtractAnnotation(
			final ImageAreaAnnotation aggregatedAnnotation,
			final ImageAreaAnnotation annotation,
			final double weight) {
		if (executor == null){
			subtractAnnotationInternal(aggregatedAnnotation, annotation, weight);
		} else {
			final ImageAreaAggregator self = this;
			executor.execute(new Runnable() {
				
				@Override
				public void run() {
					self.subtractAnnotationInternal(aggregatedAnnotation, annotation, weight);
				}
			});
		}
	}
	
	private void subtractAnnotationInternal(
			final ImageAreaAnnotation aggregatedAnnotation,
			final ImageAreaAnnotation annotation,
			final double weight) {
		final ImageAreaAnnotation subtracted;
		{
			final int length = getContent().getWidth() * getContent().getHeight();
			final double[] totalSum = new double[length];
			final double totalWeight = getTotalWeight();
			
			Arrays.parallelSetAll(totalSum, new IntToDoubleFunction() {
				
				@Override
				public double applyAsDouble(int i) {
					return (aggregatedAnnotation.getPixel(i) * totalWeight - annotation.getPixel(i) * weight) / (totalWeight - weight);
				}
			});
			subtracted = new ImageAreaAnnotation(getContent(), annotation.getAnnotator(), totalSum);
		}
		postSubtractAnnotation(subtracted);
	}

}
