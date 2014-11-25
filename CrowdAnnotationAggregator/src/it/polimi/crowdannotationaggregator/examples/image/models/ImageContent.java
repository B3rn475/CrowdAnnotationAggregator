/**
 * Developed By Carlo Bernaschina (GitHub - B3rn475)
 * www.bernaschina.com
 *
 * Copyright (c) 2014 Politecnico di Milano  
 * www.polimi.it
 *
 * Distributed under the LGPL Licence
 */
package it.polimi.crowdannotationaggregator.examples.image.models;

import it.polimi.crowdannotationaggregator.models.Content;

public class ImageContent extends Content {

	public final int height;
	public final int width;
	
	public ImageContent(int id, int width, int height) {
		super(id);
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException("The image cannot have a zero or negative size");
		this.width = width;
		this.height = height;
	}

}
