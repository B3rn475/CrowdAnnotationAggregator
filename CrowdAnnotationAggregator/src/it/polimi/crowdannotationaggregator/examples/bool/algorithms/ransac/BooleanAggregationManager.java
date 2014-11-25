/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator.examples.bool.algorithms.ransac;

import it.polimi.crowdannotationaggregator.algorithms.ransac.AggregationManager;
import it.polimi.crowdannotationaggregator.algorithms.ransac.AggregatorFactory;
import it.polimi.crowdannotationaggregator.algorithms.ransac.InlierEstimatorFactory;
import it.polimi.crowdannotationaggregator.algorithms.ransac.AggregationManager.OnProcessListener;
import it.polimi.crowdannotationaggregator.examples.bool.models.BooleanAnnotation;
import it.polimi.crowdannotationaggregator.models.Content;

/**
 * @author b3rn475
 *
 */
public final class BooleanAggregationManager extends AggregationManager<BooleanAnnotation, Content> {
	
	final static BooleanFactory factory = new BooleanFactory();
	
	public BooleanAggregationManager(OnProcessListener<BooleanAnnotation, Content> listener,
			double randomSelect, double minInliers, int maxIterations) {
		super(listener, factory, factory, randomSelect, minInliers, maxIterations);
	}

}
