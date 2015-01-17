/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator.examples.image.algorithms.ransac;

import java.util.concurrent.ExecutorService;

import it.polimi.crowdannotationaggregator.algorithms.ransac.Aggregator;
import it.polimi.crowdannotationaggregator.algorithms.ransac.AggregatorFactory;
import it.polimi.crowdannotationaggregator.algorithms.ransac.InlierEstimator;
import it.polimi.crowdannotationaggregator.algorithms.ransac.InlierEstimatorFactory;
import it.polimi.crowdannotationaggregator.examples.image.models.ImageAreaAnnotation;
import it.polimi.crowdannotationaggregator.examples.image.models.ImageContent;
import it.polimi.crowdannotationaggregator.models.Annotator;

/**
 * @author B3rn475
 *
 */
public final class ImageAreaFactory implements InlierEstimatorFactory<ImageAreaAnnotation, ImageContent>,
		AggregatorFactory<ImageAreaAnnotation, ImageContent> {
	
	private final double threshold;
	private final double maxDistance;
	private final ExecutorService executor;
	
	public ImageAreaFactory(double maxDistance, double threshold, ExecutorService executor){
		if (threshold < 0 || threshold > 1)
			throw new IllegalArgumentException("threshold must be between 0 and 1");
		if (maxDistance < 0 || maxDistance > 1)
			throw new IllegalArgumentException("maxDistance must be between 0 and 1");
		this.threshold = threshold;
		this.maxDistance = maxDistance;
		this.executor = executor;
	}
	
	public ImageAreaFactory(double maxDistance, double threshold){
		if (threshold < 0 || threshold > 1)
			throw new IllegalArgumentException("threshold must be between 0 and 1");
		if (maxDistance < 0 || maxDistance > 1)
			throw new IllegalArgumentException("maxDistance must be between 0 and 1");
		this.threshold = threshold;
		this.maxDistance = maxDistance;
		this.executor = null;
	}

	@Override
	public Aggregator<ImageAreaAnnotation, ImageContent> buildAggregator(
			Aggregator.OnAggregationCompletedListener<ImageAreaAnnotation, ImageContent> listener,
			ImageContent content) {
		return new ImageAreaAggregator(listener, content, executor);
	}

	@Override
	public InlierEstimator<ImageAreaAnnotation, ImageContent> buildEstimator(
			InlierEstimator.OnEstimationCompletedListener<ImageAreaAnnotation, ImageContent> listener,
			Annotator annotator) {
		return new ImageAreaInlierEstimator(listener, annotator, maxDistance, threshold, executor);
	}

}
