/**
 * 
 */
package it.polimi.annotationsaggregator.image;

import java.util.Collection;

import it.polimi.annotationsaggregator.Annotator;
import it.polimi.annotationsaggregator.LinearCoherenceEstimator;
import it.polimi.annotationsaggregator.Pair;

/**
 * @author B3rn475
 *
 */
public final class ImageAreaCoherenceEstimator extends LinearCoherenceEstimator<ImageAreaAnnotation> {

	public ImageAreaCoherenceEstimator(
			it.polimi.annotationsaggregator.CoherenceEstimator.OnEstimationCompletedListener<ImageAreaAnnotation> listener,
			Annotator annotator, Collection<Pair<ImageAreaAnnotation>> container) {
		super(listener, annotator, container);
	}

	@Override
	protected void comparePair(Pair<ImageAreaAnnotation> pair) {
		int unionArea = 0;
		int intersectionArea = 0;
		int length = pair.annotation.content.width * pair.annotation.content.height;
		for (int i = 0; i < length; i++){
			if (pair.annotation.getPixelValue(i) && pair.estimation.getPixelValue(i)){
				intersectionArea++;
			}
			if (pair.annotation.getPixelValue(i) || pair.estimation.getPixelValue(i)){
				unionArea++;
			}
		}
		final double dIntersectionArea = intersectionArea;
		final double dUnionArea = unionArea;
		postCamparePair(dIntersectionArea / dUnionArea);
	}

}
