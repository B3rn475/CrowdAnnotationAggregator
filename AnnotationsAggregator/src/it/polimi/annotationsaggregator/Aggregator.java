/**
 * 
 */
package it.polimi.annotationsaggregator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Aggregator class that allow to aggregate annotations.
 * @author B3rn475
 *
 * @param <A> AnnotationType
 */
public abstract class Aggregator<A extends Annotation<C, ?>, C extends Content> implements Collection<A> {
	public final C content;

	protected final OnAggregationCompletedListener<A, C> listener;

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
	protected Aggregator(OnAggregationCompletedListener<A, C> listener,
			C content, Collection<A> container) {
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
			Map<A, Double> weights);

	protected void initializingAggregation(Map<A, Double> weights) {
		postInitializingAggregation(weights);
	}
	protected void endingAggregation(){
		postEndingAggregation();
	}
	
	protected final void postInitializingAggregation(Map<A, Double> weights){
		estimated.clear();
		if (isFinal)
		{
			aggregate(Annotator.NONE, weights);
		} else {
			for (A annotation : annotations) {
				aggregate(annotation.annotator, weights);
			}
		}
	}
	
	protected final void postEndingAggregation(){
		if (isFinal) {
			listener.onFinalAggregationCompleted(this, estimated.get(Annotator.NONE));
		} else {
			HashMap<A, A> aggregatedAnnotations = new HashMap<A,A>();
			for (A annotation : annotations) {
				aggregatedAnnotations.put(annotation, estimated.get(annotation.annotator));
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
		final boolean ending;
		
		synchronized (this) {
			countDown--;

			estimated.put(annotator, aggregatedAnnotation);
			
			ending = countDown == 0;
		}
		
		if (ending) {
			endingAggregation();
		}
	}

	/**
	 * Request the aggregation the current annotations
	 * @param weights
	 */
	public final void aggregate(Map<A, Double> weights) {
		isFinal = false;
		countDown = annotations.size();
		initializingAggregation(weights);
	}

	/**
	 * Request the final aggregation based on all the annotations
	 * @param weights
	 */
	public final void aggregateFinal(Map<A, Double> weights) {
		isFinal = true;
		countDown = 1;
		initializingAggregation(weights);
	}

	/**
	 * Interface that allow to listen on events on the aggregator
	 * @author B3rn475
	 *
	 * @param <A>
	 */
	public interface OnAggregationCompletedListener<A extends Annotation<C, ?>, C extends Content> {
		public void onAggregationCompleted(Aggregator<A,C> sender,
				Map<A,A> aggregatedAnnotations);

		public void onFinalAggregationCompleted(Aggregator<A,C> sender,
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
		for (A a : c){
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
