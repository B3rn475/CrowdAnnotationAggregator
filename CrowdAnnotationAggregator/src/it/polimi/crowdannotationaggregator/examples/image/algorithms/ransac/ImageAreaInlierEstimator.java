package it.polimi.crowdannotationaggregator.examples.image.algorithms.ransac;

import java.util.Map;

import it.polimi.crowdannotationaggregator.algorithms.ransac.InlierEstimator;
import it.polimi.crowdannotationaggregator.examples.image.models.ImageAreaAnnotation;
import it.polimi.crowdannotationaggregator.examples.image.models.ImageContent;
import it.polimi.crowdannotationaggregator.models.Annotator;

public class ImageAreaInlierEstimator extends InlierEstimator<ImageAreaAnnotation, ImageContent> {

	private final double threshold;
	private final double maxDistance;
	
	protected ImageAreaInlierEstimator(
			InlierEstimator.OnEstimationCompletedListener<ImageAreaAnnotation, ImageContent> listener,
			Annotator annotator,
			double maxDistance,
			double threshold) {
		super(listener, annotator);
		if (threshold < 0 || threshold > 1)
			throw new IllegalArgumentException("threshold must be between 0 and 1");
		if (maxDistance < 0 || maxDistance > 1)
			throw new IllegalArgumentException("maxDistance must be between 0 and 1");
		this.threshold = threshold;
		this.maxDistance = maxDistance;
	}

	@Override
	public void estimate(Map<ImageContent, ImageAreaAnnotation> model) {
		int ok = 0;
		int ko = 0;
		for (final ImageAreaAnnotation annotation : this){
			final ImageContent content = annotation.getContent();
			if (model.containsKey(content)){
				final ImageAreaAnnotation mAnnotation = model.get(content);
				int unionArea = 0;
				int intersectionArea = 0;
				final int length = annotation.getContent().getWidth() * annotation.getContent().getHeight();
				for (int i = 0; i < length; i++){
					if (annotation.getPixelValue(i) && mAnnotation.getPixelValue(i)){
						intersectionArea++;
					}
					if (annotation.getPixelValue(i) || mAnnotation.getPixelValue(i)){
						unionArea++;
					}
				}
				final double dIntersectionArea = intersectionArea;
				final double dUnionArea = unionArea;
				if (dIntersectionArea / dUnionArea > maxDistance){
					ok++;
				} else {
					ko++;
				}
			}
		}
		if (ok == 0 && ko == 0){
			postEstimate(false);
		} else {
			postEstimate(ok > (ok + ko) * threshold);
		}
	}

}
