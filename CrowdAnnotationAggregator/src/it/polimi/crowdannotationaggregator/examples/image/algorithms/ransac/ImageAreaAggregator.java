package it.polimi.crowdannotationaggregator.examples.image.algorithms.ransac;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.IntToDoubleFunction;

import it.polimi.crowdannotationaggregator.algorithms.ransac.Aggregator;
import it.polimi.crowdannotationaggregator.examples.image.models.ImageAreaAnnotation;
import it.polimi.crowdannotationaggregator.examples.image.models.ImageContent;
import it.polimi.crowdannotationaggregator.models.Annotator;

public class ImageAreaAggregator extends Aggregator<ImageAreaAnnotation, ImageContent> {
	
	private final ExecutorService executor;

	protected ImageAreaAggregator(
			Aggregator.OnAggregationCompletedListener<ImageAreaAnnotation, ImageContent> listener,
			ImageContent content, ExecutorService executor) {
		super(listener, content);
		this.executor = executor;
	}
	
	protected ImageAreaAggregator(
			Aggregator.OnAggregationCompletedListener<ImageAreaAnnotation, ImageContent> listener,
			ImageContent content) {
		super(listener, content);
		this.executor = null;
	}

	@Override
	public void aggregate(final Set<Annotator> annotators) {
		if (executor == null){
			aggregateInternal(annotators);
		} else {
			final ImageAreaAggregator self = this; 
			executor.execute(new Runnable() {
				
				@Override
				public void run() {
					self.aggregateInternal(annotators);
				}
			});
		}
	}
	
	private void aggregateInternal(final Set<Annotator> annotators) {
		if (annotators.size() == 0){
			postAggregate();
		} else {
			int n = 0;
			for (ImageAreaAnnotation annotation : this){
				if (!annotators.contains(annotation.getAnnotator()))
					continue;
				n++;
			}
			
			if (n == 0){
				postAggregate();
			} else {
				final ImageAreaAnnotation annotation;
				{
					final int length = getContent().getHeight() * getContent().getWidth();
					final double[] vector = new double[length];
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
					annotation = new ImageAreaAnnotation(getContent(), Annotator.NONE, vector);
				}
				postAggregate(annotation);
			}
		}
	}
	
}
