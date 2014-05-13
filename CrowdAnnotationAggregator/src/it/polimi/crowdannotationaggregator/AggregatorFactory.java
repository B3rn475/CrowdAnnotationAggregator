/**
 * 
 */
package it.polimi.crowdannotationaggregator;

import it.polimi.crowdannotationaggregator.Aggregator.OnAggregationCompletedListener;

/**
 * This class allow the AggregationManager to initialize an Aggregator without knowing the exact Class
 * 
 * @author b3rn475
 *
 */
public interface AggregatorFactory<A extends Annotation<C, ?>, C extends Content> {
	public Aggregator<A,C> buildAggregator(OnAggregationCompletedListener<A,C> listener, C content);
}