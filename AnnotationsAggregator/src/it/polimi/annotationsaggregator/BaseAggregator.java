/**
 * 
 */
package it.polimi.annotationsaggregator;

import java.util.Collection;

/**
 * @author B3rn475
 *
 */
public abstract class BaseAggregator<A extends BaseAnnotation> extends Aggregator<A, Content> {

	protected BaseAggregator(
			OnAggregationCompletedListener<A, Content> listener,
			Content content, Collection<A> container) {
		super(listener, content, container);
	}
	
	public interface OnBaseAggregationCompletedListener<A extends BaseAnnotation> extends OnAggregationCompletedListener<A, Content>{
		
	}

}
