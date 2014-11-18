/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator.examples.bool.algorithms.weightedmajorityvoting;

import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.BaseAggregationManager;
import it.polimi.crowdannotationaggregator.examples.bool.models.BooleanAnnotation;

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
