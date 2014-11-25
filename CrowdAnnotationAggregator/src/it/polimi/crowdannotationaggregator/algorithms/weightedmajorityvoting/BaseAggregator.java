/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting;

import it.polimi.crowdannotationaggregator.models.BaseAnnotation;
import it.polimi.crowdannotationaggregator.models.Content;

/**
 * This is a wrapper of the class Aggregator that force 
 * the content to be of the base class Content
 * and the annotator to be of the base class Annotator
 * 
 * This can be used to avoid to type Content and Annotator over and over again in the templates
 * 
 * @author B3rn475
 *
 */
public abstract class BaseAggregator<A extends BaseAnnotation> extends Aggregator<A, Content> {

	protected BaseAggregator(
			OnAggregationCompletedListener<A, Content> listener,
			Content content) {
		super(listener, content);
	}
	
	public interface OnBaseAggregationCompletedListener<A extends BaseAnnotation> extends OnAggregationCompletedListener<A, Content>{
		
	}

}
