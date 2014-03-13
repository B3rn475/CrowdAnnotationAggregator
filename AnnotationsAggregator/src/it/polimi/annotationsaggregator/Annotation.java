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
	public final Content content;
	
	public Annotation(Content content, Annotator annotator){
		this.content = content;
		this.annotator = annotator;
	}
}
