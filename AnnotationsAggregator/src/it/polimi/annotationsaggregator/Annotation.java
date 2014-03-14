/**
 * 
 */
package it.polimi.annotationsaggregator;

/**
 * @author B3rn475
 *
 */
public abstract class Annotation<C extends Content, A extends Annotator> {
	public final A annotator;
	public final C content;
	
	public Annotation(C content, A annotator){
		if (content == null)
			throw new IllegalArgumentException("Content cannot be null");
		if (annotator == null)
			throw new IllegalArgumentException("Annotator cannot be null");
		this.content = content;
		this.annotator = annotator;
	}
}
