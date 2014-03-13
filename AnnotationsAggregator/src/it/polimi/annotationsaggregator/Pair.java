package it.polimi.annotationsaggregator;

public class Pair<L> {

	public final L annotation;
	public final L estimation;

	public Pair(L annotation, L estimation) {
		if (annotation == null)
			throw new IllegalArgumentException("annotation cannot be null");
		if (estimation == null)
			throw new IllegalArgumentException("estimation cannot be null");
		this.annotation = annotation;
		this.estimation = estimation;
	}

	public L getAnnotation() {
		return annotation;
	}

	public L getEstimation() {
		return estimation;
	}

	@Override
	public int hashCode() {
		return annotation.hashCode() ^ estimation.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Pair<?>))
			return false;
		Pair<?> pairo = (Pair<?>) o;
		return this.annotation.equals(pairo.annotation)
				&& this.estimation.equals(pairo.estimation);
	}

}