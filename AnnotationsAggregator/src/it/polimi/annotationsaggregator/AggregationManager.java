/**
 * 
 */
package it.polimi.annotationsaggregator;

import it.polimi.annotationsaggregator.Aggregator.OnAggregationCompletedListener;
import it.polimi.annotationsaggregator.CoherenceEstimator.OnEstimationCompletedListener;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Templated Aggregation Manager that allow to Aggregate a general kind of Annotation
 * 
 * This class is meant to be asynchronous, it is so if aggregators and/or estimators are asyncronous
 * 
 * @author B3rn475
 *
 * @param <A> Annotation type
 */
public class AggregationManager<A extends Annotation> implements OnEstimationCompletedListener<A>, OnAggregationCompletedListener<A> {
	private final OnProcessListener<A> listener;
	
	private final AggregatorFactory<A> aggregatorFactory;
	private final CoherenceEstimatorFactory<A> estimatorFactory;
	
	private final Hashtable<Content, Collection<A>> annotations = new Hashtable<Content, Collection<A>>();
	private final Hashtable<Annotator, Double> weights = new Hashtable<Annotator, Double>();
	private boolean isWorking = false;
	private final double threshold;
	private final int maxIterations;
	
	private int step = 0;
	private long countDown = 0;
	
	private final Hashtable<Annotator, Double> lastWeights = new Hashtable<Annotator, Double>();
	
	private final Hashtable<Content, Aggregator<A>> aggregators = new Hashtable<Content, Aggregator<A>>();
	private final Hashtable<Annotator, CoherenceEstimator<A>> coherenceEstimators = new Hashtable<Annotator, CoherenceEstimator<A>>();
	private final Hashtable<Content, A> finalAggregation = new Hashtable<Content, A>();
	
	/**
	 * Builder of the AggregatorManager
	 * 
	 * @param listener an object that listen for events of the process
	 * @param aggregatorFactory a factory object that allow to build an aggregator for the particular kind annotation
	 * @param estimatorFactory a factory object that allow to build an estimator for the particular kind annotation
	 * @param threshold weights threshold under which the process stops.
	 * @param maxIterations maximum number of iterations, in order to avoid infinite loops in case of non converting solutions
	 */
	public AggregationManager(OnProcessListener<A> listener, AggregatorFactory<A> aggregatorFactory, CoherenceEstimatorFactory<A> estimatorFactory, double threshold, int maxIterations) {
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
	 * Get an handle of the internal structure that contains the annotations
	 * @return
	 */
	public Dictionary<Content, Collection<A>> getAnnotations() {
		return annotations;
	}

	/**
	 * Get an handle of the internal structure that contains the weights of the annotators
	 * @return
	 */
	public Dictionary<Annotator, Double> getWeights() {
		return weights;
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
		
		final Enumeration<Content> contents = annotations.keys();		
		while(contents.hasMoreElements()){
			final Content content = contents.nextElement();
			final Aggregator<A> aggregator = aggregatorFactory.buildAggregator(this, content);
			aggregators.put(content, aggregator);
			aggregator.addAll(annotations.get(content));
		}
		
		final Enumeration<Annotator> annotators = weights.keys();
		while(annotators.hasMoreElements()){
			final Annotator annotator = annotators.nextElement();
			coherenceEstimators.put(annotator, estimatorFactory.buildEstimator(this, annotator));
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
		countDown = annotations.size();
		final Enumeration<Content> contents = annotations.keys();		
		while(contents.hasMoreElements()){
			final Aggregator<A> aggregator = aggregators.get(contents.nextElement());
			aggregator.aggregate(weights);
		}
	}
	
	/**
	 * Start the estimation phase
	 */
	private void startEstimation(){
		countDown = coherenceEstimators.size();
		final Enumeration<Annotator> annotators = coherenceEstimators.keys();		
		while(annotators.hasMoreElements()){
			final CoherenceEstimator<A> estimator = coherenceEstimators.get(annotators.nextElement());
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
		double tot = 0;
		for (double d : weights.values()){
			tot += d;
		}
		
		final Enumeration<Annotator> annotators = weights.keys();		
		while(annotators.hasMoreElements()){
			final Annotator annotator = annotators.nextElement();
			weights.put(annotator, weights.get(annotator) / tot);
		}
	}
	
	/**
	 * Computes the delta of the current weights WRT the old weights
	 * @return
	 */
	private double computeDelta(){
		double delta = 0;
		
		final Enumeration<Annotator> annotators = weights.keys();		
		while(annotators.hasMoreElements()){
			final Annotator annotator = annotators.nextElement();
			double weight = weights.get(annotator);
			double lastWeight = lastWeights.get(annotator);
			
			delta += Math.abs(weight - lastWeight);
		}
		
		return delta;
	}

	/**
	 * Initialize the final aggregation after convergence
	 */
	private void startFinalAggregation(){
		countDown = annotations.size();
		final Enumeration<Content> contents = annotations.keys();		
		while(contents.hasMoreElements()){
			final Aggregator<A> aggregator = aggregators.get(contents.nextElement());
			aggregator.aggregateFinal(weights);
		}
	}
	
	/**
	 * Listener of the aggregator completeness
	 */
	@Override
	public void onAggregationCompleted(Aggregator<A> sender, Collection<Pair<A>> aggregatedAnnotations) {
		countDown--;
		
		for (Pair<A> pair : aggregatedAnnotations){
			coherenceEstimators.get(pair.annotation.annotator).add(pair);
		}
		
		
		if (countDown == 0){
			startEstimation();
		}
	}

	/**
	 * Listener of the weight estimation completion
	 */
	@Override
	public void onEstimationCompleted(CoherenceEstimator<A> sender, double weight) {
		countDown--;
		
		weights.put(sender.annotator, weight);
		
		if (countDown == 0){
			testCompletion();
		}
	}
	
	/**
	 * Listener on the final aggregation completion
	 */
	@Override
	public void onFinalAggregationCompleted(Aggregator<A> sender,
			A aggregatedAnnotation) {
		countDown--;
		finalAggregation.put(aggregatedAnnotation.content,aggregatedAnnotation);
		
		if (countDown == 0){
			isWorking = false;
			@SuppressWarnings("unchecked")
			Dictionary<Content, A> tmp = (Dictionary<Content, A>) finalAggregation.clone();
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
	public interface OnProcessListener<A extends Annotation>{
		public void onStepInitiated(AggregationManager<A> sender,int step);
		public void onAggregationEnded(AggregationManager<A> sender, Dictionary<Content, A> aggregatedAnnotations);
	}
}
