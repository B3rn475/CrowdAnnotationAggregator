/**
 * 
 */
package it.polimi.annotationsaggregator.bool;

import java.util.Collection;

import it.polimi.annotationsaggregator.Annotator;
import it.polimi.annotationsaggregator.CoherenceEstimator;
import it.polimi.annotationsaggregator.Pair;

/**
 * @author b3rn475
 *
 */
public final class BooleanCoherenceEstimator extends CoherenceEstimator<BooleanAnnotation> {

	public BooleanCoherenceEstimator(
			it.polimi.annotationsaggregator.CoherenceEstimator.OnEstimationCompletedListener<BooleanAnnotation> listener,
			Annotator annotator, Collection<Pair<BooleanAnnotation>> pairs) {
		super(listener, annotator, pairs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void estimate() {
		double total = 0;
		for (Pair<BooleanAnnotation> pair : this){
			total += pair.annotation.value * pair.estimation.value;
		}
		postEstimation(total);
	}

}
