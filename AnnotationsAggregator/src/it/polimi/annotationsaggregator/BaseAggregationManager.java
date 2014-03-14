/**
 * 
 */
package it.polimi.annotationsaggregator;

/**
 * @author B3rn475
 *
 */
public abstract class BaseAggregationManager<A extends BaseAnnotation> extends AggregationManager<A, Content> {

	public BaseAggregationManager(
			OnBaseProcessListener<A> listener,
			BaseAggregatorFactory<A> aggregatorFactory,
			CoherenceEstimatorFactory<A> estimatorFactory, double threshold,
			int maxIterations) {
		super(listener, aggregatorFactory, estimatorFactory, threshold, maxIterations);
	}

	public interface OnBaseProcessListener<A extends BaseAnnotation> extends OnProcessListener<A, Content>{
		
	}
}
