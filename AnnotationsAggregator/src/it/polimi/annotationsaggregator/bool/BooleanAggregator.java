package it.polimi.annotationsaggregator.bool;

import java.util.Collection;
import java.util.Dictionary;
import it.polimi.annotationsaggregator.Aggregator;
import it.polimi.annotationsaggregator.Annotator;
import it.polimi.annotationsaggregator.Content;

public final class BooleanAggregator extends Aggregator<BooleanAnnotation> {
	
	public BooleanAggregator(
			it.polimi.annotationsaggregator.Aggregator.OnAggregationCompletedListener<BooleanAnnotation> listener,
			Content content, Collection<BooleanAnnotation> container) {
		super(listener, content, container);
	}

	@Override
	protected void aggregate(Annotator skip,
			Dictionary<Annotator, Double> weights) {
		double total = 0;
		for (BooleanAnnotation annotation : this){
			if (skip.equals(annotation.annotator)) continue;
			Double weight = weights.get(annotation.annotator);
			if (weight == null) continue;
			total += total * weight;
		}
		postAggregate(skip, new BooleanAnnotation(content, skip, total));
	}

}
