/**
 * 
 */
package it.polimi.annotationsaggregator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Aggregator class that allow to aggregate annotations.
 * @author B3rn475
 *
 * @param <A> AnnotationType
 */
public abstract class Aggregator<A extends Annotation<C, ?>, C extends Content> implements Collection<A> {
	public final C content;

	protected final OnAggregationCompletedListener<A, C> listener;

	private final Collection<A> annotations = Collections.synchronizedCollection(new ArrayList<A>());

	private boolean isFinal = false;
	private long countDown = 0;
	private final ConcurrentHashMap<Annotator, A> estimated = new ConcurrentHashMap<Annotator, A>();

	/**
	 * Build a new aggregator
	 * @param listener an objects that listen on events of the aggregator
	 * @param content Content that is going to be aggregated by this object
	 */
	protected Aggregator(OnAggregationCompletedListener<A, C> listener,
			C content) {
		if (listener == null)
			throw new IllegalArgumentException("The listener cannot be null");
		if (content == null)
			throw new IllegalArgumentException("The content cannot be null");
		this.listener = listener;
		this.content = content;
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

	/**
	 * This method can be Overloaded by derived classes. 
	 * It is called before the beginning of the aggregation.
	 * It can be used to initialize assets needed during the aggregation phase.
	 * It must call postInitializingAggregation(Map<A, Double> weights) at the end of the initialization
	 * 
	 * @param weights
	 */
	protected void initializingAggregation(Map<A, Double> weights) {
		postInitializingAggregation(weights);
	}
	
	/**
	 * This method can be Overloaded by derived classes. 
	 * It is called at the end of the aggregation.
	 * It can be used to tear down the assets used.
	 * It must call postEndingAggregation(Map<A, Double> weights) at the end.
	 * 
	 * @param weights
	 */
	protected void endingAggregation(){
		postEndingAggregation();
	}
	
	/**
	 * This method must be called at the end of the initialization initializingAggregation(Map<A, Double> weights)
	 * @param weights
	 */
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
	
	/**
	 * This method must be called at the end of the tear down of the assets endingAggregation()
	 */
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
		
		estimated.put(annotator, aggregatedAnnotation); // do this first to be sure to be the last
		
		synchronized (this) {
			countDown--;
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

	/**
	 * Add an Annotation to the collection to be aggregated. It must has the same content of the Aggregator
	 */
	@Override
	public boolean add(A e) {
		if (!e.content.equals(content))
			throw new IllegalArgumentException("The annotation must be of the same content of the Aggregator");
		return annotations.add(e);
	}

	/**
	 * Add a group of Annotations to the collection to be aggregated. It must has the same content of the Aggregator
	 */
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
