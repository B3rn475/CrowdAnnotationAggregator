/**
 * 
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
		postCamparePair(annotation, annotation.value * estimation.value);
	}

}
