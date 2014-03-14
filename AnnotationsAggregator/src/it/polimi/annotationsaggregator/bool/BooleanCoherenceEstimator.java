/**
 * 
 */
package it.polimi.annotationsaggregator.bool;

import java.util.Collection;

import it.polimi.annotationsaggregator.Annotator;
import it.polimi.annotationsaggregator.LinearCoherenceEstimator;
import it.polimi.annotationsaggregator.Pair;

/**
 * @author b3rn475
 *
 */
public final class BooleanCoherenceEstimator extends LinearCoherenceEstimator<BooleanAnnotation> {

	public BooleanCoherenceEstimator(
			it.polimi.annotationsaggregator.CoherenceEstimator.OnEstimationCompletedListener<BooleanAnnotation> listener,
			Annotator annotator, Collection<Pair<BooleanAnnotation>> pairs) {
		super(listener, annotator, pairs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected final void comparePair(Pair<BooleanAnnotation> pair) {
		postCamparePair(pair.annotation.value * pair.estimation.value);
	}

}
