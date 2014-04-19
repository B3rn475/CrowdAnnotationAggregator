/**
 * 
 */
package it.polimi.annotationsaggregator;

/**
 * This class is the descriptor of a particular annotator.
 * 
 * @author B3rn475
 *
 */
public class Annotator {
	/**
	 * The Id of the Annotator
	 */
	public final int id;
	
	private Annotator(int id, boolean checks)
	{
		if (checks && id < 1)
			throw new IllegalArgumentException("Annotator ID must be greater than Zero");
		this.id = id;
	}
	
	/**
	 * Initialized a new Annotator
	 * @param id Id of the annotator, must be greater than Zero
	 */
	public Annotator(int id){
		if (id < 1)
			throw new IllegalArgumentException("Annotator ID must be greater than Zero");
		this.id = id;
	}
	
	/**
	 * Get the Id of the Annotator
	 * @return
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
		if (!(obj instanceof Annotator))
			return false;
		Annotator other = (Annotator) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	/**
	 * No Annotator, it is used to reference temporary Annotations that are not referred to a particular Annotator
	 */
	public final static Annotator NONE = new Annotator(0, false);
}
