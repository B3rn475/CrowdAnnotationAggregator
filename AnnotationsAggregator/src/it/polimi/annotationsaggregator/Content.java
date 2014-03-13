package it.polimi.annotationsaggregator;

public class Content {
	public int id;
	
	public Content(int id){
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
		if (!(obj instanceof Content))
			return false;
		Content other = (Content) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public static final Content NONE = new Content(0);
}
