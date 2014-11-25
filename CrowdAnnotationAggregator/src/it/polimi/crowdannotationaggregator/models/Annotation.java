/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator.models;

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
	 * If Annotator.NONE the annotation is not related to a particular user
	 */
	private final A annotator;
	/**
	 * Content annotated
	 */
	private final C content;
	
	/**
	 * Get the Annotator of the annotation
	 * If Annotator.NONE the annotation is not related to a particular user
	 * @return
	 */
	public A getAnnotator() {
		return annotator;
	}
	
	/**
	 * Get the Content annotated
	 * @return
	 */
	public C getContent() {
		return content;
	}
	
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
