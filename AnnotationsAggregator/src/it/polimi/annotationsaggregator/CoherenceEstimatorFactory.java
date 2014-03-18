/**
 * 
 */
package it.polimi.annotationsaggregator;

import it.polimi.annotationsaggregator.CoherenceEstimator.OnEstimationCompletedListener;

/**
 * This class allow the AggregationManager to initialize a new CoherenceEstimation
 * 
 * @author b3rn475
 *
 */
public interface CoherenceEstimatorFactory<A extends Annotation<?, ?>> {
	public CoherenceEstimator<A> buildEstimator(OnEstimationCompletedListener<A> listener, Annotator annotator);
}
