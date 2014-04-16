/**
 * 
 */
package it.polimi.annotationsaggregator.bool;

import it.polimi.annotationsaggregator.Aggregator;
import it.polimi.annotationsaggregator.Aggregator.OnAggregationCompletedListener;
import it.polimi.annotationsaggregator.Annotator;
import it.polimi.annotationsaggregator.BaseAggregatorFactory;
import it.polimi.annotationsaggregator.CoherenceEstimator;
import it.polimi.annotationsaggregator.CoherenceEstimator.OnEstimationCompletedListener;
import it.polimi.annotationsaggregator.CoherenceEstimatorFactory;
import it.polimi.annotationsaggregator.Content;
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
