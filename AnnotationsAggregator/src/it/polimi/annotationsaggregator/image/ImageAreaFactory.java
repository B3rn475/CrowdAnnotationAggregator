/**
 * 
 */
package it.polimi.annotationsaggregator.image;

import it.polimi.annotationsaggregator.Aggregator;
import it.polimi.annotationsaggregator.Aggregator.OnAggregationCompletedListener;
import it.polimi.annotationsaggregator.AggregatorFactory;
import it.polimi.annotationsaggregator.Annotator;
import it.polimi.annotationsaggregator.CoherenceEstimator;
import it.polimi.annotationsaggregator.CoherenceEstimator.OnEstimationCompletedListener;
import it.polimi.annotationsaggregator.CoherenceEstimatorFactory;

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
