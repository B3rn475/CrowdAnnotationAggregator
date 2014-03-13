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
			throw new NullPointerException("Content cannot be null");
		if (annotator == null)
			throw new NullPointerException("Annotator cannot be null");
		this.content = content;
		this.annotator = annotator;
	}
}
