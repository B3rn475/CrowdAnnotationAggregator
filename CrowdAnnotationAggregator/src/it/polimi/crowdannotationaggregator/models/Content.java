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
 * This class describe a generic Content that is going to be annotated.
 * It can be derived to add more informations to id
 * 
 * @author B3rn475
 *
 */
public class Content {
	/**
	 * The Id of the Content
	 */
	private final int id;
	
	/**
	 * Initialize a new Content
	 * @param id Id of the Content, it must be grater than Zero
	 */
	public Content(int id){
		if (id<1)
			throw new IllegalArgumentException("The Content Id must be grater than Zero");
		this.id = id;
	}
	
	/**
	 * Get the Id of the Content;
	 * @return the Id of the Content;
	 */
	public int getId(){
		return id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Content))
			return false;
		Content other = (Content) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
