/**
 * 
 */
package it.polimi.crowdannotationaggregator;

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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((annotator == null) ? 0 : annotator.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Annotation<?,?>))
			return false;
		Annotation<?,?> other = (Annotation<?,?>) obj;
		if (annotator.equals(other.annotator) && content.equals(other.content))
			return true;
		return false;
	}
}
