/**
 * 
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
