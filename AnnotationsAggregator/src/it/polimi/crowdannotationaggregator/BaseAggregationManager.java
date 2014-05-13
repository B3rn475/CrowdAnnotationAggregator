/**
 * 
 */
package it.polimi.crowdannotationaggregator;

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

	public interface OnBaseProcessListener<A extends BaseAnnotation> extends OnProcessListener<A, Content>{
		
	}
}
