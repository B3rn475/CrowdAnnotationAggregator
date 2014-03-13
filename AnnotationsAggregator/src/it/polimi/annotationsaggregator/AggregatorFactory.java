/**
 * 
 */
package it.polimi.annotationsaggregator;

import it.polimi.annotationsaggregator.Aggregator.OnAggregationCompletedListener;

/**
 * @author b3rn475
 *
 */
public interface AggregatorFactory<A extends Annotation> {
	public Aggregator<A> buildAggregator(OnAggregationCompletedListener<A> manager, Content content);
}
