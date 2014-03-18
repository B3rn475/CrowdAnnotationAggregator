package it.polimi.annotationsaggregator;

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
	public int id;
	
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
		return id;
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
