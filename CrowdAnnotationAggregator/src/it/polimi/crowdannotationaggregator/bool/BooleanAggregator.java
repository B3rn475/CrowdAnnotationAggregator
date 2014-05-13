package it.polimi.crowdannotationaggregator.bool;

import it.polimi.crowdannotationaggregator.Annotator;
import it.polimi.crowdannotationaggregator.BaseLinearAggregator;
import it.polimi.crowdannotationaggregator.Content;

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