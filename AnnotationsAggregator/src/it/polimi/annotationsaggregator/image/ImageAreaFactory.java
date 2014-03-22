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
import it.polimi.annotationsaggregator.Content;

/**
 * @author B3rn475
 *
 */
public final class ImageAreaFactory implements CoherenceEstimatorFactory<ImageAreaAnnotation>,
		AggregatorFactory<ImageAreaAnnotation, ImageContent> {

	/**
	 * 
	 */
	public ImageAreaFactory() {
	}

	@Override
	public Aggregator<ImageAreaAnnotation, ImageContent> buildAggregator(
			OnAggregationCompletedListener<ImageAreaAnnotation, ImageContent> listener,
			Content content) {
		return null;
	}

	@Override
	public CoherenceEstimator<ImageAreaAnnotation> buildEstimator(
			OnEstimationCompletedListener<ImageAreaAnnotation> listener,
			Annotator annotator) {
		return new ImageAreaCoherenceEstimator(listener, annotator);
	}

}
