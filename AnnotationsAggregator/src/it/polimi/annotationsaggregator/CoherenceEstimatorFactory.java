/**
 * 
 */
package it.polimi.annotationsaggregator;

/**
 * @author b3rn475
 *
 */
public interface CoherenceEstimatorFactory<A extends Annotation> {
	public CoherenceEstimator<A> buildEstimator(AggregationManager<A> manager, Annotator annotator);
}
