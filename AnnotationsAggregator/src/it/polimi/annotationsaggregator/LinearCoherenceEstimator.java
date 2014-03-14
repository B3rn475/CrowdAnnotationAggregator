/**
 * 
 */
package it.polimi.annotationsaggregator;

import java.util.Collection;

/**
 * @author B3rn475
 *
 */
public abstract class LinearCoherenceEstimator<A extends Annotation<?, ?>> extends CoherenceEstimator<A> {

	private double total = 0;
	private int countDown = 0;
	
	public LinearCoherenceEstimator(
			it.polimi.annotationsaggregator.CoherenceEstimator.OnEstimationCompletedListener<A> listener,
			Annotator annotator, Collection<Pair<A>> container) {
		super(listener, annotator, container);
	}

	@Override
	public final void estimate() {
		total = 0;
		countDown = this.size();
		for (Pair<A> pair : this){
			comparePair(pair);
		}
	}

	protected abstract void comparePair(Pair<A> pair);

	protected final void postCamparePair(double weight){
		countDown--;
		total += weight;
		if (countDown == 0){
			postEstimation(total);
		}
	}
	
}
