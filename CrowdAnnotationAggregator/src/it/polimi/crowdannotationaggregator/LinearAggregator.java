/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author B3rn475
 *
 */
public abstract class LinearAggregator<A extends Annotation<C, ?>, C extends Content> extends Aggregator<A, C> {

	private A aggregatedAnnotation = null;
	private final Map<Annotator, A> lookup = new HashMap<Annotator, A>();
	
	protected LinearAggregator(
			OnAggregationCompletedListener<A, C> listener,
			C content) {
		super(listener, content);
	}

	@Override
	protected final void aggregate(Annotator skip) {
		if (skip.equals(Annotator.NONE) || !lookup.containsKey(skip)) {
			postAggregate(aggregatedAnnotation);
		} else {
			final A annotation = lookup.get(skip);
			subtractAnnotation(aggregatedAnnotation, annotation, getWeights().get(annotation));
		}
	}

	@Override
	protected final void initializingAggregation() {
		for (A a : this){
			lookup.put(a.getAnnotator(), a);
		}
		sumAllAnnotations();
	}

	protected abstract void sumAllAnnotations();
	protected abstract void subtractAnnotation(A aggregatedAnnotation, A annotation, double weight);

	protected final void postSumAllAnnotations(A aggregatedAnnotation){
		this.aggregatedAnnotation = aggregatedAnnotation;
		postInitializingAggregation();
	}
	
	protected final void postSubtractAnnotation(A annotation){
		postAggregate(annotation);
	}
	
	@Override
	protected final void endingAggregation() {
		aggregatedAnnotation = null;
		lookup.clear();
		postEndingAggregation();
	}

}
