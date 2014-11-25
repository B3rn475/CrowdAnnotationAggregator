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

import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.Aggregator;
import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.BaseAggregatorFactory;
import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.CoherenceEstimator;
import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.CoherenceEstimatorFactory;
import it.polimi.crowdannotationaggregator.examples.bool.models.BooleanAnnotation;
import it.polimi.crowdannotationaggregator.models.Annotator;
import it.polimi.crowdannotationaggregator.models.Content;

/**
 * @author b3rn475
 *
 */
public final class BooleanFactory implements BaseAggregatorFactory<BooleanAnnotation>,
		CoherenceEstimatorFactory<BooleanAnnotation, Content> {

	@Override
	public CoherenceEstimator<BooleanAnnotation, Content> buildEstimator(
			CoherenceEstimator.OnEstimationCompletedListener<BooleanAnnotation, Content> manager, Annotator annotator) {
		return new BooleanCoherenceEstimator(manager, annotator);
	}

	@Override
	public Aggregator<BooleanAnnotation, Content> buildAggregator(
			Aggregator.OnAggregationCompletedListener<BooleanAnnotation, Content> listener,
			Content content) {
		return new BooleanAggregator(listener, content);
	}
}
