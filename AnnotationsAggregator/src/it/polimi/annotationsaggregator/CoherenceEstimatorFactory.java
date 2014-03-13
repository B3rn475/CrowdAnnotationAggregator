/**
 * 
 */
package it.polimi.annotationsaggregator;

import it.polimi.annotationsaggregator.CoherenceEstimator.OnEstimationCompletedListener;

/**
 * @author b3rn475
 *
 */
public interface CoherenceEstimatorFactory<A extends Annotation> {
	public CoherenceEstimator<A> buildEstimator(OnEstimationCompletedListener<A> manager, Annotator annotator);
}
