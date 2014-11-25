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

import it.polimi.crowdannotationaggregator.algorithms.ransac.Aggregator;
import it.polimi.crowdannotationaggregator.algorithms.ransac.Aggregator.OnAggregationCompletedListener;
import it.polimi.crowdannotationaggregator.algorithms.ransac.AggregatorFactory;
import it.polimi.crowdannotationaggregator.algorithms.ransac.InlierEstimator;
import it.polimi.crowdannotationaggregator.algorithms.ransac.InlierEstimator.OnEstimationCompletedListener;
import it.polimi.crowdannotationaggregator.algorithms.ransac.InlierEstimatorFactory;
import it.polimi.crowdannotationaggregator.examples.bool.models.BooleanAnnotation;
import it.polimi.crowdannotationaggregator.models.Annotator;
import it.polimi.crowdannotationaggregator.models.Content;

/**
 * @author b3rn475
 *
 */
public final class BooleanFactory implements AggregatorFactory<BooleanAnnotation, Content>,
		InlierEstimatorFactory<BooleanAnnotation, Content> {

	@Override
	public InlierEstimator<BooleanAnnotation, Content> buildEstimator(
			OnEstimationCompletedListener<BooleanAnnotation, Content> listener,
			Annotator annotator) {
		return new BooleanInlierEstimator(listener, annotator);
	}

	@Override
	public Aggregator<BooleanAnnotation, Content> buildAggregator(
			OnAggregationCompletedListener<BooleanAnnotation, Content> listener,
			Content content) {
		return new BooleanAggregator(listener, content);
	}
}
