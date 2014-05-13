/**
 * 
 */
package it.polimi.crowdannotationaggregator.bool;

import it.polimi.crowdannotationaggregator.Annotator;
import it.polimi.crowdannotationaggregator.BaseAnnotation;
import it.polimi.crowdannotationaggregator.Content;

/**
 * @author b3rn475
 *
 */
public final class BooleanAnnotation extends BaseAnnotation {

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
		if (Double.isNaN(value))
			throw new IllegalArgumentException("value must be a number");
		if (Double.isInfinite(value))
			throw new IllegalArgumentException("value must be a number");
		this.value = value;
	}
	
	public boolean getValue(){
		return value >= 0.0 ;
	}

}