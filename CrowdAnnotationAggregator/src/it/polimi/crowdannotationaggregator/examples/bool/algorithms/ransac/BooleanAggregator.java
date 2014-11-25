/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator.examples.bool.algorithms.ransac;

import java.util.Set;

import it.polimi.crowdannotationaggregator.algorithms.ransac.Aggregator;
import it.polimi.crowdannotationaggregator.examples.bool.models.BooleanAnnotation;
import it.polimi.crowdannotationaggregator.models.Annotator;
import it.polimi.crowdannotationaggregator.models.Content;

public final class BooleanAggregator extends Aggregator<BooleanAnnotation, Content> {
	
	public BooleanAggregator(
			Aggregator.OnAggregationCompletedListener<BooleanAnnotation, Content> listener,
			Content content) {
		super(listener, content);
	}

	@Override
	public void aggregate(Set<Annotator> annotators) {
		int mv = 0;
		int count = 0;
		for (final BooleanAnnotation annotation: this){
			if (annotators.contains(annotation.getAnnotator())){
				mv += annotation.getValue() ? 1 : -1 ;
				count++;
			}
		}
		if (count == 0){
			postAggregate();
		} else {
			postAggregate(new BooleanAnnotation(getContent(), Annotator.NONE, mv >= 0));
		}
	}
}
