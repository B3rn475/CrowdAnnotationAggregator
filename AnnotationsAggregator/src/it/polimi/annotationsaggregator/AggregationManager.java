/**
 * 
 */
package it.polimi.annotationsaggregator;

import it.polimi.annotationsaggregator.Aggregator.OnAggregationCompletedListener;
import it.polimi.annotationsaggregator.CoherenceEstimator.OnEstimationCompletedListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Templated Aggregation Manager that allow to Aggregate a general kind of Annotation
 * 
 * This class is meant to be asynchronous, it is so if aggregators and/or estimators are asyncronous
 * 
 * @author B3rn475
 *
 * @param <A> Annotation type
 */
public class AggregationManager<A extends Annotation<C, ?>, C extends Content> implements Map<A,Double>, OnEstimationCompletedListener<A>, OnAggregationCompletedListener<A,C> {
	private final OnProcessListener<A, C> listener;
	
	private final AggregatorFactory<A, C> aggregatorFactory;
	private final CoherenceEstimatorFactory<A> estimatorFactory;
	
	private final Map<A, Double> weights = new HashMap<A, Double>();
	private boolean isWorking = false;
	private final double threshold;
	private final int maxIterations;
	
	private int step = 0;
	private long countDown = 0;
	
	private final Map<A, Double> lastWeights = new HashMap<A, Double>();
	
	private final Map<C,Aggregator<A, C>> aggregators = new HashMap<C, Aggregator<A, C>>();
	private final Map<Annotator, CoherenceEstimator<A>> coherenceEstimators = new HashMap<Annotator, CoherenceEstimator<A>>();
	private final HashMap<C, A> finalAggregation = new HashMap<C, A>();
	
	/**
	 * Builder of the AggregatorManager
	 * 
	 * @param listener an object that listen for events of the process
	 * @param aggregatorFactory a factory object that allow to build an aggregator for the particular kind annotation
	 * @param estimatorFactory a factory object that allow to build an estimator for the particular kind annotation
	 * @param threshold weights threshold under which the process stops.
	 * @param maxIterations maximum number of iterations, in order to avoid infinite loops in case of non converting solutions
	 */
	public AggregationManager(OnProcessListener<A,C> listener, AggregatorFactory<A,C> aggregatorFactory, CoherenceEstimatorFactory<A> estimatorFactory, double threshold, int maxIterations) {
		if (listener == null)
			throw new IllegalArgumentException("Listener cannot be null");
		if (aggregatorFactory == null)
			throw new IllegalArgumentException("aggregatorFactory cannot be null");
		if (estimatorFactory == null)
			throw new IllegalArgumentException("estimatorFactory cannot be null");
		if (threshold < 0)
			throw new IllegalArgumentException("threshold cannot be less than 0");
		if (maxIterations < 1)
			throw new IllegalArgumentException("threshold cannot be less than 1");
		this.listener = listener;
		this.aggregatorFactory = aggregatorFactory;
		this.estimatorFactory = estimatorFactory;
		this.threshold = threshold;
		this.maxIterations = maxIterations;
	}
		
	/**
	 * If the manager is currently working
	 * @return
	 */
	public boolean isWorking(){
		return isWorking;
	}
	
	/**
	 * Starts the aggregation process
	 */
	public void startProcess(){
		isWorking = true;
		
		step = 0;
		
		//save last weights to test threshold
		lastWeights.putAll(weights);
		
		for (A annotation : weights.keySet()){
			final Aggregator<A,C> aggregator;
			if (aggregators.containsKey(annotation.content)){
				aggregator = aggregators.get(annotation.content);
			} else {
				aggregator = aggregatorFactory.buildAggregator(this, annotation.content);
				aggregators.put(annotation.content, aggregator);
			}
			aggregator.add(annotation);
			if (!coherenceEstimators.containsKey(annotation.annotator)){
				coherenceEstimators.put(annotation.annotator, estimatorFactory.buildEstimator(this, annotation.annotator));
			}
		}
		
		nextStep();
	}
	
	/**
	 * Start the next step of the aggregation process
	 */
	private void nextStep(){
		listener.onStepInitiated(this, step);
		step++;
		
		startAggregation();
	}
	
	/**
	 * Start the aggregation phase
	 */
	private void startAggregation(){
		countDown = aggregators.size();
		for(Aggregator<A,C> aggregator : aggregators.values()){
			aggregator.aggregate(weights);
		}
	}
	
	/**
	 * Start the estimation phase
	 */
	private void startEstimation(){
		countDown = coherenceEstimators.size();
		for (CoherenceEstimator<A> estimator : coherenceEstimators.values()){
			estimator.estimate();
		}
	}
	
