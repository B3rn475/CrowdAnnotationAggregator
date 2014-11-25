/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator.algorithms.ransac;

import it.polimi.crowdannotationaggregator.models.Annotation;
import it.polimi.crowdannotationaggregator.models.Annotator;
import it.polimi.crowdannotationaggregator.models.Content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * Aggregator class that allow to aggregate annotations.
 * @author B3rn475
 *
 * @param <A> AnnotationType
 */
public abstract class Aggregator<A extends Annotation<C, ?>, C extends Content> implements Set<A> {
	private final C content;

	protected final OnAggregationCompletedListener<A, C> listener;

	private final Collection<A> annotations = Collections.synchronizedCollection(new ArrayList<A>());

	/**
	 * The Content the Aggregator is about
	 * @return
	 */
	public C getContent(){
		return content;
	}
	
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
	 */
	public abstract void aggregate(final Set<Annotator> annotators);
	
	/**
	 * Method to call at the end of each aggregation request
	 * @param aggregatedAnnotation Aggregated annotation output of the process
	 */
	protected final void postAggregate(A aggregatedAnnotation) {
		listener.onAggregationCompleted(this, aggregatedAnnotation);
	}
	
	/**
	 * Method to call at the end of each aggregation request
	 * @param aggregatedAnnotation Aggregated annotation output of the process
	 */
	protected final void postAggregate() {
		listener.onAggregationCompleted(this);
	}

	/**
	 * Interface that allow to listen on events on the aggregator
	 * @author B3rn475
	 *
	 * @param <A>
	 */
	public interface OnAggregationCompletedListener<A extends Annotation<C, ?>, C extends Content> {
		public void onAggregationCompleted(Aggregator<A,C> sender, A aggregatedAnnotation);
		public void onAggregationCompleted(Aggregator<A,C> sender);
	}

	/**
	 * Add an Annotation to the collection to be aggregated. It must has the same content of the Aggregator
	 */
	@Override
	public boolean add(A e) {
		if (!e.getContent().equals(content))
			throw new IllegalArgumentException("The annotation must be of the same content of the Aggregator");
		return annotations.add(e);
	}

	/**
	 * Add a group of Annotations to the collection to be aggregated. It must has the same content of the Aggregator
	 */
	@Override
	public boolean addAll(Collection<? extends A> c) {
		for (A a : c){
			if (!a.getContent().equals(content))
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
