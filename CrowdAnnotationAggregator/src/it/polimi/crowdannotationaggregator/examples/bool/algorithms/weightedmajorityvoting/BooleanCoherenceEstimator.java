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

import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.LinearCoherenceEstimator;
import it.polimi.crowdannotationaggregator.examples.bool.models.BooleanAnnotation;
import it.polimi.crowdannotationaggregator.models.Annotator;
import it.polimi.crowdannotationaggregator.models.Content;

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
		postComparePair(annotation.getContent(), annotation.value * estimation.value);
	}

}
