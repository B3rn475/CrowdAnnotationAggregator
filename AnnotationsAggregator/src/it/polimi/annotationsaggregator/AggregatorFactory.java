/**
 * 
 */
package it.polimi.annotationsaggregator;

import it.polimi.annotationsaggregator.Aggregator.OnAggregationCompletedListener;

/**
 * @author b3rn475
 *
 */
public interface AggregatorFactory<A extends Annotation<C, ?>, C extends Content> {
	public Aggregator<A,C> buildAggregator(OnAggregationCompletedListener<A,C> listener, Content content);
}
