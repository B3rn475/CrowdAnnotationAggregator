/**
 * 
 */
package it.polimi.annotationsaggregator;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author B3rn475
 *
 */
public abstract class LinearAggregator<A extends Annotation> extends Aggregator<A> {

	private A aggregatedAnnotation = null;
	private Hashtable<Annotator, A> lookup = null;
	
	protected LinearAggregator(
			it.polimi.annotationsaggregator.Aggregator.OnAggregationCompletedListener<A> listener,
			Content content, Collection<A> container) {
		super(listener, content, container);
	}

	@Override
	protected final void aggregate(Annotator skip,
			Dictionary<Annotator, Double> weights) {
		if (skip.equals(Annotator.NONE) || !lookup.containsKey(skip)) {
			postAggregate(skip, aggregatedAnnotation);
		} else {
			subtractAnnotation(aggregatedAnnotation, lookup.get(skip), weights.get(skip));
		}
	}

	@Override
	protected final void initializingAggregation(Dictionary<Annotator, Double> weights) {
		lookup = new Hashtable<Annotator, A>();
		for (A a : this){
			lookup.put(a.annotator, a);
		}
		sumAllAnnotations(weights);
	}

	protected abstract void sumAllAnnotations(Dictionary<Annotator, Double> weights);
	protected abstract void subtractAnnotation(A aggregatedAnnotation, A annotation, double weight);

	protected final void postSumAllAnnotations(Dictionary<Annotator, Double> weights, A aggregatedAnnotation){
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
