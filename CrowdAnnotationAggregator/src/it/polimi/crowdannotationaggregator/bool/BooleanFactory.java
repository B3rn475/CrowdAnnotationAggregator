/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator.bool;

import it.polimi.crowdannotationaggregator.Aggregator;
import it.polimi.crowdannotationaggregator.Aggregator.OnAggregationCompletedListener;
import it.polimi.crowdannotationaggregator.Annotator;
import it.polimi.crowdannotationaggregator.BaseAggregatorFactory;
import it.polimi.crowdannotationaggregator.CoherenceEstimator;
import it.polimi.crowdannotationaggregator.CoherenceEstimator.OnEstimationCompletedListener;
import it.polimi.crowdannotationaggregator.CoherenceEstimatorFactory;
import it.polimi.crowdannotationaggregator.Content;
/**
 * @author b3rn475
 *
 */
public final class BooleanFactory implements BaseAggregatorFactory<BooleanAnnotation>,
		CoherenceEstimatorFactory<BooleanAnnotation, Content> {

	@Override
	public CoherenceEstimator<BooleanAnnotation, Content> buildEstimator(
			OnEstimationCompletedListener<BooleanAnnotation, Content> manager, Annotator annotator) {
		return new BooleanCoherenceEstimator(manager, annotator);
	}

	@Override
	public Aggregator<BooleanAnnotation, Content> buildAggregator(
			OnAggregationCompletedListener<BooleanAnnotation, Content> listener,
			Content content) {
		return new BooleanAggregator(listener, content);
	}
}
