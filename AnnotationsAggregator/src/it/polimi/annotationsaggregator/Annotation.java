/**
 * 
 */
package it.polimi.annotationsaggregator;

/**
 * @author B3rn475
 *
 */
public abstract class Annotation {
	public final Annotator annotator;
	
	public Annotation(Annotator annotator){
		this.annotator = annotator;
	}
}
