/**
 * 
 */
package it.polimi.annotationsaggregator;

/**
 * @author B3rn475
 *
 */
public abstract class Annotation {
	public abstract Annotation aggregateWith(Annotation rhs);
}
