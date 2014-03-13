/**
 * 
 */
package it.polimi.annotationsaggregator;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;

/**
 * @author b3rn475
 *
 */
public abstract class Aggregator<A extends Annotation> implements Collection<A> {
	public final Content content;
	
	protected final OnAggregationCompletedListener<A> listener;
	
	private final Collection<A> annotations;
	
	
	protected Aggregator(OnAggregationCompletedListener<A> listener, Content content, Collection<A> container){
		this.listener = listener;
		this.content = content;
		this.annotations = container;
	}
	
	public abstract void aggregate(Dictionary<Annotator, Double> weights);
	
	public interface OnAggregationCompletedListener<A extends Annotation>{
		public void onAggregationCompleted(Aggregator<A> sender, Collection<Pair<A>> aggregatedAnnotations);
	}

	@Override
	public boolean add(A e) {
		return annotations.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends A> c) {
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
