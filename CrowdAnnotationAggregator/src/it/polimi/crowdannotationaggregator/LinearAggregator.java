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
import java.util.Map.Entry;

/**
 * @author B3rn475
 *
 */
public abstract class LinearAggregator<A extends Annotation<C, ?>, C extends Content> extends Aggregator<A, C> {

	private A aggregatedAnnotation = null;
	private final Map<Annotator, A> lookup = new HashMap<Annotator, A>();
	private Double totalWeight = null;
	
	protected LinearAggregator(
			OnAggregationCompletedListener<A, C> listener,
			C content) {
		super(listener, content);
	}

	public double getTotalWeight() {
		if (totalWeight == null) {
			double value = 0;
			for (Entry<A, Double> entry : getWeights().entrySet()) {
				value += entry.getValue();
			}
			totalWeight = value;
		}
		return totalWeight;
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
		totalWeight = null;
		sumAllAnnotations();
	}

	/**
	 * This callback must call postSumAllAnnotations()
	 */
	protected abstract void sumAllAnnotations();
	/**
	 * This callback must call postSubtractAnnotation()
	 * @param aggregatedAnnotation
	 * @param annotation
	 * @param weight
	 */
	protected abstract void subtractAnnotation(A aggregatedAnnotation, A annotation, double weight);

	/**
	 * This callback must be called at the end of subtractAnnotation()
	 * The annotation must have Annotator.NONE as Annotator
	 * @param annotation
	 */
	protected final void postSumAllAnnotations(A aggregatedAnnotation){
		this.aggregatedAnnotation = aggregatedAnnotation;
		postInitializingAggregation();
	}
	
	/**
	 * This callback must be called at the end of subtractAnnotation()
	 * The annotation must have the same Annotator of the subtracted Annotation
	 * @param annotation
	 */
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
