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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
		implements Set<A>, Aggregator.OnAggregationCompletedListener<A, C>,
		InlierEstimator.OnEstimationCompletedListener<A, C> {
	private final OnProcessListener<A, C> listener;

	private final Set<A> annotations = new HashSet<A>();
	private final Set<Annotator> annotators = new HashSet<Annotator>();
	private boolean isWorking = false;
	private final double minInliers;
	private final double randomSelect;
	private final int maxIterations;

	private final AggregatorFactory<A, C> aggregatorsFactory;
	private final InlierEstimatorFactory<A, C> inlierEstimatorsFactory;

	private final Map<C, Aggregator<A, C>> aggregators = new HashMap<C, Aggregator<A, C>>();
	private final Map<Annotator, InlierEstimator<A, C>> inlierEstimators = new HashMap<Annotator, InlierEstimator<A, C>>();

	private final Set<Annotator> inliers = new HashSet<Annotator>();
	
	private int step = 0;
	private boolean isFinal = false;
	/**
	 * must be synchronized
	 */
	private long countDown = 0;

	private final Map<C, A> model = new ConcurrentHashMap<C, A>();

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
			AggregatorFactory<A, C> aggregatorsFactory,
			InlierEstimatorFactory<A, C> inlierEstimatorsFactory,
			double randomSelect, double minInliers, int maxIterations) {
		if (listener == null)
			throw new IllegalArgumentException("listener cannot be null");
		if (aggregatorsFactory == null)
			throw new IllegalArgumentException(
					"aggregatorsFactory cannot be null");
		if (inlierEstimatorsFactory == null)
			throw new IllegalArgumentException(
					"inlierEstimatorsFactory cannot be null");
		if (randomSelect < 0 || 1 < randomSelect)
			throw new IllegalArgumentException(
					"randomSelect must be between 0 and 1");
		if (minInliers < 0 || 1 < minInliers)
			throw new IllegalArgumentException(
					"minInliers must be between 0 and 1");
		if (maxIterations < 1)
			throw new IllegalArgumentException(
					"maxIterations cannot be less than 1");
		this.listener = listener;
		this.aggregatorsFactory = aggregatorsFactory;
		this.inlierEstimatorsFactory = inlierEstimatorsFactory;
		this.randomSelect = randomSelect;
		this.minInliers = minInliers;
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
		isFinal = false;
		step = 0;
		annotators.clear();
		aggregators.clear();
		inlierEstimators.clear();

		for (A annotation : annotations) {
			final Annotator annotator = annotation.getAnnotator();
			final C content = annotation.getContent();
			if (!annotators.contains(annotator)) {
				annotators.add(annotator);
			}
			final Aggregator<A, C> aggregator;
			if (aggregators.containsKey(content)) {
				aggregator = aggregators.get(content);
			} else {
				aggregator = aggregatorsFactory.buildAggregator(this, content);
				aggregators.put(content, aggregator);
			}
			aggregator.add(annotation);
			final InlierEstimator<A, C> estimator;
			if (inlierEstimators.containsKey(annotator)) {
				estimator = inlierEstimators.get(annotator);
			} else {
				estimator = inlierEstimatorsFactory.buildEstimator(this,
						annotator);
				inlierEstimators.put(annotator, estimator);
			}
			estimator.add(annotation);
		}

		nextStep();
	}

	/**
	 * Start the next step of the aggregation process
	 */
	private void nextStep() {
		listener.onStepInitiated(this, step);
		inliers.clear();

		final Set<Annotator> goodUsers = selectRandomUsersSubset();

		startAggregation(goodUsers);
	}

	private Set<Annotator> selectRandomUsersSubset() {
		final int totalNumber = this.annotators.size();
		final int toSelect = (int) Math.floor(totalNumber * randomSelect);
		final Set<Annotator> selected = new HashSet<Annotator>();
		final Annotator[] annotators = new Annotator[totalNumber];
		this.annotators.toArray(annotators);
		while (selected.size() < toSelect) {
			final Annotator annotator = annotators[(int) Math.floor(Math
					.random() * totalNumber)];
			if (!selected.contains(annotator)) {
				selected.add(annotator);
			}
		}
		return selected;
	}

	private void startAggregation(final Set<Annotator> annotators) {
		model.clear();
		countDown = aggregators.size();
		for (Aggregator<A, C> aggregator : aggregators.values()) {
			aggregator.aggregate(annotators);
		}
	}
	
	private void startFinalAggregation() {
		isFinal = true;
		startAggregation(inliers);
	}

	@Override
	public void onAggregationCompleted(Aggregator<A, C> sender,
			A aggregatedAnnotation) {
		model.put(aggregatedAnnotation.getContent(), aggregatedAnnotation);
		onAggregationCompleted(sender);
	}

	@Override
	public void onAggregationCompleted(Aggregator<A, C> sender) {
		final boolean ended;
		synchronized (this) {
			countDown--;
			ended = countDown == 0;
		}
		if (ended) {
			if (isFinal){
				listener.onAggregationEnded(this, model);
			} else {
				startInliersEstimation();
			}
		}
	}

	private void startInliersEstimation() {
		countDown = inlierEstimators.size();
		for (InlierEstimator<A, C> estimator : inlierEstimators.values()) {
			estimator.estimate(model);
		}
	}

	@Override
	public void onEstimationCompleted(InlierEstimator<A, C> sender,
			boolean inlier) {
		if (inlier){
			inliers.add(sender.getAnnotator());
		}
		final boolean ended;
		synchronized (this) {
			countDown--;
			ended = countDown == 0;
		}
		if (ended) {
			testCompletion();
		}
	}
	
	/**
	 * End of the step
	 */
	private void testCompletion() {
		listener.onStepCompleted(this, step, inliers.size());

		step++;

		if (step < maxIterations) {
			if (inliers.size() / (double) annotators.size() < minInliers) {
				nextStep();
				return;
			}
		}

		startFinalAggregation();
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
				int inliers);

		public void onAggregationEnded(AggregationManager<A, C> sender,
				Map<C, A> aggregatedAnnotations);
	}

	@Override
	public boolean add(A annotation) {
		return annotations.add(annotation);
	}

	@Override
	public boolean addAll(Collection<? extends A> annotations) {
		return this.annotations.addAll(annotations);
	}

	@Override
	public void clear() {
		annotations.clear();
	}

	@Override
	public boolean contains(Object annotation) {
		return annotations.contains(annotation);
	}

	@Override
	public boolean containsAll(Collection<?> annotations) {
		return this.annotations.containsAll(annotations);
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
	public boolean remove(Object annotation) {
		return annotations.remove(annotation);
	}

	@Override
	public boolean removeAll(Collection<?> annotations) {
		return this.annotations.removeAll(annotations);
	}

	@Override
	public boolean retainAll(Collection<?> annotations) {
		return this.annotations.retainAll(annotations);
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
	public <T> T[] toArray(T[] array) {
		return annotations.toArray(array);
	}
}