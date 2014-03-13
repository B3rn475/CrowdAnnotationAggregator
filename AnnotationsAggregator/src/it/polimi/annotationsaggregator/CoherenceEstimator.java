/**
 * 
 */
package it.polimi.annotationsaggregator;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author B3rn475
 *
 */
public abstract class CoherenceEstimator<A extends Annotation> implements Collection<Pair<A>> {
	public final Annotator annotator;
	protected final OnEstimationCompletedListener<A> listener;
	private final Collection<Pair<A>> pairs;
	
	public CoherenceEstimator(OnEstimationCompletedListener<A> listener, Annotator annotator, Collection<Pair<A>> pairs){
		this.listener = listener;
		this.annotator = annotator;
		this.pairs = pairs;
	}
	
	public abstract void estimate();
	
	protected void postEstimation(double weight){
		listener.onEstimationCompleted(this, weight);
	}
	
	public interface OnEstimationCompletedListener<A extends Annotation>{
		public void onEstimationCompleted(CoherenceEstimator<A> sender, double weight);
	}

	@Override
	public boolean add(Pair<A> e) {
		return pairs.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends Pair<A>> c) {
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
