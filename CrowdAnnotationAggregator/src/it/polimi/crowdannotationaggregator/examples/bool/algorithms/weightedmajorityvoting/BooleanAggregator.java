/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator.examples.bool.algorithms.weightedmajorityvoting;

import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.BaseLinearAggregator;
import it.polimi.crowdannotationaggregator.examples.bool.models.BooleanAnnotation;
import it.polimi.crowdannotationaggregator.models.Annotator;
import it.polimi.crowdannotationaggregator.models.Content;

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
			total += annotation.getDoubleValue() * getWeights().get(annotation);
		}
		postSumAllAnnotations(new BooleanAnnotation(getContent(), Annotator.NONE, total));
	}

	@Override
	protected final void subtractAnnotation(BooleanAnnotation aggregatedAnnotation,
			BooleanAnnotation annotation, double weight) {
		postSubtractAnnotation(new BooleanAnnotation(getContent(), annotation.getAnnotator(), aggregatedAnnotation.getDoubleValue() - annotation.getDoubleValue() * weight));
	}
}
