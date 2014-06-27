/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator;

import it.polimi.crowdannotationaggregator.Aggregator.OnAggregationCompletedListener;

/**
 * This class allow the AggregationManager to initialize an Aggregator without knowing the exact Class
 * 
 * @author b3rn475
 *
 */
public interface AggregatorFactory<A extends Annotation<C, ?>, C extends Content> {
	public Aggregator<A,C> buildAggregator(OnAggregationCompletedListener<A,C> listener, C content);
}
