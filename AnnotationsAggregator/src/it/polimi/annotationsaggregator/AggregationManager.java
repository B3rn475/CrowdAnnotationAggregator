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
 * @author b3rn475
 *
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

	public Dictionary<Content, Collection<A>> getAnnotations() {
		return annotations;
	}

	public Dictionary<Annotator, Double> getWeights() {
		return weights;
	}
	
	public boolean isWorking(){
		return isWorking;
	}
	
	public void startProcess(){
		isWorking = true;
		
		step = 0;
		
		//save last weights to test threshold
		lastWeights.putAll(weights);
		
		//initializing aggregators
		aggregators.clear();
		finalAggregation.clear();
		coherenceEstimators.clear();
		
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
	
	private void nextStep(){
		listener.onStepInitiated(this, step);
		step++;
		
		startAggregation();
	}
	
	private void startAggregation(){
		countDown = annotations.size();
		final Enumeration<Content> contents = annotations.keys();		
		while(contents.hasMoreElements()){
			final Aggregator<A> aggregator = aggregators.get(contents.nextElement());
			aggregator.aggregate(weights);
		}
	}
	
	private void startEstimation(){
		countDown = coherenceEstimators.size();
		final Enumeration<Annotator> annotators = coherenceEstimators.keys();		
		while(annotators.hasMoreElements()){
			final CoherenceEstimator<A> estimator = coherenceEstimators.get(annotators.nextElement());
			estimator.estimate();
		}
	}
	
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

	private void startFinalAggregation(){
		countDown = annotations.size();
		final Enumeration<Content> contents = annotations.keys();		
		while(contents.hasMoreElements()){
			final Aggregator<A> aggregator = aggregators.get(contents.nextElement());
			aggregator.aggregateFinal(weights);
		}
	}
	
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

	@Override
	public void onEstimationCompleted(CoherenceEstimator<A> sender, double weight) {
		countDown--;
		
		weights.put(sender.annotator, weight);
		
		if (countDown == 0){
			testCompletion();
		}
	}
	
	@Override
	public void onFinalAggregationCompleted(Aggregator<A> sender,
			A aggregatedAnnotation) {
		countDown--;
		finalAggregation.put(aggregatedAnnotation.content,aggregatedAnnotation);
		
		if (countDown == 0){
			isWorking = false;
			listener.onAggregationEnded(this, finalAggregation);
		}
	}
	
	public interface OnProcessListener<A extends Annotation>{
		public void onStepInitiated(AggregationManager<A> sender,int step);
		public void onAggregationEnded(AggregationManager<A> sender, Dictionary<Content, A> aggregatedAnnotations);
	}
}
