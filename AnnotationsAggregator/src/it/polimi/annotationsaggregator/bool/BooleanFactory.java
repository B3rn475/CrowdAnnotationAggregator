/**
 * 
 */
package it.polimi.annotationsaggregator.bool;

import java.util.ArrayList;

import it.polimi.annotationsaggregator.Aggregator;
import it.polimi.annotationsaggregator.Aggregator.OnAggregationCompletedListener;
import it.polimi.annotationsaggregator.AggregatorFactory;
import it.polimi.annotationsaggregator.Annotator;
import it.polimi.annotationsaggregator.CoherenceEstimator;
import it.polimi.annotationsaggregator.CoherenceEstimator.OnEstimationCompletedListener;
import it.polimi.annotationsaggregator.CoherenceEstimatorFactory;
import it.polimi.annotationsaggregator.Content;
import it.polimi.annotationsaggregator.Pair;

/**
 * @author b3rn475
 *
 */
public final class BooleanFactory implements AggregatorFactory<BooleanAnnotation>,
		CoherenceEstimatorFactory<BooleanAnnotation> {

	@Override
	public CoherenceEstimator<BooleanAnnotation> buildEstimator(
			OnEstimationCompletedListener<BooleanAnnotation> manager, Annotator annotator) {
		return new BooleanCoherenceEstimator(manager, annotator, new ArrayList<Pair<BooleanAnnotation>>());
	}

	@Override
	public Aggregator<BooleanAnnotation> buildAggregator(
			OnAggregationCompletedListener<BooleanAnnotation> listener,
			Content content){
		return new BooleanAggregator(listener, content, new ArrayList<BooleanAnnotation>());
	}

}
