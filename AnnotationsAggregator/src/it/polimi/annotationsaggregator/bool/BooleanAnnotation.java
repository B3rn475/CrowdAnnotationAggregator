/**
 * 
 */
package it.polimi.annotationsaggregator.bool;

import it.polimi.annotationsaggregator.Annotation;
import it.polimi.annotationsaggregator.Annotator;
import it.polimi.annotationsaggregator.Content;

/**
 * @author b3rn475
 *
 */
public final class BooleanAnnotation extends Annotation {

	public final double value;
	
	public BooleanAnnotation(Content content, Annotator annotator){
		super(content, annotator);
		value = 0;
	}
	
	public BooleanAnnotation(Content content, Annotator annotator, boolean value) {
		super(content, annotator);
		this.value = value?1:-1;
	}
	
	public BooleanAnnotation(Content content, Annotator annotator, double value) {
		super(content, annotator);
		this.value = value;
	}
	
	public boolean getValue(){
		return value > 0;
	}

}
