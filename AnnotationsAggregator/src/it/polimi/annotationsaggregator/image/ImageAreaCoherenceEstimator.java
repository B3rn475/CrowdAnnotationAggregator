/**
 * 
 */
package it.polimi.annotationsaggregator.image;

import java.util.Map;

import it.polimi.annotationsaggregator.Annotator;
import it.polimi.annotationsaggregator.LinearCoherenceEstimator;

/**
 * @author B3rn475
 *
 */
public final class ImageAreaCoherenceEstimator extends LinearCoherenceEstimator<ImageAreaAnnotation> {

	public ImageAreaCoherenceEstimator(
			it.polimi.annotationsaggregator.CoherenceEstimator.OnEstimationCompletedListener<ImageAreaAnnotation> listener,
			Annotator annotator, Map<ImageAreaAnnotation, ImageAreaAnnotation> container) {
		super(listener, annotator, container);
	}

	@Override
	protected void comparePair(final ImageAreaAnnotation annotation, final ImageAreaAnnotation estimation) {
		int unionArea = 0;
		int intersectionArea = 0;
		final int length = annotation.content.width * annotation.content.height;
		for (int i = 0; i < length; i++){
			if (annotation.getPixelValue(i) && estimation.getPixelValue(i)){
				intersectionArea++;
			}
			if (annotation.getPixelValue(i) || estimation.getPixelValue(i)){
				unionArea++;
			}
		}
		final double dIntersectionArea = intersectionArea;
		final double dUnionArea = unionArea;
		postCamparePair(annotation, dIntersectionArea / dUnionArea);
	}

}
