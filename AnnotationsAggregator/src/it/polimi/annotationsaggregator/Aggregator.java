/**
 * 
 */
package it.polimi.annotationsaggregator;

import java.util.Collection;
import java.util.Dictionary;

/**
 * @author b3rn475
 *
 */
public abstract class Aggregator {
	public abstract Collection<Annotation> aggregate(Collection<Annotation> annotations, Dictionary<Annotator, Double> weights);
}
