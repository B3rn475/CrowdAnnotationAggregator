/**
 * 
 */
package it.polimi.annotationsaggregator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is a Cohernece Estimator. That allow to generate a weight based on the pairs (annotation . estimation) for each content annotated by the user
 * 
 * This class can be asynchronous
 * 
 * @author B3rn475
 *
 * @param <A> AnnotationType
 */
public abstract class CoherenceEstimator<A extends Annotation<?, ?>> implements Map<A,A> {
	public final Annotator annotator;
	protected final OnEstimationCompletedListener<A> listener;
	private final Map<A, A> pairs;
	private final HashMap<A, Double> estimatedWeights = new HashMap<A, Double>();
	
	private int countDown = 0;
	
	/**
	 * Builds a new Estimator
	 * 
	 * @param listener Object that listen on events on the 
	 * @param annotator the Annotator we are estimating
	 * @param container the container of the pairs
	 */
	public CoherenceEstimator(OnEstimationCompletedListener<A> listener, Annotator annotator, Map<A,A> container){
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
	 * Estimate the weight of an annotation
	 * This method must be implemented by the derived classes
	 * @param skip
	 */
	protected abstract void estimate(A skip);
	
	/**
	 * Start the estimation.
	 */
	public final void estimate(){
		estimatedWeights.clear();
		countDown = this.size();
		initializingEstimation();
	}
	
	protected void initializingEstimation() {
		postInitializingEstimation();
	}
	
	protected void endingEstimation(){
		postEndingEstimation();
	}
	
	@SuppressWarnings("unchecked")
	protected final void postEndingEstimation(){
		listener.onEstimationCompleted(this, (Map<A, Double>) estimatedWeights.clone());
	}

	protected final void postInitializingEstimation() {
		for (A annotation : this.keySet()){
			estimate(annotation);
		}
	}

	/**
	 * This method must be called by derived classes at the end on the estimation
	 * @param weight
	 */
	protected final void postEstimation(A annotation, double weight){
		final boolean ending;
		
		synchronized (this) {
			countDown--;
			
			estimatedWeights.put(annotation, weight);
			
			ending = countDown == 0;
		}
		
		if (ending){
			endingEstimation();
		}
	}
	
	/**
	 * Interface for an object that can listen on events on the CoherenceEstimator
	 * @author B3rn475
	 *
	 * @param <A> AnnotationType
	 */
	public interface OnEstimationCompletedListener<A extends Annotation<?, ?>>{
		public void onEstimationCompleted(CoherenceEstimator<A> sender, Map<A, Double> estimatedWeights);
	}

	@Override
	public void clear() {
		pairs.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return pairs.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return pairs.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<A, A>> entrySet() {
		return pairs.entrySet();
	}

	@Override
	public A get(Object key) {
		return pairs.get(key);
	}

	@Override
	public boolean isEmpty() {
		return pairs.isEmpty();
	}

	@Override
	public Set<A> keySet() {
		return pairs.keySet();
	}
	
	public Set<A> annotationSet() {
		return pairs.keySet();
	}

	@Override
	public A put(A annotation, A estimation) {
		if (annotation == null)
			throw new IllegalArgumentException("annotation cannot be null");
		if (estimation == null)
			throw new IllegalArgumentException("estimation cannot be null");
		if (annotation.content != estimation.content)
			throw new IllegalArgumentException("annotation and estimation must be related to the same content");
		if (annotation.annotator != estimation.annotator)
			throw new IllegalArgumentException("annotation and estimation must be related to the same annotator");
		return pairs.put(annotation, estimation);
	}

	@Override
	public void putAll(Map<? extends A, ? extends A> m) {
		for (Entry<? extends A, ? extends A> entry : m.entrySet()){
			final A annotation = entry.getKey();
			final A estimation = entry.getValue();
			if (annotation == null)
				throw new IllegalArgumentException("annotation cannot be null");
			if (estimation == null)
				throw new IllegalArgumentException("estimation cannot be null");
			if (annotation.content != estimation.content)
				throw new IllegalArgumentException("annotation and estimation must be related to the same content");
			if (annotation.annotator != estimation.annotator)
				throw new IllegalArgumentException("annotation and estimation must be related to the same annotator");
		}
		pairs.putAll(m);
	}

	@Override
	public A remove(Object key) {
		return pairs.remove(key);
	}

	@Override
	public int size() {
		return pairs.size();
	}

	@Override
	public Collection<A> values() {
		return pairs.values();
	}
}
