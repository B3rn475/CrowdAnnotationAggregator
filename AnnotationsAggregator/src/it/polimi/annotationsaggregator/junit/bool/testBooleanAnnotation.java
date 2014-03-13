/**
 * 
 */
package it.polimi.annotationsaggregator.junit.bool;

import static org.junit.Assert.*;
import it.polimi.annotationsaggregator.Annotator;
import it.polimi.annotationsaggregator.Content;
import it.polimi.annotationsaggregator.bool.BooleanAnnotation;

import org.junit.Test;

/**
 * @author b3rn475
 *
 */
public class testBooleanAnnotation {

	/**
	 * Test method for {@link it.polimi.annotationsaggregator.bool.BooleanAnnotation#BooleanAnnotation(it.polimi.annotationsaggregator.Content, it.polimi.annotationsaggregator.Annotator)}.
	 * Invalid Content
	 */
	@Test(expected=NullPointerException.class)
	public void testBooleanAnnotationNullContent() {
		new BooleanAnnotation(null, Annotator.NONE);
	}
	
	/**
	 * Test method for {@link it.polimi.annotationsaggregator.bool.BooleanAnnotation#BooleanAnnotation(it.polimi.annotationsaggregator.Content, it.polimi.annotationsaggregator.Annotator)}.
	 * Invalid Annotator
	 */
	@Test(expected=NullPointerException.class)
	public void testBooleanAnnotationNullAnnotator() {
		new BooleanAnnotation(Content.NONE, null);
	}
	
	/**
	 * Test method for {@link it.polimi.annotationsaggregator.bool.BooleanAnnotation#BooleanAnnotation(it.polimi.annotationsaggregator.Content, it.polimi.annotationsaggregator.Annotator, boolean)}.
	 */
	@Test
	public void testBooleanAnnotationContentAnnotatorBoolean() {
		BooleanAnnotation trueAnnotation = new BooleanAnnotation(Content.NONE, Annotator.NONE, true);
		assertTrue("True Annotation is False", trueAnnotation.getValue());
		
		BooleanAnnotation falseAnnotation = new BooleanAnnotation(Content.NONE, Annotator.NONE, false);
		assertFalse("False Annotation is True", falseAnnotation.getValue());
	}

	/**
	 * Test method for {@link it.polimi.annotationsaggregator.bool.BooleanAnnotation#BooleanAnnotation(it.polimi.annotationsaggregator.Content, it.polimi.annotationsaggregator.Annotator, double)}.
	 */
	@Test
	public void testBooleanAnnotationContentAnnotatorDouble() {
		BooleanAnnotation trueAnnotation = new BooleanAnnotation(Content.NONE, Annotator.NONE, 1);
		assertTrue("True Annotation is False", trueAnnotation.getValue());
		
		BooleanAnnotation zeroAnnotation = new BooleanAnnotation(Content.NONE, Annotator.NONE, 0);
		assertTrue("Zero Annotation is False", zeroAnnotation.getValue());
		
		BooleanAnnotation falseAnnotation = new BooleanAnnotation(Content.NONE, Annotator.NONE, -1);
		assertFalse("False Annotation is True", falseAnnotation.getValue());
	}

}
