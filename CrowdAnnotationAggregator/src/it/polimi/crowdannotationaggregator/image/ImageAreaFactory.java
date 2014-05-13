/**
 * 
 */
package it.polimi.crowdannotationaggregator.image;

import it.polimi.crowdannotationaggregator.Aggregator;
import it.polimi.crowdannotationaggregator.Aggregator.OnAggregationCompletedListener;
import it.polimi.crowdannotationaggregator.AggregatorFactory;
import it.polimi.crowdannotationaggregator.Annotator;
import it.polimi.crowdannotationaggregator.CoherenceEstimator;
import it.polimi.crowdannotationaggregator.CoherenceEstimator.OnEstimationCompletedListener;
import it.polimi.crowdannotationaggregator.CoherenceEstimatorFactory;

/**
 * @author B3rn475
 *
 */
public final class ImageAreaFactory implements CoherenceEstimatorFactory<ImageAreaAnnotation, ImageContent>,
		AggregatorFactory<ImageAreaAnnotation, ImageContent> {

	/**
	 * 
	 */
	public ImageAreaFactory() {
	}

	@Override
	public Aggregator<ImageAreaAnnotation, ImageContent> buildAggregator(
			OnAggregationCompletedListener<ImageAreaAnnotation, ImageContent> listener,
			ImageContent content) {
		return null;
	}

	@Override
	public CoherenceEstimator<ImageAreaAnnotation, ImageContent> buildEstimator(
			OnEstimationCompletedListener<ImageAreaAnnotation, ImageContent> listener,
			Annotator annotator) {
		return new ImageAreaCoherenceEstimator(listener, annotator);
	}

}
