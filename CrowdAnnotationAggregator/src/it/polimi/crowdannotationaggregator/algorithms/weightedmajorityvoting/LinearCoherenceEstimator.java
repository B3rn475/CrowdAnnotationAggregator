/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting;

import it.polimi.crowdannotationaggregator.models.Annotation;
import it.polimi.crowdannotationaggregator.models.Annotator;
import it.polimi.crowdannotationaggregator.models.Content;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author B3rn475
 *
 */
public abstract class LinearCoherenceEstimator<A extends Annotation<C, ?>, C extends Content> extends CoherenceEstimator<A,C> {

	private double total = 0;
	private int countDown = 0;
	private final Map<C, Double> lookup = new ConcurrentHashMap<C, Double>();
	
	public LinearCoherenceEstimator(
			OnEstimationCompletedListener<A,C> listener,
			Annotator annotator) {
		super(listener, annotator);
	}
	
	@Override
	protected void initializingEstimation(){
		total = 0;
		countDown = this.size();
		for (Entry<A,A> entry : this.entrySet()){
			comparePair(entry.getKey(), entry.getValue());
		}
	}
	
	@Override
	public final void estimate(C skip) {
		postEstimation(skip, total - lookup.get(skip));
	}
	
	@Override
	protected void endingEstimation(){
		lookup.clear();
		total = 0;
		postEndingEstimation();
	}

	/**
	 * This must call postComparePair
	 * @param annotation
	 * @param estimation
	 */
	protected abstract void comparePair(A annotation, A estimation);

	protected final void postComparePair(C content, double weight){
		final boolean ending;
		
		lookup.put(content, weight); // do this first to be sure to be the last
		
		synchronized (this) {
			countDown--;
			total += weight;
			ending = countDown == 0;
		}
		
		if (ending){
			postInitializingEstimation();
		}
	}
	
}
