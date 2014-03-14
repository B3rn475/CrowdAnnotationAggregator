/**
 * 
 */
package it.polimi.annotationsaggregator.bool;

import java.util.Map;

import it.polimi.annotationsaggregator.Annotator;
import it.polimi.annotationsaggregator.LinearCoherenceEstimator;

/**
 * @author b3rn475
 *
 */
public final class BooleanCoherenceEstimator extends LinearCoherenceEstimator<BooleanAnnotation> {

	public BooleanCoherenceEstimator(
			it.polimi.annotationsaggregator.CoherenceEstimator.OnEstimationCompletedListener<BooleanAnnotation> listener,
			Annotator annotator, Map<BooleanAnnotation, BooleanAnnotation> pairs) {
		super(listener, annotator, pairs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected final void comparePair(BooleanAnnotation annotation, BooleanAnnotation estimation) {
		postCamparePair(annotation, annotation.value * estimation.value);
	}

}
