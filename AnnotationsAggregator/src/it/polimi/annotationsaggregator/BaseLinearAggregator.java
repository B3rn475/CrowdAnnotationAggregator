/**
 * 
 */
package it.polimi.annotationsaggregator;

import java.util.Collection;

/**
 * @author B3rn475
 *
 */
public abstract class BaseLinearAggregator<A extends BaseAnnotation> extends LinearAggregator<A, Content> {

	protected BaseLinearAggregator(
			OnAggregationCompletedListener<A, Content> listener,
			Content content, Collection<A> container) {
		super(listener, content, container);
	}

	public interface OnBaseLinearAggregationCompletedListener<A extends BaseAnnotation> extends OnAggregationCompletedListener<A, Content>{
		
	}
}
