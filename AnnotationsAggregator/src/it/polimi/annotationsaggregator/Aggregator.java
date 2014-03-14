/**
 * 
 */
package it.polimi.annotationsaggregator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Aggregator class that allow to aggregate annotations.
 * @author B3rn475
 *
 * @param <A> AnnotationType
 */
public abstract class Aggregator<A extends Annotation> implements Collection<A> {
	public final Content content;

	protected final OnAggregationCompletedListener<A> listener;

	private final Collection<A> annotations;

	private boolean isFinal = false;
	private long countDown = 0;
	private final Hashtable<Annotator, A> estimated = new Hashtable<Annotator, A>();

	/**
	 * Build a new aggregator
	 * @param listener an objects that listen on events of the aggregator
	 * @param content Content that is going to be aggregated by this object
	 * @param container An container for the annotations
	 */
	protected Aggregator(OnAggregationCompletedListener<A> listener,
			Content content, Collection<A> container) {
		if (listener == null)
			throw new IllegalArgumentException("The listener cannot be null");
		if (content == null)
			throw new IllegalArgumentException("The content cannot be null");
		if (container == null)
			throw new IllegalArgumentException("The container cannot be null");
		this.listener = listener;
		this.content = content;
		this.annotations = container;
	}

	/**
	 * Method that need to be implemented by specialized classes
	 * 
	 * It can be asynchronous.
	 * 
	 * It must call postAggregate to inform the aggregator that it has ended.
	 * 
	 * @param skip annotator to skip
	 * @param weights weights to use the process
	 */
	protected abstract void aggregate(Annotator skip,
			Dictionary<Annotator, Double> weights);

	protected void initializingAggregation(Dictionary<Annotator, Double> weights) {
		postInitializingAggregation(weights);
	}
	protected void endingAggregation(){
		postEndingAggregation();
	}
	
	protected final void postInitializingAggregation(Dictionary<Annotator, Double> weights){
		estimated.clear();
		if (isFinal)
		{
			countDown = 1;
			aggregate(Annotator.NONE, weights);
		} else {
			countDown = annotations.size();
			for (A annotation : annotations) {
				aggregate(annotation.annotator, weights);
			}
		}
	}
	
	protected final void postEndingAggregation(){
		if (isFinal) {
			listener.onFinalAggregationCompleted(this, estimated.get(Annotator.NONE));
		} else {
			Collection<Pair<A>> aggregatedAnnotations = new ArrayList<Pair<A>>(
					annotations.size());
			for (A annotation : annotations) {
				aggregatedAnnotations.add(new Pair<A>(annotation, estimated
						.get(annotation.annotator)));
			}
			listener.onAggregationCompleted(this, aggregatedAnnotations);
		}
	}
	
	/**
	 * Method to call at the end of each aggregation request
	 * @param annotator skipped annotator
	 * @param aggregatedAnnotation Aggregated annotation output of the process
	 */
	protected final void postAggregate(Annotator annotator, A aggregatedAnnotation) {
		countDown--;

		estimated.put(annotator, aggregatedAnnotation);
		
		if (countDown == 0) {
			endingAggregation();
		}
	}

	/**
	 * Request the aggregation the current annotations
	 * @param weights
	 */
	public final void aggregate(Dictionary<Annotator, Double> weights) {
		isFinal = false;
		initializingAggregation(weights);
	}

	/**
	 * Request the final aggregation based on all the annotations
	 * @param weights
	 */
	public final void aggregateFinal(Dictionary<Annotator, Double> weights) {
		isFinal = true;
		initializingAggregation(weights);
	}

	/**
	 * Interface that allow to listen on events on the aggregator
	 * @author B3rn475
	 *
	 * @param <A>
	 */
	public interface OnAggregationCompletedListener<A extends Annotation> {
		public void onAggregationCompleted(Aggregator<A> sender,
				Collection<Pair<A>> aggregatedAnnotations);

		public void onFinalAggregationCompleted(Aggregator<A> sender,
				A aggregatedAnnotation);
	}

	@Override
	public boolean add(A e) {
		if (!e.content.equals(content))
			throw new IllegalArgumentException("The annotation must be of the same content of the Aggregator");
		return annotations.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends A> c) {
		for (Annotation a : c){
			if (!a.content.equals(content))
				throw new IllegalArgumentException("The annotation must be of the same content of the Aggregator");
		}
		return annotations.addAll(c);
	}

	@Override
	public void clear() {
		annotations.clear();
	}

	@Override
	public boolean contains(Object o) {
		return annotations.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return annotations.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return annotations.isEmpty();
	}

	@Override
	public Iterator<A> iterator() {
		return annotations.iterator();
	}

	@Override
	public boolean remove(Object o) {
		return annotations.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return annotations.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return annotations.retainAll(c);
	}

	@Override
	public int size() {
		return annotations.size();
	}

	@Override
	public Object[] toArray() {
		return annotations.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return annotations.toArray(a);
	}
}
