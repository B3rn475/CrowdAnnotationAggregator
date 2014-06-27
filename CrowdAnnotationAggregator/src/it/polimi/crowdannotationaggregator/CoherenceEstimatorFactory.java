/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator;

import it.polimi.crowdannotationaggregator.CoherenceEstimator.OnEstimationCompletedListener;

/**
 * This class allow the AggregationManager to initialize a new CoherenceEstimation
 * 
 * @author b3rn475
 *
 */
public interface CoherenceEstimatorFactory<A extends Annotation<C, ?>, C extends Content> {
	public CoherenceEstimator<A,C> buildEstimator(OnEstimationCompletedListener<A,C> listener, Annotator annotator);
}
