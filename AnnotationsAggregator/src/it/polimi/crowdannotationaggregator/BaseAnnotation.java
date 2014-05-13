/**
 * 
 */
package it.polimi.crowdannotationaggregator;

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
