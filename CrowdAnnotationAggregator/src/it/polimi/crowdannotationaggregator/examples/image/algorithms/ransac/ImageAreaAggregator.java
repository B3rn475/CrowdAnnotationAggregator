package it.polimi.crowdannotationaggregator.examples.image.algorithms.ransac;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.IntToDoubleFunction;

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
			
			int n = 0;
			for (ImageAreaAnnotation annotation : this){
				if (!annotators.contains(annotation.getAnnotator()))
					continue;
				n++;
			}
			
			if (n == 0){
				postAggregate();
			} else {
				
				final Collection<ImageAreaAnnotation> self = this;
				final int tot = n;
				
				Arrays.parallelSetAll(vector, new IntToDoubleFunction() {
					
					@Override
					public double applyAsDouble(int i) {
						double ret = 0;
						for (ImageAreaAnnotation annotation : self){
							if (!annotators.contains(annotation.getAnnotator()))
								continue;
							ret += annotation.getPixel(i);
						}
						return ret / tot;
					}
				});
				postAggregate(new ImageAreaAnnotation(getContent(), Annotator.NONE, vector));
			}
		}
	}
	
}
