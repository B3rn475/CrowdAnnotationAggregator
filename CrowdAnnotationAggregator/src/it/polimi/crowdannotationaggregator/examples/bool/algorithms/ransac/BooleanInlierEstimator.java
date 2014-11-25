/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator.examples.bool.algorithms.ransac;

import java.util.Map;

import it.polimi.crowdannotationaggregator.algorithms.ransac.InlierEstimator;
import it.polimi.crowdannotationaggregator.examples.bool.models.BooleanAnnotation;
import it.polimi.crowdannotationaggregator.models.Annotator;
import it.polimi.crowdannotationaggregator.models.Content;

/**
 * @author b3rn475
 *
 */
public final class BooleanInlierEstimator extends InlierEstimator<BooleanAnnotation, Content> {

	protected BooleanInlierEstimator(
			InlierEstimator.OnEstimationCompletedListener<BooleanAnnotation, Content> listener,
			Annotator annotator) {
		super(listener, annotator);
	}

	@Override
	public void estimate(Map<Content, BooleanAnnotation> model) {
		int ok = 0;
		int ko = 0;
		for (final BooleanAnnotation annotation : this){
			if (model.containsKey(annotation.getContent())){
				if (annotation.getValue() == model.get(annotation.getContent()).getValue()){
					ok++;
				} else {
					ko++;
				}
			}
		}
		if (ok == 0 && ko == 0){
			postEstimate(false);
		} else {
			postEstimate(ok > (ok + ko) * 2 / 3);
		}
	}

}
