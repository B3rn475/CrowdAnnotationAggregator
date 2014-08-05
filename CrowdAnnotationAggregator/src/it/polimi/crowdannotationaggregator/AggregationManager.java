/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator;

import it.polimi.crowdannotationaggregator.Aggregator.OnAggregationCompletedListener;
import it.polimi.crowdannotationaggregator.CoherenceEstimator.OnEstimationCompletedListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Templated Aggregation Manager that allow to Aggregate a general kind of
 * Annotation
 * 
 * This class is meant to be asynchronous, it is so if aggregators and/or
 * estimators are asyncronous
 * 
 * @author B3rn475
 * 
 * @param <A>
 *            Annotation type
 */
public class AggregationManager<A extends Annotation<C, ?>, C extends Content>
		implements Map<A, Double>, OnEstimationCompletedListener<A, C>,
		OnAggregationCompletedListener<A, C> {
	private final OnProcessListener<A, C> listener;

	private final AggregatorFactory<A, C> aggregatorFactory;
	private final CoherenceEstimatorFactory<A, C> estimatorFactory;

	private final Map<A, Double> weights = new ConcurrentHashMap<A, Double>();
	private boolean isWorking = false;
	private final double threshold;
	private final int maxIterations;

	private int step = 0;
	/**
	 * must be synchronized
	 */
	private long countDown = 0;

	/**
	 * no need to synchronize it is used by one thread only.
	 */
	private final Map<A, Double> lastWeights = new HashMap<A, Double>();

	/**
	 * no need to synchronize it is read-only when multi-thread are active
	 */
	private final Map<C, Aggregator<A, C>> aggregators = new HashMap<C, Aggregator<A, C>>();
	/**
	 * no need to synchronize it is read-only when multi-thread are active
	 */
	private final Map<Annotator, CoherenceEstimator<A, C>> coherenceEstimators = new HashMap<Annotator, CoherenceEstimator<A, C>>();
	private final Map<C, A> finalAggregation = new ConcurrentHashMap<C, A>();

	/**
	 * Builder of the AggregatorManager
	 * 
	 * @param listener
	 *            an object that listen for events of the process
	 * @param aggregatorFactory
	 *            a factory object that allow to build an aggregator for the
	 *            particular kind annotation
	 * @param estimatorFactory
	 *            a factory object that allow to build an estimator for the
	 *            particular kind annotation
	 * @param threshold
	 *            weights threshold under which the process stops.
	 * @param maxIterations
	 *            maximum number of iterations, in order to avoid infinite loops
	 *            in case of non converting solutions
	 */
	public AggregationManager(OnProcessListener<A, C> listener,
			AggregatorFactory<A, C> aggregatorFactory,
			CoherenceEstimatorFactory<A, C> estimatorFactory, double threshold,
			int maxIterations) {
		if (listener == null)
			throw new IllegalArgumentException("Listener cannot be null");
		if (aggregatorFactory == null)
			throw new IllegalArgumentException(
					"aggregatorFactory cannot be null");
		if (estimatorFactory == null)
			throw new IllegalArgumentException(
					"estimatorFactory cannot be null");
		if (threshold < 0)
			throw new IllegalArgumentException(
					"threshold cannot be less than 0");
		if (maxIterations < 1)
			throw new IllegalArgumentException(
					"threshold cannot be less than 1");
		this.listener = listener;
		this.aggregatorFactory = aggregatorFactory;
		this.estimatorFactory = estimatorFactory;
		this.threshold = threshold;
		this.maxIterations = maxIterations;
	}

	/**
	 * If the manager is currently working
	 * 
	 * @return
	 */
	public boolean isWorking() {
		return isWorking;
	}

	/**
	 * Starts the aggregation process
	 */
	public void startProcess() {
		isWorking = true;

		step = 0;

		removeInvalidAnnotations();

		for (A annotation : weights.keySet()) {
			final Aggregator<A, C> aggregator;
			final C content = annotation.getContent();
			final Annotator annotator = annotation.getAnnotator();
			if (aggregators.containsKey(content)) {
				aggregator = aggregators.get(content);
			} else {
				aggregator = aggregatorFactory.buildAggregator(this,
						content);
				aggregators.put(content, aggregator);
			}
			aggregator.add(annotation);
			if (!coherenceEstimators.containsKey(annotator)) {
				coherenceEstimators.put(annotator, estimatorFactory
						.buildEstimator(this, annotator));
			}
		}

		// avoid to force the external code to normalize weights
		normalizeWeights();

		nextStep();
	}

	/**
	 * Remove all the annotations realted to: Content that has only 1 annotation
	 * Annotators that has only 1 annotation
	 */
	private void removeInvalidAnnotations() {
		final Set<C> rContents = new HashSet<C>();
		final Set<Annotator> rAnnotators = new HashSet<Annotator>();
		
		for (A annotation : weights.keySet()) {
			rContents.add(annotation.getContent());
			rAnnotators.add(annotation.getAnnotator());
		}
		
		final Set<A> removedAnnotations = new HashSet<A>();
		while (true) {
			final Map<C, Set<A>> annotations = new HashMap<C, Set<A>>();
			final Map<Annotator, Set<A>> annotators = new HashMap<Annotator, Set<A>>();

			for (A annotation : weights.keySet()) {
				final Set<A> alist;
				final C content = annotation.getContent();
				final Annotator annotator = annotation.getAnnotator();
				if (annotations.containsKey(content)) {
					alist = annotations.get(content);
				} else {
					alist = new HashSet<A>();
					annotations.put(content, alist);
				}
				alist.add(annotation);
				final Set<A> blist;
				if (annotators.containsKey(annotator)) {
					blist = annotators.get(annotator);
				} else {
					blist = new HashSet<A>();
					annotators.put(annotator, blist);
				}
				blist.add(annotation);
			}

			final Set<A> removed = new HashSet<A>();
			for (Entry<C, Set<A>> entry : annotations.entrySet()) {
				if (entry.getValue().size() <= 2) {
					removed.addAll(entry.getValue());
				}
			}
			for (Entry<Annotator, Set<A>> entry : annotators.entrySet()) {
				if (entry.getValue().size() <= 2) {
					removed.addAll(entry.getValue());
				}
			}

			if (removed.isEmpty())
				break;
			
			removedAnnotations.addAll(removed);

			for (A annotation : removed) {
				weights.remove(annotation);
			}
		}
		
		for (A annotation : weights.keySet()) {
			rContents.remove(annotation.getContent());
			rAnnotators.remove(annotation.getAnnotator());
		}
		
		listener.onInvalidAnnotationsRemoved(removedAnnotations, rContents, rAnnotators);
	}

	/**
	 * Start the next step of the aggregation process
	 */
	private void nextStep() {
		listener.onStepInitiated(this, step);

		// save last weights to test threshold
		lastWeights.clear();
		lastWeights.putAll(weights);
		
		startAggregation();
	}

	/**
	 * Start the aggregation phase
	 */
	private void startAggregation() {
		countDown = aggregators.size();
		for (Aggregator<A, C> aggregator : aggregators.values()) {
			aggregator.aggregate(weights);
		}
	}

	/**
	 * Start the estimation phase
	 */
	private void startEstimation() {
		countDown = coherenceEstimators.size();
		for (CoherenceEstimator<A, C> estimator : coherenceEstimators.values()) {
			estimator.estimate();
		}
	}

	/**
	 * End of the step
	 */
	private void testCompletion() {
		normalizeWeights();

		final double delta = computeDelta();

		listener.onStepCompleted(this, step, delta);
		
		step++;

		if (step < maxIterations) {
			if (delta > threshold) {
				nextStep();
				return;
			}
		}

		startFinalAggregation();
	}

	/**
	 * Normalizes the weights of the annotators
	 */
	private void normalizeWeights() {
		double max = 0;
		for (double d : weights.values()) {
			max = Math.max(max, Math.abs(d));
		}

		if (max == 0.0)
			return;

		for (A annotation : weights.keySet()) {
			weights.put(annotation, weights.get(annotation) / max);
		}
	}

	/**
	 * Computes the delta of the current weights WRT the old weights
	 * 
	 * @return
	 */
	private double computeDelta() {
		double nominator = 0;
		double denominator = 0;

		for (A annotation : weights.keySet()) {
			double weight = weights.get(annotation);
			double lastWeight = lastWeights.get(annotation);

			nominator += Math.abs(weight - lastWeight);
			denominator += Math.abs(lastWeight);
		}

		if (denominator == 0)
			return 0;
		else
			return nominator / denominator;
	}

	/**
	 * Initialize the final aggregation after convergence
	 */
	private void startFinalAggregation() {
		countDown = aggregators.size();
		for (Aggregator<A, C> aggregator : aggregators.values()) {
			aggregator.aggregateFinal(weights);
		}
	}

	/**
	 * Listener of the aggregator completeness
	 */
	@Override
	public void onAggregationCompleted(Aggregator<A, C> sender,
			Map<A, A> aggregatedAnnotations) {
		final boolean ending;

		// do this first to be sure to be the last
		for (Entry<A, A> entry : aggregatedAnnotations.entrySet()) {
			final A annotation = entry.getKey();
			final A estimation = entry.getValue();
			coherenceEstimators.get(annotation.getAnnotator()).put(annotation,
					estimation);
		}

		synchronized (this) {
			countDown--;
			ending = countDown == 0;
		}

		if (ending) {
			startEstimation();
		}
	}

	/**
	 * Listener of the weight estimation completion
	 */
	@Override
	public void onEstimationCompleted(CoherenceEstimator<A, C> sender,
			Map<A, Double> estimatedWeights) {
		final boolean ending;

		// do this first to be sure to be the last
		for (Entry<A, Double> entry : estimatedWeights.entrySet()) {
			weights.put(entry.getKey(), entry.getValue());
		}

		synchronized (this) {
			countDown--;
			ending = countDown == 0;
		}

		if (ending) {
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

		// do this first to be sure to be the last
		finalAggregation
				.put(aggregatedAnnotation.getContent(), aggregatedAnnotation); 

		synchronized (this) {
			countDown--;
			ending = countDown == 0;
		}

		if (ending) {
			isWorking = false;
			Map<C, A> tmp = new HashMap<C, A>(finalAggregation);
			listener.onAggregationEnded(this, tmp);
			aggregators.clear();
			coherenceEstimators.clear();
			finalAggregation.clear();
			lastWeights.clear();
		}
	}

	/**
	 * Public interface that allow to listen for events on the
	 * AggregationManager
	 * 
	 * @author B3rn475
	 * 
	 * @param <A>
	 *            AnnotationType
	 */
	public interface OnProcessListener<A extends Annotation<C, ?>, C extends Content> {
		public void onStepInitiated(AggregationManager<A, C> sender, int step);

		public void onStepCompleted(AggregationManager<A, C> sender, int step,
				double delta);

		public void onAggregationEnded(AggregationManager<A, C> sender,
				Map<C, A> aggregatedAnnotations);
		
		public void onInvalidAnnotationsRemoved(Set<A> annotations, 
				Set<C> contents, 
				Set<Annotator> annotators);
	}

	@Override
	public void clear() {
		weights.clear();
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
