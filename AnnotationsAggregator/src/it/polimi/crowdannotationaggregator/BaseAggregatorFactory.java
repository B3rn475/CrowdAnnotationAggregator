/**
 * 
 */
package it.polimi.crowdannotationaggregator;

/**
 * This is just a wrapper class for AggregatorFactiory that force 
 * the content to be of the base class Content.
 * and the annotator of the base class Annotator
 * 
 * This can be used to avoid to type Content and Annotator over and over again in the templates
 * 
 * @author B3rn475
 *
 */
public interface BaseAggregatorFactory<A extends BaseAnnotation> extends AggregatorFactory<A, Content> {

}
