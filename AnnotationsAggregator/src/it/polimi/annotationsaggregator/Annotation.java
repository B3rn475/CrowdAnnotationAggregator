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
		if (content == null)
			throw new IllegalArgumentException("Content cannot be null");
		if (annotator == null)
			throw new IllegalArgumentException("Annotator cannot be null");
		this.content = content;
		this.annotator = annotator;
	}
}
