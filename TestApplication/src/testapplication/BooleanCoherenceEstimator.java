/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package testapplication;

import it.polimi.crowdannotationaggregator.Annotator;
import it.polimi.crowdannotationaggregator.Content;
import it.polimi.crowdannotationaggregator.LinearCoherenceEstimator;

/**
 * @author b3rn475
 *
 */
public final class BooleanCoherenceEstimator extends LinearCoherenceEstimator<BooleanAnnotation, Content> {

	public BooleanCoherenceEstimator(
			OnEstimationCompletedListener<BooleanAnnotation, Content> listener,
			Annotator annotator) {
		super(listener, annotator);
	}

	@Override
	protected final void comparePair(BooleanAnnotation annotation, BooleanAnnotation estimation) {
		postComparePair(annotation.getContent(), annotation.getDoubleValue() * estimation.getDoubleValue());
	}

}
