/**
 * 
 */
package it.polimi.crowdannotationaggregator.image;

import it.polimi.crowdannotationaggregator.Annotator;
import it.polimi.crowdannotationaggregator.LinearCoherenceEstimator;

/**
 * @author B3rn475
 *
 */
public final class ImageAreaCoherenceEstimator extends LinearCoherenceEstimator<ImageAreaAnnotation, ImageContent> {

	public ImageAreaCoherenceEstimator(
			OnEstimationCompletedListener<ImageAreaAnnotation, ImageContent> listener,
			Annotator annotator) {
		super(listener, annotator);
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