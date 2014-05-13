/**
 * 
 */
package it.polimi.crowdannotationaggregator.bool;

import it.polimi.crowdannotationaggregator.BaseAggregationManager;

/**
 * @author b3rn475
 *
 */
public final class BooleanAggregationManager extends BaseAggregationManager<BooleanAnnotation> {
	
	final static BooleanFactory factory = new BooleanFactory();
	
	public BooleanAggregationManager(
			OnBaseProcessListener<BooleanAnnotation> listener,
			double threshold, int maxIterations) {
		super(listener, factory, factory, threshold, maxIterations);
	}

}
