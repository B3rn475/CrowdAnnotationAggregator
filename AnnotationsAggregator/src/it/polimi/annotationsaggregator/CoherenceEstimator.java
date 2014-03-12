/**
 * 
 */
package it.polimi.annotationsaggregator;

import java.util.Collection;
import java.util.Dictionary;

/**
 * @author B3rn475
 *
 */
public abstract class CoherenceEstimator {
	public abstract Dictionary<Annotator, Double> estimate(Collection<Pair<Annotation>> annotations);
}
