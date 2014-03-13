/**
 * 
 */
package it.polimi.annotationsaggregator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 * @author b3rn475
 * 
 */
public abstract class Aggregator<A extends Annotation> implements Collection<A> {
	public final Content content;

	protected final OnAggregationCompletedListener<A> listener;

	private final Collection<A> annotations;

	private boolean isFinal = false;
	private long countDown = 0;
	private final Dictionary<Annotator, A> estimated = new Hashtable<Annotator, A>();

	protected Aggregator(OnAggregationCompletedListener<A> listener,
			Content content, Collection<A> container) {
		this.listener = listener;
		this.content = content;
		this.annotations = container;
	}

	protected abstract void aggregate(Set<Annotator> skip,
			Dictionary<Annotator, Double> weights);

	protected void postAggregate(Annotator annotator, A aggregatedAnnotation) {
		if (isFinal) {
			listener.onFinalAggregationCompleted(this, aggregatedAnnotation);
		} else {
			countDown--;
			estimated.put(annotator, aggregatedAnnotation);

			if (countDown == 0) {
				fire();
			}
		}
	}

	private void fire() {
		Collection<Pair<A>> aggregatedAnnotations = new ArrayList<Pair<A>>(
				annotations.size());
		for (A annotation : annotations) {
			aggregatedAnnotations.add(new Pair<A>(annotation, estimated
					.get(annotation.annotator)));
		}
		listener.onAggregationCompleted(this, aggregatedAnnotations);
	}

	public void aggregate(Dictionary<Annotator, Double> weights) {
		isFinal = false;
		countDown = annotations.size();
		for (A annotation : annotations) {
			Set<Annotator> skip = new HashSet<Annotator>();
			skip.add(annotation.annotator);
			aggregate(skip, weights);
		}
	}

	public void aggregateFinal(Dictionary<Annotator, Double> weights) {
		isFinal = true;
		Set<Annotator> skip = new HashSet<Annotator>();
		aggregate(skip, weights);
	}

	public interface OnAggregationCompletedListener<A extends Annotation> {
		public void onAggregationCompleted(Aggregator<A> sender,
				Collection<Pair<A>> aggregatedAnnotations);

		public void onFinalAggregationCompleted(Aggregator<A> sender,
				A aggregatedAnnotation);
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
