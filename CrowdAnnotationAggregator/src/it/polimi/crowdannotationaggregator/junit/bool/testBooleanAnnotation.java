/**
 * 
 */
package it.polimi.crowdannotationaggregator.junit.bool;

import static org.junit.Assert.*;
import it.polimi.crowdannotationaggregator.examples.bool.models.BooleanAnnotation;
import it.polimi.crowdannotationaggregator.models.Annotator;
import it.polimi.crowdannotationaggregator.models.Content;

import org.junit.Test;

/**
 * @author b3rn475
 *
 */
public class testBooleanAnnotation {

	private Content content = new Content(1);
	/**
	 * Test method for {@link it.polimi.annotationsaggregator.bool.BooleanAnnotation#BooleanAnnotation(it.polimi.annotationsaggregator.Content, it.polimi.annotationsaggregator.Annotator)}.
	 * Invalid Content
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testBooleanAnnotationNullContent() {
		new BooleanAnnotation(null, Annotator.NONE);
	}
	
	/**
	 * Test method for {@link it.polimi.annotationsaggregator.bool.BooleanAnnotation#BooleanAnnotation(it.polimi.annotationsaggregator.Content, it.polimi.annotationsaggregator.Annotator)}.
	 * Invalid Annotator
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testBooleanAnnotationNullAnnotator() {
		new BooleanAnnotation(content, null);
	}
	
	/**
	 * Test method for {@link it.polimi.annotationsaggregator.bool.BooleanAnnotation#BooleanAnnotation(it.polimi.annotationsaggregator.Content, it.polimi.annotationsaggregator.Annotator, boolean)}.
	 */
	@Test
	public void testBooleanAnnotationContentAnnotatorBoolean() {
		BooleanAnnotation trueAnnotation = new BooleanAnnotation(content, Annotator.NONE, true);
		assertTrue("True Annotation is False", trueAnnotation.getValue());
		
		BooleanAnnotation falseAnnotation = new BooleanAnnotation(content, Annotator.NONE, false);
		assertFalse("False Annotation is True", falseAnnotation.getValue());
	}

	/**
	 * Test method for {@link it.polimi.annotationsaggregator.bool.BooleanAnnotation#BooleanAnnotation(it.polimi.annotationsaggregator.Content, it.polimi.annotationsaggregator.Annotator, double)}.
	 */
	@Test
	public void testBooleanAnnotationContentAnnotatorDouble() {
		BooleanAnnotation trueAnnotation = new BooleanAnnotation(content, Annotator.NONE, 1);
		assertTrue("True Annotation is False", trueAnnotation.getValue());
		
		BooleanAnnotation zeroAnnotation = new BooleanAnnotation(content, Annotator.NONE, 0);
		assertTrue("Zero Annotation is False", zeroAnnotation.getValue());
		
		BooleanAnnotation falseAnnotation = new BooleanAnnotation(content, Annotator.NONE, -1);
		assertFalse("False Annotation is True", falseAnnotation.getValue());
	}

}
