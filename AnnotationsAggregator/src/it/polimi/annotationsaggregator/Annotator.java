/**
 * 
 */
package it.polimi.annotationsaggregator;

/**
 * @author B3rn475
 *
 */
public class Annotator {
	public final int id;
	
	public Annotator(int id){
		this.id = id;
	}
	
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
	
	
	public final static Annotator NONE = new Annotator(0);
}
