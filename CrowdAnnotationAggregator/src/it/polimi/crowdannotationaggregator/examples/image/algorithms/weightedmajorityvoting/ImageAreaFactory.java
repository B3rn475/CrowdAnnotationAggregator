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

import java.util.concurrent.ExecutorService;

import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.Aggregator;
import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.AggregatorFactory;
import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.CoherenceEstimator;
import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.CoherenceEstimatorFactory;
import it.polimi.crowdannotationaggregator.examples.image.algorithms.weightedmajorityvoting.ImageAreaAggregator;
import it.polimi.crowdannotationaggregator.examples.image.models.ImageAreaAnnotation;
import it.polimi.crowdannotationaggregator.examples.image.models.ImageContent;
import it.polimi.crowdannotationaggregator.models.Annotator;

/**
 * @author B3rn475
 *
 */
public final class ImageAreaFactory implements CoherenceEstimatorFactory<ImageAreaAnnotation, ImageContent>,
		AggregatorFactory<ImageAreaAnnotation, ImageContent> {

	private final ExecutorService executor;
	
	public ImageAreaFactory(ExecutorService executor){
		this.executor = executor;
	}
	
	public ImageAreaFactory(){
		this.executor = null;
	}
	
	
	@Override
	public Aggregator<ImageAreaAnnotation, ImageContent> buildAggregator(
			Aggregator.OnAggregationCompletedListener<ImageAreaAnnotation, ImageContent> listener,
			ImageContent content) {
		return new ImageAreaAggregator(listener, content, executor);
	}

	@Override
	public CoherenceEstimator<ImageAreaAnnotation, ImageContent> buildEstimator(
			CoherenceEstimator.OnEstimationCompletedListener<ImageAreaAnnotation, ImageContent> listener,
			Annotator annotator) {
		return new ImageAreaCoherenceEstimator(listener, annotator, executor);
	}

}
