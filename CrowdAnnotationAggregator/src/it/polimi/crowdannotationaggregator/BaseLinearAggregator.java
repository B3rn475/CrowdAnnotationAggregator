/**
 * 
 */
package it.polimi.crowdannotationaggregator;

/**
 * This is a wrapper of the class LinearAggregator that force 
 * the content to be of the base class Content
 * and the annotator to be of the base class Annotator
 * 
 * This can be used to avoid to type Content and Annotator over and over again in the templates
 * 
 * @author B3rn475
 *
 */
public abstract class BaseLinearAggregator<A extends BaseAnnotation> extends LinearAggregator<A, Content> {

	protected BaseLinearAggregator(
			OnAggregationCompletedListener<A, Content> listener,
			Content content) {
		super(listener, content);
	}

	public interface OnBaseLinearAggregationCompletedListener<A extends BaseAnnotation> extends OnAggregationCompletedListener<A, Content>{
		
	}
}
