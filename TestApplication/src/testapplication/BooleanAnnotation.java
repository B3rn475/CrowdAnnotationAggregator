/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package testapplication;

import it.polimi.crowdannotationaggregator.Annotator;
import it.polimi.crowdannotationaggregator.BaseAnnotation;
import it.polimi.crowdannotationaggregator.Content;

/**
 * @author b3rn475
 *
 */
public final class BooleanAnnotation extends BaseAnnotation {

	private final double value;
	
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
	
	public double getDoubleValue(){
		return value;
	}

}
