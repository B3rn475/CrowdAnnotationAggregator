/**
 * 
 */
package it.polimi.annotationsaggregator;

import java.util.Collection;
import java.util.Iterator;

/**
 * This is a Cohernece Estimator. That allow to generate a weight based on the pairs (annotation . estimation) for each content annotated by the user
 * 
 * This class can be asynchronous
 * 
 * @author B3rn475
 *
 * @param <A> AnnotationType
 */
public abstract class CoherenceEstimator<A extends Annotation> implements Collection<Pair<A>> {
	public final Annotator annotator;
	protected final OnEstimationCompletedListener<A> listener;
	private final Collection<Pair<A>> pairs;
	
	/**
	 * Builds a new Estimator
	 * 
	 * @param listener Object that listen on events on the 
	 * @param annotator the Annotator we are estimating
	 * @param container the container of the pairs
	 */
	public CoherenceEstimator(OnEstimationCompletedListener<A> listener, Annotator annotator, Collection<Pair<A>> container){
		if (listener == null)
			throw new IllegalArgumentException("The listener cannot be null");
		if (annotator == null)
			throw new IllegalArgumentException("The annotator cannot be null");
		if (container == null)
			throw new IllegalArgumentException("The container cannot be null");
		this.listener = listener;
		this.annotator = annotator;
		this.pairs = container;
	}
	
	/**
	 * Start the estimation.
	 * This method must be implemented by the derived classes
	 */
	public abstract void estimate();
	
	/**
	 * This method must be called by derived classes at the end on the estimation
	 * @param weight
	 */
	protected void postEstimation(double weight){
		listener.onEstimationCompleted(this, weight);
	}
	
	/**
	 * Interface for an object that can listen on events on the CoherenceEstimator
	 * @author B3rn475
	 *
	 * @param <A> AnnotationType
	 */
	public interface OnEstimationCompletedListener<A extends Annotation>{
		public void onEstimationCompleted(CoherenceEstimator<A> sender, double weight);
	}

	@Override
	public boolean add(Pair<A> e) {
		if (!e.annotation.annotator.equals(annotator))
			throw new IllegalArgumentException("The annotation must be of the same annotator of the Estimator");
		if (!e.estimation.annotator.equals(annotator))
			throw new IllegalArgumentException("The estimation must be of the same annotator of the Estimator");
		return pairs.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends Pair<A>> c) {
		for (Pair<A> e : c){
			if (!e.annotation.annotator.equals(annotator))
				throw new IllegalArgumentException("The annotation must be of the same annotator of the Estimator");
			if (!e.estimation.annotator.equals(annotator))
				throw new IllegalArgumentException("The estimation must be of the same annotator of the Estimator");
		}
		return pairs.addAll(c);
	}

	@Override
	public void clear() {
		pairs.clear();
	}

	@Override
	public boolean contains(Object o) {
		return pairs.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return pairs.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return pairs.isEmpty();
	}

	@Override
	public Iterator<Pair<A>> iterator() {
		return pairs.iterator();
	}

	@Override
	public boolean remove(Object o) {
		return pairs.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return pairs.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return pairs.retainAll(c);
	}

	@Override
	public int size() {
		return pairs.size();
	}

	@Override
	public Object[] toArray() {
		return pairs.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return pairs.toArray(a);
	}
}