	/**
	 * End of the step
	 */
	private void testCompletion(){
		normalizeWeights();
		
		if (step < maxIterations)
		{
			final double delta = computeDelta();
			if (delta > threshold){
				nextStep();
				return;
			}
		}
		
		startFinalAggregation();
	}

	/**
	 * Normalizes the weights of the annotators
	 */
	private void normalizeWeights(){
		double max = 0;
		for (double d : weights.values()){
			max = Math.max(max, Math.abs(d));
		}
		
		if (max == 0.0) return;
		
		for(A annotation : weights.keySet()){
			weights.put(annotation, weights.get(annotation) / max);
		}
	}
	
	/**
	 * Computes the delta of the current weights WRT the old weights
	 * @return
	 */
	private double computeDelta(){
		double delta = 0;
		
		for(A annotation : weights.keySet()){
			double weight = weights.get(annotation);
			double lastWeight = lastWeights.get(annotation);
			
			delta += Math.abs(weight - lastWeight);
		}
		
		return delta;
	}

	/**
	 * Initialize the final aggregation after convergence
	 */
	private void startFinalAggregation(){
		countDown = aggregators.size();
		for(Aggregator<A, C> aggregator : aggregators.values()){
			aggregator.aggregateFinal(weights);
		}
	}
	
	/**
	 * Listener of the aggregator completeness
	 */
	@Override
	public void onAggregationCompleted(Aggregator<A, C> sender, Map<A, A> aggregatedAnnotations) {
		final boolean ending;
		
		synchronized (aggregatedAnnotations) {
			countDown--;
			
			for (Entry<A,A> entry : aggregatedAnnotations.entrySet()){
				final A annotation = entry.getKey();
				final A estimation = entry.getValue();
				coherenceEstimators.get(annotation.annotator).put(annotation, estimation);
			}
			
			ending = countDown == 0;
		}
		
		if (ending){
			startEstimation();
		}
	}

	/**
	 * Listener of the weight estimation completion
	 */
	@Override
	public void onEstimationCompleted(CoherenceEstimator<A> sender, Map<A, Double> estimatedWeights) {
		final boolean ending;
		synchronized (this) {
			countDown--;
			
			for (Entry<A, Double> entry : estimatedWeights.entrySet()){
				weights.put(entry.getKey(), entry.getValue());
			}
			
			ending = countDown == 0;
		}
		
		if (ending){
			testCompletion();
		}
	}
	
	/**
	 * Listener on the final aggregation completion
	 */
	@Override
	public void onFinalAggregationCompleted(Aggregator<A, C> sender,
			A aggregatedAnnotation) {
		final boolean ending;
		
		synchronized (this) {
			countDown--;
			finalAggregation.put(aggregatedAnnotation.content,aggregatedAnnotation);
			
			ending = countDown == 0;
		}
		
		if (ending){
			isWorking = false;
			@SuppressWarnings("unchecked")
			Map<Content, A> tmp = (Map<Content, A>) finalAggregation.clone();
			listener.onAggregationEnded(this, tmp);
			aggregators.clear();
			coherenceEstimators.clear();
			finalAggregation.clear();
			lastWeights.clear();
		}
	}
	
	/**
	 * Public interface that allow to listen for events on the AggregationManager
	 * @author B3rn475
	 *
	 * @param <A> AnnotationType
	 */
	public interface OnProcessListener<A extends Annotation<C, ?>, C extends Content>{
		public void onStepInitiated(AggregationManager<A, C> sender,int step);
		public void onAggregationEnded(AggregationManager<A, C> sender, Map<Content, A> aggregatedAnnotations);
	}

	@Override
	public void clear() {
		weights.clear();
		lastWeights.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return weights.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return weights.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<A, Double>> entrySet() {
		return weights.entrySet();
	}

	@Override
	public Double get(Object key) {
		return weights.get(key);
	}

	@Override
	public boolean isEmpty() {
		return weights.isEmpty();
	}

	@Override
	public Set<A> keySet() {
		return weights.keySet();
	}

	@Override
	public Double put(A annotation, Double weight) {
		return weights.put(annotation, weight);
	}

	@Override
	public void putAll(Map<? extends A, ? extends Double> m) {
		weights.putAll(m);
	}

	@Override
	public Double remove(Object key) {
		return weights.remove(key);
	}

	@Override
	public int size() {
		return weights.size();
	}

	@Override
	public Collection<Double> values() {
		return weights.values();
	}
}
