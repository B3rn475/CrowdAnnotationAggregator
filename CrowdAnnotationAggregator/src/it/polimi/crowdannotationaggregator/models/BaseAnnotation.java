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
 * This is just a wrapper of the base class Annotation that forces both 
 * the annotator to be of the base class Annotator
 * and the content to be of the base of the base class Content
 * 
 * This can be used to avoid to type the templates over and over again.
 * 
 * @author B3rn475
 *
 */
public class BaseAnnotation extends Annotation<Content, Annotator> {

	public BaseAnnotation(Content content, Annotator annotator) {
		super(content, annotator);
	}
}
