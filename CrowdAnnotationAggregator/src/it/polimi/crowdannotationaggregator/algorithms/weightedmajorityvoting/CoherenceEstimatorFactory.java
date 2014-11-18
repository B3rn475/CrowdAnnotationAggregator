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

import it.polimi.crowdannotationaggregator.models.Annotation;
import it.polimi.crowdannotationaggregator.models.Annotator;
import it.polimi.crowdannotationaggregator.models.Content;

/**
 * This class allow the AggregationManager to initialize a new CoherenceEstimation
 * 
 * @author b3rn475
 *
 */
public interface CoherenceEstimatorFactory<A extends Annotation<C, ?>, C extends Content> {
	public CoherenceEstimator<A,C> buildEstimator(CoherenceEstimator.OnEstimationCompletedListener<A,C> listener, Annotator annotator);
}
