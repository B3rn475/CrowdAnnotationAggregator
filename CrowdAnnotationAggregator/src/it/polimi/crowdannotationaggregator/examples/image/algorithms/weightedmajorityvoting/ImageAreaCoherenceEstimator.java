/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator.examples.image.algorithms.weightedmajorityvoting;

import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.LinearCoherenceEstimator;
import it.polimi.crowdannotationaggregator.examples.image.models.ImageAreaAnnotation;
import it.polimi.crowdannotationaggregator.examples.image.models.ImageContent;
import it.polimi.crowdannotationaggregator.models.Annotator;

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
		final int length = annotation.getContent().width * annotation.getContent().height;
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
		postComparePair(annotation.getContent(), dIntersectionArea / dUnionArea);
	}

}
