/**
 * 
 */
package it.polimi.annotationsaggregator;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author B3rn475
 *
 */
public abstract class LinearAggregator<A extends Annotation<C, ?>, C extends Content> extends Aggregator<A, C> {

	private A aggregatedAnnotation = null;
	private Hashtable<Annotator, A> lookup = null;
	
	protected LinearAggregator(
			it.polimi.annotationsaggregator.Aggregator.OnAggregationCompletedListener<A, C> listener,
			C content, Collection<A> container) {
		super(listener, content, container);
	}

	@Override
	protected final void aggregate(Annotator skip,
			Map<A, Double> weights) {
		if (skip.equals(Annotator.NONE) || !lookup.containsKey(skip)) {
			postAggregate(skip, aggregatedAnnotation);
		} else {
			final A annotation = lookup.get(skip);
			subtractAnnotation(aggregatedAnnotation, annotation, weights.get(annotation));
		}
	}

	@Override
	protected final void initializingAggregation(Map<A, Double> weights) {
		lookup = new Hashtable<Annotator, A>();
		for (A a : this){
			lookup.put(a.annotator, a);
		}
		sumAllAnnotations(weights);
	}

	protected abstract void sumAllAnnotations(Map<A, Double> weights);
	protected abstract void subtractAnnotation(A aggregatedAnnotation, A annotation, double weight);

	protected final void postSumAllAnnotations(Map<A, Double> weights, A aggregatedAnnotation){
		this.aggregatedAnnotation = aggregatedAnnotation;
		postInitializingAggregation(weights);
	}
	
	protected final void postSubtractAnnotation(A annotation){
		postAggregate(annotation.annotator, annotation);
	}
	
	@Override
	protected final void endingAggregation() {
		aggregatedAnnotation = null;
		lookup = null;
		postEndingAggregation();
	}

}
