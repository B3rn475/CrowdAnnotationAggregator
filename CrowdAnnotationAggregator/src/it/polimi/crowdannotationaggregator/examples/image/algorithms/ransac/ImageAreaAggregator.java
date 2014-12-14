package it.polimi.crowdannotationaggregator.examples.image.algorithms.ransac;

import java.util.Arrays;
import java.util.Set;

import it.polimi.crowdannotationaggregator.algorithms.ransac.Aggregator;
import it.polimi.crowdannotationaggregator.examples.image.models.ImageAreaAnnotation;
import it.polimi.crowdannotationaggregator.examples.image.models.ImageContent;
import it.polimi.crowdannotationaggregator.models.Annotator;

public class ImageAreaAggregator extends Aggregator<ImageAreaAnnotation, ImageContent> {

	protected ImageAreaAggregator(
			Aggregator.OnAggregationCompletedListener<ImageAreaAnnotation, ImageContent> listener,
			ImageContent content) {
		super(listener, content);
	}

	@Override
	public void aggregate(Set<Annotator> annotators) {
		if (annotators.size() == 0){
			postAggregate();
		} else {
			final int length = getContent().getHeight() * getContent().getWidth();
			double[] vector = new double[length];
			
			Arrays.fill(vector, 0);
			
			int n = 0;
			for (ImageAreaAnnotation annotation : this){
				if (!annotators.contains(annotation.getAnnotator()))
					continue;
				for (int i = 0; i < length; i++){
					vector[i] += annotation.getPixel(i);
				}
				n++;
			}
			
			if (n == 0){
				postAggregate();
			} else {
				for (int i = 0; i < length; i++){
					vector[i] /= n;
				}
				postAggregate(new ImageAreaAnnotation(getContent(), Annotator.NONE, vector));
			}
		}
	}
	
}
