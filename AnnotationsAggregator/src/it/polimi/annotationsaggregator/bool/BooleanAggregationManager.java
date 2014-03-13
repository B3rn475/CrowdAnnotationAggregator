/**
 * 
 */
package it.polimi.annotationsaggregator.bool;

import it.polimi.annotationsaggregator.AggregationManager;

/**
 * @author b3rn475
 *
 */
public final class BooleanAggregationManager extends AggregationManager<BooleanAnnotation> {
	
	final static BooleanFactory factory = new BooleanFactory();
	
	public BooleanAggregationManager(
			it.polimi.annotationsaggregator.AggregationManager.OnProcessListener<BooleanAnnotation> listener,
			double threshold, int maxIterations) {
		super(listener, factory, factory, threshold, maxIterations);
	}

}
