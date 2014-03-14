package it.polimi.annotationsaggregator.bool;

import java.util.Collection;
import java.util.Dictionary;

import it.polimi.annotationsaggregator.Annotator;
import it.polimi.annotationsaggregator.BaseLinearAggregator;
import it.polimi.annotationsaggregator.Content;

public final class BooleanAggregator extends BaseLinearAggregator<BooleanAnnotation> {
	
	public BooleanAggregator(
			OnAggregationCompletedListener<BooleanAnnotation, Content> listener,
			Content content, Collection<BooleanAnnotation> container) {
		super(listener, content, container);
	}

	@Override
	protected final void sumAllAnnotations(Dictionary<Annotator, Double> weights) {
		double total = 0;
		for (BooleanAnnotation annotation : this){
			total += annotation.value * weights.get(annotation.annotator);
		}
		postSumAllAnnotations(weights, new BooleanAnnotation(content, Annotator.NONE, total));
	}

	@Override
	protected final void subtractAnnotation(BooleanAnnotation aggregatedAnnotation,
			BooleanAnnotation annotation, double weight) {
		postSubtractAnnotation(new BooleanAnnotation(content, annotation.annotator, aggregatedAnnotation.value - annotation.value * weight));
	}
}
