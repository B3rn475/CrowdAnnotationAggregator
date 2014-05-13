package it.polimi.crowdannotationaggregator.image;

import it.polimi.crowdannotationaggregator.Content;

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
