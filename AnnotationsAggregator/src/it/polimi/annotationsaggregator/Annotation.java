/**
 * 
 */
package it.polimi.annotationsaggregator;

/**
 * This class describe a generic annotation.
 * 
 * From an Annotator to a Content
 * 
 * @author B3rn475
 *
 */
public abstract class Annotation<C extends Content, A extends Annotator> {
	/**
	 * Annotator of the annotation
	 * If Annotator.NONE the annotation is not releated to a particular user
	 */
	public final A annotator;
	/**
	 * Content annotated
	 */
	public final C content;
	
	/**
	 * Initialize a new Annotation
	 * 
	 * 
	 * @param content Content of the annotation
	 * @param annotator Annotator of the annotation. Can be Annnotator.NONE if it is not related to a particular Annotator
	 */
	public Annotation(C content, A annotator){
		if (content == null)
			throw new IllegalArgumentException("Content cannot be null");
		if (annotator == null)
			throw new IllegalArgumentException("Annotator cannot be null");
		this.content = content;
		this.annotator = annotator;
	}
}
