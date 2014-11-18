/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting;

import it.polimi.crowdannotationaggregator.models.BaseAnnotation;
import it.polimi.crowdannotationaggregator.models.Content;

/**
 * This class is just a wrapper of the aggregation manager that force 
 * the content to be of the base class Content
 * and the annotator to be of the base class Annotator
 * 
 * It can be used to avoid to define Content and Annotator over and over again in the templates
 * 
 * @author B3rn475
 *
 */
public abstract class BaseAggregationManager<A extends BaseAnnotation> extends AggregationManager<A, Content> {

	public BaseAggregationManager(
			OnBaseProcessListener<A> listener,
			BaseAggregatorFactory<A> aggregatorFactory,
			CoherenceEstimatorFactory<A, Content> estimatorFactory, double threshold,
			int maxIterations) {
		super(listener, aggregatorFactory, estimatorFactory, threshold, maxIterations);
	}

	public interface OnBaseProcessListener<A extends BaseAnnotation> extends AggregationManager.OnProcessListener<A, Content>{
		
	}
}
