package it.polimi.annotationsaggregator.bool;

import it.polimi.annotationsaggregator.Annotator;
import it.polimi.annotationsaggregator.BaseLinearAggregator;
import it.polimi.annotationsaggregator.Content;

public final class BooleanAggregator extends BaseLinearAggregator<BooleanAnnotation> {
	
	public BooleanAggregator(
			OnAggregationCompletedListener<BooleanAnnotation, Content> listener,
			Content content) {
		super(listener, content);
	}

	@Override
	protected final void sumAllAnnotations() {
		double total = 0;
		for (BooleanAnnotation annotation : this){
			total += annotation.value * getWeights().get(annotation);
		}
		postSumAllAnnotations(new BooleanAnnotation(content, Annotator.NONE, total));
	}

	@Override
	protected final void subtractAnnotation(BooleanAnnotation aggregatedAnnotation,
			BooleanAnnotation annotation, double weight) {
		postSubtractAnnotation(new BooleanAnnotation(content, annotation.annotator, aggregatedAnnotation.value - annotation.value * weight));
	}
}
